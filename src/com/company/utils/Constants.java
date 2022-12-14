package com.company.utils;


import java.util.ArrayList;

public class Constants {
    static public final String SELF_TYPE  = "SELF_TYPE";
    static public final String Object  = "Object";
    static public final String Int  = "Int";
    static public final String Bool  = "Bool";
    static public final String IO  = "IO";
    static public final String String  = "String";
    static public final int BaseSpace = 8;
    static public final String classNameValue = "class name";
    static public final String vTableValue = "v name";
    //Space need for complex structures
    //We need the space for the address
    //32 bits
    static public final int ReferenceSpace = 4;
    static public final int StringSpace = 255;

    static public final String[] NonReferenceTypes = { Int, Bool };
    static public final String[] SpecialFunctions = { "out_string", "in_string", "out_int", "in_int" };
    static public final String[] NonVTableType = { "Int", "Bool", "String" };

}
