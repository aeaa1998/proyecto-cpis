package com.company.errors;

public class TypeNotDeclared extends Exception {
    private final String userText;
    private final String parent;
    private final int column;
    private final int line;

    public TypeNotDeclared(String userText, String parent, int column, int line) {
        super(
                userText + " utiliza la clase " + parent + " la cual no fue declarada.\n" +
                "Error en la linea: " + line + ", columna: " + column
        );
        this.userText = userText;
        this.parent = parent;
        this.column = column;
        this.line = line;
    }

    public String getUserText() {
        return userText;
    }

    public String getParent() {
        return parent;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
