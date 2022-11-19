package com.company.intermedary;

import com.company.codegen.CodeBlock;
import com.company.codegen.CodeGenerator;
import com.company.registers.AddressDescriptor;
import com.company.registers.RegisterDescriptor;
import com.company.tables.Symbol;

import java.util.ArrayList;
import java.util.List;

public class ThreeAddressCode {
    public    ArrayList<Quadruplets> dataSection;
    public    ArrayList<Quadruplets> vTable;
    private   ArrayList<Quadruplets> holder = new ArrayList<>();
    public ThreeAddressCode(ArrayList<Quadruplets> dataSection, ArrayList<Quadruplets> vTable){
        this.dataSection = dataSection;
        this.vTable = vTable;
    }
    public ArrayList<Quadruplets> quads = new ArrayList<>();

    public void setToHolder(){
        holder = this.quads;
    }

    public void reverseHolder(){
        this.quads = holder;
    }

    public void addComment(String comment, int tab){
        this.quads.add(
                new Quadruplets(
                        QuadType.Comment,
                        QuadArgument.createConstantArgument(comment),
                        null,
                        null,
                        tab
                )
        );
    }

    public Quadruplets createComment(String comment, int tab){
        return new Quadruplets(
                QuadType.Comment,
                QuadArgument.createConstantArgument(comment),
                null,
                null,
                tab
        );
    }

    public void addQuadsFirst(ArrayList<Quadruplets> qs){
        qs.addAll(this.quads);
        this.quads = qs;
    }

    public void addQuad(
            Quadruplets quad
    ){
//        if (quad.getOperator() == QuadType.Assign && quad.getArg1() == null){
//            printTAC();
//        }
        this.quads.add(quad);
    }

    public List<CodeBlock> generateCode(){
        CodeGenerator generator = new CodeGenerator(this, new AddressDescriptor(), new RegisterDescriptor());
        //Create data section
        for (Quadruplets quad: dataSection){
            generator.assignToQuad(quad);
        }
        for (Quadruplets quad: vTable){
            generator.assignToQuad(quad);
        }
        int i = 0;
        for (Quadruplets quad: quads) {
            generator.assignToQuad(quad);
            i++;
        }
        return generator.getCode();
    }

    public String printTAC(){
        StringBuilder printer = new StringBuilder();
        for (Quadruplets quad: quads) {
            int tabs = quad.getTab();
            String tabsString = "";
            for (int i = 0; i < tabs; i++) {
                tabsString += "\t";
            }
            printer.append(tabsString);
            switch (quad.getOperator()){
                case Label -> {
                    //Print the label
                    printer.append(quad.getArg1().getExpression()).append(":").append("\n");
                }
                case Return -> {
                    //We return the temporal for now
//                    printer.println(quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression());
                    if (quad.getArg1() != null) printer.append(quad.getOperator().getRepresentation() + " " + quad.getArg1().getFullExpression()).append("\n");
                    else printer.append(quad.getOperator().getRepresentation()).append("\n");
                }
                case If -> {
                    printer.append(
                            quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression() + " " + quad.getArg2().getExpression()
                    ).append("\n");
                }
                case Else -> {
                    printer.append(quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression()).append("\n");
                }
                case Not -> printer.append(quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression()).append("\n");

                case Call -> {
                    String methodName = quad.getArg1().getExpression();
                    String numberOfParams;
                    if (quad.getArg2() != null){
                        numberOfParams = quad.getArg2().getExpression();
                    }else{
                        numberOfParams = "0";
                    }
                    Symbol resultPointer = quad.getResult();
                    //Function name and params
                    printer.append(quad.getOperator().getRepresentation() + " " + methodName + " " + numberOfParams).append("\n");
                    printer.append(tabsString);
//                    printer.println("setCall " + resultPointer.getId());
                    if (resultPointer != null){
                        printer.append("setCall " + resultPointer.getOffsetAndMemory()).append("\n");
                    }
                }
                case goTo -> {
                    String labelToJump = quad.getArg1().getExpression();
                    printer.append(quad.getOperator().getRepresentation() + " " + labelToJump).append("\n");
                }
                case Plus, Minus, Multiply, Division -> {
                    String t1 = quad.getArg1().getExpression();
                    String t2 = quad.getArg2().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.append(result.getId() + " = " + t1 + " " + operator + " " + t2).append("\n");
                }
                case Assign -> {
                    QuadArgumentType argumentTypeToAssign = quad.getArg1().getType();
                    Symbol result = quad.getResult();
                    if (argumentTypeToAssign == QuadArgumentType.ReturnNewClass){
//                        printer.println(result.getId() + " = " + quad.getArg1().getExpression());
                        printer.append(result.getOffset() + " = " + quad.getArg1().getExpression()).append("\n");
                    }else{
//                        printer.println(result.getId() + " = " + quad.getArg1().getExpression());
                        printer.append(result.getOffsetAndMemory()  + " = " + quad.getArg1().getExpression()).append("\n");
                    }
                }
                case IsVoid -> {
                    String t1 = quad.getArg1().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.append(result.getId() + " = " + operator + " " + t1).append("\n");
                }
                case Smaller, SmallerOrEqual, Equal -> {
                    String t1 = quad.getArg1().getExpression();
                    String t2 = quad.getArg2().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.append(result.getId() + " = " + t1 + " " + operator + " " + t2).append("\n");
                }
                case SmallNegation -> {
                    String t1 = quad.getArg1().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.append(result.getId() + " = " + operator + " " + t1).append("\n");
                }
                case AssignSpaceHeap -> {
                    String spaceNeeded = quad.getArg1().getExpression();
                    printer.append(quad.getOperator().getRepresentation() + " " + spaceNeeded).append("\n");
                }
                case Parameter -> {
                    QuadArgument paramArgument = quad.getArg1();
                    String param;
                    if (paramArgument.getType() == QuadArgumentType.Self){
                        param = "0 en " + paramArgument.getMemoryType().getName();
                    }else if (paramArgument.getType() == QuadArgumentType.Constant) {
                        param = paramArgument.getExpression();
                    }else{
                        Symbol paramSymbol = paramArgument.getSymbol();
                        param = paramSymbol.getOffsetAndMemory();
                    }


//                    printer.println(quad.getOperator().getRepresentation() + " " + param);
                    printer.append(quad.getOperator().getRepresentation() + " " + param).append("\n");
                }

                case Comment -> {
                    printer.append(quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression()).append("\n");
                }
                case ReturnAddress -> {
                    printer.append("set return heap address").append("\n");
                }
                case RegistersToUse -> {
                    printer.append("Save the next symbols: ").append("\n");
                    for (Symbol s:
                    quad.getArg1().symbolsToUse) {
                        printer.append(tabsString);
                        printer.append("Save " + s.getOffsetAndMemory()).append("\n");
                    }
                }
                case RegistersToRestore -> {
                    printer.append("Restore the next symbols: ").append("\n");
                    for (Symbol s:
                            quad.getArg1().symbolsToUse) {
                        printer.append(tabsString);
                        printer.append("Restore " + s.getOffsetAndMemory()).append("\n");
                    }
                }
                case AssignSpaceStack -> {
                    printer.append("Assign in stack " + quad.getArg1().getExpression()).append("\n");
                }
            }
        }
        return printer.toString();
    }
}
