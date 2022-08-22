package com.company.errors;

public class YAPLError {
    private String message;
    private int column, row;
    public YAPLError(
        String message,
        int column,
        int row
    ){
        this.message = message;
        this.row = row;
        this.column = column;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorText(){
        return "Se econtro un error.\n" + getMessage() + "\n" +
                "En la linea " + getRow() + "\n" +
                "En la columna " + getColumn();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
