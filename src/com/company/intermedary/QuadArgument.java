package com.company.intermedary;

import com.company.tables.Method;
import com.company.tables.Symbol;
import com.company.tables.Type;
import com.company.utils.MemoryType;

import java.util.ArrayList;

public class QuadArgument {
    public Symbol getSymbol() {
        return symbol;
    }

    //The expression found in other terms the string
    //it can be t1, 1 or a parameter like d
    private String expression;
    private Symbol symbol;
    private Type typeAssociated;
    public ArrayList<Symbol> symbolsToUse = new ArrayList<>();
    public Method method = null;
    //The type.. a function, a primitive to tell what it is holding
    private QuadArgumentType type;

    private MemoryType memoryType;

    private QuadArgument(String expression, QuadArgumentType type, MemoryType memoryType) {
        this.expression = expression;
        this.type = type;
        this.memoryType = memoryType;
    }

    public static QuadArgument createSymbolList(ArrayList<Symbol> symbols){
        QuadArgument arg = new QuadArgument(
              "lsit"  , QuadArgumentType.Symbol, null
        );
        arg.symbolsToUse.addAll(symbols);
        return arg;
    }

    public static QuadArgument createConstantArgument(String primitive){
        return new QuadArgument(
                primitive, QuadArgumentType.Constant, null
        );
    }

    public static QuadArgument createMethodArgument(Method method){
        QuadArgument q = new QuadArgument(
                null, QuadArgumentType.Constant, null
        );
        q.method = method;
        return q;
    }

    public static QuadArgument createLabelRefArgument(String primitive){
        return new QuadArgument(
                primitive, QuadArgumentType.LabelRef, null
        );
    }



    public static QuadArgument createSelfArgument(String self){
        return new QuadArgument(
                self, QuadArgumentType.Self, MemoryType.Heap
        );
    }

    public static QuadArgument createSymbolArgument(Symbol symbol){
        MemoryType mem = symbol.getMemoryTypePlacement();
        QuadArgument arg = new QuadArgument(
//                String.valueOf(symbol.getOffset() + " offset " + mem.getName()), QuadArgumentType.Symbol, mem
//                String.valueOf(symbol.getId()), QuadArgumentType.Symbol, symbol.getAssociatedType(0,0).getType().getMemory()
                String.valueOf(symbol.getId()), QuadArgumentType.Symbol, MemoryType.Stack
        );
        arg.symbol = symbol;
        return arg;
    }

    public String getExpression() {
        return expression;
    }

    public String getFullExpression(){
        if (getType() == QuadArgumentType.Self){
            return "0 en " + getMemoryType().getName();
        }else if (getType() == QuadArgumentType.Constant) {
            return getExpression();
        }else{
            Symbol paramSymbol = getSymbol();
            return paramSymbol.getOffsetAndMemory();
        }
    }

    public QuadArgumentType getType() {
        return type;
    }

    public MemoryType getMemoryType() {
        return memoryType;
    }

    public Type getTypeAssociated() {
        return typeAssociated;
    }

    public boolean isSymbol(){
        return this.symbol != null;
    }
}
