package com.company.visitor;

import com.company.tables.Type;
import com.company.tables.TypesTable;

public class VisitorTypeResponse {
    private final Type type;
    private final Exception error;

    public VisitorTypeResponse(Type type, Exception error) {
        this.type = type;
        this.error = error;
    }

    public VisitorTypeResponse(Type type) {
        this.type = type;
        this.error = null;
    }

    public VisitorTypeResponse(Exception error) {
        this.type = null;
        this.error = error;
    }

    public Type getType() {
        return type;
    }

    public Exception getError() {
        return error;
    }

    public String getId(){
        if (type != null){
            return type.getId();
        }
        if (isError()){
            return "(no se pudo resolver probablemente no fue declarada)";
        }
        return "(void)";
    }

    public VisitorTypeResponse getMCT(VisitorTypeResponse right){
        if (isVoid() || right.isVoid()){
            return getVoidResponse();
        }
        if (isError() || right.isError()){
            return  new VisitorTypeResponse(null, new Exception("no se pudo resolver la expresión"));
        }
        if (this.getId().equals(right.getId())){
            return this;
        }
        //Case they are valid classes get the lowest option
        VisitorTypeResponse bigger;
        VisitorTypeResponse lower;
        if (right.getType().getDepth() > this.getType().getDepth()){
            bigger = right;
            lower = this;
        }else {
            bigger = this;
            lower = right;
        }
        //While is accepted meaning is not something is valid for both
        while (!bigger.isInAncestors(lower.getType())){
            lower = lower.getType().getParent().toResponse();
        }

        return lower;

//        while (!lower.getType().getId().equals(bigger.getType().getId()) && lower.getType().getDepth() < bigger.getType().getDepth()){
//            bigger = bigger.getType().getParent().toResponse();
//        }
//
//        if (bigger.getType().getId().equals(lower.getType().getId())){
//            return bigger;
//        }
//        return TypesTable.getInstance().getObjectType().toResponse();
    }

    public boolean isVoid(){
        return type == null && error == null;
    }

    public boolean isError(){
        return type == null && error != null;
    }

    public boolean isValid(){
        return type != null && error == null;
    }

    public boolean acceptsType(Type acceptType){
        //Is void it does not matter
        if (isVoid()){
            return false;
        }
        if (type != null){
            return type.acceptsType(acceptType);
        }
        return false;
    }

    public boolean isInAncestors(Type toCheckInAncestor){
        if (isVoid() || type == null || isError()){
            return false;
        }
        return  type.isInAncestors(toCheckInAncestor);
    }

    public static VisitorTypeResponse getVoidResponse(){
        return new VisitorTypeResponse(null, null);
    }

    public static VisitorTypeResponse getErrorResponse(String error){
        return new VisitorTypeResponse(null, new Exception(error));
    }
}
