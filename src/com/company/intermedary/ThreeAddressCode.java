package com.company.intermedary;

import com.company.tables.Symbol;

import java.io.PrintStream;
import java.util.ArrayList;

public class ThreeAddressCode {
    private final ArrayList<Quadruplets> quads = new ArrayList<>();

    public void addQuad(
            Quadruplets quad
    ){
//        if (quad.getOperator() == QuadType.Assign && quad.getArg1() == null){
//            printTAC();
//        }
        this.quads.add(quad);
    }

    public void printTAC(){
        PrintStream printer = System.out;
        for (Quadruplets quad: quads) {
            int tabs = quad.getTab();
            String tabsString = "";
            for (int i = 0; i < tabs; i++) {
                tabsString += "\t";
            }
            printer.print(tabsString);
            switch (quad.getOperator()){
                case Label -> {
                    //Print the label
                    printer.println(quad.getArg1().getExpression() + ":");
                }
                case Return -> {
                    //We return the temporal for now
                    printer.println(quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression());
                }
                case If -> {
                    printer.println(
                            quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression() + " " + quad.getArg2().getExpression()
                    );
                }
                case Else -> {
                    printer.println(quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression());
                }
                case Not -> {
                    printer.println(quad.getOperator().getRepresentation() + " " + quad.getArg1().getExpression());
                }
                case Call -> {
                    String methodName = quad.getArg1().getExpression();
                    String numberOfParams = quad.getArg2().getExpression();
                    Symbol resultPointer = quad.getResult();
                    printer.println(quad.getOperator().getRepresentation() + " " + methodName + " " + numberOfParams);
                    printer.print(tabsString);
                    printer.println("setCall " + resultPointer.getId());
                }
                case goTo -> {
                    String labelToJump = quad.getArg1().getExpression();
                    printer.println(quad.getOperator().getRepresentation() + " " + labelToJump);
                }
                case Plus, Minus, Multiply, Division -> {
                    String t1 = quad.getArg1().getExpression();
                    String t2 = quad.getArg2().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.println(result.getId() + " = " + t1 + " " + operator + " " + t2);
                }
                case Assign -> {
                    QuadArgumentType argumentTypeToAssign = quad.getArg1().getType();
                    Symbol result = quad.getResult();
                    if (argumentTypeToAssign == QuadArgumentType.ReturnNewClass){
//                        printer.println(result.getId() + " = " + quad.getArg1().getExpression());
                        printer.println(result.getOffset() + " = " + quad.getArg1().getExpression());
                    }else{
//                        printer.println(result.getId() + " = " + quad.getArg1().getExpression());
                        printer.println(result.getOffset() + " offset " + result.getMemoryTypePlacement().getName()  + " = " + quad.getArg1().getExpression());
                    }
                }
                case IsVoid -> {
                    String t1 = quad.getArg1().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.println(result.getId() + " = " + operator + " " + t1);
                }
                case Smaller, SmallerOrEqual, Equal -> {
                    String t1 = quad.getArg1().getExpression();
                    String t2 = quad.getArg2().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.println(result.getId() + " = " + t1 + " " + operator + " " + t2);
                }
                case SmallNegation -> {
                    String t1 = quad.getArg1().getExpression();
                    Symbol result = quad.getResult();
                    String operator = quad.getOperator().getRepresentation();
                    printer.println(result.getId() + " = " + operator + " " + t1);
                }
                case AssignSpaceHeap -> {
                    String spaceNeeded = quad.getArg1().getExpression();
                    printer.println(quad.getOperator().getRepresentation() + " " + spaceNeeded);
                }
                case Parameter -> {
                    String param = quad.getArg1().getExpression();
                    printer.println(quad.getOperator().getRepresentation() + " " + param);
                }

            }
        }
    }
}
