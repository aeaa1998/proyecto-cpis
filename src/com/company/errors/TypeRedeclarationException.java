package com.company.errors;

public class TypeRedeclarationException extends Exception {
    private final String redeclaration;
    private final int column;
    private final int line;


    public TypeRedeclarationException(String redeclaration, int column, int line) {
        super("La clase " + redeclaration + " ya ha sido declarada previamente.\n" +
                "Error en la linea: " + line + ", columna: " + column
        );
        this.redeclaration = redeclaration;
        this.column = column;
        this.line = line;
    }


    public String getRedeclaration() {
        return redeclaration;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
