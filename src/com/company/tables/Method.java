package com.company.tables;

import com.company.errors.*;
import com.company.visitor.VisitorTypeResponse;

import java.util.ArrayList;
import java.util.List;

public class Method {
    private final String id;
    private VisitorTypeResponse returnType;
    private final ArrayList<VisitorTypeResponse> params;
    //It has the type of the param in the ith position
    private final ArrayList<String> paramsStrings;
    private final ArrayList<String> paramsStringNames;
    private final String returnTypeName;
    private final int column;
    private final int line;

    public Method(String id, String returnType, ArrayList<String> params, int column, int line) {
        this.id = id;
        this.returnTypeName = returnType;
        this.paramsStrings = params;
        this.column  = column;
        this.line = line;
        this.params = new ArrayList<VisitorTypeResponse>();
        paramsStringNames = new ArrayList<>();
    }

    public Method(String id, String returnType, int column, int line) {
        this.id = id;
        this.returnTypeName = returnType;
        this.paramsStrings = new ArrayList<>();
        this.column  = column;
        this.line = line;
        this.params = new ArrayList<VisitorTypeResponse>();
        paramsStringNames = new ArrayList<>();
    }

    public void addParamString(String name, String param, int column, int line){
        if (paramsStringNames.contains(name)){
            //Add the parameter error
            TableErrorsContainer.getInstance().addError(
                    "El parametro " + param + " ya ha sido declarado en la función " + id,
                    column,
                    line
            );
        }else {
            // Add the parameter
            paramsStrings.add(param);
            paramsStringNames.add(name);
        }
    }

    private TypeNotDeclared getReturnTypeNotDeclaredError(){
        return new TypeNotDeclared(
                "El método " + id,
                returnTypeName,
                column,
                line
        );
    }

    public VisitorTypeResponse check(String returnTypeName){
        return check(TypesTable.getInstance().getTypeByName(returnTypeName));
    }


    public VisitorTypeResponse check(Type returnType){

        //Check returnType is valid
        if (returnType == null){
            YAPLError error =  SymbolErrorsContainer.getInstance().addError(
                     "El método " + id + " utiliza la clase " + returnTypeName + " la cual no esta definida",
                    column,
                    line
            );

            return VisitorTypeResponse.getErrorResponse(error.getMessage());
        }else{
            return returnType.toResponse();
        }
    }

//    public void addParameterType(VisitorTypeResponse type){
//        params.add(type);
//    }

    public VisitorTypeResponse getParameterTypeAtPosition(int index){
        //Index does not exist here
        if (index + 1 > params.size()){
            //Assign it
            params.add(index, TypesTable.getInstance().getTypeByName(paramsStrings.get(index)).toResponse());
        }
        return params.get(index);
    }
    public VisitorTypeResponse getReturnType(Type invoker) {
//        if (!built){
            //If it is self return the invoker
            if (this.returnTypeName.equalsIgnoreCase("SELF_TYPE")){
                return check(invoker.getId());
            }else {
                return check(returnTypeName);
            }

//        }
//        return returnType;
    }

    public String getId() {
        return id;
    }

    public String getSignature() {
        StringBuilder signature = new StringBuilder();
        if (!paramsStrings.isEmpty()){
            signature.append(" ");
            for(String param: paramsStrings){
                signature.append(param);
            }
        }
        return id + signature.toString();
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
    public int getParamCount() {
        return paramsStrings.size();
    }
}
