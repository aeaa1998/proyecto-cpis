package com.company.visitor;

import com.company.tables.Method;
import com.company.tables.Symbol;
import com.company.tables.Type;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.HashMap;

public class YAPLVisitorContext extends ParserRuleContext {
    public Type typeScope;
    public Method method;
    public Symbol symbolToAssign;
    public YAPLVisitorContext(){}
    public YAPLVisitorContext(ParserRuleContext parent, int invokingStateNumber) {
        super(parent, invokingStateNumber);
    }
}
