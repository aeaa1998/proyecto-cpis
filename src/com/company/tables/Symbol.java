package com.company.tables;

import com.company.errors.SymbolErrorsContainer;
import com.company.registers.Register;
import com.company.utils.MemoryType;
import com.company.visitor.VisitorTypeResponse;

public class Symbol {

    private  String id, typeName;
    private int scope = 0, offset = 0;
    //Tells where the offset is
    private MemoryType memoryTypePlacement;

    public Symbol(String id, String typeName, MemoryType memoryTypePlacement){
        this.id = id;
        this.typeName = typeName;
        this.memoryTypePlacement = memoryTypePlacement;

    }

    public void setTypeName(String name){
        typeName = name;
    }

    public String getId(){
        return id;
    }
    public int getOffset() { return offset; }
    public String getOffsetAndMemory(){
        return getOffset() + " offset " + getMemoryTypePlacement().getName();
    }

    public String getActionLoad(){
        if (getMemoryTypePlacement() == MemoryType.Heap){
            return "la";
        }
        return "lw";
    }
    public String getOffsetAndMemoryUsable(){
        if (getMemoryTypePlacement() == MemoryType.Heap){
            return getOffset() + "(" + Register.heapRegister.getId() + ")";
        }
        return getOffset() + "(" + Register.stackRegister.getId() + ")";
    }
    public void setOffset(int offset) {
        this.offset = offset;
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

    public MemoryType getMemoryTypePlacement() {
        return memoryTypePlacement;
    }
}
