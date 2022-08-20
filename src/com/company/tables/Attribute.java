package com.company.tables;

import com.company.errors.SymbolErrorsContainer;
import com.company.errors.TypeNotDeclared;
import com.company.visitor.VisitorTypeResponse;

public class Attribute {
    private final String id;
    private Type type;
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
            Exception error = new TypeNotDeclared(
                    "El attributo " + id,
                    typeName,
                    column,
                    line
            );
            SymbolErrorsContainer.getInstance().addError(error);
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
        if (typeName.equalsIgnoreCase("SELF_TYPE")){
            return check(invoker.getId());
        }
        return check(typeName);
    }
    public String getId() { return id; };

    public int getSize(){
        return 0;
    }

}
