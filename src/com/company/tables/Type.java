package com.company.tables;

import com.company.errors.AttributeRedeclaration;
import com.company.errors.MethodRedeclaration;
import com.company.errors.TableErrorsContainer;
import com.company.errors.TypeNotDeclared;
import com.company.utils.Constants;
import com.company.utils.CycleStatus;
import com.company.utils.MemoryType;
import com.company.visitor.VisitorTypeResponse;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;

public class Type {
    private final String id;
    private final HashMap<String, Attribute> attributes;
    //Have the attributes ordered for knowing the offsets
    private final ArrayList<String> attributesOrdered;
    private HashMap<String, Method> methods = new HashMap<>();
    private Type parent;
    private CycleStatus status = CycleStatus.NONE;
    private final String parentName;
    private ArrayList<String> casteables = new ArrayList<String>();
    private boolean built;
    private boolean canBeInherited = true;
    private int column ;
    private int line;

    //Size in bytes of the class
    private int size;
    //Size of the parent
    private int parentSize;


    public int getDepth() {
        return depth;
    }

    private int depth = 0;


    public Type(String id, String parentName){
        this.id = id;
        this.parentName = parentName;
        attributesOrdered = new ArrayList<>();
        attributes = new HashMap<>();
        methods = new HashMap<>();
    }

    public Type(String id, String parentName, int column, int line){
        this.id = id;
        this.parentName = parentName;
        this.column = column;
        this.line = line;
        methods = new HashMap<>();
        attributes = new HashMap<>();
        attributesOrdered = new ArrayList<>();
    }

    public void build(){
        //It was already built lets not build it
        if (built){
            return;
        }
        built = true;
        //Parent block

        if (parentName != null){
            parent = TypesTable.getInstance().getTypeByName(parentName);
        }else{
            //Everyone extends object
            parent = TypesTable.getInstance().getObjectType();
        }
        //Parent not declared
        if (parent == null){
            TableErrorsContainer.getInstance().addError(
                    "La clase " + parentName + " no ha sido declarada la cual es usada como padre para " + id,
                    column,
                    line
            );
        }else{
            //Build the parent if needed
            parent.build();

            //It has a parent so lets start cycle check
            if (getHasCycle()){
                TableErrorsContainer.getInstance().addError(
                        "Hay un ciclo en la clase " + id + " de herencia.",
                        column,
                        line
                );
            }

            //Check if it is a inheritable parent
            if (!parent.canBeInherited){
                TableErrorsContainer.getInstance().addError(
                        "La clase " + id + " hereda de " + parentName + " la cual no puede ser heredada",
                        column,
                        line
                );
            }


//            this.parent = parent;
            this.depth = this.parent.depth + 1;
        }

        //Build and check attributes
        checkAttributesInParents();

        //Build and check methods
        checkMethodsInParents();

        //Check size
        calculateSize();
        built = true;
    }

    public int getTotalSize() {
        return this.size + this.parentSize;
    }

    int getReferenceSize(){
        if (!isPointer()){
            return getTotalSize();
        }
        //If it is a complex structure we return the pointer
        return Constants.ReferenceSpace;
    }

    public boolean isPointer(){
        return !Arrays.stream(Constants.NonReferenceTypes).toList().contains(id);
    }

    public MemoryType getMemory(){
        if (isPointer()){
            return MemoryType.Heap;
        }

        return MemoryType.Stack;
    }

    boolean getHasCycle(){
        boolean value = false;
        switch (status){
            case NONE -> {
                Type pointer = parent;
                status = CycleStatus.PASS;
                while (pointer != null){
                    //They have the same ids
                    if (pointer.getId().equals(id)){
                        status = CycleStatus.CYCLE;
                        //Escape from cycle
                        pointer = null;
                    }else{
                        //Go to the next parent
                        pointer = pointer.parent;
                    }
                }
                //Return result
                return getHasCycle();
            }
            case CYCLE -> {
                value =  true;
            }
            case PASS -> {
            }
        }
        return value;
    }

