package com.company.registers

import com.company.tables.Symbol
import java.util.*

class AddressDescriptor {
    val descriptor: HashMap<Symbol, ArrayList<Register>>  = HashMap();



    fun getRegistersForSymbol(sym: Symbol): ArrayList<Register> {
        if (!descriptor.containsKey(sym)){
            descriptor.put(sym, ArrayList())
        }
        return descriptor[sym] ?: ArrayList();
    }

    fun setRegisterForSymbol(sym: Symbol, register: Register) {
        if (!descriptor.containsKey(sym)){
            descriptor.put(sym, ArrayList())
        }
        descriptor[sym]?.add(register)
    }


    companion object {
        private val instance: AddressDescriptor = AddressDescriptor()
//        fun getInstance(): AddressDescriptor {
//            return instance;
//        }
    }
}