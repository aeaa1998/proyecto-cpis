package com.company.tables;

import com.company.errors.SymbolErrorsContainer;
import com.company.visitor.VisitorTypeResponse;

public class Symbol {
    private final String id, typeName;
    private int scope, offset;

    public Symbol(String id, String typeName){
        this.id = id;
        this.typeName = typeName;
    }

    public String getId(){
        return id;
    }

    public VisitorTypeResponse getAssociatedType(int column, int line){
        Type associatedType = TypesTable.getInstance().getTypeByName(typeName);
        if (associatedType == null){
            SymbolErrorsContainer.getInstance().addError(
                    "La variable " + id + " se declaro con el tipo " + typeName + " que no ha sido declarado " + ".\n",
                    column,
                    line
            );
            //Return error
            return new VisitorTypeResponse(null, null);
        }

        return associatedType.toResponse();
    }
}
