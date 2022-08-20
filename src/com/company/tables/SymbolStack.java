package com.company.tables;

import com.company.errors.SymbolErrorsContainer;

import java.util.NoSuchElementException;
import java.util.Stack;

public class SymbolStack {
    private final Stack<SymbolTable> stack = new Stack<>();
    private static SymbolStack instance;
    private SymbolStack(){ };

    public static SymbolStack getInstance(){
        if (instance == null){
            instance = new SymbolStack();
        }
        return instance;
    }

    public SymbolTable addClassScope(String id){
        SymbolTable table = new SymbolTable(id, true);
        stack.add(table);
        return table;
    }

    public SymbolTable addFunctionScope(String id){
        SymbolTable table = new SymbolTable(id, false);
        stack.add(table);
        return table;
    }

    public SymbolTable addLetScope(String id){
        SymbolTable table = new SymbolTable(currentScope().getId()+"_"+id, false);
        stack.add(table);
        return table;
    }

    public SymbolTable currentScope(){
        return stack.peek();
    }

    public SymbolTable globalScope(){
        return stack.firstElement();
    }

    public void removeCurrentScope(){
        stack.pop();
    }

    private SymbolTableResponse getSymbolFromTable(String symbolId, SymbolTable table, int column, int line){
        Symbol symbol = table.getSymbolByName(symbolId);
        if (symbol != null){
            return new SymbolTableResponse(symbol, table);
        }

        return null;
    }

    private SymbolTableResponse getSymbolInAnyScope(String symbolId, int position, int column, int line){
        SymbolTable currentScope = stack.get(position);
        SymbolTableResponse response = getSymbolFromTable(symbolId, currentScope, column, line);

        //It was found on current scope
        if (response != null){
            return response;
        }
        //The current and global scope are not the same
        //Return the global scope
        else if (!globalScope().getId().equals(currentScope.getId())){
            return getSymbolInAnyScope(symbolId, position - 1, column, line);
        }

        return null;
    }

    public SymbolTableResponse getSymbolInAnyScope(String symbolId, int column, int line){
        SymbolTableResponse response = getSymbolFromTable(symbolId, currentScope(), column, line);

        //It was found on current scope
        if (response != null){
            return response;
        }
        //The current and global scope are not the same
        //Go up in the scopes
        else if (!globalScope().getId().equals(currentScope().getId())){
            int position = stack.size() - 1;
            //Get up
            return getSymbolInAnyScope(symbolId, position - 1, column, line);
        }
        SymbolErrorsContainer.getInstance().addError(
                new Exception(
                        "No se pudo resolver el tipo de el simbolo: " + symbolId + "\n" +
                                "Columna: " + column + ", linea: " + line
                )
        );
        return null;
    }



}
