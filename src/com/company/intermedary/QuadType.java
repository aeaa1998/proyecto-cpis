package com.company.intermedary;

public enum QuadType {
    Label, Assign, Return, IsVoid, Plus, Minus, Division,
    Multiply, If, Else, Call, goTo, Not, SmallNegation, AssignSpaceHeap, AssignSpaceStack, Parameter,
    Smaller, SmallerOrEqual, Equal
    ;

    public static QuadType getOperator(String op){
        QuadType type = null;
        switch (op){
            case "+" -> type = Plus;
            case "-" -> type = Minus;
            case "/" -> type = Division;
            case "*" -> type = Multiply;
        }
        return type;
    }

    public static QuadType getCompOperator(String op){
        QuadType type = null;
        switch (op){
            case "<" -> type = Smaller;
            case "<=" -> type = SmallerOrEqual;
            case "=" -> type = Equal;
        }
        return type;
    }

    public String getRepresentation(){
        String representation = "";
        switch (this){
            case Label -> {
                representation =  "label";
            }
            case Assign -> {
                representation =  "=";
            }
            case Return -> {
                representation = "return";
            }
            case IsVoid -> representation = "isvoid";
            case Plus -> representation = "+";
            case Minus -> representation = "-";
            case Division -> representation = "/";
            case Multiply -> representation = "*";
            case If -> representation = "if";
            case Else -> representation = "else";
            case Call -> representation = "call";
            case goTo -> representation = "goTo";
            case Not -> representation = "not";
            case Smaller -> representation = "<";
            case SmallerOrEqual -> representation = "<=";
            case Equal -> representation = "=";
            case SmallNegation -> representation = "~";
            case AssignSpaceHeap -> representation = "assign_space_heap";
            case AssignSpaceStack -> representation = "assign_space_stack";
            case Parameter -> representation = "param";
        }
        return representation;
    }
}
