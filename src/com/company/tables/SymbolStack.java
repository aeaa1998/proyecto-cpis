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
        SymbolTable nullableCurrentScope = safeCurrentScope();
        int offset = 0;
        if (nullableCurrentScope != null){
            //if there is a scope present add the offset else is 0
            offset = nullableCurrentScope.offset;
        }
        SymbolTable table = new SymbolTable(id, true, offset);
        stack.add(table);
        return table;
    }

    public SymbolTable addFunctionScope(String id){
        //We dont need to check if there is a scope or not there will always be a scope here
        SymbolTable table = new SymbolTable(id, false, currentScope().offset);
        stack.add(table);
        return table;
    }

    public SymbolTable addLetScope(String id){
        //We dont need to check if there is a scope or not there will always be a scope here
        SymbolTable table = new SymbolTable(currentScope().getId()+"_"+id, false, currentScope().offset);
        stack.add(table);
        return table;
    }

    public SymbolTable addBracketScope(){
        //We dont need to check if there is a scope or not there will always be a scope here
        SymbolTable table = new SymbolTable(currentScope().getId()+"_"+"bracket", false, currentScope().offset);
        stack.add(table);
        return table;
    }

    public SymbolTable currentScope(){
        return stack.peek();
    }

    public SymbolTable safeCurrentScope(){
         try {
             return currentScope();
        } catch (Exception e){
             return null;
        }
    }

    public SymbolTable globalScope(){
        return stack.firstElement();
    }

    public void removeCurrentScope(boolean deletes){
        SymbolTable table = stack.pop();
        if (deletes) table.cleanOffsets();
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
                "No se pudo resolver el tipo de el simbolo: " + symbolId + "\n",
                column,
                line
        );
        return null;
    }



}
