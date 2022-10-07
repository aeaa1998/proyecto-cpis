package com.company.intermedary;


import com.company.tables.Symbol;
import com.company.visitor.VisitorTypeResponse;

public class Quadruplets {
    //The operator
    private QuadType operator;
    //Each of the arguments
    private QuadArgument arg1, arg2;
    //The result temporal or symbol
    private Symbol result;

    public void setTab(int tab) {
        this.tab = tab;
    }

    //For better legibility in prints
    private int tab;

    public Quadruplets(QuadType operator, QuadArgument arg1, QuadArgument arg2, Symbol result, int tab) {
        this.operator = operator;
//        if (operator == QuadType.Assign && arg1 == null){
//            arg1 = arg2;
//        }
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
        this.tab = tab;
    }

    public QuadType getOperator() {
        return operator;
    }

    public QuadArgument getArg1() {
        return arg1;
    }

    public QuadArgument getArg2() {
        return arg2;
    }

    public Symbol getResult() {
        return result;
    }

    public int getTab() {
        return tab;
    }
}