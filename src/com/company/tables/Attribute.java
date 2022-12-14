package com.company.tables;

import com.company.errors.SymbolErrorsContainer;
import com.company.errors.TypeNotDeclared;
import com.company.errors.YAPLError;
import com.company.utils.Constants;
import com.company.visitor.VisitorTypeResponse;

public class Attribute {
    private final String id;
    private final String typeName;
    private final int column;
    private final int line;


    public Attribute(String id, String type, int column, int line){
        this.id = id;
        typeName = type;
        this.line = line;
        this.column = column;
    }

    private VisitorTypeResponse check(String name){
        return check(TypesTable.getInstance().getTypeByName(name));
    }

    public VisitorTypeResponse check(Type type){
        //Type of the attribute has not been declared
        if (type == null){
            YAPLError error = SymbolErrorsContainer.getInstance().addError(
                    "El atributo " + id + " utiliza la clase " + typeName + " que no se encuentra definida",
                    column,
                    line
            );
            return VisitorTypeResponse.getErrorResponse(error.getMessage());
        }
        return type.toResponse();
    }
    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
    public VisitorTypeResponse getType(Type invoker) {
        //In case is self_type get the moment
        if (typeName.equalsIgnoreCase(Constants.SELF_TYPE)){
            return check(invoker.getId());
        }
        return check(typeName);
    }
    public String getId() { return id; };

    public int getSize(Type invoker){
        VisitorTypeResponse response;
        //Get the type
        if (typeName.equalsIgnoreCase(Constants.SELF_TYPE)){
            response = check(invoker.getId());
        }else{
            response =  check(typeName);
        }
        Type responseType = response.getType();
        //It there is space return it
        if (responseType != null){
            //Build it if it is needed
            responseType.build();
            //This will be called to know the space so just return the reference space
            return response.getType().getReferenceSize();
        }
        return 0;
    }

}
