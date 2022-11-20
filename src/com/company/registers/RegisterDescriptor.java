package com.company.registers;


import com.company.tables.Symbol;

import java.util.*;

public class RegisterDescriptor {
    public HashMap<Register, ArrayList<Symbol>> descriptors = Register.getRegistersMapped();
    public Stack<HashMap<Register, ArrayList<Symbol>>> descriptorsSnapshots = new Stack<>();
    public ArrayList<Symbol> getSymbolsInRegister(Register register){
        return descriptors.get(register);
    }

    public HashMap<Register, ArrayList<Symbol>> takeSnapShot(){
        HashMap<Register, ArrayList<Symbol>> hashmap = new HashMap<>();
        for (Register entry:
                descriptors.keySet()) {
            ArrayList<Symbol> arrayListCopy = new ArrayList<Symbol>(descriptors.get(entry));
            hashmap.put(entry, arrayListCopy);
        }

        descriptorsSnapshots.push(hashmap);
        return hashmap;
    }

    public HashMap<Register, ArrayList<Symbol>> removeSnapShot(){
        HashMap<Register, ArrayList<Symbol>> a = descriptorsSnapshots.pop();
        descriptors = a;
//        descriptors = descriptorsSnapshots.pop();
        return a;
    }

    public void setSymbolInRegister(Register register, Symbol symbol){
        descriptors.get(register).add(symbol);

    }

    public PriorityQueue<Register> getAvailableRegisters() {
        return availableRegisters;
    }

    PriorityQueue<Register> availableRegisters = Register.getValidRegisters();

    private static RegisterDescriptor instance;
    public RegisterDescriptor(){};
//    public static RegisterDescriptor getInstance(){
//        if (instance == null) instance = new RegisterDescriptor();
//        return instance;
//    }

    public Register getRegisterWithLessWeight(List<String> blockedRegisters) {
        int min = -1;
        Register register = null;
        for (Register keyRegister : descriptors.keySet()) {
            int size = descriptors.get(keyRegister).size();
            if (min == -1){
                register = keyRegister;
            }

            if ((min == -1 || size < min) && !blockedRegisters.contains(register.getId())){
                min = size;
            }
            //There cant be smaller than that
            if (min == 1 && register != null && !blockedRegisters.contains(register.getId())){
                return register;
            }
        }
        return register;
    }



    public void getRegisterForSymbol(Symbol symbol){
//        ArrayList<Register> registersForSymbol = AddressDescriptor.Companion.getInstance().getRegistersForSymbol(symbol);
        //Don't have registers
//        if (registersForSymbol.isEmpty()){
//
//        }
    }

}
