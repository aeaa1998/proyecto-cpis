package com.company.tables;

import com.company.utils.MemoryType;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    public static int stackOffset = 0;
    public static int maxStackOffset = 0;
    //This is the initial offset always
    public static int heapOffset = 0;
    private String id;
    //delimiter
    int offset = 0;
    int space = 0;
    static int temporalCounter = 0;
    boolean isClass;

    private final HashMap<String, Symbol> symbols = new HashMap<>();
    public static final HashMap<String, Symbol> temporals = new HashMap<>();

    public ArrayList<String> getSymbolOrder() {
        return symbolOrder;
    }

    //We will have the order of the symbols
    private final ArrayList<String> symbolOrder = new ArrayList<>();

    public SymbolTable(String id, boolean isClass, int offset) {
        this.id = id;
        this.isClass = isClass;
        this.offset = offset;
    }

    public Symbol getSymbolByName(String name) {
        return symbols.get(name);
    }

    public Symbol storeTemporal(String typeName, MemoryType memory){
        String symbolName = "tmp"+ temporalCounter;
        return this.storeTemporal(symbolName, typeName, memory);
    }

    public Symbol storeTemporal(String temporalName, String typeName, MemoryType memory){
        if (temporals.containsKey(temporalName)){
            return temporals.get(getTemporalName());
        }

        storeSymbol(temporalName, typeName, memory);
//        temporals.put(getTemporalName(), SymbolStack.getInstance().getSymbolInAnyScope(temporalName,1,1).getSymbolFound());
        temporalCounter += 1;
//        temporalCounter = temporalCounter % 3;
        return this.getSymbolByName(temporalName);
    }

    public Symbol storeSymbol(Symbol temporal){
        symbols.put(temporal.getId(), temporal);
        symbolOrder.add(temporal.getId());

        //if it is placed in the heap we use the total size
        if (temporal.getMemoryTypePlacement() == MemoryType.Heap){
            temporal.setOffset(heapOffset);
            //We add the full size
//            heapOffset += temporal.getAssociatedType(0, 0).getType().getReferenceSize();
            heapOffset += temporal.getAssociatedType(0, 0).getType().getTotalSize();
        }else{
            temporal.setOffset(stackOffset);
            //We add the full size
            stackOffset += temporal.getAssociatedType(0, 0).getType().getReferenceSize();
            if (stackOffset > maxStackOffset){
                maxStackOffset = stackOffset;
            }
        }
        return this.getSymbolByName(temporal.getId());
    }

    public String getTemporalName(){
        int holder = temporalCounter;
        temporalCounter += 1;
        return "tmp"+ holder;
    }

    /**
     *
     * @param id [String] id of the symbol
     * @param typeName [String] The name of the type
     * @return [boolean] If the assignment was successful returns true, else returns false meaning it is trying to
     * override a prev symbol
     */
    public boolean storeSymbol(String id, String typeName, MemoryType memoryPlacement) {
        Symbol symbol = new Symbol(id, typeName, memoryPlacement);
        String name = symbol.getId();
        if (memoryPlacement == MemoryType.Heap){
            symbol.setOffset(heapOffset);
            //We add the full size
            heapOffset += symbol.getAssociatedType(0, 0).getType().getReferenceSize();
        }else{
            symbol.setOffset(stackOffset);
            //We add the full size
            stackOffset += symbol.getAssociatedType(0, 0).getType().getReferenceSize();
        }
        if(symbols.containsKey(name)){
            return false;
        }else{
            symbols.put(name, symbol);
            symbolOrder.add(name);
            return true;
        }

    }

    public void cleanOffsets(){
        for (int i = 0; i < symbolOrder.size(); i++) {
            Symbol currentSymbol = symbols.get(symbolOrder.get(i));
            if (currentSymbol.getMemoryTypePlacement() == MemoryType.Heap){
                heapOffset -= currentSymbol.getAssociatedType(0, 0).getType().getReferenceSize();
            }else{
                stackOffset -= currentSymbol.getAssociatedType(0, 0).getType().getReferenceSize();
            }
        }
        stackOffset  = 0;
        heapOffset = 0;
        temporals.clear();
    }

    public String getId() {
        return id;
    }
}