    private void calculateSize(){
        //The parent always will be built before
        if (parent == null){
            parentSize = 0;
        }else{
            parentSize = parent.getTotalSize();
        }
        if (this.size != 0) return;
        int current = 0;
        for(Attribute attribute: attributes.values()){
            current += attribute.getSize(this);
        }
        this.size = current;
    }

    private void checkAttributesInParents(){
        Set<String> keyset = attributes.keySet();
        for(String key: keyset){
            Attribute attribute = attributes.get(key);
            if (parent != null){
                Attribute parentAttr = parent.getAttribute(key);
                //Check declaration has been done in parent
                if (parentAttr != null){
                    //Add error
                    TableErrorsContainer.getInstance().addError(
                            "El atributo " + key +  " en la clase " + id + " ya ha sido declarada previamente.",
                            attribute.getColumn(),
                            attribute.getLine()
                    );
                }else{
                    //Attribute has not been built
//                    attribute.build();
                }
            }
        }
    }

    private void checkMethodsInParents(){
        Set<String> keyset = methods.keySet();
        for(String key: keyset){
            Method method = methods.get(key);
            if (parent != null){
//                method.build();
            }
        }
    }

    public void setAttribute(Attribute attribute){
        if (attributes.get(attribute.getId()) != null){
            TableErrorsContainer.getInstance().addError(
                    "El atributo " + attribute.getId() +  " en la clase " + id + " ya ha sido declarada previamente.",
                    attribute.getColumn(), attribute.getLine()
            );
        }else{
            attributesOrdered.add(attribute.getId());
            attributes.put(attribute.getId(), attribute);
        }
    }

    public void setMethod(Method method){
        if (methods.get(method.getSignature()) != null){
            TableErrorsContainer.getInstance().addError(
                    "El metodo " + method.getId() + " en la clase " + id + " ya ha sido declarado previamente.",
                    method.getColumn(),
                    method.getLine()
            );
        }else{
            methods.put(method.getSignature(), method);
        }

    }

    public Attribute getAttribute(String name){
        Attribute attr = attributes.get(name);
        if (attr != null){
          return attr;
        } else if (parent != null){
            return parent.getAttribute(name);
        }
        return null;
    }
    public static String getSignature(String name, List<String> params) {
        StringBuilder paramBuilder = new StringBuilder("");
        if (!params.isEmpty()){
            paramBuilder.append(" ");
            int index = 0;
            for(String param: params){
                paramBuilder.append(param);
                if (params.size() - 1 != index){
                    paramBuilder.append(" ");
                }
                index++;
            }
        }
        return name+paramBuilder;
    }
    public Method getMethod(String name, List<String> params){
        String sig = getSignature(name, params);
        Method meth = methods.get(sig);
        if (meth != null){
            return meth;
        } else if (parent != null){
            return parent.getMethod(sig);
        }
        return null;
    }

    private Method getMethod(String signature){
        Method meth = methods.get(signature);
        if (meth != null){
            return meth;
        } else if (parent != null){
            return parent.getMethod(signature);
        }
        return null;
    }
    public Type getParent() {
        return parent;
    }
    public Method getMethod(String name, String scope){
        if (scope.equals(id)){
            return methods.get(name);
        }else if (parent != null){
            return getMethod(name, scope);
        }else{
            return null;
        }

    }


    public boolean acceptsType(Type type){
        if (type == null){
            return false;
        }
        boolean accepted = this.id.equals(type.id);
        //We will check if it is false it by a cast it can be accepted
        if (!accepted){
            accepted = this.casteables.contains(type.id);
        }

        //It is not accepted but the type to check has a parent
        //maybe it is a lower implementation so lets check
        if (!accepted && type.parent != null){
            return acceptsType(type.parent);
        }
        return accepted;
    }

