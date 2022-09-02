package com.company.tables;

import com.company.errors.TableErrorsContainer;
import com.company.errors.TypeRedeclarationException;
import com.company.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class TypesTable {

    /**
     * Intended only to be a singleton
     */
    private TypesTable(){
        //Set base types
        types = new HashMap<>();
        setUp();
    }

    private void setUp(){
        types.put("Object", Type.getObjectType());
        types.put(Constants.String, Type.getStringType());
        types.put("IO", Type.getIOType());
        types.put("Int", Type.getIntType());
        types.put("Bool", Type.getBoolType());
    }

    private static TypesTable instance;

    public static TypesTable getInstance(){
        if (instance == null){
            instance = new TypesTable();
        }
        return instance;
    }

    public void clear(){
        types.clear();
        setUp();
    }

    public void buildTables(){
        for(String key : types.keySet()){
            types.get(key).build();
        }
        //Check it has Main class and main method
        Type type = types.get("Main");
        if (type == null){
            TableErrorsContainer.getInstance().addError(
                    "Clase Main no ha sido definida y es obligatoria",
                    0,
                    0
            );
        }else {
            Method method = type.getMethod("main", new ArrayList<>());
            if (method == null){
                TableErrorsContainer.getInstance().addError(
                        "Dentro de la clase Main la función main es obligatoria.\n" +
                                "Con la siguiente firma: main()" ,
                        type.getColumn(),
                        type.getLine()
                );
            }
        }

    }

    private final HashMap<String, Type> types;

    public Type getBoolType() {
        return types.get("Bool");
    }

    public Type getIntType() {
        return types.get("Int");
    }

    public Type getStringType() {
        return types.get(Constants.String);
    }

    public Type getIOType() {
        return types.get("IO");
    }

    public Type getObjectType() {
        return types.get("Object");
    }

    public Type getTypeByName(String name) {
        return types.get(name);
    }

    /**
     * @param type [Type] The type instance to store
     * @return [Type] Returns the type created or already existing
     */
    public Type storeType(Type type) {
        //Handle error
        String name = type.getId();
        if(types.containsKey(name)){
            TableErrorsContainer.getInstance().addError(
                    "La clase " + name + " ya ha sido declarada previamente.",
                    type.getColumn(),
                    type.getLine()
            );
        }else{
            types.put(name, type);
        }
        return types.get(name);
    }
}
