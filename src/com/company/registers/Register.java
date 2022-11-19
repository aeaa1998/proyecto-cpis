package com.company.registers;

import com.company.tables.Symbol;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Register implements Comparable<Register> {

    private String id;

    private  Register (String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    static ArrayList<Register> valuesRegisters = new ArrayList<>(
            Arrays.asList(
                    //2-3
                    new Register("$v0"),
                    new Register("$v1")
            )
    );

    static ArrayList<Register> argumentsRegisters = new ArrayList<>(
            Arrays.asList(
                    //4-7
                    new Register("$a0"),
                    new Register("$a1"),
                    new Register("$a2"),
                    new Register("$a3")
            )
    );

    static ArrayList<Register> savedValuesRegister = new ArrayList<>(
            Arrays.asList(
                    //16-23
                    new Register("$s0"),
                    new Register("$s1"),
                    new Register("$s2"),
                    new Register("$s3"),
                    new Register("$s4"),
                    new Register("$s5"),
                    new Register("$s6"),
                    new Register("$s7")
                    )
    );

    public static ArrayList<Register> temporariesRegisters = new ArrayList<>(
            Arrays.asList(
                    //8-15
                    new Register("$t0"),
                    new Register("$t1"),
                    new Register("$t2")
//                    new Register("$t3")
//                    new Register("$t4"),
//                    new Register("$t5"),
//                    new Register("$t6"),
//                    new Register("$t7")
                    //24-25
//                    new Register("$t8")
//                    new Register("$t9")
            )
    );

    public static Register heapRegister = new Register("$t9");
    public static Register stackRegister = new Register("$sp");
    public static Register zeroRegister = new Register("$zero");
    public static Register registerReturnAddress = new Register("$ra");

    public static PriorityQueue<Register> getValidRegisters(){
        PriorityQueue<Register> queue = new PriorityQueue<Register>();
        queue.addAll(temporariesRegisters);
//        queue.addAll(savedValuesRegister);

        return queue;
    }

    public static HashMap<Register, ArrayList<Symbol>> getRegistersMapped(){
        HashMap<Register, ArrayList<Symbol>> registers = new HashMap<>();
        for (Register temp:  temporariesRegisters){
            registers.put(temp, new ArrayList<>());
        }
//        for (Register temp:  savedValuesRegister){
//            registers.put(temp, null);
//        }
        return registers;
    }

    @Override
    public int compareTo(@NotNull Register o) {
        if (o.id.equals(id)) return 0;
        return id.compareTo(o.id);
    }
}
