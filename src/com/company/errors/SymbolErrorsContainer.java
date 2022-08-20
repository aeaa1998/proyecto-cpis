package com.company.errors;

import java.util.HashSet;

public class SymbolErrorsContainer {
    private final HashSet<Exception> errors;
    private static SymbolErrorsContainer instance;

    private SymbolErrorsContainer(){
        errors = new HashSet<>();
    }

    public void addError(Exception e){
        errors.add(e);
    }

    public void printStackTrace(){
        for (Exception e : errors){
            e.printStackTrace();
        }
    }

    public static SymbolErrorsContainer getInstance() {
        if(instance == null){
            instance = new SymbolErrorsContainer();
        }
        return instance;
    }

    public HashSet<Exception> getErrors() {
        return errors;
    }


}
