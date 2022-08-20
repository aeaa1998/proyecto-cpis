package com.company.tables;

import java.util.HashMap;

public class SymbolTable {
    private String id;
    //delimiter
    int offset = 0;
    int space = 0;

    boolean isClass;

    private final HashMap<String, Symbol> symbols = new HashMap<>();

    public SymbolTable(String id, boolean isClass) {
        this.id = id;
        this.isClass = isClass;
    }

    public Symbol getSymbolByName(String name) {
        return symbols.get(name);
    }

    /**
     *
     * @param symbol [Symbol] Instance of the symbol
     * @return [boolean] If the assignment was successful returns true, else returns false meaning it is trying to
     * override a prev symbol
     */
    public boolean storeSymbol(Symbol symbol) {
        String name = symbol.getId();
        if(symbols.containsKey(name)){
            return false;
        }else{
            symbols.put(name, symbol);
            return true;
        }

    }

    public String getId() {
        return id;
    }
}
