package com.company.registers

import com.company.tables.Symbol
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class AddressDescriptor {
    var descriptor: HashMap<Symbol, ArrayList<Register>>  = HashMap();

    val descriptorsSnapshots: Stack<HashMap<Symbol, ArrayList<Register>>> = Stack()

    fun takeSnapShot(){
        val hashmap = HashMap<Symbol, ArrayList<Register>>()
        descriptor.forEach { t, u ->
            val arrayListCopy = ArrayList<Register>()
            arrayListCopy.addAll(u)
            hashmap[t] = u
        }
        descriptorsSnapshots.push(hashmap)
    }

    fun removeSnapShot(): HashMap<Symbol, ArrayList<Register>> {
//        descriptor = descriptorsSnapshots.pop()
        val a  = descriptorsSnapshots.pop();
        descriptor = a
        return descriptor;
    }

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