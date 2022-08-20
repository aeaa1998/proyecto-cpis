package com.company.errors;

public class ParameterMethodRedeclaration  extends Exception {
    private final String method;
    private final String parameter;

    private final int column;
    private final int line;


    public ParameterMethodRedeclaration(String method, String parameter, int column, int line) {
        super(
                "El parámetro " + parameter + " en la función " + method +" ya ha sido delcarado.\n" +
                "Error en la linea: " + line + " Columna: " + column
        );
        this.method = method;
        this.parameter = parameter;
        this.column = column;
        this.line = line;
    }

    public String getMethod() {
        return method;
    }

    public String getParameter() {
        return parameter;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }
}
