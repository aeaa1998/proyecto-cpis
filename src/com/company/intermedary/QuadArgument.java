package com.company.intermedary;

import com.company.tables.Symbol;
import com.company.tables.Type;
import com.company.utils.MemoryType;

public class QuadArgument {
    public Symbol getSymbol() {
        return symbol;
    }

    //The expression found in other terms the string
    //it can be t1, 1 or a parameter like d
    private String expression;
    private Symbol symbol;
    private Type typeAssociated;

    //The type.. a function, a primitive to tell what it is holding
    private QuadArgumentType type;

    private MemoryType memoryType;

    private QuadArgument(String expression, QuadArgumentType type, MemoryType memoryType) {
        this.expression = expression;
        this.type = type;
        this.memoryType = memoryType;
    }

    public static QuadArgument createConstantArgument(String primitive){
        return new QuadArgument(
                primitive, QuadArgumentType.Constant, null
        );
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
                String.valueOf(symbol.getId()), QuadArgumentType.Symbol, symbol.getAssociatedType(0,0).getType().getMemory()
        );
        arg.symbol = symbol;
        return arg;
    }

    public String getExpression() {
        return expression;
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
}
