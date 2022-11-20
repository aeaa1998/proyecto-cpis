package com.company.visitor;

import com.company.errors.SymbolErrorsContainer;
import com.company.intermedary.QuadArgument;
import com.company.intermedary.QuadType;
import com.company.intermedary.Quadruplets;
import com.company.intermedary.ThreeAddressCode;
import com.company.registers.Register;
import com.company.tables.*;
import com.company.utils.Constants;
import com.company.utils.MemoryType;
import com.company.yapl.YAPLParser;
import com.company.yapl.YAPLVisitor;
import kotlin.Pair;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class YAPLSymbolsVisitor extends AbstractParseTreeVisitor<VisitorTypeResponse> implements YAPLVisitor<VisitorTypeResponse> {

    private final ArrayList<Quadruplets> quadsPropertyForClass = new ArrayList<>();
//    private final ArrayList<Quadruplets> methodActivationRecordsForClass = new ArrayList<>();

    public final ArrayList<Quadruplets> dataSectionQuads = new ArrayList<>();
    public final ArrayList<Quadruplets> vTableSectionQuads = new ArrayList<>();
    public final ArrayList<Quadruplets> vTableParamOffsets = new ArrayList<>();
    private final ThreeAddressCode TAC = new ThreeAddressCode(dataSectionQuads, vTableSectionQuads);
    int ifCounter = 0;
    int whileCounter = 0;
    int tabCounter = 0;
    int stringCounter = 0;

    public ThreeAddressCode getTAC() { return TAC; }

    /**
     * First node visits the whole program
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitProgram(YAPLParser.ProgramContext ctx) {
        //Enter the program
        VisitorTypeResponse response = visitChildren(ctx);
//        TAC.addQuadsFirst(methodActivationRecordsForClass);
        return response;
    }

    /**
     * Visits a class declaration and visit the nodes
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitClass_grammar(YAPLParser.Class_grammarContext ctx) {
        //Continue with the grammar to start
        String className = ctx.className.getText();
        vTableSectionQuads.add(
                new Quadruplets(
                        QuadType.VTable,
                        QuadArgument.createConstantArgument(className + "_vtable:"),
                        null,
                        null,
                    1
                        )
        );
        vTableParamOffsets.add(
                new Quadruplets(
                        QuadType.VTable,
                        //You are going to fix the size issues THE REAL ANSWER IS TO USE THE AR_STATES!!
                        //OR TO OPEN A SPACE TO MOVE THE STACKS BEFORE AND AFTER THE PARAMS
                        QuadArgument.createConstantArgument(className + "_size:"),
                        null,
                        null,
                        1
                )
        );
        Type type = TypesTable.getInstance().getTypeByName(className);
        SymbolStack stack = SymbolStack.getInstance();
        //Add current class scope
        SymbolTable table = stack.addClassScope(className);
        type.fillTable(table);
        type.fillTableQuads(vTableSectionQuads);
        //Set the type
        if(ctx.features() != null){
            ctx.features().typeScope = type;
        };
        //Visit all
        visitChildren(ctx);
         //Escape the scope
        //TODO: here implement
        TAC.addComment("Defining new class " + className, tabCounter);
        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createLabelRefArgument("instantiate_new_" + className),
                        null,
                        null,
                        tabCounter
                )
        );
        tabCounter += 1;
        //SPACE of the class

        TAC.addQuad(
                new Quadruplets(
                        QuadType.AssignSpaceHeap,
                        QuadArgument.createConstantArgument(String.valueOf(type.getTotalSize())),
                        null,
                        null,
                        tabCounter
                )
        );



        quadsPropertyForClass.add(
                new Quadruplets(
                        QuadType.Comment,
                        QuadArgument.createConstantArgument("Declaring name value"),
                        null,
                        null,
                        tabCounter
                )
        );

        //Success case
        String name = "string"+String.valueOf(stringCounter);
        stringCounter+=1;
        int length = SymbolStack.getInstance().globalScope().getId().length();
        //Name is 0 -4
        quadsPropertyForClass.add(
                new Quadruplets(
                        QuadType.CopyStringName,
                        QuadArgument.createConstantArgument(name),
                        QuadArgument.createConstantArgument(String.valueOf(length)),
                        //Copy on the name
                        SymbolStack.getInstance().globalScope().getSymbolByName(Constants.classNameValue),
                        tabCounter
                )
        );

        //vTable is 4 - 8
        TAC.addQuad(
                new Quadruplets(
                        QuadType.AssignVTable,
                        QuadArgument.createConstantArgument(className),
                        QuadArgument.createConstantArgument(String.valueOf(4)),
                        //Copy on the name
                        SymbolStack.getInstance().globalScope().getSymbolByName(Constants.vTableValue),
                        tabCounter
                )
        );

        dataSectionQuads.add(
                new Quadruplets(
                        QuadType.Data,
                        QuadArgument.createConstantArgument(name),
                        QuadArgument.createConstantArgument("\""+SymbolStack.getInstance().globalScope().getId()+"\""),
                        null,
                        1
                )
        );

        for (Quadruplets quadClass: quadsPropertyForClass) {
            quadClass.setTab(tabCounter);
            TAC.addQuad(quadClass);
        }

        TAC.addQuad(
                new Quadruplets(
                        QuadType.ReturnAddress,
                        QuadArgument.createConstantArgument(MemoryType.Heap.getName()),
                        QuadArgument.createConstantArgument(String.valueOf(type.getTotalSize())),
                        null,
                        tabCounter
                )
        );


        //HERE WE FINISH CLASS INSTANCE


//        for (Quadruplets arQuad: methodActivationRecordsForClass) {
//            TAC.addQuad(arQuad);
//        }



        tabCounter -= 1;
        stack.removeCurrentScope(true);
        //assure
        SymbolTable.heapOffset = 0;
        SymbolTable.stackOffset = 0;
        quadsPropertyForClass.clear();
//        methodActivationRecordsForClass.clear();

        //Returns void
        return null;
    }

    /**
     * Visits a feature of a class it can be a declaration of a property or a function declaration
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitFeatures(YAPLParser.FeaturesContext ctx) {
        //Binf values
        for(YAPLParser.Class_featureContext classFeature : ctx.class_feature()){
            classFeature.typeScope = ctx.typeScope;
        }
        //Visit each feature
        return visitChildren(ctx);
    }

    /**
     * Visits a feature of a class it can be a declaration of a property or a function declaration
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitType_grammar(YAPLParser.Type_grammarContext ctx) {
        Type classType = null;
        if (ctx.CLASS_LEXER() != null){
            classType = TypesTable.getInstance().getTypeByName(ctx.CLASS_LEXER().getText());
        }else if (ctx.SELF_TYPE() != null){
            //The type of the self expression is the current class scope
            SymbolTable current = SymbolStack.getInstance().globalScope();
            //Main
            classType = TypesTable.getInstance().getTypeByName(current.getId());
        }
        if (classType == null){
            return new VisitorTypeResponse(null, null);
        }
        return classType.toResponse();

    }

    @Override
    public VisitorTypeResponse visitClass_function_declaration(YAPLParser.Class_function_declarationContext ctx) {
        String functionId = ctx.id.getText();
        //A function declaration requires a new element in the stack
        SymbolStack stack = SymbolStack.getInstance();



        //Then add the space for the machine state
        Quadruplets saveAr = new Quadruplets(QuadType.SaveAr, null, null, null, 1);
        Quadruplets loadAr = new Quadruplets(QuadType.LoadAr, null, null, null, 1);

        //From here
        TAC.addComment("Declaring new function " + functionId, tabCounter);
        // Add new wuad with just with the label

        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createLabelRefArgument(stack.globalScope().getId() + "_"+ functionId),
                        null,
                        null,
                        tabCounter
                )
        );

        tabCounter += 1;

        int currentStack = SymbolTable.stackOffset;
        Quadruplets stackMachineArQuad = new Quadruplets(QuadType.AssignSpaceStack, null, null, null, tabCounter);
        Quadruplets stackMachineStateQuad = new Quadruplets(QuadType.AssignSpaceStack, null, null, null, tabCounter);
        Quadruplets stackMachineStateQuadList = new Quadruplets(QuadType.RegistersToUse, null, null, null, tabCounter);
        TAC.addQuad(stackMachineStateQuad);
        TAC.addQuad(stackMachineStateQuadList);
        TAC.addQuad(TAC.createComment("Adding space for the parameters + body", tabCounter));
        TAC.addQuad(stackMachineArQuad);
        TAC.addQuad(saveAr);
        TAC.addQuad(new Quadruplets(QuadType.LoadNewHeap, null, null, null, tabCounter));

        //The name of the stack if with the name of the function
        stack.addFunctionScope(functionId);


        //Store the heap address
        stack.currentScope().storeTemporal(Register.heapRegister.getId(), Constants.Int, MemoryType.Stack);
//        //Store the return address
//        stack.currentScope().storeTemporal(Register.registerReturnAddress.getId(), Constants.Int, MemoryType.Stack);




        //Return Type of return statement
        VisitorTypeResponse returnTypeResponse = visitType_grammar(ctx.return_type);
        Type returnType = returnTypeResponse.getType();

        //Get the params
        List<YAPLParser.DeclarationContext> declarations = ctx.declaration();
        ArrayList<String> paramsStrings = new ArrayList<>();
        ArrayList<Symbol> paramsSymbols = new ArrayList<>();
        //Now go to the parameters binding
        for(YAPLParser.DeclarationContext declaration : declarations){
            declaration.typeScope = ctx.typeScope;
            //Visit each param
            //Here it is stored
            visitDeclaration(declaration);
            //Add param string type
            paramsStrings.add(declaration.className.getText());
            //Store the current symbol
            paramsSymbols.add(SymbolStack.getInstance().currentScope().getSymbolByName(declaration.id.getText()));
        }

        //Method get
        Method currentMethod = ctx.typeScope.getMethod(ctx.id.getText(), paramsStrings);
        //Check for the method
        currentMethod.check(returnType);

        //Bind each expression
        List<YAPLParser.ExprContext> expressions = ctx.expr();
        Quadruplets returnQuad = null;
        for (int i = 0; i < expressions.size(); i++){
            YAPLParser.ExprContext exprContext = expressions.get(i);
            exprContext.typeScope = ctx.typeScope;
            VisitorTypeResponse resultResponse = visit(exprContext);
            Type result = resultResponse.getType();
            //If it is the last expression
            if (i == expressions.size() - 1 && returnTypeResponse.isValid()){
                //Check return type exists
                Token startReturnToken = ctx.return_type.start;
                //Check type exists
                if (checkTypeExists(returnTypeResponse, ctx.return_type.getText(), startReturnToken.getCharPositionInLine(), startReturnToken.getLine())){
                    //Return type and expression are not compatible
                    if (!returnTypeResponse.acceptsType(result)){
                        SymbolErrorsContainer.getInstance().addError(
                        "El tipo esperado en la declaración de la función " + functionId + " : " + returnTypeResponse.getId() + " y se recibio " + resultResponse.getId() + " que no es compatible.",
                                ctx.start.getCharPositionInLine(),
                                ctx.start.getLine()
                        );
                    }else{
                        //Store the symbols in the params
                        currentMethod.storeSymbolsOfParams(paramsSymbols);
                        returnQuad = new Quadruplets(
                                        QuadType.Return,
                                        resultResponse.getArgument(),
                                        QuadArgument.createConstantArgument(functionId),
                                        null,
                                        tabCounter
                                );

                    }
                }

            }
        }

        ArrayList<String> symOrder = stack.getAllSymbolsFromGlobalToCurrentAndDeleted();
        int numberOfSymbolWords = symOrder.size();
        //We add another 4 extras for the ra register

//        int totalSize = SymbolTable.stackOffset-currentStack+8;
        int totalSize = 300;
        //TODO: PARAM THIS
//        currentMethod.stackSize = Integer.min(20, (numberOfSymbolWords+2)*4);
        currentMethod.stackSize = 20;
        stackMachineStateQuad.setArg1(
                QuadArgument.createConstantArgument(String.valueOf(currentMethod.stackSize))
        );

        stackMachineArQuad.setArg1(
                QuadArgument.createConstantArgument(String.valueOf(totalSize))
        );

        currentMethod.totalSize = totalSize;

        ArrayList<Symbol> symbolsToRestore = new ArrayList<>();
        for (String symId: symOrder) {
            SymbolTableResponse symbolResponse = stack.getSymbolInAnyScope(symId, 0 ,0);
            if (symbolResponse != null){
                symbolsToRestore.add(symbolResponse.getSymbolFound());
            }else{
                symbolsToRestore.add(stack.getSymbolInDeleted(symId));
            }

        }

        stackMachineStateQuadList.setArg1(
                QuadArgument.createSymbolList(symbolsToRestore)
        );
        TAC.addQuad(loadAr);

        //Return statement must be before
        if (returnQuad != null) {
            TAC.addComment("We are going to assign the response", tabCounter);
            TAC.addQuad(returnQuad);
        }

        loadAr.setArg1(QuadArgument.createConstantArgument(String.valueOf(totalSize-4)));

        saveAr.setArg1(QuadArgument.createConstantArgument(String.valueOf(totalSize-4)));

        ///MORE STATE

        Quadruplets stackMachineStateQuadListRestore = new Quadruplets(QuadType.RegistersToRestore, null, null, null, tabCounter);
        Quadruplets stackMachineStateQuadListRestoreSpace =  new Quadruplets(
                QuadType.AssignSpaceStack,
                null,
                null,
                null,
                tabCounter
        );

        //FROM HERE

        //De activation records

        TAC.addQuad(TAC.createComment("removing space for the parameters + body", 1));
        TAC.addQuad(
                new Quadruplets(
                        QuadType.AssignSpaceStack,
                        QuadArgument.createConstantArgument(String.valueOf(-totalSize)),
                        null,
                        null,
                        tabCounter
                )
        );
        //Add the resotration process
        TAC.addQuad(stackMachineStateQuadListRestore);
        //Remove space of the symbols to save for state
        TAC.addQuad(stackMachineStateQuadListRestoreSpace);
        //TO HERE ARE STATES


        stackMachineStateQuadListRestoreSpace.setArg1(QuadArgument.createConstantArgument(String.valueOf(-currentMethod.stackSize)));
        stackMachineStateQuadListRestore.setArg1(QuadArgument.createSymbolList(symbolsToRestore));
        TAC.addQuad(new Quadruplets(QuadType.Exit, null, null, null, tabCounter));
        tabCounter -= 1;

        //Remove the stack created
        stack.removeCurrentScope(true);
        //Return void
        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitClass_property_declaration(YAPLParser.Class_property_declarationContext ctx) {

        VisitorTypeResponse typeObligatoryResponse = visitType_grammar(ctx.class_);
        //Check type exists and is valid
        if (checkTypeExists(typeObligatoryResponse, ctx.class_.getText(), ctx.class_.start.getCharPositionInLine(), ctx.class_.start.getLine())) {
            //Its being assigned
            if (ctx.ASSIGN() != null) {
//                TAC.setToHolder();
//                TAC.quads = quadsPropertyForClass;
                VisitorTypeResponse assignTypeResponse = visit(ctx.value);
//                quadsPropertyForClass = TAC.quads;
//                TAC.reverseHolder();
                Type assignType = assignTypeResponse.getType();
                Token startTkn = ctx.value.start;
                if (checkTypeExists(assignTypeResponse, "N/A", startTkn.getCharPositionInLine(), startTkn.getLine())) {
                    //It is not the same as assigned or a more specific implementation
                    //Or if expected is void we just accept it
                    if (!typeObligatoryResponse.acceptsType(assignType)) {
                        SymbolErrorsContainer.getInstance().addError(
                                "La propiedad " + ctx.id.getText() + " se inicializo con una expresión inválida que tiene un valor " +
                                        assignType.getId() + " se esperaba " + typeObligatoryResponse.getId() + " en la clase " + ctx.typeScope.getId() + "\n",
                                ctx.value.start.getCharPositionInLine(),
                                ctx.value.start.getLine()
                        );
                    }else{
                        quadsPropertyForClass.add(
                            new Quadruplets(
                                    QuadType.Comment,
                                    QuadArgument.createConstantArgument("Declaring property  " + ctx.id.getText() + " of class " + ctx.class_.getText()),
                                    null,
                                    null,
                                    tabCounter
                            )
                        );
                        //Success case
                        quadsPropertyForClass.add(
                                new Quadruplets(
                                        QuadType.Assign,
                                        assignTypeResponse.getArgument(),
                                        null,
                                        SymbolStack.getInstance().globalScope().getSymbolByName(ctx.id.getText()),
                                        tabCounter
                                )
                        );
                    }
                }
            }
        }
        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitClass_feature(YAPLParser.Class_featureContext ctx) {
        YAPLVisitorContext context = ctx.class_property_declaration();
        if (context == null){
            context = ctx.class_function_declaration();
        }
        if (context != null){
            //Bind the type scope
            context.typeScope = ctx.typeScope;
        }
        //Visit the children of the class features
        return visitChildren(ctx);
    }

    //PARAMS DECLARATIONS
    @Override
    public VisitorTypeResponse visitDeclaration(YAPLParser.DeclarationContext ctx) {
        SymbolStack stack = SymbolStack.getInstance();
        //Get the current scope
        SymbolTable symbolTableInFunction = stack.currentScope();
        String paramName = ctx.id.getText();
        String paramClass = ctx.className.getText();
        //Get from type grammar the type in case its a self
        VisitorTypeResponse typeOfParamResponse = visitType_grammar(ctx.className);

        int column = ctx.start.getCharPositionInLine();
        int line = ctx.start.getLine();
        //If it is not a valid response
        if (typeOfParamResponse.isError()){
            //Store the error if the type was not defined
            SymbolErrorsContainer.getInstance().addError(
                    "El parametro " + paramName +  " en el método " + SymbolStack.getInstance().currentScope().getId() +
                            " con la clase " + paramClass + " no ha sido declarado.",
                    column,
                    line
            );
        }else{
            //Add parameter type
            //We add the param class as a string in case it is self_type
            //This are added in the stack
            symbolTableInFunction.storeSymbol(paramName, paramClass, MemoryType.Stack);
        }

        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitDeclaration_with_possible_assignation(YAPLParser.Declaration_with_possible_assignationContext ctx) {
        SymbolStack stack = SymbolStack.getInstance();
        //Get the current scope in let
        SymbolTable symbolTableInLet = stack.currentScope();
        String scopedLetName = ctx.id.getText();
        String scopedLetClass = ctx.className.getText();
        //Get from type grammar the type in case its a self
        VisitorTypeResponse typeOfLetResponse = visitType_grammar(ctx.className);
        Type typeOfLet = typeOfLetResponse.getType();
        int column = ctx.start.getCharPositionInLine();
        int line = ctx.start.getLine();
        //If it is not a valid response
        if (typeOfLetResponse.isError()){
            //Store the error if the type was not defined
            SymbolErrorsContainer.getInstance().addError(
                    "El valor " + scopedLetName + " en un let en el método " + SymbolStack.getInstance().currentScope().getId() + " se declaro con la clase " +
                            scopedLetClass + " la cual no ha sido declarada",
                    column,
                    line
            );
        }else{
            //The values created in the let scope are part of a function are in the stack
            symbolTableInLet.storeSymbol(scopedLetName, scopedLetClass, MemoryType.Stack);
        }
        //Check has assignation
        if (ctx.ASSIGN() != null){
            YAPLParser.ExprContext assignExpr = ctx.expr();
            Token assignExprToken = assignExpr.start;
            VisitorTypeResponse response = visit(assignExpr);
            //Exists
            if (checkTypeExists(response,  response.getId(), assignExprToken.getCharPositionInLine(), assignExprToken.getLine())){
                //If it is not accepted
                if (!typeOfLetResponse.acceptsType(response.getType())){
                    SymbolErrorsContainer.getInstance().addError(
                            "En la declaración de una variable el tipo esperado era: " + typeOfLetResponse.getType().getId() + " y se recibio " + response.getId() + " que no es compatible.\n" ,
                            assignExprToken.getCharPositionInLine(),
                            assignExprToken.getLine()
                    );
                }else{
                    TAC.addQuad(
                            new Quadruplets(
                                    QuadType.Assign,
                                    response.getArgument(),
                                    null,
                                    symbolTableInLet.getSymbolByName(scopedLetName),
                                    tabCounter
                            )
                    );
                }
            }
        }

        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitIsVoidExpression(YAPLParser.IsVoidExpressionContext ctx) {
        //Visit the expression
        VisitorTypeResponse resultType = visit(ctx.expr());

        checkTypeExists(resultType, "no se pudo resolver", ctx.expr().start.getCharPositionInLine(), ctx.expr().start.getLine());
        Type boolType = TypesTable.getInstance().getBoolType();
        //is void is in stack
        Symbol temporalIsVoid = SymbolStack.getInstance().currentScope().storeTemporal(boolType.getId(), MemoryType.Stack);
        TAC.addQuad(
                new Quadruplets(
                    QuadType.IsVoid,
                    resultType.getArgument(),
                    null,
                    temporalIsVoid,
                    tabCounter
                )
        );
        return boolType.toResponse().setArgument(
                QuadArgument.createSymbolArgument(temporalIsVoid)
        );
    }

    @Override
    public VisitorTypeResponse visitBoolExpression(YAPLParser.BoolExpressionContext ctx) {
        String exp;
        if (ctx.getText().equalsIgnoreCase("true")){
            exp = "1";
        }else{
            exp = "0";
        }
        return TypesTable.getInstance().getBoolType().toResponse().setArgument(
                QuadArgument.createConstantArgument(exp)
        );
    }

    @Override
    public VisitorTypeResponse visitWhileExpression(YAPLParser.WhileExpressionContext ctx) {
//        String whileCheckName = "WHILE_CEHCK_" + whileCounter;
        String whileName = "WHILE_" + whileCounter;
        String endWhileName = "END_WHILE_" + whileCounter;
//        TAC.addQuad(
//                new Quadruplets(
//                        QuadType.Label,
//                        QuadArgument.createConstantArgument(whileCheckName),
//                        null,
//                        null,
//                        tabCounter
//                )
//        );
        VisitorTypeResponse conditionTypeResponse = visit(ctx.condition);
        Type conditionType = conditionTypeResponse.getType();
        Token conditionToken = ctx.condition.start;
        //Check condition type could be resolved
        if (checkTypeExists(conditionTypeResponse, "no se pudo resolver", conditionToken.getCharPositionInLine(), conditionToken.getLine())) {
            //If the condition is not bool type
            //Here it is valid
            if (!TypesTable.getInstance().getBoolType().toResponse().acceptsType(conditionType)) {
                SymbolErrorsContainer.getInstance().addError(
                        "La condición del while statement debe de ser Bool y se retornó: " + conditionType.getId() + "\n.",
                        ctx.condition.start.getCharPositionInLine(),
                        ctx.condition.start.getLine()
                );
            }
        }


        whileCounter+=1;

        QuadArgument leftArgument;
        if (conditionTypeResponse.getArgument().isSymbol()){
            leftArgument = conditionTypeResponse.getArgument();
        }else{
            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(conditionTypeResponse.getId(), MemoryType.Stack);
            leftArgument = QuadArgument.createSymbolArgument(temporal);
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            conditionTypeResponse.getArgument(),
                            null,
                            temporal,
                            tabCounter
                    )
            );
        }


        TAC.addQuad(
                new Quadruplets(
                        QuadType.If,
                        leftArgument,
                        QuadArgument.createConstantArgument(whileName),
                        null,
                        tabCounter
                )
        );
        TAC.addQuad(
                new Quadruplets(
                        QuadType.Else,
                        QuadArgument.createConstantArgument(endWhileName),
                        null,
                        null,
                        tabCounter
                )
        );




        TAC.addQuad(new Quadruplets(QuadType.TakeSnapshot, null, null, null, 0));
        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createLabelRefArgument(whileName),
                        null,
                        null,
                        tabCounter
                )
        );
        tabCounter += 1;

        //Visit block body
        visit(ctx.block);
//        TAC.addQuad(new Quadruplets(QuadType.RemoveSnapshot, null, null, null, 0));
//        TAC.addQuad(new Quadruplets(QuadType.TakeSnapshot, null, null, null, 0));
        //Replicate condition
        VisitorTypeResponse conditionTypeResponse2 = visit(ctx.condition);
        tabCounter -= 1;


        if (conditionTypeResponse2.getArgument().isSymbol()){
            leftArgument = conditionTypeResponse2.getArgument();
        }else{
            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(conditionTypeResponse2.getId(), MemoryType.Stack);
            leftArgument = QuadArgument.createSymbolArgument(temporal);
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            conditionTypeResponse2.getArgument(),
                            null,
                            temporal,
                            tabCounter
                    )
            );
        }



        TAC.addQuad(
                new Quadruplets(
                        QuadType.If,
                        leftArgument,
                        QuadArgument.createConstantArgument(whileName),
                        null,
                        tabCounter
                )
        );
        TAC.addQuad(new Quadruplets(QuadType.RemoveSnapshot, null, null, null, 0));


        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createConstantArgument(endWhileName),
                        null,
                        null,
                        tabCounter
                )
        );


        //Always return an object
        Symbol objectTemporal = SymbolStack.getInstance().currentScope().storeTemporal(Constants.Object, MemoryType.Stack);

        return TypesTable.getInstance().getObjectType().toResponse().setArgument(
                QuadArgument.createSymbolArgument(objectTemporal)
        );
    }

    @Override
    public VisitorTypeResponse visitIntExpression(YAPLParser.IntExpressionContext ctx) {
        return TypesTable.getInstance().getIntType().toResponse().setArgument(
                QuadArgument.createConstantArgument(ctx.getText())
        );
    }

    @Override
    public VisitorTypeResponse visitParenthesisExpression(YAPLParser.ParenthesisExpressionContext ctx) {
        //Its return type is from expr
        //Here it includes the argument
        VisitorTypeResponse response = visit(ctx.expr());
        return response.setArgument(
                response.getArgument()
        );
    }

    //When the function is called with the scope specifie
    @Override
    public VisitorTypeResponse visitScopeInvocationExpression(YAPLParser.ScopeInvocationExpressionContext ctx) {
        int functionColumn = ctx.functionId.getCharPositionInLine();
        int functionLine = ctx.functionId.getLine();
        String functionIdentifier = ctx.functionId.getText();
        boolean isSpecial = Arrays.stream(Constants.SpecialFunctions).toList().contains(functionIdentifier);
        VisitorTypeResponse invocatorTypeResponse = visit(ctx.invocator);
        Type invocatorType = invocatorTypeResponse.getType();
        if (invocatorType == null){
            SymbolErrorsContainer.getInstance().addError(
                    "En la incovación de la función " + functionIdentifier + " el invocador es de tipo " + ctx.invocator.getText() +
                        " el cual no ha sido declarado.",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
        }
        //In case the expr is in the following format new A.function
        //It is invalid it does not matter the tree accepts it
        else if (ctx.invocator instanceof YAPLParser.ClassInstantiationExpressionContext){
            SymbolErrorsContainer.getInstance().addError(
                    "El formato de new " + invocatorType.getId() + "." + functionIdentifier + " no es válido.\n",
                    ctx.functionId.getCharPositionInLine(),
                    ctx.functionId.getLine()
            );
        }
        else{
            Type finalType = invocatorType;
            //A scope was supplied
            if (ctx.Scope() != null){
                VisitorTypeResponse scopeTypeResponse = visitType_grammar(ctx.type_grammar());
                Type scopeType = scopeTypeResponse.getType();
                Token typeGrammarToken = ctx.type_grammar().start;
                //Check scope type exists
                if (checkTypeExists(scopeTypeResponse, ctx.type_grammar().getText(), typeGrammarToken.getCharPositionInLine(), typeGrammarToken.getLine())){
                    //First check scope type is compatible with the expr scope
                    //In this instance it cant be null
                    if (!scopeType.acceptsType(invocatorType)){
                        SymbolErrorsContainer.getInstance().addError(
                                "Se especifico la función " + functionIdentifier + " en el tipo " + scopeType.getId(),
                                functionColumn,
                                functionLine
                        );
                    }else{
                        // If everything is correct type is set
                        finalType = scopeType;
                    }
                }
            }
            Token startParenthesisToken = ctx.PARENTHESIS_START().getSymbol();
            List<YAPLParser.ExprContext> parametersPassed = ctx.expr();
            //Normalize params
            if (parametersPassed.size() > 1){
                parametersPassed = parametersPassed.subList(1, parametersPassed.size());
            }else{
                parametersPassed = List.of();
            }

            ArrayList<VisitorTypeResponse> paramTypes = new ArrayList<>();
            ArrayList<String> paramNames = new ArrayList<>();
            parametersPassed.forEach(classP -> {
                VisitorTypeResponse p = visit(classP);
                paramTypes.add(p);
                paramNames.add(p.getId());
            });


            //Check function exists
            //TODO Checks if this works correctly like this. Else check if only exists in specified scope
            //TODO: AKA get method in scope.
            Pair<Method, Type> methodAndT = finalType.getMethodAndParent(functionIdentifier, paramNames);
            Method method = methodAndT.getFirst();
            if (method == null){
                SymbolErrorsContainer.getInstance().addError(
            "La función " + functionIdentifier + " no ha sido declarada en la clase " +  finalType.getId() +".\n"+
                        "La firma que se recibio fue: " + Type.getSignature(functionIdentifier, paramNames),
                    functionColumn,
                    functionLine
                );
            }
            else{
                String prefixName = methodAndT.getSecond().getId() + "_" + functionIdentifier;
                if (isSpecial){
                    prefixName = functionIdentifier;
                }
                //Different count of params passed
                String P = functionIdentifier;

                int N = paramTypes.size();
                //We pass the equivalent to the self
                TAC.addQuad(new Quadruplets(QuadType.NotifyParamsFunction, QuadArgument.createMethodArgument(methodAndT.getFirst()), null, null, 0));
                TAC.addComment("We are going to pass an instance as a param", tabCounter);
                TAC.addQuad(
                        new Quadruplets(
                                QuadType.Parameter,
                                invocatorTypeResponse.getArgument(),
                                null,
                                null,
                                tabCounter
                        )
                );
                if (method.getParamCount() != parametersPassed.size()){
                    SymbolErrorsContainer.getInstance().addError(
                "La función " + functionIdentifier + " se le pasaron " + parametersPassed.size() + " se esperaban " + method.getParamCount() + ".\n",
                        startParenthesisToken.getCharPositionInLine(),
                        startParenthesisToken.getLine()
                    );
                }else{
                    //Correct amount of parameters was passed
                    //Initial offset is always 8/
                    //TODO: THIS IS WRONG
                    int offset = 4;
//                    int offset = Constants.BaseSpace;
                    for (int index = 0; index < paramTypes.size(); index++){
                        YAPLParser.ExprContext parameterPassedCtx = parametersPassed.get(index);
                        Token parameterPassedTkn = parameterPassedCtx.start;
                        VisitorTypeResponse parameterTypeExpected = method.getParameterTypeAtPosition(index);
                        VisitorTypeResponse parameterTypePassed = paramTypes.get(index);
                        //Check types are valid
                        if (
                            checkTypeExists(parameterTypePassed, "no especifica", parameterPassedTkn.getCharPositionInLine(), parameterPassedTkn.getLine())
                            && parameterTypeExpected != null
                        ){
                            //Check parameter type is accepted
                            if (!parameterTypeExpected.acceptsType(parameterTypePassed.getType())){
                                SymbolErrorsContainer.getInstance().addError(
                                        "El tipo esperado era: " + parameterTypeExpected.getType().getId() + " y se recibio " + parameterTypePassed.getId() + " que no es compatible.\n",
                                        parameterPassedTkn.getCharPositionInLine(),
                                        parameterPassedTkn.getLine()
                                );
                            }else{

                                QuadArgument leftArgument;
                                if (parameterTypePassed.getArgument().isSymbol()){
                                    leftArgument = parameterTypePassed.getArgument();
                                }else{
                                    //Create a temporal
                                    Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(parameterTypeExpected.getId(), MemoryType.Stack);
                                    leftArgument = QuadArgument.createSymbolArgument(temporal);
                                    //Add Assign instructiom
                                    TAC.addQuad(
                                            new Quadruplets(
                                                    QuadType.Assign,
                                                    parameterTypePassed.getArgument(),
                                                    null,
                                                    temporal,
                                                    tabCounter
                                            )
                                    );
                                }


                                TAC.addQuad(
                                        new Quadruplets(
                                                QuadType.Parameter,
                                                leftArgument,
                                                //We set the param info
//                                                QuadArgument.createSymbolArgument(method.paramSymbolInfo.get(index)),
                                                QuadArgument.createConstantArgument(String.valueOf(offset)),
                                                null,
                                                tabCounter
                                        )
                                );
                                VisitorTypeResponse rParam = method.getParameterTypeAtPosition(index);
                                offset += rParam.getType().getReferenceSize();
                            }
                        }

                    }
                }
                VisitorTypeResponse methodResponse = method.getReturnType(finalType);
                Symbol resultTemporal = SymbolStack.getInstance().currentScope().storeTemporal(methodResponse.getId(), MemoryType.Stack);



                TAC.addComment("#Calling " + ctx.getText(), tabCounter);
                if (Arrays.stream(Constants.NonVTableType).toList().contains(finalType.getId())){
                    TAC.addQuad(
                            new Quadruplets(
                                    QuadType.Call,
                                    QuadArgument.createConstantArgument(prefixName),
                                    QuadArgument.createConstantArgument(String.valueOf(0)),
                                    resultTemporal,
                                    tabCounter
                            )
                    );
                }else{
//                    methodAndT
                    if (methodAndT.getSecond().getId().equals(invocatorType.getId())){
                        TAC.addQuad(
                                new Quadruplets(
                                        QuadType.Call,
                                        QuadArgument.createConstantArgument(prefixName),
                                        QuadArgument.createMethodAndSymbol(method, invocatorTypeResponse.getArgument().getSymbol()),
                                        resultTemporal,
                                        tabCounter
                                )
                        );
                    }else{
                        TAC.addQuad(
                                new Quadruplets(
                                        QuadType.Call,
                                        QuadArgument.createConstantArgument(prefixName),
                                        QuadArgument.createConstantArgument(methodAndT.getSecond().getId()),
                                        resultTemporal,
                                        tabCounter
                                )
                        );
                    }

                }

//

                //SCOPE CALL
                return methodResponse.setArgument(
                        QuadArgument.createSymbolArgument(resultTemporal)
                );
            }

        }

        //Method is not valid
        //Not resolved
        return new VisitorTypeResponse(null, new Exception("Class no se resolvio en el método " + functionIdentifier));
    }

    @Override
    public VisitorTypeResponse visitFunctionInvocationExpression(YAPLParser.FunctionInvocationExpressionContext ctx) {
        int functionColumn = ctx.functionId.getCharPositionInLine();
        int functionLine = ctx.functionId.getLine();
        String functionIdentifier = ctx.functionId.getText();
        boolean isSpecial = Arrays.stream(Constants.SpecialFunctions).toList().contains(functionIdentifier);

        Type invocatorType = TypesTable.getInstance().getTypeByName(SymbolStack.getInstance().globalScope().getId());
        if (invocatorType == null){
            SymbolErrorsContainer.getInstance().addError(
                    "En la invocación de la función " + functionIdentifier + " en el ambito de " + SymbolStack.getInstance().globalScope().getId()
                    + " no ha sido declarado correctamente",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
        }else{
            List<YAPLParser.ExprContext> parametersPassed = ctx.expr();
            ArrayList<VisitorTypeResponse> paramTypes = new ArrayList<>();
            ArrayList<String> paramNames = new ArrayList<>();
            parametersPassed.forEach(classP -> {
                VisitorTypeResponse p = visit(classP);
                paramTypes.add(p);
                paramNames.add(p.getId());
            });
            Pair<Method, Type> methodAndT = invocatorType.getMethodAndParent(functionIdentifier, paramNames);
            Method method = methodAndT.getFirst();
            if (method == null){
                SymbolErrorsContainer.getInstance().addError(
                        "La función " + functionIdentifier + " no ha sido declarada en la clase " +  SymbolStack.getInstance().globalScope().getId() +".\n"+
                        "Se recibio la firma: " + Type.getSignature(functionIdentifier, paramNames),
                        functionColumn,
                        functionLine
                );
            }
            else{
                String prefixName = methodAndT.getSecond().getId() + "_" + method.getId();
                if (isSpecial){
                    prefixName = method.getId();
                }
                Token startParenthesisToken = ctx.PARENTHESIS_START().getSymbol();

                //Different count of params passed
                if (method.getParamCount() != parametersPassed.size()){
                    SymbolErrorsContainer.getInstance().addError(
                            "La función " + functionIdentifier + " se le pasaron " + parametersPassed.size() + " se esperaban " + method.getParamCount() + ".\n",
                            startParenthesisToken.getCharPositionInLine(),
                            startParenthesisToken.getLine()
                    );
                }else{

                    TAC.addQuad(new Quadruplets(QuadType.NotifyParamsFunction, QuadArgument.createMethodArgument(methodAndT.getFirst()), null, null, 0));
                    //Correct amount of parameters was passed
                    //Set base param
                    TAC.addQuad(
                            new Quadruplets(
                                    QuadType.Parameter,
                                    QuadArgument.createSelfArgument(invocatorType.getId()),
                                    null,
                                    null,
                                    tabCounter
                            )
                    );
                    //TODO MANAGE THIS BETTEr
//                    int offset = Constants.BaseSpace;
                    int offset = 4;
                    for (int index = 0; index < parametersPassed.size(); index++){
                        YAPLParser.ExprContext parameterPassedCtx = parametersPassed.get(index);
                        Token parameterPassedTkn = parameterPassedCtx.start;
                        VisitorTypeResponse parameterTypeExpected = method.getParameterTypeAtPosition(index);
                        VisitorTypeResponse parameterTypePassed = paramTypes.get(index);
                        //Check types are valid
                        if (
                                checkTypeExists(parameterTypePassed, "no especifica", parameterPassedTkn.getCharPositionInLine(), parameterPassedTkn.getLine())
                                        && parameterTypeExpected != null
                        ){
                            //Check parameter type is accepted
                            if (!parameterTypeExpected.acceptsType(parameterTypePassed.getType())){
                                SymbolErrorsContainer.getInstance().addError(
                                        "El tipo esperado era: " + parameterTypeExpected.getId() + " y se recibio " + parameterTypePassed.getId() + " que no es compatible.\n",
                                        parameterPassedTkn.getCharPositionInLine(),
                                        parameterPassedTkn.getLine()
                                );
                            }else{
                                QuadArgument leftArgument;
                                if (parameterTypePassed.getArgument().isSymbol()){
                                    leftArgument = parameterTypePassed.getArgument();
                                }else{
                                    Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(parameterTypeExpected.getId(), MemoryType.Stack);
                                    leftArgument = QuadArgument.createSymbolArgument(temporal);
                                    //Add Assign instructiom
                                    TAC.addQuad(
                                            new Quadruplets(
                                                    QuadType.Assign,
                                                    parameterTypePassed.getArgument(),
                                                    null,
                                                    temporal,
                                                    tabCounter
                                            )
                                    );
                                }

                                TAC.addQuad(
                                        new Quadruplets(
                                                QuadType.Parameter,
                                                leftArgument,
                                                //We set the param info
//                                                QuadArgument.createSymbolArgument(method.paramSymbolInfo.get(index)),
                                                QuadArgument.createConstantArgument(String.valueOf(offset)),
                                                null,
                                                tabCounter
                                        )
                                );
                                VisitorTypeResponse rParam = method.getParameterTypeAtPosition(index);
                                offset += rParam.getType().getReferenceSize();
                            }
                        }

                    }
                }
                VisitorTypeResponse methodResponse = method.getReturnType(invocatorType);
                Symbol resultTemporal = SymbolStack.getInstance().currentScope().storeTemporal(methodResponse.getId(), MemoryType.Stack);

//                TAC.addQuad(
//                        new Quadruplets(
//                                QuadType.Call,
//                                QuadArgument.createConstantArgument(prefixName +"_state_save"),
//                                null,
//                                null,
//                                tabCounter
//                        )
//                );
//                TAC.addQuad(
//                        new Quadruplets(
//                                QuadType.Call,
//                                QuadArgument.createConstantArgument(prefixName +"_ar_create"),
//                                null,
//                                null,
//                                tabCounter
//                        )
//                );
                if (Arrays.stream(Constants.NonVTableType).toList().contains(invocatorType.getId())){
                    TAC.addQuad(
                            new Quadruplets(
                                    QuadType.Call,
                                    QuadArgument.createConstantArgument(prefixName),
                                    QuadArgument.createConstantArgument(String.valueOf(0)),
                                    resultTemporal,
                                    tabCounter
                            )
                    );
                }else{
                    TAC.addQuad(
                            new Quadruplets(
                                    QuadType.Call,
                                    QuadArgument.createConstantArgument(prefixName),
                                    QuadArgument.createMethodAndSymbol(method, null),
                                    resultTemporal,
                                    tabCounter
                            )
                    );
                }


                //SELF CALL
                return methodResponse.setArgument(
                        QuadArgument.createSymbolArgument(resultTemporal)
                );
            }
        }

        // Method is not valid
        //Not resolved
        return new VisitorTypeResponse(null, new Exception("Class not resolved in method " + functionIdentifier));
    }

    @Override
    public VisitorTypeResponse visitNotExpression(YAPLParser.NotExpressionContext ctx) {
        YAPLParser.ExprContext expr = ctx.expr();
        VisitorTypeResponse boolTypeMust = visit(expr);
        Type boolType = TypesTable.getInstance().getBoolType();
        //We create a temporal for the not expression
        Symbol negateSymbol = SymbolStack.getInstance().currentScope().storeTemporal(boolType.getId(), MemoryType.Stack);
        if (!boolType.toResponse().acceptsType(boolTypeMust.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "Le expresión en el not debe de ser Booleana se encontro: " + boolTypeMust.getId() + "\n",
                    expr.start.getCharPositionInLine(),
                    expr.start.getLine()
            );
        }

        TAC.addQuad(
                new Quadruplets(
                        QuadType.Not,
                        boolTypeMust.getArgument(),
                    null,
                    negateSymbol,
                        tabCounter
                )
        );
        return TypesTable.getInstance().getBoolType().toResponse()
                .setArgument(
                        QuadArgument.createSymbolArgument(negateSymbol)
                );
    }

    @Override
    public VisitorTypeResponse visitBracketExpression(YAPLParser.BracketExpressionContext ctx) {
        SymbolStack.getInstance().addBracketScope();
        VisitorTypeResponse response = visitChildren(ctx);
        if (response.isValid()){
            SymbolStack.getInstance().removeCurrentScope(false);
            return response.setArgument(response.getArgument());
        }
        SymbolStack.getInstance().removeCurrentScope(false);
        return response;
    }

    @Override
    public VisitorTypeResponse visitChildren(RuleNode node) {
        VisitorTypeResponse result = this.defaultResult();
        int n = node.getChildCount();

        for(int i = 0; i < n && this.shouldVisitNextChild(node, result); ++i) {
            ParseTree c = node.getChild(i);
            VisitorTypeResponse childResult = c.accept(this);
            if (childResult!=null) {
                result = this.aggregateResult(result, childResult);
            }
        }

        return result;
    }

    @Override
    public VisitorTypeResponse visitVariableValueAsignationExpression(YAPLParser.VariableValueAsignationExpressionContext ctx) {
        String id = ctx.OBJ_TYPE().getText();
        int column = ctx.OBJ_TYPE().getSymbol().getCharPositionInLine();
        int line = ctx.OBJ_TYPE().getSymbol().getLine();
        //Check id exists
        SymbolTableResponse result = SymbolStack.getInstance().getSymbolInAnyScope(id, column, line);
        if (result == null){
            SymbolErrorsContainer.getInstance().addError(
                    "Error al asignar un valor a la variable " + id + " que no ha sido declarada.\n",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
            return VisitorTypeResponse.getErrorResponse("Error al asignar");
        }else{
            YAPLParser.ExprContext expr = ctx.expr();
            expr.symbolToAssign = result.getSymbolFound();
            VisitorTypeResponse expectedType = result.getSymbolFound().getAssociatedType(column, line);
            VisitorTypeResponse resultType = visit(expr);
            //Types are valid
            if (
                checkTypeExists(expectedType, "no se pudo resolver", column, line) &&
                checkTypeExists(resultType, "no se pudo resolver", expr.start.getCharPositionInLine(), expr.start.getLine())
            ){
                if(!expectedType.acceptsType(resultType.getType())){
                    SymbolErrorsContainer.getInstance().addError(
                            "El tipo esperado era: " + expectedType.getId() + " y se recibio " + resultType.getId() + " que no es compatible.\n",
                            column,
                            line
                    );
                }
            }
            if (!(expr instanceof YAPLParser.ClassInstantiationExpressionContext)){
                TAC.addQuad(
                        new Quadruplets(
                                QuadType.Assign,
                                resultType.getArgument(),
                                null,
                                result.getSymbolFound(),
                                tabCounter
                        )
                );
            }

            return expectedType.setArgument(
                    QuadArgument.createSymbolArgument(result.getSymbolFound())
            );
        }


    }

    @Override
    public VisitorTypeResponse visitSmallNegationExpression(YAPLParser.SmallNegationExpressionContext ctx) {
        YAPLParser.ExprContext exprContext = ctx.expr();
        Token exprToken = exprContext.start;
        VisitorTypeResponse exprResponse = visit(exprContext);
        VisitorTypeResponse intResponse = TypesTable.getInstance().getIntType().toResponse();

        if (!intResponse.acceptsType(exprResponse.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "La expresión ~ espera que lo evaluado sea Int y se retorno " + exprResponse.getId() +"\n",
                    exprToken.getCharPositionInLine(),
                    exprToken.getLine()
            );
        }
        QuadArgument leftArgument;
        //it is a constant
        if(!exprResponse.getArgument().isSymbol()){
            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(exprResponse.getId(), MemoryType.Stack);
            leftArgument = QuadArgument.createSymbolArgument(temporal);
            //Add Assign instructiom
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            exprResponse.getArgument(),
                            null,
                            temporal,
                            tabCounter
                    )
            );
        }else{
            leftArgument = exprResponse.getArgument();

        }
        Symbol smallNegation = SymbolStack.getInstance().currentScope().storeTemporal(Constants.Int, MemoryType.Stack);
        TAC.addQuad(
                new Quadruplets(
                        QuadType.SmallNegation,
                        leftArgument,
                        null,
                        smallNegation,
                        tabCounter
                )
        );
        return intResponse.setArgument(
                QuadArgument.createSymbolArgument(smallNegation)
        );
    }

    @Override
    public VisitorTypeResponse visitStringExpression(YAPLParser.StringExpressionContext ctx) {
        //The stirng is in the heap but the temporal contains only the address
        Symbol stringTemp = SymbolStack.getInstance().currentScope().storeTemporal(Constants.String, MemoryType.Stack);
        //We assign the string
        String name = "string"+String.valueOf(stringCounter);
        stringCounter+=1;
        int length = ctx.getText().replaceAll("\\\\", "").length() - 2;

        TAC.addQuad(
                new Quadruplets(
                        QuadType.CopyString,
                        QuadArgument.createConstantArgument(name),
                        QuadArgument.createConstantArgument(String.valueOf(Integer.max(length, 0))),
                        stringTemp,
                        tabCounter
                )
        );
        dataSectionQuads.add(
                new Quadruplets(
                        QuadType.Data,
                        QuadArgument.createConstantArgument(name),
                        QuadArgument.createConstantArgument(ctx.getText()),
                        null,
                        1
                )
        );
        return TypesTable.getInstance().getStringType().toResponse().setArgument(
                QuadArgument.createSymbolArgument(stringTemp)
        );
    }

    private boolean isZero(YAPLParser.ExprContext ctx){
        return ctx.getText().equals("0");
    }

    @Override
    public VisitorTypeResponse visitOperatorExpression(YAPLParser.OperatorExpressionContext ctx) {
        VisitorTypeResponse leftType = visit(ctx.left);
        VisitorTypeResponse rightType = visit(ctx.right);
        String operator = ctx.op.getText();
        boolean isLeftZero = isZero(ctx.left);
        boolean isRightZero = isZero(ctx.right);
        checkIsIntExpression(leftType, operator, ctx.left.start.getCharPositionInLine(), ctx.left.start.getLine());
        checkIsIntExpression(rightType, operator, ctx.right.start.getCharPositionInLine(), ctx.right.start.getLine());
        Type intType = TypesTable.getInstance().getIntType();

        QuadType opQuad = QuadType.getOperator(operator);
        Symbol operationTemporal = SymbolStack.getInstance().currentScope().storeTemporal(intType.getId(), MemoryType.Stack);
        QuadArgument argumentResult;
        if (isLeftZero || isRightZero) {
            //If it is a multiplication or divisionwe know the result is 0
            if (opQuad == QuadType.Division || opQuad == QuadType.Multiply) {
                argumentResult = QuadArgument.createConstantArgument("0");
            }
            //Check if it is a sum or rest and it is zero left
            else if (isLeftZero) {
                argumentResult = rightType.getArgument();
            }else{
                argumentResult = leftType.getArgument();
            }
        }else{
            QuadArgument leftArgument;
            //it is a constant
            if(!leftType.getArgument().isSymbol()){
                Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(leftType.getId(), MemoryType.Stack);
                leftArgument = QuadArgument.createSymbolArgument(temporal);
                //Add Assign instructiom
                TAC.addQuad(
                        new Quadruplets(
                                QuadType.Assign,
                                leftType.getArgument(),
                                null,
                                temporal,
                                tabCounter
                        )
                );
            }else{
                leftArgument = leftType.getArgument();
            }
            TAC.addQuad(
                    new Quadruplets(
                            opQuad,
                            //We get the argument of the left
                            leftArgument,
                            //We get the argument of the right
                            rightType.getArgument(),
                            //We get the id
                            operationTemporal,
                            tabCounter
                    )
            );
            argumentResult = QuadArgument.createSymbolArgument(operationTemporal);
        }
        return intType.toResponse().setArgument(
                argumentResult
        );
    }

    @Override
    public VisitorTypeResponse visitSelfExpression(YAPLParser.SelfExpressionContext ctx) {
        //The type of the self expression is the current class scope
        SymbolTable current = SymbolStack.getInstance().globalScope();
        Type currentType = TypesTable.getInstance().getTypeByName(current.getId());
        if (currentType == null) {
            SymbolErrorsContainer.getInstance().addError(
                    "Se utilizo la variable self fuera de un contexto",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
            return new VisitorTypeResponse(null, null);
        }
        return currentType.toResponse().setArgument(
                QuadArgument.createSelfArgument(currentType.getId())
        );
    }

    @Override
    public VisitorTypeResponse visitIdExpression(YAPLParser.IdExpressionContext ctx) {
        String id = ctx.OBJ_TYPE().getText();
        int column = ctx.start.getCharPositionInLine();
        int line = ctx.start.getLine();
        //Check id exists
        SymbolTableResponse response = SymbolStack.getInstance().getSymbolInAnyScope(id, column, line);

        if (response != null){
            //Returns the id
            return response.getSymbolFound().getAssociatedType(column, line).setArgument(
                    QuadArgument.createSymbolArgument(response.getSymbolFound())
            );
        }
        SymbolErrorsContainer.getInstance().addError(
    "La variable " + id + " no ha sido declarada en el contexto.\n",
            column,
            line
        );

        //Not resolved the id value
        return new VisitorTypeResponse(null, null);
    }

    @Override
    public VisitorTypeResponse visitLetExpression(YAPLParser.LetExpressionContext ctx) {
        //TODO MUST ADD STACK SPACE
        //A function declaration requires a new element in the stack.
        SymbolStack stack = SymbolStack.getInstance();
        //The name of the stack if with the postfix of a let.
        TAC.addComment("Entrando a scope de let", tabCounter);
        stack.addLetScope("let");
        //We add space for the state
//        int currentStack = SymbolTable.stackOffset;

//        Quadruplets stackMachineStateQuadList = new Quadruplets(QuadType.RegistersToUse, null, null, null, 1);
//        TAC.addQuad(stackMachineStateQuad);
//        TAC.addQuad(stackMachineStateQuadList);
        for (YAPLParser.Declaration_with_possible_assignationContext d : ctx.declaration_with_possible_assignation()){
            visitDeclaration_with_possible_assignation(d);
        }
        VisitorTypeResponse response = visit(ctx.expr());

//        int totalSize = SymbolTable.stackOffset-currentStack+4;
        stack.removeCurrentScope(false);
        if(response.isValid()){
//            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(response.getId(), MemoryType.Stack);
            return response.setArgument(
                    response.getArgument()
            );
        }

        return response;
    }

    @Override
    public VisitorTypeResponse visitIfExpression(YAPLParser.IfExpressionContext ctx) {
        VisitorTypeResponse conditionType = visit(ctx.condition);

        //If the condition is not bool type
        if (!TypesTable.getInstance().getBoolType().toResponse().acceptsType(conditionType.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "La condición del if statement debe de ser Bool y se retorno: " + conditionType.getId() + "\n.",
                    ctx.condition.start.getCharPositionInLine(),
                    ctx.condition.start.getLine()
            );
        }
        String temporalName = SymbolStack.getInstance().currentScope().getTemporalName();


        String trueFlag = "TRUE_" + String.valueOf(ifCounter);
        String falseFlag = "FALSE_" + String.valueOf(ifCounter);
        String endFlag = "ENDIF_" + String.valueOf(ifCounter);
        Symbol ifResult  = new Symbol(temporalName, null, MemoryType.Stack);
        this.ifCounter = ifCounter + 1;
        QuadArgument leftArgument;
        if (conditionType.getArgument().isSymbol()){
            leftArgument = conditionType.getArgument();
        }else{
            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(conditionType.getId(), MemoryType.Stack);
            leftArgument = QuadArgument.createSymbolArgument(temporal);
            //Add Assign instructiom

            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            conditionType.getArgument(),
                            null,
                            temporal,
                            tabCounter
                    )
            );
        }

        TAC.addQuad(new Quadruplets(
                QuadType.If,
                leftArgument,
                QuadArgument.createConstantArgument(trueFlag),
                null,
                tabCounter
        ));

        TAC.addQuad(new Quadruplets(
                QuadType.Else,
                QuadArgument.createConstantArgument(falseFlag),
                null,
                null,
                tabCounter
        ));



        //Set true flag
        TAC.addQuad(new Quadruplets(
                QuadType.Label,
                QuadArgument.createLabelRefArgument(trueFlag),
                null,
                null,
                tabCounter
        ));
        tabCounter += 1;
        //Generates then body
        TAC.addQuad(new Quadruplets(QuadType.TakeSnapshot, null, null, null, 0));
//        SymbolStack.getInstance().addBracketScope();
        VisitorTypeResponse thenResponse = visit(ctx.expr(1));
//        SymbolStack.getInstance().removeCurrentScope(false);
        if (thenResponse.isValid() && thenResponse.getArgument() != null){
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.AssignIf,
                            //We store the value
                            thenResponse.getArgument(),
                            null,
                            ifResult,
                            tabCounter
                    )
            );
        }
        TAC.addQuad(new Quadruplets(QuadType.RemoveSnapshot, null, null, null, 0));
        TAC.addQuad(new Quadruplets(
                QuadType.goTo,
                QuadArgument.createConstantArgument(endFlag),
                null,
                null,
                tabCounter
        ));
        tabCounter -= 1;
        //Visit else expr
        //Set false flag
        TAC.addQuad(new Quadruplets(
                QuadType.Label,
                QuadArgument.createLabelRefArgument(falseFlag),
                null,
                null,
                tabCounter
        ));
        tabCounter += 1;
        TAC.addQuad(new Quadruplets(QuadType.TakeSnapshot, null, null, null, 0));
//        SymbolStack.getInstance().addBracketScope();
        VisitorTypeResponse elseResponse = visit(ctx.expr(2));
//        SymbolStack.getInstance().removeCurrentScope(false);
        if (elseResponse.isValid()){
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.AssignIf,
                            //We store the value
                            elseResponse.getArgument(),
                            null,
                            ifResult,
                            tabCounter
                    )
            );
        }


        tabCounter -= 1;
        //For now return void type
        VisitorTypeResponse mct = thenResponse.getMCT(elseResponse);

        if (mct.isValid()) {

            TAC.addQuad(new Quadruplets(
                    QuadType.Label,
                    QuadArgument.createLabelRefArgument(endFlag),
                    null,
                    null,
                    tabCounter
            ));


            TAC.addQuad(new Quadruplets(QuadType.RemoveSnapshot, null, null, null, tabCounter));

            ifResult.setTypeName(mct.getId());
            //We add the symbol
            SymbolStack.getInstance().currentScope().storeSymbol(ifResult);
            return mct.setArgument(
                    QuadArgument.createSymbolArgument(ifResult)
            );
        }
        return mct;
    }

    @Override
    public VisitorTypeResponse visitClassInstantiationExpression(YAPLParser.ClassInstantiationExpressionContext ctx) {
        VisitorTypeResponse response = visit(ctx.type_grammar());
        if (response.isValid()){
            //Where we will store the value
            Symbol resultSymbol = ctx.symbolToAssign;
            if (ctx.symbolToAssign == null){
                //It is a class the value but we are only using the address
                resultSymbol = SymbolStack.getInstance().currentScope().storeTemporal(response.getId(), MemoryType.Stack);
            }

            TAC.addComment(
                    "Creating a new instance of the class " + response.getId(),
                    tabCounter
            );

            //We call the instantiation function
            TAC.addQuad(
                    response.getType().getInstantiationCallQuad(resultSymbol, tabCounter)
            );

            return response.setArgument(
                    QuadArgument.createSymbolArgument(resultSymbol)
            );
        }

        return response;
    }

    @Override
    public VisitorTypeResponse visitBoolComparisonExpression(YAPLParser.BoolComparisonExpressionContext ctx) {
        VisitorTypeResponse leftType = visit(ctx.left);
        VisitorTypeResponse rightType = visit(ctx.right);
        String op = ctx.op.getText();
        int column = ctx.left.start.getCharPositionInLine();
        int line = ctx.left.start.getLine();
        if (
            !leftType.getId().equals(rightType.getId()) ||
            leftType.getId().equals("(no se pudo resolver probablemente no fue declarada)") ||
            leftType.getId().equals("(void)")
        ){
            SymbolErrorsContainer.getInstance().addError(
                    "En una comparación (" + op + ")" + " ambos operadores deben de ser iguals " + "se encontró: " + leftType.getId() + " y " + rightType.getId(),
                    column,
                    line
            );
        }
        //We create a temporal for the not expression
        Symbol comparisonTemporal = SymbolStack.getInstance().currentScope().storeTemporal(leftType.getId(), MemoryType.Stack);
        QuadType comp = QuadType.getCompOperator(op);

        QuadArgument leftArgument;
        //it is a constant as true or false
        if(!leftType.getArgument().isSymbol()){
            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(leftType.getId(), MemoryType.Stack);
            leftArgument = QuadArgument.createSymbolArgument(temporal);
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            leftType.getArgument(),
                            null,
                            temporal,
                            tabCounter
                    )
            );
        }else{
            leftArgument = leftType.getArgument();
        }

        TAC.addQuad(
                new Quadruplets(
                        comp,
                        //We get the argument of the left
                        leftArgument,
                        //We get the argument of the right
                        rightType.getArgument(),
                        //We get the id
                        comparisonTemporal,
                        tabCounter
                )
        );
        return TypesTable.getInstance().getBoolType().toResponse().setArgument(
                QuadArgument.createSymbolArgument(comparisonTemporal)
        );
    }
    /** HELPERS */
    private void checkIsBoolExpressionInComparison(VisitorTypeResponse type, String comparisonSign, int column, int line){
        if (!TypesTable.getInstance().getBoolType().toResponse().acceptsType(type.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "En una comparación (" + comparisonSign + ")" + " ambos operadores deben de ser booleanos " + "se encontró: " + type.getId(),
                    column,
                    line
            );
        }
    }
    private void checkIsIntExpression(VisitorTypeResponse type, String operation, int column, int line){
        if (!TypesTable.getInstance().getIntType().toResponse().acceptsType(type.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "En una operación (" + operation + ")" + " ambos operadores deben de ser Integers " + "se encontró: " + type.getId(),
                    column,
                    line
            );
        }
    }
    private boolean checkTypeExists(VisitorTypeResponse response, String id, int column, int line){
        if (response.isError() || response.isVoid()){
            SymbolErrorsContainer.getInstance().addError(
                    "Cerca de la linea " + line + " se utiliza la clase " + id + " que no ha sido declarada.",
                column,
                line
            );
            return false;
        }
        return true;
    }
}