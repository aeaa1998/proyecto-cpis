package com.company.errors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TableErrorsContainer {
    private final ArrayList<YAPLError> errors;
    private static TableErrorsContainer instance;

    private TableErrorsContainer(){
        this.errors  = new ArrayList<>();
    }

    public void resetErrors(){
        errors.clear();
    }

    public YAPLError addError(
        String message,
        int column,
        int line
    ){
        YAPLError error = new YAPLError(message, column, line);
        errors.add(error);
        return error;
    }

    public void printStackTrace(){
        for (YAPLError e : errors){
//            e.printStackTrace();
            System.err.println("Se econtro un error.\n" + e.getMessage());
            System.err.println("En la linea: " + e.getRow());
            System.err.println("Y la columna: " + e.getColumn());
        }
    }



    public ArrayList<YAPLError> getErrors(){
        return errors;
    }

    public static TableErrorsContainer getInstance() {
        if(instance == null){
            instance = new TableErrorsContainer();
        }
        return instance;
    }



}