    public boolean isInAncestors(Type type){
        if (type == null) return false;

        boolean accepted = this.id.equals(type.id);

        //It will check if it is in one of its parents
        if (!accepted){
            //There is no such parent
            if (parent == null) return false;
            return parent.isInAncestors(type);
        }
        return true;

    }

    public String getId(){
        return this.id;
    }
    public int getLine(){
        return this.line;
    }
    public int getColumn(){
        return this.column;
    }

    public VisitorTypeResponse toResponse(){
        return new VisitorTypeResponse(this);
    }

    public void fillTable(SymbolTable symbolTable){
        if (parent != null){
            parent.fillTable(symbolTable);
            if (SymbolTable.heapOffset != parent.getTotalSize()){
                SymbolTable.heapOffset = parent.getTotalSize();
            }
        }



        for (String attributeName : attributesOrdered){

            VisitorTypeResponse attr = attributes.get(attributeName).getType(this);
            if (attr.isValid()){
                //All the attributes are going to be added in the heap
                symbolTable.storeSymbol(attributeName, attr.getId(), MemoryType.Heap);
            }


        }
    }

    public static @NotNull
    Type getObjectType() {
        Type type = new Type(Constants.Object, null);
        type.canBeInherited = true;
        type.built = true;
        type.getHasCycle();
        Method abortMethod = new Method("abort", Constants.Object, 0, 0);
        Method typeNameMethod = new Method("type_name", Constants.String, 0, 0);
//        Method copy = new Method("type_name", "Object", 0, 0);
        type.setMethod(abortMethod);
        type.setMethod(typeNameMethod);

        //Base size
        type.size = Constants.BaseSpace;
        type.parentSize = 0;
        return type;
    }

    public static @NotNull Type getIntType() {
        Type type = new Type(Constants.Int, Constants.Object);
        type.getHasCycle();
        type.casteables.add(Constants.Bool);
        type.canBeInherited = false;
        //4 bytes needed for the int type
        type.size = 4;
        return type;
    }



    public static @NotNull Type getBoolType() {
        Type type = new Type(Constants.Bool, Constants.Object);
        type.getHasCycle();
        type.casteables.add(Constants.Int);
        type.canBeInherited = false;
        // 1 byte needed for bool
        type.size = 1;
        return type;
    }

    public static @NotNull Type getStringType() {
        Type type = new Type(Constants.String, Constants.Object);
        Method length = new Method("length", Constants.Int, 0, 0);
        Method concat = new Method("concat", Constants.String, 0, 0);
        concat.addParamString("s", Constants.String, 0, 0);
        Method substr = new Method("substr", Constants.String, 0, 0);
        substr.addParamString("i", Constants.Int, 0, 0);
        substr.addParamString("l", Constants.Int, 0, 0);
        type.setMethod(length);
        type.setMethod(concat);
        type.setMethod(substr);
        type.getHasCycle();
        type.canBeInherited = false;
        //Space needed for the reference
        type.size = Constants.StringSpace;
        return type;
    }

    public static @NotNull Type getIOType() {
        Type type = new Type(Constants.IO, Constants.Object);
        type.getHasCycle();
        Method outString = new Method("out_string", Constants.SELF_TYPE, 0, 0);
        outString.addParamString("x", Constants.String, 0, 0);
        Method typeNameMethod = new Method("out_int", Constants.SELF_TYPE, 0, 0);
        typeNameMethod.addParamString("x", Constants.Int, 0 , 0);
        Method in_string = new Method("in_string", Constants.String, 0, 0);
        Method in_int = new Method("in_int", Constants.Int, 0, 0);
        type.setMethod(outString);
        type.setMethod(typeNameMethod);
        type.setMethod(in_string);
        type.setMethod(in_int);
        type.canBeInherited = true;
        //It is needed a reference
        type.size = Constants.ReferenceSpace;
//        type.size = 0;
        return type;
    }
}
