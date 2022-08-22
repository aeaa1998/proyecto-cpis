package com.company.errors;

import java.util.HashSet;

public class SymbolErrorsContainer {
    private final HashSet<YAPLError> errors;
    private static SymbolErrorsContainer instance;

    private SymbolErrorsContainer(){
        errors = new HashSet<>();
    }

    public void resetErrors(){
        errors.clear();
    }

    public YAPLError addError(String message, int column, int row){
        YAPLError err = new YAPLError(message, column, row);
        errors.add(
            err
        );
        return err;
    }

    public void printStackTrace(){
        for (YAPLError e : errors){
//            e.printStackTrace();
            System.err.println("Se econtro un error.\n" + e.getMessage());
            System.err.println("En la linea: " + e.getRow());
            System.err.println("Y la columna: " + e.getColumn() + "\n\n");
        }
    }

    public static SymbolErrorsContainer getInstance() {
        if(instance == null){
            instance = new SymbolErrorsContainer();
        }
        return instance;
    }

    public HashSet<YAPLError> getErrors() {
        return errors;
    }


}
