package com.company.tables;

public class SymbolTableResponse {
    private final Symbol symbolFound;
    private final SymbolTable scopeFound;

    public SymbolTableResponse(Symbol symbolFound, SymbolTable scopeFound) {
        this.symbolFound = symbolFound;
        this.scopeFound = scopeFound;
    }

    public Symbol getSymbolFound() {
        return symbolFound;
    }

    public SymbolTable getScopeFound() {
        return scopeFound;
    }
}
