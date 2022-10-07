package com.company.utils;

public enum MemoryType {
    Heap, Stack;

    public String getName(){
        if (this == Heap){
            return "heap";
        }
        return "stack";
    }
}
