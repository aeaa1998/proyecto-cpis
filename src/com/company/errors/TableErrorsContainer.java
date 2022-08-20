package com.company.errors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TableErrorsContainer {
    private final ArrayList<Exception> errors;
    private static TableErrorsContainer instance;

    private TableErrorsContainer(){
        this.errors  = new ArrayList<>();
    }

    public void addError(Exception e){
        errors.add(e);
    }

    public void printStackTrace(){
        for (Exception e : errors){
            e.printStackTrace();
        }
    }

    public ArrayList<Exception> getErrors(){
        return errors;
    }

    public static TableErrorsContainer getInstance() {
        if(instance == null){
            instance = new TableErrorsContainer();
        }
        return instance;
    }



}
