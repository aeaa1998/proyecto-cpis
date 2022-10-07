package com.company.visitor;

import com.company.errors.SymbolErrorsContainer;
import com.company.intermedary.QuadArgument;
import com.company.intermedary.QuadType;
import com.company.intermedary.Quadruplets;
import com.company.intermedary.ThreeAddressCode;
import com.company.tables.*;
import com.company.utils.Constants;
import com.company.utils.MemoryType;
import com.company.yapl.YAPLParser;
import com.company.yapl.YAPLVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YAPLSymbolsVisitor extends AbstractParseTreeVisitor<VisitorTypeResponse> implements YAPLVisitor<VisitorTypeResponse> {
    private final ThreeAddressCode TAC = new ThreeAddressCode();
    private final ArrayList<Quadruplets> quadsPropertyForClass = new ArrayList<>();
    int ifCounter = 0;
    int whileCounter = 0;
    int tabCounter = 0;

    public ThreeAddressCode getTAC() { return TAC; }

    /**
     * First node visits the whole program
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitProgram(YAPLParser.ProgramContext ctx) {
        //Enter the program
        return visitChildren(ctx);
    }

    /**
     * Visits a class declaration and visit the nodes
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitClass_grammar(YAPLParser.Class_grammarContext ctx) {
        //Continue with the grammar to start
        String className = ctx.className.getText();
        Type type = TypesTable.getInstance().getTypeByName(className);
        SymbolStack stack = SymbolStack.getInstance();
        //Add current class scope
        SymbolTable table = stack.addClassScope(className);
        type.fillTable(table);
        //Set the type
        if(ctx.features() != null){
            ctx.features().typeScope = type;
        };
        //Visit all
        visitChildren(ctx);
         //Escape the scope
        //TODO: here implement

        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createLabelRefArgument("instantiate_new_" + className),
                        null,
                        null,
                        tabCounter
                )
        );
        tabCounter += 1;
        TAC.addQuad(
                new Quadruplets(
                        QuadType.AssignSpaceHeap,
                        QuadArgument.createConstantArgument(String.valueOf(type.getTotalSize())),
                        null,
                        null,
                        tabCounter
                )
        );
        for (Quadruplets quadClass: quadsPropertyForClass) {
            quadClass.setTab(tabCounter);
            TAC.addQuad(quadClass);
        }
        tabCounter -= 1;
        stack.removeCurrentScope(true);
        //assure
        SymbolTable.heapOffset = 0;
        SymbolTable.stackOffset = 0;
        quadsPropertyForClass.clear();

        //Returns void
        return null;
    }

    /**
     * Visits a feature of a class it can be a declaration of a property or a function declaration
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitFeatures(YAPLParser.FeaturesContext ctx) {
        //Binf values
        for(YAPLParser.Class_featureContext classFeature : ctx.class_feature()){
            classFeature.typeScope = ctx.typeScope;
        }
        //Visit each feature
        return visitChildren(ctx);
    }

    /**
     * Visits a feature of a class it can be a declaration of a property or a function declaration
     * @param ctx the parse tree
     * @return [VisitorTypeResponse]
     */
    @Override
    public VisitorTypeResponse visitType_grammar(YAPLParser.Type_grammarContext ctx) {
        Type classType = null;
        if (ctx.CLASS_LEXER() != null){
            classType = TypesTable.getInstance().getTypeByName(ctx.CLASS_LEXER().getText());
        }else if (ctx.SELF_TYPE() != null){
            //The type of the self expression is the current class scope
            SymbolTable current = SymbolStack.getInstance().globalScope();
            //Main
            classType = TypesTable.getInstance().getTypeByName(current.getId());
        }
        if (classType == null){
            return new VisitorTypeResponse(null, null);
        }
        return classType.toResponse();

    }

    @Override
    public VisitorTypeResponse visitClass_function_declaration(YAPLParser.Class_function_declarationContext ctx) {
        //TODO: Add activation register
        //A function declaration requires a new element in the stack
        SymbolStack stack = SymbolStack.getInstance();
        String functionId = ctx.id.getText();
        //The name of the stack if with the name of the function
        stack.addFunctionScope(functionId);

        // Add new wuad with just with the label

        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createLabelRefArgument(stack.globalScope().getId() + "_"+ functionId),
                        null,
                        null,
                        tabCounter
                )
        );

        tabCounter += 1;
        //Return Type of return statement
        VisitorTypeResponse returnTypeResponse = visitType_grammar(ctx.return_type);
        Type returnType = returnTypeResponse.getType();

        //Get the params
        List<YAPLParser.DeclarationContext> declarations = ctx.declaration();
        ArrayList<String> paramsStrings = new ArrayList<>();
        //Now go to the parameters binding
        for(YAPLParser.DeclarationContext declaration : declarations){
            declaration.typeScope = ctx.typeScope;
            //Visit each param
            //Here it is stored
            visitDeclaration(declaration);
            //Add param string type
            paramsStrings.add(declaration.className.getText());
        }

        //Method get
        Method currentMethod = ctx.typeScope.getMethod(ctx.id.getText(), paramsStrings);
        //Check for the method
        currentMethod.check(returnType);

        //Bind each expression
        List<YAPLParser.ExprContext> expressions = ctx.expr();
        for (int i = 0; i < expressions.size(); i++){
            YAPLParser.ExprContext exprContext = expressions.get(i);
            exprContext.typeScope = ctx.typeScope;
            VisitorTypeResponse resultResponse = visit(exprContext);
            Type result = resultResponse.getType();
            //If it is the last expression
            if (i == expressions.size() - 1 && returnTypeResponse.isValid()){
                //Check return type exists
                Token startReturnToken = ctx.return_type.start;
                //Check type exists
                if (checkTypeExists(returnTypeResponse, ctx.return_type.getText(), startReturnToken.getCharPositionInLine(), startReturnToken.getLine())){
                    //Return type and expression are not compatible
                    if (!returnTypeResponse.acceptsType(result)){
                        SymbolErrorsContainer.getInstance().addError(
                        "El tipo esperado en la declaración de la función " + functionId + " : " + returnTypeResponse.getId() + " y se recibio " + resultResponse.getId() + " que no es compatible.",
                                ctx.start.getCharPositionInLine(),
                                ctx.start.getLine()
                        );
                    }else{

                        //Create temporal
                        //The returned value will be placed in the stack
                        Symbol returnTemporal = SymbolStack.getInstance().currentScope().storeTemporal(resultResponse.getId(), MemoryType.Stack);

                        //Function is valid
                        TAC.addQuad(
                                new Quadruplets(
                                        QuadType.Assign,
                                        resultResponse.getArgument(),
                                        null,
                                        returnTemporal,
                                        tabCounter
                                )
                        );

                        TAC.addQuad(
                                new Quadruplets(
                                        QuadType.Return,
                                        QuadArgument.createSymbolArgument(returnTemporal),
                                        null,
                                        //TODO: Check where to return this
                                        null,
                                        tabCounter
                                )
                        );

                    }
                }

            }
        }
        tabCounter -= 1;
        //Remove the stack created
        stack.removeCurrentScope(true);
        //Return void
        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitClass_property_declaration(YAPLParser.Class_property_declarationContext ctx) {
        VisitorTypeResponse typeObligatoryResponse = visitType_grammar(ctx.class_);
        //Check type exists and is valid
        if (checkTypeExists(typeObligatoryResponse, ctx.class_.getText(), ctx.class_.start.getCharPositionInLine(), ctx.class_.start.getLine())) {
            //Its being assigned
            if (ctx.ASSIGN() != null) {
                VisitorTypeResponse assignTypeResponse = visit(ctx.value);
                Type assignType = assignTypeResponse.getType();
                Token startTkn = ctx.value.start;
                if (checkTypeExists(assignTypeResponse, "N/A", startTkn.getCharPositionInLine(), startTkn.getLine())) {
                    //It is not the same as assigned or a more specific implementation
                    //Or if expected is void we just accept it
                    if (!typeObligatoryResponse.acceptsType(assignType)) {
                        SymbolErrorsContainer.getInstance().addError(
                                "La propiedad " + ctx.id.getText() + " se inicializo con una expresión inválida que tiene un valor " +
                                        assignType.getId() + " se esperaba " + typeObligatoryResponse.getId() + " en la clase " + ctx.typeScope.getId() + "\n",
                                ctx.value.start.getCharPositionInLine(),
                                ctx.value.start.getLine()
                        );
                    }else{
                        //Success case
                        quadsPropertyForClass.add(
                                new Quadruplets(
                                        QuadType.Assign,
                                        assignTypeResponse.getArgument(),
                                        null,
                                        SymbolStack.getInstance().globalScope().getSymbolByName(ctx.id.getText()),
                                        tabCounter
                                )
                        );
                    }
                }
            }
        }
        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitClass_feature(YAPLParser.Class_featureContext ctx) {
        YAPLVisitorContext context = ctx.class_property_declaration();
        if (context == null){
            context = ctx.class_function_declaration();
        }
        if (context != null){
            //Bind the type scope
            context.typeScope = ctx.typeScope;
        }
        //Visit the children of the class features
        return visitChildren(ctx);
    }

    @Override
    public VisitorTypeResponse visitDeclaration(YAPLParser.DeclarationContext ctx) {
        SymbolStack stack = SymbolStack.getInstance();
        //Get the current scope
        SymbolTable symbolTableInFunction = stack.currentScope();
        String paramName = ctx.id.getText();
        String paramClass = ctx.className.getText();
        //Get from type grammar the type in case its a self
        VisitorTypeResponse typeOfParamResponse = visitType_grammar(ctx.className);

        int column = ctx.start.getCharPositionInLine();
        int line = ctx.start.getLine();
        //If it is not a valid response
        if (typeOfParamResponse.isError()){
            //Store the error if the type was not defined
            SymbolErrorsContainer.getInstance().addError(
                    "El parametro " + paramName +  " en el método " + SymbolStack.getInstance().currentScope().getId() +
                            " con la clase " + paramClass + " no ha sido declarado.",
                    column,
                    line
            );
        }else{
            //Add parameter type
            //We add the param class as a string in case it is self_type
            //This are added in the stack
            symbolTableInFunction.storeSymbol(paramName, paramClass, MemoryType.Stack);
        }

        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitDeclaration_with_possible_assignation(YAPLParser.Declaration_with_possible_assignationContext ctx) {
        SymbolStack stack = SymbolStack.getInstance();
        //Get the current scope in let
        SymbolTable symbolTableInLet = stack.currentScope();
        String scopedLetName = ctx.id.getText();
        String scopedLetClass = ctx.className.getText();
        //Get from type grammar the type in case its a self
        VisitorTypeResponse typeOfLetResponse = visitType_grammar(ctx.className);
        Type typeOfLet = typeOfLetResponse.getType();
        int column = ctx.start.getCharPositionInLine();
        int line = ctx.start.getLine();
        //If it is not a valid response
        if (typeOfLetResponse.isError()){
            //Store the error if the type was not defined
            SymbolErrorsContainer.getInstance().addError(
                    "El valor " + scopedLetName + " en un let en el método " + SymbolStack.getInstance().currentScope().getId() + " se declaro con la clase " +
                            scopedLetClass + " la cual no ha sido declarada",
                    column,
                    line
            );
        }else{
            //The values created in the let scope are part of a function are in the stack
            symbolTableInLet.storeSymbol(scopedLetName, scopedLetClass, MemoryType.Stack);
        }
        //Check has assignation
        if (ctx.ASSIGN() != null){
            YAPLParser.ExprContext assignExpr = ctx.expr();
            Token assignExprToken = assignExpr.start;
            VisitorTypeResponse response = visit(assignExpr);
            //Exists
            if (checkTypeExists(response,  response.getId(), assignExprToken.getCharPositionInLine(), assignExprToken.getLine())){
                //If it is not accepted
                if (!typeOfLetResponse.acceptsType(response.getType())){
                    SymbolErrorsContainer.getInstance().addError(
                            "En la declaración de una variable el tipo esperado era: " + typeOfLetResponse.getType().getId() + " y se recibio " + response.getId() + " que no es compatible.\n" ,
                            assignExprToken.getCharPositionInLine(),
                            assignExprToken.getLine()
                    );
                }else{
                    TAC.addQuad(
                            new Quadruplets(
                                    QuadType.Assign,
                                    response.getArgument(),
                                    null,
                                    symbolTableInLet.getSymbolByName(scopedLetName),
                                    tabCounter
                            )
                    );
                }
            }
        }

        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitIsVoidExpression(YAPLParser.IsVoidExpressionContext ctx) {
        //Visit the expression
        VisitorTypeResponse resultType = visit(ctx.expr());

        checkTypeExists(resultType, "no se pudo resolver", ctx.expr().start.getCharPositionInLine(), ctx.expr().start.getLine());
        Type boolType = TypesTable.getInstance().getBoolType();
        //is void is in stack
        Symbol temporalIsVoid = SymbolStack.getInstance().currentScope().storeTemporal(boolType.getId(), MemoryType.Stack);
        TAC.addQuad(
                new Quadruplets(
                    QuadType.IsVoid,
                    resultType.getArgument(),
                    null,
                    temporalIsVoid,
                    tabCounter
                )
        );
        return boolType.toResponse().setArgument(
                QuadArgument.createSymbolArgument(temporalIsVoid)
        );
    }

    @Override
    public VisitorTypeResponse visitBoolExpression(YAPLParser.BoolExpressionContext ctx) {
        return TypesTable.getInstance().getBoolType().toResponse().setArgument(
                QuadArgument.createConstantArgument(ctx.getText())
        );
    }

    @Override
    public VisitorTypeResponse visitWhileExpression(YAPLParser.WhileExpressionContext ctx) {
        VisitorTypeResponse conditionTypeResponse = visit(ctx.condition);
        Type conditionType = conditionTypeResponse.getType();
        Token conditionToken = ctx.condition.start;
        //Check condition type could be resolved
        if (checkTypeExists(conditionTypeResponse, "no se pudo resolver", conditionToken.getCharPositionInLine(), conditionToken.getLine())) {
            //If the condition is not bool type
            //Here it is valid
            if (!TypesTable.getInstance().getBoolType().toResponse().acceptsType(conditionType)) {
                SymbolErrorsContainer.getInstance().addError(
                        "La condición del while statement debe de ser Bool y se retornó: " + conditionType.getId() + "\n.",
                        ctx.condition.start.getCharPositionInLine(),
                        ctx.condition.start.getLine()
                );
            }
        }

        String whileName = "WHILE_" + whileCounter;
        String endWhileName = "END_WHILE_" + whileCounter;
        whileCounter+=1;
        TAC.addQuad(
                new Quadruplets(
                        QuadType.If,
                        conditionTypeResponse.getArgument(),
                        QuadArgument.createConstantArgument("goTo "  + whileName),
                        null,
                        tabCounter
                )
        );
        TAC.addQuad(
                new Quadruplets(
                        QuadType.Else,
                        QuadArgument.createConstantArgument("goTo "  + endWhileName),
                        null,
                        null,
                        tabCounter
                )
        );




        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createLabelRefArgument(whileName),
                        null,
                        null,
                        tabCounter
                )
        );
        tabCounter += 1;
        //Visit block body
        visit(ctx.block);
        tabCounter -= 1;
        TAC.addQuad(
                new Quadruplets(
                        QuadType.If,
                        conditionTypeResponse.getArgument(),
                        QuadArgument.createConstantArgument("goTo "  + whileName),
                        null,
                        tabCounter
                )
        );

        TAC.addQuad(
                new Quadruplets(
                        QuadType.Label,
                        QuadArgument.createConstantArgument(endWhileName),
                        null,
                        null,
                        tabCounter
                )
        );


        Symbol objectTemporal = SymbolStack.getInstance().currentScope().storeTemporal(Constants.Object, MemoryType.Stack);

        return TypesTable.getInstance().getObjectType().toResponse().setArgument(
                QuadArgument.createSymbolArgument(objectTemporal)
        );
    }

    @Override
    public VisitorTypeResponse visitIntExpression(YAPLParser.IntExpressionContext ctx) {
        return TypesTable.getInstance().getIntType().toResponse().setArgument(
                QuadArgument.createConstantArgument(ctx.getText())
        );
    }

    @Override
    public VisitorTypeResponse visitParenthesisExpression(YAPLParser.ParenthesisExpressionContext ctx) {
        //Its return type is from expr
        //Here it includes the argument
        VisitorTypeResponse response = visit(ctx.expr());
        return response.setArgument(
                response.getArgument()
        );
    }

    //When the function is called with the scope specifie
    @Override
    public VisitorTypeResponse visitScopeInvocationExpression(YAPLParser.ScopeInvocationExpressionContext ctx) {
        int functionColumn = ctx.functionId.getCharPositionInLine();
        int functionLine = ctx.functionId.getLine();
        String functionIdentifier = ctx.functionId.getText();
        VisitorTypeResponse invocatorTypeResponse = visit(ctx.invocator);
        Type invocatorType = invocatorTypeResponse.getType();
        if (invocatorType == null){
            SymbolErrorsContainer.getInstance().addError(
                    "En la incovación de la función " + functionIdentifier + " el invocador es de tipo " + ctx.invocator.getText() +
                        " el cual no ha sido declarado.",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
        }
        //In case the expr is in the following format new A.function
        //It is invalid it does not matter the tree accepts it
        else if (ctx.invocator instanceof YAPLParser.ClassInstantiationExpressionContext){
            SymbolErrorsContainer.getInstance().addError(
                    "El formato de new " + invocatorType.getId() + "." + functionIdentifier + " no es válido.\n",
                    ctx.functionId.getCharPositionInLine(),
                    ctx.functionId.getLine()
            );
        }
        else{
            Type finalType = invocatorType;
            //A scope was supplied
            if (ctx.Scope() != null){
                VisitorTypeResponse scopeTypeResponse = visitType_grammar(ctx.type_grammar());
                Type scopeType = scopeTypeResponse.getType();
                Token typeGrammarToken = ctx.type_grammar().start;
                //Check scope type exists
                if (checkTypeExists(scopeTypeResponse, ctx.type_grammar().getText(), typeGrammarToken.getCharPositionInLine(), typeGrammarToken.getLine())){
                    //First check scope type is compatible with the expr scope
                    //In this instance it cant be null
                    if (!scopeType.acceptsType(invocatorType)){
                        SymbolErrorsContainer.getInstance().addError(
                                "Se especifico la función " + functionIdentifier + " en el tipo " + scopeType.getId(),
                                functionColumn,
                                functionLine
                        );
                    }else{
                        // If everything is correct type is set
                        finalType = scopeType;
                    }
                }
            }
            Token startParenthesisToken = ctx.PARENTHESIS_START().getSymbol();
            List<YAPLParser.ExprContext> parametersPassed = ctx.expr();
            //Normalize params
            if (parametersPassed.size() > 1){
                parametersPassed = parametersPassed.subList(1, parametersPassed.size());
            }else{
                parametersPassed = List.of();
            }

            ArrayList<VisitorTypeResponse> paramTypes = new ArrayList<>();
            ArrayList<String> paramNames = new ArrayList<>();
            parametersPassed.forEach(classP -> {
                VisitorTypeResponse p = visit(classP);
                paramTypes.add(p);
                paramNames.add(p.getId());
            });


            //Check function exists
            //TODO Checks if this works correctly like this. Else check if only exists in specified scope
            //TODO: AKA get method in scope.
            Method method = finalType.getMethod(functionIdentifier, paramNames);
            if (method == null){
                SymbolErrorsContainer.getInstance().addError(
            "La función " + functionIdentifier + " no ha sido declarada en la clase " +  finalType.getId() +".\n"+
                        "La firma que se recibio fue: " + Type.getSignature(functionIdentifier, paramNames),
                    functionColumn,
                    functionLine
                );
            }
            else{
//                if (parametersPassed.so)
                //Different count of params passed
                String P = functionIdentifier;
                int N = paramTypes.size();
                TAC.addQuad(
                        new Quadruplets(
                                QuadType.Parameter,
                                invocatorTypeResponse.getArgument(),
                                null,
                                null,
                                tabCounter
                        )
                );
                if (method.getParamCount() != parametersPassed.size()){
                    SymbolErrorsContainer.getInstance().addError(
                "La función " + functionIdentifier + " se le pasaron " + parametersPassed.size() + " se esperaban " + method.getParamCount() + ".\n",
                        startParenthesisToken.getCharPositionInLine(),
                        startParenthesisToken.getLine()
                    );
                }else{
                    //Correct amount of parameters was passed
                    for (int index = 0; index < paramTypes.size(); index++){
                        YAPLParser.ExprContext parameterPassedCtx = parametersPassed.get(index);
                        Token parameterPassedTkn = parameterPassedCtx.start;
                        VisitorTypeResponse parameterTypeExpected = method.getParameterTypeAtPosition(index);
                        VisitorTypeResponse parameterTypePassed = paramTypes.get(index);
                        //Check types are valid
                        if (
                            checkTypeExists(parameterTypePassed, "no especifica", parameterPassedTkn.getCharPositionInLine(), parameterPassedTkn.getLine())
                            && parameterTypeExpected != null
                        ){
                            //Check parameter type is accepted
                            if (!parameterTypeExpected.acceptsType(parameterTypePassed.getType())){
                                SymbolErrorsContainer.getInstance().addError(
                                        "El tipo esperado era: " + parameterTypeExpected.getType().getId() + " y se recibio " + parameterTypePassed.getId() + " que no es compatible.\n",
                                        parameterPassedTkn.getCharPositionInLine(),
                                        parameterPassedTkn.getLine()
                                );
                            }else{
                                TAC.addQuad(
                                        new Quadruplets(
                                                QuadType.Parameter,
                                                parameterTypePassed.getArgument(),
                                                null,
                                                null,
                                                tabCounter
                                        )
                                );
                            }
                        }

                    }
                }
                VisitorTypeResponse methodResponse = method.getReturnType(finalType);
                Symbol resultTemporal = SymbolStack.getInstance().currentScope().storeTemporal(methodResponse.getId(), MemoryType.Stack);

                TAC.addQuad(
                        new Quadruplets(
                                QuadType.Call,
                                QuadArgument.createConstantArgument(finalType.getId() + "_" + P),
                                QuadArgument.createConstantArgument(String.valueOf(N + 1)),
                                resultTemporal,
                                tabCounter
                        )
                );
                //SCOPE CALL
                return methodResponse.setArgument(
                        QuadArgument.createSymbolArgument(resultTemporal)
                );
            }

        }

        //Method is not valid
        //Not resolved
        return new VisitorTypeResponse(null, new Exception("Class no se resolvio en el método " + functionIdentifier));
    }

    @Override
    public VisitorTypeResponse visitFunctionInvocationExpression(YAPLParser.FunctionInvocationExpressionContext ctx) {
        int functionColumn = ctx.functionId.getCharPositionInLine();
        int functionLine = ctx.functionId.getLine();
        String functionIdentifier = ctx.functionId.getText();
        Type invocatorType = TypesTable.getInstance().getTypeByName(SymbolStack.getInstance().globalScope().getId());
        if (invocatorType == null){
            SymbolErrorsContainer.getInstance().addError(
                    "En la invocación de la función " + functionIdentifier + " en el ambito de " + SymbolStack.getInstance().globalScope().getId()
                    + " no ha sido declarado correctamente",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
        }else{
            List<YAPLParser.ExprContext> parametersPassed = ctx.expr();
            ArrayList<VisitorTypeResponse> paramTypes = new ArrayList<>();
            ArrayList<String> paramNames = new ArrayList<>();
            parametersPassed.forEach(classP -> {
                VisitorTypeResponse p = visit(classP);
                paramTypes.add(p);
                paramNames.add(p.getId());
            });
            Method method = invocatorType.getMethod(functionIdentifier, paramNames);
            if (method == null){
                SymbolErrorsContainer.getInstance().addError(
                        "La función " + functionIdentifier + " no ha sido declarada en la clase " +  SymbolStack.getInstance().globalScope().getId() +".\n"+
                        "Se recibio la firma: " + Type.getSignature(functionIdentifier, paramNames),
                        functionColumn,
                        functionLine
                );
            }
            else{
                Token startParenthesisToken = ctx.PARENTHESIS_START().getSymbol();

                //Different count of params passed
                if (method.getParamCount() != parametersPassed.size()){
                    SymbolErrorsContainer.getInstance().addError(
                            "La función " + functionIdentifier + " se le pasaron " + parametersPassed.size() + " se esperaban " + method.getParamCount() + ".\n",
                            startParenthesisToken.getCharPositionInLine(),
                            startParenthesisToken.getLine()
                    );
                }else{
                    //Correct amount of parameters was passed
                    //Set base param
                    TAC.addQuad(
                            new Quadruplets(
                                    QuadType.Parameter,
                                    QuadArgument.createSelfArgument(invocatorType.getId()),
                                    null,
                                    null,
                                    tabCounter
                            )
                    );
                    for (int index = 0; index < parametersPassed.size(); index++){
                        YAPLParser.ExprContext parameterPassedCtx = parametersPassed.get(index);
                        Token parameterPassedTkn = parameterPassedCtx.start;
                        VisitorTypeResponse parameterTypeExpected = method.getParameterTypeAtPosition(index);
                        VisitorTypeResponse parameterTypePassed = paramTypes.get(index);
                        //Check types are valid
                        if (
                                checkTypeExists(parameterTypePassed, "no especifica", parameterPassedTkn.getCharPositionInLine(), parameterPassedTkn.getLine())
                                        && parameterTypeExpected != null
                        ){
                            //Check parameter type is accepted
                            if (!parameterTypeExpected.acceptsType(parameterTypePassed.getType())){
                                SymbolErrorsContainer.getInstance().addError(
                                        "El tipo esperado era: " + parameterTypeExpected.getId() + " y se recibio " + parameterTypePassed.getId() + " que no es compatible.\n",
                                        parameterPassedTkn.getCharPositionInLine(),
                                        parameterPassedTkn.getLine()
                                );
                            }else{
                                TAC.addQuad(
                                        new Quadruplets(
                                                QuadType.Parameter,
                                                parameterTypePassed.getArgument(),
                                                null,
                                                null,
                                                tabCounter
                                        )
                                );
                            }
                        }

                    }
                }
                VisitorTypeResponse methodResponse = method.getReturnType(invocatorType);
                Symbol resultTemporal = SymbolStack.getInstance().currentScope().storeTemporal(methodResponse.getId(), MemoryType.Stack);

                TAC.addQuad(
                        new Quadruplets(
                                QuadType.Call,
                                QuadArgument.createConstantArgument(invocatorType.getId() + "_" + method.getId()),
                                QuadArgument.createConstantArgument(String.valueOf(method.getParamCount() + 1)),
                                resultTemporal,
                                tabCounter
                        )
                );
                //SELF CALL
                return methodResponse.setArgument(
                        QuadArgument.createSymbolArgument(resultTemporal)
                );
            }
        }

        // Method is not valid
        //Not resolved
        return new VisitorTypeResponse(null, new Exception("Class not resolved in method " + functionIdentifier));
    }

    @Override
    public VisitorTypeResponse visitNotExpression(YAPLParser.NotExpressionContext ctx) {
        YAPLParser.ExprContext expr = ctx.expr();
        VisitorTypeResponse boolTypeMust = visit(expr);
        Type boolType = TypesTable.getInstance().getBoolType();
        //We create a temporal for the not expression
        Symbol negateSymbol = SymbolStack.getInstance().currentScope().storeTemporal(boolType.getId(), MemoryType.Stack);
        if (!boolType.toResponse().acceptsType(boolTypeMust.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "Le expresión en el not debe de ser Booleana se encontro: " + boolTypeMust.getId() + "\n",
                    expr.start.getCharPositionInLine(),
                    expr.start.getLine()
            );
        }

        TAC.addQuad(
                new Quadruplets(
                        QuadType.Not,
                        boolTypeMust.getArgument(),
                    null,
                    negateSymbol,
                        tabCounter
                )
        );
        return TypesTable.getInstance().getBoolType().toResponse()
                .setArgument(
                        QuadArgument.createSymbolArgument(negateSymbol)
                );
    }

    @Override
    public VisitorTypeResponse visitBracketExpression(YAPLParser.BracketExpressionContext ctx) {
        SymbolStack.getInstance().addBracketScope();
        VisitorTypeResponse response = visitChildren(ctx);
        if (response.isValid()){
            SymbolStack.getInstance().removeCurrentScope(false);
            return response.setArgument(response.getArgument());
        }
        SymbolStack.getInstance().removeCurrentScope(false);
        return response;
    }

    @Override
    public VisitorTypeResponse visitChildren(RuleNode node) {
        VisitorTypeResponse result = this.defaultResult();
        int n = node.getChildCount();

        for(int i = 0; i < n && this.shouldVisitNextChild(node, result); ++i) {
            ParseTree c = node.getChild(i);
            VisitorTypeResponse childResult = c.accept(this);
            if (childResult!=null) {
                result = this.aggregateResult(result, childResult);
            }
        }

        return result;
    }

    @Override
    public VisitorTypeResponse visitVariableValueAsignationExpression(YAPLParser.VariableValueAsignationExpressionContext ctx) {
        String id = ctx.OBJ_TYPE().getText();
        int column = ctx.OBJ_TYPE().getSymbol().getCharPositionInLine();
        int line = ctx.OBJ_TYPE().getSymbol().getLine();
        //Check id exists
        SymbolTableResponse result = SymbolStack.getInstance().getSymbolInAnyScope(id, column, line);
        if (result == null){
            SymbolErrorsContainer.getInstance().addError(
                    "Error al asignar un valor a la variable " + id + " que no ha sido declarada.\n",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
            return VisitorTypeResponse.getErrorResponse("Error al asignar");
        }else{
            YAPLParser.ExprContext expr = ctx.expr();

            VisitorTypeResponse expectedType = result.getSymbolFound().getAssociatedType(column, line);
            VisitorTypeResponse resultType = visit(expr);
            //Types are valid
            if (
                checkTypeExists(expectedType, "no se pudo resolver", column, line) &&
                checkTypeExists(resultType, "no se pudo resolver", expr.start.getCharPositionInLine(), expr.start.getLine())
            ){
                if(!expectedType.acceptsType(resultType.getType())){
                    SymbolErrorsContainer.getInstance().addError(
                            "El tipo esperado era: " + expectedType.getId() + " y se recibio " + resultType.getId() + " que no es compatible.\n",
                            column,
                            line
                    );
                }
            }
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            resultType.getArgument(),
                            null,
                            result.getSymbolFound(),
                            tabCounter
                    )
            );
            return expectedType.setArgument(
                    QuadArgument.createSymbolArgument(result.getSymbolFound())
            );
        }


    }

    @Override
    public VisitorTypeResponse visitSmallNegationExpression(YAPLParser.SmallNegationExpressionContext ctx) {
        YAPLParser.ExprContext exprContext = ctx.expr();
        Token exprToken = exprContext.start;
        VisitorTypeResponse exprResponse = visit(exprContext);
        VisitorTypeResponse intResponse = TypesTable.getInstance().getIntType().toResponse();

        if (!intResponse.acceptsType(exprResponse.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "La expresión ~ espera que lo evaluado sea Int y se retorno " + exprResponse.getId() +"\n",
                    exprToken.getCharPositionInLine(),
                    exprToken.getLine()
            );
        }

        Symbol smallNegation = SymbolStack.getInstance().currentScope().storeTemporal(Constants.Int, MemoryType.Stack);
        TAC.addQuad(
                new Quadruplets(
                        QuadType.SmallNegation,
                        exprResponse.getArgument(),
                        null,
                        smallNegation,
                        tabCounter
                )
        );
        return intResponse.setArgument(
                QuadArgument.createSymbolArgument(smallNegation)
        );
    }

    @Override
    public VisitorTypeResponse visitStringExpression(YAPLParser.StringExpressionContext ctx) {
        return TypesTable.getInstance().getStringType().toResponse().setArgument(
                QuadArgument.createConstantArgument(
                        ctx.getText()
                )
        );
    }

    @Override
    public VisitorTypeResponse visitOperatorExpression(YAPLParser.OperatorExpressionContext ctx) {
        VisitorTypeResponse leftType = visit(ctx.left);
        VisitorTypeResponse rightType = visit(ctx.right);
        String operator = ctx.op.getText();

        checkIsIntExpression(leftType, operator, ctx.left.start.getCharPositionInLine(), ctx.left.start.getLine());
        checkIsIntExpression(rightType, operator, ctx.right.start.getCharPositionInLine(), ctx.right.start.getLine());
        Type intType = TypesTable.getInstance().getIntType();
        Symbol comparisonTemporal = SymbolStack.getInstance().currentScope().storeTemporal(intType.getId(), MemoryType.Stack);
        TAC.addQuad(
                new Quadruplets(
                        QuadType.getOperator(operator),
                        //We get the argument of the left
                        leftType.getArgument(),
                        //We get the argument of the right
                        rightType.getArgument(),
                        //We get the id
                        comparisonTemporal,
                        tabCounter
                )
        );
        return intType.toResponse().setArgument(
                QuadArgument.createSymbolArgument(comparisonTemporal)
        );
    }

    @Override
    public VisitorTypeResponse visitSelfExpression(YAPLParser.SelfExpressionContext ctx) {
        //The type of the self expression is the current class scope
        SymbolTable current = SymbolStack.getInstance().globalScope();
        Type currentType = TypesTable.getInstance().getTypeByName(current.getId());
        if (currentType == null) {
            SymbolErrorsContainer.getInstance().addError(
                    "Se utilizo la variable self fuera de un contexto",
                    ctx.start.getCharPositionInLine(),
                    ctx.start.getLine()
            );
            return new VisitorTypeResponse(null, null);
        }
        return currentType.toResponse().setArgument(
                QuadArgument.createSelfArgument(currentType.getId())
        );
    }

    @Override
    public VisitorTypeResponse visitIdExpression(YAPLParser.IdExpressionContext ctx) {
        String id = ctx.OBJ_TYPE().getText();
        int column = ctx.start.getCharPositionInLine();
        int line = ctx.start.getLine();
        //Check id exists
        SymbolTableResponse response = SymbolStack.getInstance().getSymbolInAnyScope(id, column, line);

        if (response != null){
            //Returns the id
            return response.getSymbolFound().getAssociatedType(column, line).setArgument(
                    QuadArgument.createSymbolArgument(response.getSymbolFound())
            );
        }
        SymbolErrorsContainer.getInstance().addError(
    "La variable " + id + " no ha sido declarada en el contexto.\n",
            column,
            line
        );

        //Not resolved the id value
        return new VisitorTypeResponse(null, null);
    }

    @Override
    public VisitorTypeResponse visitLetExpression(YAPLParser.LetExpressionContext ctx) {
        //A function declaration requires a new element in the stack.
        SymbolStack stack = SymbolStack.getInstance();
        //The name of the stack if with the postfix of a let.
        stack.addLetScope("let");
        for (YAPLParser.Declaration_with_possible_assignationContext d : ctx.declaration_with_possible_assignation()){
            visitDeclaration_with_possible_assignation(d);
        }
        VisitorTypeResponse response = visit(ctx.expr());
        //Pop the scope
        stack.removeCurrentScope(false);
        if(response.isValid()){
            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(response.getId(), MemoryType.Stack);
            return response.setArgument(
                    QuadArgument.createSymbolArgument(temporal)
            );
        }

        return response;
    }

    @Override
    public VisitorTypeResponse visitIfExpression(YAPLParser.IfExpressionContext ctx) {
        VisitorTypeResponse conditionType = visit(ctx.condition);

        //If the condition is not bool type
        if (!TypesTable.getInstance().getBoolType().toResponse().acceptsType(conditionType.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "La condición del if statement debe de ser Bool y se retorno: " + conditionType.getId() + "\n.",
                    ctx.condition.start.getCharPositionInLine(),
                    ctx.condition.start.getLine()
            );
        }
        String temporalName = SymbolStack.getInstance().currentScope().getTemporalName();

        String ifCheckFlag = "IF_FLAG_" + String.valueOf(ifCounter);
        String trueFlag = "TRUE_" + String.valueOf(ifCounter);
        String falseFlag = "FALSE_" + String.valueOf(ifCounter);
        String endFlag = "ENDIF_" + String.valueOf(ifCounter);
        Symbol ifResult  = new Symbol(temporalName, null, MemoryType.Stack);
        this.ifCounter = ifCounter + 1;
        TAC.addQuad(new Quadruplets(
                QuadType.goTo,
                QuadArgument.createConstantArgument(ifCheckFlag),
                null,
                null,
                tabCounter
        ));


        //Set true flag
        TAC.addQuad(new Quadruplets(
                QuadType.Label,
                QuadArgument.createLabelRefArgument(trueFlag),
                null,
                null,
                tabCounter
        ));
        tabCounter += 1;
        //Generates then body
        VisitorTypeResponse thenResponse = visit(ctx.expr(1));
        if (thenResponse.isValid() && thenResponse.getArgument() != null){
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            //We store the value
                            thenResponse.getArgument(),
                            null,
                            ifResult,
                            tabCounter
                    )
            );
        }

        TAC.addQuad(new Quadruplets(
                QuadType.goTo,
                QuadArgument.createConstantArgument(endFlag),
                null,
                null,
                tabCounter
        ));
        tabCounter -= 1;
        //Visit else expr
        //Set false flag
        TAC.addQuad(new Quadruplets(
                QuadType.Label,
                QuadArgument.createLabelRefArgument(falseFlag),
                null,
                null,
                tabCounter
        ));
        tabCounter += 1;
        VisitorTypeResponse elseResponse = visit(ctx.expr(2));
        if (elseResponse.isValid()){
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            //We store the value
                            elseResponse.getArgument(),
                            null,
                            ifResult,
                            tabCounter
                    )
            );
        }
        tabCounter -= 1;
        //For now return void type
        VisitorTypeResponse mct = thenResponse.getMCT(elseResponse);





        if (mct.isValid()) {

            //Check flag
            TAC.addQuad(new Quadruplets(
                    QuadType.Label,
                    QuadArgument.createLabelRefArgument(ifCheckFlag),
                    null,
                    null,
                    tabCounter
            ));

            tabCounter += 1;

            TAC.addQuad(
                    new Quadruplets(
                            QuadType.AssignSpaceHeap,
                            QuadArgument.createConstantArgument(
                                    String.valueOf(
                                            Integer.max(
                                                    thenResponse.getType().getTotalSize(),
                                                    elseResponse.getType().getTotalSize()
                                            )
                                    )
                            ),
                            null,
                            null,
                            tabCounter
                    )
            );

            TAC.addQuad(new Quadruplets(
                    QuadType.If,
                    conditionType.getArgument(),
                    QuadArgument.createConstantArgument("goTo " + trueFlag),
                    null,
                    tabCounter
            ));

            TAC.addQuad(new Quadruplets(
                    QuadType.Else,
                    QuadArgument.createConstantArgument("goTo " + falseFlag),
                    null,
                    null,
                    tabCounter
            ));
            tabCounter -= 1;

            TAC.addQuad(new Quadruplets(
                    QuadType.Label,
                    QuadArgument.createLabelRefArgument(endFlag),
                    null,
                    null,
                    tabCounter
            ));




            ifResult.setTypeName(mct.getId());
            //We add the symbol
            SymbolStack.getInstance().currentScope().storeSymbol(ifResult);
            return mct.setArgument(
                    QuadArgument.createSymbolArgument(ifResult)
            );
        }
        return mct;
    }

    @Override
    public VisitorTypeResponse visitClassInstantiationExpression(YAPLParser.ClassInstantiationExpressionContext ctx) {
        VisitorTypeResponse response = visit(ctx.type_grammar());
        if (response.isValid()){
            //TODO: Remove temporal
            Symbol temporal = SymbolStack.getInstance().currentScope().storeTemporal(response.getId(), MemoryType.Stack);
            TAC.addQuad(
                    new Quadruplets(
                            QuadType.Assign,
                            QuadArgument.createSymbolArgument(temporal),
                            null,
                            temporal,
                            tabCounter
                    )
            );
            return response.setArgument(
                    QuadArgument.createSymbolArgument(temporal)
            );
        }

        return response;
    }

    @Override
    public VisitorTypeResponse visitBoolComparisonExpression(YAPLParser.BoolComparisonExpressionContext ctx) {
        VisitorTypeResponse leftType = visit(ctx.left);
        VisitorTypeResponse rightType = visit(ctx.right);
        String op = ctx.op.getText();
        int column = ctx.left.start.getCharPositionInLine();
        int line = ctx.left.start.getLine();
        if (
            !leftType.getId().equals(rightType.getId()) ||
            leftType.getId().equals("(no se pudo resolver probablemente no fue declarada)") ||
            leftType.getId().equals("(void)")
        ){
            SymbolErrorsContainer.getInstance().addError(
                    "En una comparación (" + op + ")" + " ambos operadores deben de ser iguals " + "se encontró: " + leftType.getId() + " y " + rightType.getId(),
                    column,
                    line
            );
        }
        //We create a temporal for the not expression
        Symbol comparisonTemporal = SymbolStack.getInstance().currentScope().storeTemporal(leftType.getId(), MemoryType.Stack);
        QuadType comp = QuadType.getCompOperator(op);

        TAC.addQuad(
                new Quadruplets(
                        comp,
                        //We get the argument of the left
                        leftType.getArgument(),
                        //We get the argument of the right
                        rightType.getArgument(),
                        //We get the id
                        comparisonTemporal,
                        tabCounter
                )
        );
        return TypesTable.getInstance().getBoolType().toResponse().setArgument(
                QuadArgument.createSymbolArgument(comparisonTemporal)
        );
    }
    /** HELPERS */
    private void checkIsBoolExpressionInComparison(VisitorTypeResponse type, String comparisonSign, int column, int line){
        if (!TypesTable.getInstance().getBoolType().toResponse().acceptsType(type.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "En una comparación (" + comparisonSign + ")" + " ambos operadores deben de ser booleanos " + "se encontró: " + type.getId(),
                    column,
                    line
            );
        }
    }
    private void checkIsIntExpression(VisitorTypeResponse type, String operation, int column, int line){
        if (!TypesTable.getInstance().getIntType().toResponse().acceptsType(type.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "En una operación (" + operation + ")" + " ambos operadores deben de ser Integers " + "se encontró: " + type.getId(),
                    column,
                    line
            );
        }
    }
    private boolean checkTypeExists(VisitorTypeResponse response, String id, int column, int line){
        if (response.isError() || response.isVoid()){
            SymbolErrorsContainer.getInstance().addError(
                    "Cerca de la linea " + line + " se utiliza la clase " + id + " que no ha sido declarada.",
                column,
                line
            );
            return false;
        }
        return true;
    }
}