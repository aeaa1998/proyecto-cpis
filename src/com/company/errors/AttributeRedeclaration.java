package com.company.errors;

public class AttributeRedeclaration extends Exception {
    private final String name;
    private final String type;
    private final int column;
    private final int line;

    public AttributeRedeclaration(String name, String type, int column, int line) {
        super(
                "El attributo ya " + name + " ha sido declarado en el contexto de " + type + ".\n" +
                "Error en la linea: " + line + " Columna: " + column
        );
        this.name = name;
        this.type = type;
        this.column = column;
        this.line = line;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
