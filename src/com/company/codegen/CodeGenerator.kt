package com.company.codegen

import com.company.intermedary.*
import com.company.registers.AddressDescriptor
import com.company.registers.Register
import com.company.registers.Register.*
import com.company.registers.RegisterDescriptor
import com.company.tables.Method
import com.company.tables.Symbol
import com.company.tables.TypesTable
import com.company.utils.Constants
import com.company.utils.MemoryType
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class CodeGenerator(
    val threeAddressCode: ThreeAddressCode,
    val addressDescriptor: AddressDescriptor,
    val registerDescriptor: RegisterDescriptor
) {
    var mainSize: Int = 0
    lateinit var currentMethod: Method
    private var indexToInsert = 0
    private val dataCodeGenerated: ArrayList<CodeBlock> = ArrayList<CodeBlock>().apply {
        add(CodeBlock(".data", 0))
        add(CodeBlock("\tinvalid_string: .asciiz \"Los indices pasados son inválidos\"", 0))
        add(CodeBlock("\tobjectName: .asciiz \"Object\"", 0))
        add(CodeBlock("\tstringName: .asciiz \"String\"", 0))
        add(CodeBlock("\tintName: .asciiz \"Int\"", 0))
        add(CodeBlock("\tboolName: .asciiz \"Bool\"", 0))
        add(CodeBlock("\tabortMessage: .asciiz \"Interrupción por algún error\"", 0))
        add(CodeBlock("Object_vtable:", 1))
        add(CodeBlock(".word Object_abort", 2))
        add(CodeBlock(".word Object_type_name", 2))


    }


    private val textCodeGenerated: ArrayList<CodeBlock> = ArrayList<CodeBlock>().apply {
//        add(CodeBlock(".text", 0))
//        add(CodeBlock("main:", 0))
//        add(CodeBlock("jal instantiate_new_Main", 0))
//        add(CodeBlock("move \$t9, \$v0", 0))
////        add(CodeBlock("jal Main_main_state_save", 0))
////        add(CodeBlock("jal Main_main_ar_create", 0))
//        add(CodeBlock("sw \$t9, 0(\$sp)", 0))
//        add(CodeBlock("jal Main_main", 0))
////        add(CodeBlock("jal Main_main_ar_remove", 0))
////        add(CodeBlock("jal Main_main_state_restore", 0))
//        add(CodeBlock("li \$v0, 10", 0))
//        add(CodeBlock("syscall", 0))
    }

    public fun getCode(): List<CodeBlock> {
        mainSize = with(TypesTable.getInstance().getTypeByName("Main").getMethod("main", ArrayList())){
            totalSize + stackSize
        }
        val initialBlock: ArrayList<CodeBlock> = ArrayList<CodeBlock>().apply {
            add(CodeBlock(".text", 0))
            add(CodeBlock("main:", 0))
            add(CodeBlock("jal instantiate_new_Main", 0))
            add(CodeBlock("move \$t9, \$v0", 0))
//        add(CodeBlock("jal Main_main_state_save", 0))
//        add(CodeBlock("jal Main_main_ar_create", 0))
            add(CodeBlock("sw \$t9, ${-mainSize}(\$sp)", 0))
            add(CodeBlock("jal Main_main", 0))
//        add(CodeBlock("jal Main_main_ar_remove", 0))
//        add(CodeBlock("jal Main_main_state_restore", 0))
            add(CodeBlock("li \$v0, 10", 0))
            add(CodeBlock("syscall", 0))
        }
        return dataCodeGenerated+initialBlock+textCodeGenerated;
    }

    private fun assignRegisterToSymbol(symbol: Symbol, register: Register){
        registerDescriptor.setSymbolInRegister(register, symbol)
        addressDescriptor.setRegisterForSymbol(symbol, register)
    }

    private fun removeAllDataFromRegister(register: Register): ArrayList<Symbol> {
        //Get all symbols in that register
        val symbols = arrayListOf<Symbol>()
        symbols.addAll(registerDescriptor.getSymbolsInRegister(register) ?: listOf())
        //Remove all symbols in that register
        registerDescriptor.descriptors[register]?.clear()

        symbols.forEach { symbol ->
            //Remove the registers
            addressDescriptor.descriptor[symbol]?.removeIf { it.id == register.id }
        }
        return symbols
    }

    private fun getRegisterForSymbol(symbol: Symbol, tab: Int, blockedRegisters: List<String> = emptyList()) : Register {
        //Check first if it is present
        val possible = addressDescriptor.getRegistersForSymbol(symbol)
        if (possible.isNotEmpty()){
            return possible.first()
        }

        val registersForSym = addressDescriptor.getRegistersForSymbol(symbol)
        //If there are registers
        if (registersForSym.isNotEmpty()){
            //We return the register found
            return registersForSym.first()
        }else{
            //Check if there are any available
            if(registerDescriptor.availableRegisters.isNotEmpty()){
                val registerToAssign  = registerDescriptor.availableRegisters.poll()
                assignRegisterToSymbol(symbol, registerToAssign)
                //We add the instruction to load from the stack or the heap
                textCodeGenerated.add(CodeBlock("lw " + registerToAssign.id + ", " + symbol.offsetAndMemoryUsable, tab))
                return registerToAssign
            }else {
                //There are not registers
                val registerWithLowWeight = registerDescriptor.getRegisterWithLessWeight(blockedRegisters)
                val symbolsRemovedFromRegisters = removeAllDataFromRegister(registerWithLowWeight)
                symbolsRemovedFromRegisters.forEach { symbolRemoved ->
                    //We store the values in the stack or the heap
                    textCodeGenerated.add(
                        CodeBlock(
                            "sw ${registerWithLowWeight.id}, ${symbolRemoved.offsetAndMemoryUsable}",
                            tab
                        )
                    )
                }
                //Now we assign
                assignRegisterToSymbol(symbol, registerWithLowWeight)
                //We add the instruction to load
                textCodeGenerated.add(
                    CodeBlock(
                        "lw " + registerWithLowWeight.id + ", " + symbol.offsetAndMemoryUsable,
                        tab
                    )
                )
                return registerWithLowWeight

            }
        }
    }

    private fun getRegisterForSymbolOrConstant(argument: QuadArgument?, tab: Int, blockedRegisters: List<String> = emptyList()): String? {
        if (argument == null) return null
        //Argument is self in this context
        if (argument.type == QuadArgumentType.Self){
            return Register.heapRegister.id
        }
        if (argument.isSymbol){
            return getRegisterForSymbol(argument.symbol, tab, blockedRegisters).id
        }
        argument.expression.toIntOrNull()?.let { _ ->
            return argument.expression
        }



        return null
    }

    private fun generateArithmeticCode(quadruplets: Quadruplets){


        val firstRegisterOrConstant = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab, listOf())
        val secondRegisterOrConstant = getRegisterForSymbolOrConstant(quadruplets.arg2, quadruplets.tab, listOf(firstRegisterOrConstant?:""))
        val thirdRegisterOrConstant = getRegisterForSymbol(quadruplets.result, quadruplets.tab, listOf(firstRegisterOrConstant?:"", secondRegisterOrConstant?:""))

        if (secondRegisterOrConstant != null && firstRegisterOrConstant != null){
            val secondIsConstant = !secondRegisterOrConstant.startsWith("$")
            val start = when(quadruplets.operator){
                QuadType.Plus -> if (secondIsConstant) "addi" else "add"
                QuadType.Minus -> if (secondIsConstant) "subi" else "sub"
                QuadType.Multiply -> "mul"
                else -> "div"
            }
            //Add code
            textCodeGenerated.add(CodeBlock("$start ${thirdRegisterOrConstant.id}, $firstRegisterOrConstant, $secondRegisterOrConstant", quadruplets.tab))
        }
    }

    private fun generateIfCode(quadruplets: Quadruplets){
        val firstRegisterOrConstant = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab, emptyList())
        val second = quadruplets.arg2.expression
        textCodeGenerated.add(CodeBlock("bne $firstRegisterOrConstant, 0, $second", quadruplets.tab))
    }

    private fun generateBooleanCode(quadruplets: Quadruplets){

        val firstRegisterOrConstant = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab, emptyList())
        val secondRegisterOrConstant = getRegisterForSymbolOrConstant(quadruplets.arg2, quadruplets.tab, listOf(firstRegisterOrConstant ?:""))
        val thirdRegisterOrConstant = getRegisterForSymbol(quadruplets.result, quadruplets.tab, listOf(firstRegisterOrConstant ?:"", secondRegisterOrConstant ?: ""))

        when(quadruplets.operator){
            QuadType.Equal -> {
                textCodeGenerated.add(CodeBlock("xor ${thirdRegisterOrConstant.id}, $firstRegisterOrConstant, $secondRegisterOrConstant", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("slti ${thirdRegisterOrConstant.id}, ${thirdRegisterOrConstant.id}, 1", quadruplets.tab))
            }
            QuadType.Smaller -> {
                val slt = if (secondRegisterOrConstant?.startsWith("$") == true){ "slt" }else{ "slti" }
                textCodeGenerated.add(CodeBlock("$slt ${thirdRegisterOrConstant.id}, $firstRegisterOrConstant, $secondRegisterOrConstant", quadruplets.tab))
            }
            // 1 is smaller or equal than two we just need to say it is not bigger
            QuadType.SmallerOrEqual -> {
                val slt = if (secondRegisterOrConstant?.startsWith("$") == true){ "slt" }else{ "slti" }
                textCodeGenerated.add(CodeBlock("$slt ${thirdRegisterOrConstant.id}, $secondRegisterOrConstant, $firstRegisterOrConstant", quadruplets.tab))
                //Operate a bot
                textCodeGenerated.add(CodeBlock("addi ${thirdRegisterOrConstant.id}, ${thirdRegisterOrConstant.id}, 1", quadruplets.tab))
                //Mod by two
                textCodeGenerated.add(CodeBlock("div ${thirdRegisterOrConstant.id}, ${thirdRegisterOrConstant.id}, 2", quadruplets.tab))
                //Load the mod
                textCodeGenerated.add(CodeBlock("mfhi ${thirdRegisterOrConstant.id}", quadruplets.tab))
            }
            else -> {
                //Operate a not
                textCodeGenerated.add(CodeBlock("addi ${firstRegisterOrConstant}, ${firstRegisterOrConstant}, 1", quadruplets.tab))
                //Mod by two
                textCodeGenerated.add(CodeBlock("div ${firstRegisterOrConstant}, ${firstRegisterOrConstant}, 2", quadruplets.tab))
                //Load the mod
                textCodeGenerated.add(CodeBlock("mfhi ${thirdRegisterOrConstant.id}", quadruplets.tab))
            }
        }
    }

    private fun generateSpaceHeap(quadruplets: Quadruplets){
//        if (quadruplets.arg1.expression.toInt() != 0) {
            textCodeGenerated.add(CodeBlock("li \$v0, 9", quadruplets.tab))
            textCodeGenerated.add(CodeBlock("li \$a0, ${quadruplets.arg1.expression}", quadruplets.tab))
            textCodeGenerated.add(CodeBlock("syscall", quadruplets.tab))
//        }
        //We move the heap address to the register
        textCodeGenerated.add(CodeBlock("move ${Register.heapRegister.id}, \$v0", quadruplets.tab))
    }

    private fun generateSpaceStack(quadruplets: Quadruplets){
        textCodeGenerated.add(CodeBlock("subi ${Register.stackRegister.id}, ${Register.stackRegister.id}, ${quadruplets.arg1.expression}", quadruplets.tab))
    }

    private fun storeParamsInStack(quadruplets: Quadruplets){
        // Argument two is the offset of AR
        val stackSize = currentMethod.stackSize
        val totalSize = currentMethod.totalSize

        val initialTotalOffset = -(totalSize + stackSize)
        if (quadruplets.arg2 != null){
            val paramRegister = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab, emptyList())
            //Store in the stack
            textCodeGenerated.add(CodeBlock("#We are going to send a param with offset defined", quadruplets.tab))

            textCodeGenerated.add(CodeBlock("sw ${paramRegister}, ${initialTotalOffset + quadruplets.arg2.expression.toInt()}(${Register.stackRegister.id})", quadruplets.tab))
        }else{
                //the first param we are goint to save heap address
             //It is always going to be 4
            textCodeGenerated.add(CodeBlock("#We are going to send a heap", quadruplets.tab))
            if (quadruplets.arg1.isSymbol){
                val paramRegister = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab, emptyList())
                textCodeGenerated.add(CodeBlock("sw ${paramRegister}, ${initialTotalOffset}(${Register.stackRegister.id})", quadruplets.tab))
            }else{
                //It is implicit self
                textCodeGenerated.add(CodeBlock("sw ${Register.heapRegister.id}, ${initialTotalOffset}(${Register.stackRegister.id})", quadruplets.tab))
            }

        }
    }




    private fun generateSecuenceRestore(quadruplets: Quadruplets, indexToInsert: Int){
        val symbolsToRestore = quadruplets.arg1.symbolsToUse
        val registersSaved = mutableSetOf<Register>()
        //We save them
        textCodeGenerated.add(indexToInsert, CodeBlock("# We are going to store the current state", quadruplets.tab))
        var _index = 0
//        symbolsToRestore.forEachIndexed { _, symbol ->
//            val index = _index + 1
//            if(symbol.id == registerReturnAddress.id){
//                textCodeGenerated.add(indexToInsert + index, CodeBlock("sw ${registerReturnAddress.id}, ${_index*4}(${Register.stackRegister.id})", quadruplets.tab))
//                _index++
//            } else if(symbol.id == heapRegister.id){
//                textCodeGenerated.add(indexToInsert + index, CodeBlock("sw ${heapRegister.id}, ${_index*4}(${Register.stackRegister.id})", quadruplets.tab))
//                _index++
//            }else{
//
//                addressDescriptor.getRegistersForSymbol(symbol).firstOrNull()?.let { register ->
//
//
//                    //Now we create the restore
//                    if (!registersSaved.contains(register)) {
//                        textCodeGenerated.add(
//                            indexToInsert + index,
//                            CodeBlock("sw ${register.id}, ${_index * 4}(${Register.stackRegister.id})", quadruplets.tab)
//                        )
//                        _index++
//                    }
//                    registersSaved.add(register)
//                }
//                if (registersSaved.count() == temporariesRegisters.size){
//                    return@forEachIndexed
//                }
//            }
//        }
//        textCodeGenerated.add(indexToInsert +_index + 1, CodeBlock("sw ${registerReturnAddress.id}, ${_index * 4}(${Register.stackRegister.id})",  quadruplets.tab))
        textCodeGenerated.add(indexToInsert + 1, CodeBlock("sw \$t9, 0(\$sp)",quadruplets.tab))
        textCodeGenerated.add(indexToInsert + 2, CodeBlock("sw \$t0, 4(\$sp)",quadruplets.tab))
        textCodeGenerated.add(indexToInsert + 3, CodeBlock("sw \$t1, 8(\$sp)",quadruplets.tab))
        textCodeGenerated.add(indexToInsert + 4, CodeBlock("sw \$t2, 12(\$sp)",quadruplets.tab))

        registersSaved.clear()
        //We restore them
        textCodeGenerated.add(CodeBlock("# We are going to restore the current state", quadruplets.tab))
        _index = 0
//        symbolsToRestore.forEachIndexed { _, symbol ->
//
//            if(symbol.id == registerReturnAddress.id){
//                textCodeGenerated.add(CodeBlock("lw ${registerReturnAddress.id}, ${_index*4}(${Register.stackRegister.id})", quadruplets.tab))
//                _index++
//            } else if(symbol.id == heapRegister.id){
//                textCodeGenerated.add(CodeBlock("lw ${heapRegister.id}, ${_index*4}(${Register.stackRegister.id})", quadruplets.tab))
//                _index++
//            }else {
//                addressDescriptor.getRegistersForSymbol(symbol).firstOrNull()?.let { register ->
//                    //We load it from the stack
//                    if (!registersSaved.contains(register)) {
//                        textCodeGenerated.add(
//                            CodeBlock(
//                                "lw ${register.id}, ${_index * 4}(${Register.stackRegister.id})",
//                                quadruplets.tab
//                            )
//                        )
//                        _index++
//                    }
//                    registersSaved.add(register)
//                }
//                if (registersSaved.count() == temporariesRegisters.size){
//                    return@forEachIndexed
//                }
//            }
//        }
        textCodeGenerated.add(CodeBlock("lw \$t9, 0(\$sp)",quadruplets.tab))
        textCodeGenerated.add(CodeBlock("lw \$t0, 4(\$sp)",quadruplets.tab))
        textCodeGenerated.add(CodeBlock("lw \$t1, 8(\$sp)",quadruplets.tab))
        textCodeGenerated.add(CodeBlock("lw \$t2, 12(\$sp)",quadruplets.tab))
//        textCodeGenerated.add(CodeBlock("sw ${registerReturnAddress.id}, ${_index * 4}(${Register.stackRegister.id})",  quadruplets.tab))
    }

    fun assignToQuad(quadruplets: Quadruplets){

        //Check each type
        when(quadruplets.operator){
            QuadType.CopyString -> {
                val nameOfDataString = quadruplets.arg1.expression
                val stringSize = quadruplets.arg2.expression
                val result = quadruplets.result
                val register = getRegisterForSymbol(result, quadruplets.tab)
                textCodeGenerated.add(CodeBlock("li \$a0, $stringSize", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("la \$a1, $nameOfDataString", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("jal copyString", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("move ${register.id}, \$v0", quadruplets.tab))
            }

            QuadType.AssignVTable -> {
                val name = "${quadruplets.arg1.expression}_vtable"
                val symbol = quadruplets.result

                textCodeGenerated.add(CodeBlock("la \$v0, $name", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("sw \$v0, ${symbol.offsetAndMemoryUsable}", quadruplets.tab))
            }

            QuadType.CopyStringName -> {
                val nameOfDataString = quadruplets.arg1.expression
                val stringSize = quadruplets.arg2.expression
                val result = quadruplets.result

                textCodeGenerated.add(CodeBlock("li \$a0, $stringSize", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("la \$a1, $nameOfDataString", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("jal copyString", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("sw \$v0,  ${result.offsetAndMemoryUsable}", quadruplets.tab))
            }
            QuadType.Data -> {
                dataCodeGenerated.add(CodeBlock("${quadruplets.arg1.expression}: .asciiz ${quadruplets.arg2.expression}", 1))
            }
            QuadType.VTable -> {
                val name = quadruplets.arg1.expression
                if (name.contains("vtable")){
                    dataCodeGenerated.add(CodeBlock(name, 1))
                }else{
                    dataCodeGenerated.add(CodeBlock(".word $name", 2))
                }
            }
            QuadType.Comment -> textCodeGenerated.add(CodeBlock("#"+quadruplets.arg1.expression, quadruplets.tab))
            QuadType.Label -> textCodeGenerated.add(CodeBlock(quadruplets.arg1.expression + ":", quadruplets.tab))
            QuadType.Minus, QuadType.Plus, QuadType.Multiply, QuadType.Division -> {
                generateArithmeticCode(quadruplets)
            }
            QuadType.Equal, QuadType.Smaller, QuadType.SmallerOrEqual, QuadType.Not -> {
                generateBooleanCode(quadruplets)
            }
            QuadType.SmallNegation -> {
                val register = getRegisterForSymbol(quadruplets.arg1.symbol, quadruplets.tab)
                val resultRegister = getRegisterForSymbol(quadruplets.result, quadruplets.tab, listOf(register.id))
                textCodeGenerated.add(CodeBlock("nor ${resultRegister.id}, ${register.id}, ${register.id}", quadruplets.tab))
            }
            QuadType.AssignSpaceHeap -> {
                textCodeGenerated.add(CodeBlock("subi \$sp, \$sp, 20",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("sw ${registerReturnAddress.id}, 0(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("sw ${heapRegister.id}, 4(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("sw \$t0, 8(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("sw \$t1, 12(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("sw \$t2, 16(${stackRegister.id})",quadruplets.tab))
                generateSpaceHeap(quadruplets)
            }
            QuadType.AssignSpaceStack -> generateSpaceStack(quadruplets)
            QuadType.goTo -> {
                textCodeGenerated.add(CodeBlock("j ${quadruplets.arg1.expression}", quadruplets.tab))
            }
            QuadType.Parameter -> storeParamsInStack(quadruplets)
            QuadType.RegistersToUse ->{
                //Secuence start
                indexToInsert = textCodeGenerated.size
            }
            QuadType.NotifyParamsFunction -> {
                currentMethod = quadruplets.arg1.method
            }
            QuadType.RegistersToRestore ->{
                //Secuence start
                generateSecuenceRestore(quadruplets, indexToInsert)
            }
            QuadType.If -> {
                generateIfCode(quadruplets)
            }
            QuadType.Else -> {
                textCodeGenerated.add(CodeBlock("j ${quadruplets.arg1.expression}", quadruplets.tab))
            }
            QuadType.ReturnAddress -> {
                //We need to set the remaining values in the stack
                registerDescriptor.descriptors.forEach { t, u ->
                    u.forEach {
                        textCodeGenerated.add(CodeBlock("sw ${t.id}, ${it.offsetAndMemoryUsable}", quadruplets.tab))
                    }
                }

                //We finish here
                textCodeGenerated.add(CodeBlock("move \$v0, ${Register.heapRegister.id}", quadruplets.tab))

                textCodeGenerated.add(CodeBlock("lw ${registerReturnAddress.id}, 0(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("lw ${heapRegister.id}, 4(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("lw \$t0, 8(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("lw \$t1, 12(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("lw \$t2, 16(${stackRegister.id})",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("addi \$sp, \$sp, 20",quadruplets.tab))
                textCodeGenerated.add(CodeBlock("jr ${registerReturnAddress.id}", 1))
                registerDescriptor.descriptors.forEach { t, u ->
                    u.clear()
                    if (!registerDescriptor.availableRegisters.contains(t)) registerDescriptor.availableRegisters.add(t)
                }
                addressDescriptor.descriptor.forEach {
                    it.value.clear()
                }
            }
            QuadType.Return -> {
                //Just return
                if (quadruplets.arg1 == null && quadruplets.arg2 == null && quadruplets.result == null){
                    textCodeGenerated.add(CodeBlock("jr ${registerReturnAddress.id}", 1))
                    registerDescriptor.descriptors.forEach { t, u ->
                        u.clear()
                        if (!registerDescriptor.availableRegisters.contains(t)) registerDescriptor.availableRegisters.add(t)
                    }
                    addressDescriptor.descriptor.forEach {
                        it.value.clear()
                    }
                }else{
                    //We load the value on the return register value
                        textCodeGenerated.add(CodeBlock("#Finalizo la función ${quadruplets.arg2.expression}", quadruplets.tab))
                    getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab)?.let { a1 ->
                        if (a1.startsWith("$")){
                            textCodeGenerated.add(CodeBlock("move \$v0, $a1", quadruplets.tab))
                        }   else{
                            textCodeGenerated.add(CodeBlock("li \$v0, $a1", quadruplets.tab))
                        }
                    }

                }

            }
            QuadType.Exit -> {
                textCodeGenerated.add(CodeBlock("jr ${registerReturnAddress.id}", 1))
                registerDescriptor.descriptors.forEach { t, u ->
                    u.clear()
                    if (!registerDescriptor.availableRegisters.contains(t)) registerDescriptor.availableRegisters.add(t)
                }
                addressDescriptor.descriptor.forEach {

                    it.value.clear()
                }
            }
            //Only in v table functions
            QuadType.Call -> {
                val functionName = quadruplets.arg1.expression
                val method = quadruplets.arg2.method
                val symbol = quadruplets.arg2.symbol

                //Dynamic
                if (method != null){
                    if (symbol != null){
                        val paramRegister = getRegisterForSymbol(symbol, quadruplets.tab, emptyList())
                        textCodeGenerated.add(CodeBlock("lw \$s0, 4(${paramRegister.id})", quadruplets.tab))
                    }else{
                        //It is implicit self
                        textCodeGenerated.add(CodeBlock("lw \$s0, 4(${heapRegister.id})", quadruplets.tab))
                    }
                    //We call the function
                    //Load the v table
                    textCodeGenerated.add(CodeBlock("lw \$s0, ${method.order * 4}(\$s0)", quadruplets.tab))
                    textCodeGenerated.add(CodeBlock("jalr \$s0", quadruplets.tab))
                }
                else {
                    textCodeGenerated.add(CodeBlock("jal $functionName", quadruplets.tab))
                }
                //If there is a result we return it
                val result = quadruplets.result
                result?.let {
                    val register = getRegisterForSymbol(result, quadruplets.tab)
                    //Set the value in the desired register
//                    if (result.memoryTypePlacement == MemoryType.Heap) {
                    textCodeGenerated.add(CodeBlock("#Setting value for ${quadruplets.arg1.expression.replace("_state_restore", "")}", quadruplets.tab))
                        textCodeGenerated.add(CodeBlock("move ${register.id}, \$v0", quadruplets.tab))
//                    }else{
//                        textCodeGenerated.add(CodeBlock("lw ${register.id}, \$v0", quadruplets.tab))
//                    }
                }
            }
            QuadType.IsVoid -> {
                val operand = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab)
                val result = getRegisterForSymbol(quadruplets.result, quadruplets.tab)
                textCodeGenerated.add(CodeBlock("xor ${result.id}, ${Register.zeroRegister.id}, $operand", quadruplets.tab))
                textCodeGenerated.add(CodeBlock("slt ${result.id}, ${Register.zeroRegister.id}, ${result.id}", quadruplets.tab))
            }
            QuadType.Assign -> {
                val registerForValueToCopy = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab)
                val symbolToMoveInNewRegister = quadruplets.result
                //It is a movement
                if (registerForValueToCopy?.startsWith("$") == true) {
                    val loaderIfNeeded = getRegisterForSymbol(symbolToMoveInNewRegister, quadruplets.tab, listOf(registerForValueToCopy))
                    textCodeGenerated.add(CodeBlock("sw $registerForValueToCopy, ${symbolToMoveInNewRegister.offsetAndMemoryUsable}", quadruplets.tab))

                    val allRegisterThatAreTheSame = addressDescriptor.getRegistersForSymbol(symbolToMoveInNewRegister)
                        .filter { it.id != registerForValueToCopy }
                    val allSymbolsToMove = allRegisterThatAreTheSame.fold(mutableSetOf<Symbol>()) { carry, register ->
                        carry.addAll(registerDescriptor.getSymbolsInRegister(register))
                        carry
                    }
                    allRegisterThatAreTheSame.forEach {
                        //Copy the registers to the new value to update
                        textCodeGenerated.add(CodeBlock("move ${it.id}, ${registerForValueToCopy}", quadruplets.tab))
                    }

//
                    registerDescriptor.descriptors.keys.firstOrNull { reg -> reg.id == registerForValueToCopy }
                        ?.let { register ->
                            //Add it
                            registerDescriptor.setSymbolInRegister(register, symbolToMoveInNewRegister)
                            addressDescriptor.setRegisterForSymbol(symbolToMoveInNewRegister, register)
                        }

                    //Now clean the registers
                    registerDescriptor.descriptors.keys.forEach { register ->
                        if (registerDescriptor.getSymbolsInRegister(register).isEmpty()) {
                            //push it again
                            if (!registerDescriptor.availableRegisters.contains(register)) {
                                registerDescriptor.availableRegisters.add(register)
                            }
                        }
                    }
                }
                else {
                    val register = getRegisterForSymbol(symbolToMoveInNewRegister, quadruplets.tab)
                    textCodeGenerated.add(CodeBlock("li ${register.id}, $registerForValueToCopy", quadruplets.tab))
                    //Save it just in case
                    textCodeGenerated.add(CodeBlock("sw ${register.id}, ${symbolToMoveInNewRegister.offsetAndMemoryUsable}", quadruplets.tab))

                }
            }
            QuadType.LoadNewHeap -> {
                textCodeGenerated.add(CodeBlock("lw ${heapRegister.id}, 0(${stackRegister.id})", quadruplets.tab))
            }

            QuadType.SaveAr -> {
                textCodeGenerated.add(CodeBlock("sw ${registerReturnAddress.id}, ${quadruplets.arg1.expression}(${stackRegister.id})", quadruplets.tab))
            }
            QuadType.LoadAr -> {
                textCodeGenerated.add(CodeBlock("lw ${registerReturnAddress.id}, ${quadruplets.arg1.expression}(${stackRegister.id})", quadruplets.tab))
            }
            QuadType.TakeSnapshot -> {
                addressDescriptor.takeSnapShot()
                val registersToRestore = registerDescriptor.takeSnapShot()
                registersToRestore.forEach { t, u ->
                    u.forEach {
                        textCodeGenerated.add(CodeBlock("sw ${t.id}, ${it.offsetAndMemoryUsable}", quadruplets.tab))
                    }
                }
            }
            QuadType.AssignIf -> {
                val registerForValueToCopy = getRegisterForSymbolOrConstant(quadruplets.arg1, quadruplets.tab)
                val symbolToMoveInNewRegister = quadruplets.result
                //It is a movement
                if (registerForValueToCopy?.startsWith("$") == true) {
                    val loaderIfNeeded =
                        getRegisterForSymbol(symbolToMoveInNewRegister, quadruplets.tab, listOf(registerForValueToCopy))
                    textCodeGenerated.add(
                        CodeBlock(
                            "sw $registerForValueToCopy, ${symbolToMoveInNewRegister.offsetAndMemoryUsable}",
                            quadruplets.tab
                        )
                    )

                    val allRegisterThatAreTheSame = addressDescriptor.getRegistersForSymbol(symbolToMoveInNewRegister)
                        .filter { it.id != registerForValueToCopy }
                    val allSymbolsToMove = allRegisterThatAreTheSame.fold(mutableSetOf<Symbol>()) { carry, register ->
                        carry.addAll(registerDescriptor.getSymbolsInRegister(register))
                        carry
                    }
                    allRegisterThatAreTheSame.forEach {
                        //Copy the registers to the new value to update
                        textCodeGenerated.add(CodeBlock("move ${it.id}, ${registerForValueToCopy}", quadruplets.tab))
                    }

//
                    registerDescriptor.descriptors.keys.firstOrNull { reg -> reg.id == registerForValueToCopy }
                        ?.let { register ->
                            //Add it
                            registerDescriptor.setSymbolInRegister(register, symbolToMoveInNewRegister)
                            addressDescriptor.setRegisterForSymbol(symbolToMoveInNewRegister, register)
                        }

                    //Now clean the registers
                    registerDescriptor.descriptors.keys.forEach { register ->
                        if (registerDescriptor.getSymbolsInRegister(register).isEmpty()) {
                            //push it again
                            if (!registerDescriptor.availableRegisters.contains(register)) {
                                registerDescriptor.availableRegisters.add(register)
                            }
                        }
                    }
                }
                else {
                    val register = getRegisterForSymbol(symbolToMoveInNewRegister, quadruplets.tab)
                    textCodeGenerated.add(CodeBlock("li ${register.id}, $registerForValueToCopy", quadruplets.tab))
                    //Save it just in case
                    textCodeGenerated.add(CodeBlock("sw ${register.id}, ${symbolToMoveInNewRegister.offsetAndMemoryUsable}", quadruplets.tab))

                }
            }
            QuadType.RemoveSnapshot -> {
//                registerDescriptor.descriptors.forEach { t, u ->
//                    u.forEach {
//                        textCodeGenerated.add(CodeBlock("sw ${t.id}, ${it.offsetAndMemoryUsable}", quadruplets.tab))
//
//                    }
//                }
                val addresses = addressDescriptor.removeSnapShot()
                val registersThatMightbeenChanged = registerDescriptor.removeSnapShot()
                registersThatMightbeenChanged.forEach { t, u ->
                    u.firstOrNull()?.let {
                        textCodeGenerated.add(CodeBlock("lw ${t.id}, ${it.offsetAndMemoryUsable}", quadruplets.tab))
                    }
                }
            }
            else -> {

            }
        }
    }
}