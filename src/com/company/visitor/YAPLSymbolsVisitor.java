package com.company.visitor;

import com.company.errors.SymbolErrorsContainer;
import com.company.errors.TypeNotDeclared;
import com.company.tables.*;
import com.company.yapl.YAPLParser;
import com.company.yapl.YAPLVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import java.util.ArrayList;
import java.util.List;

public class YAPLSymbolsVisitor extends AbstractParseTreeVisitor<VisitorTypeResponse> implements YAPLVisitor<VisitorTypeResponse> {

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
        stack.removeCurrentScope();

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
        //A function declaration requires a new element in the stack
        SymbolStack stack = SymbolStack.getInstance();
        String functionId = ctx.id.getText();
        //The name of the stack if with the name of the function
        stack.addFunctionScope(functionId);



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
                    }
                }

            }
        }

        //Remove the stack created
        stack.removeCurrentScope();
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
            symbolTableInFunction.storeSymbol(
                    new Symbol(paramName, paramClass)
            );
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
            symbolTableInLet.storeSymbol(
                    new Symbol(scopedLetName, scopedLetClass)
            );
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
                }
            }
        }

        return VisitorTypeResponse.getVoidResponse();
    }

    @Override
    public VisitorTypeResponse visitIsVoidExpression(YAPLParser.IsVoidExpressionContext ctx) {
        //Visit the expression
        VisitorTypeResponse resultType = visit(ctx.expr());
        Type result = resultType.getType();
        checkTypeExists(resultType, "no se pudo resolver", ctx.expr().start.getCharPositionInLine(), ctx.expr().start.getLine());
        return TypesTable.getInstance().getBoolType().toResponse();
    }

    @Override
    public VisitorTypeResponse visitBoolExpression(YAPLParser.BoolExpressionContext ctx) {
        return TypesTable.getInstance().getBoolType().toResponse();
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

        //Visit block body
        visit(ctx.block);

        return TypesTable.getInstance().getObjectType().toResponse();
    }

    @Override
    public VisitorTypeResponse visitIntExpression(YAPLParser.IntExpressionContext ctx) {
        return TypesTable.getInstance().getIntType().toResponse();
    }

    @Override
    public VisitorTypeResponse visitParenthesisExpression(YAPLParser.ParenthesisExpressionContext ctx) {
        //Its return type is from expr
        return visit(ctx.expr());
    }

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
                            }
                        }

                    }
                }
                //SCOPE CALL
                return method.getReturnType(finalType);
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
                            }
                        }

                    }
                }
                //SELF CALL
                return method.getReturnType(invocatorType);
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
        if (!TypesTable.getInstance().getBoolType().toResponse().acceptsType(boolTypeMust.getType())){
            SymbolErrorsContainer.getInstance().addError(
                    "Le expresión en el not debe de ser Booleana se encontro: " + boolTypeMust.getId() + "\n",
                    expr.start.getCharPositionInLine(),
                    expr.start.getLine()
            );
        }
        return TypesTable.getInstance().getBoolType().toResponse();
    }

    @Override
    public VisitorTypeResponse visitBracketExpression(YAPLParser.BracketExpressionContext ctx) {
        return visitChildren(ctx);
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
            return expectedType;
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

        return intResponse;
    }

    @Override
    public VisitorTypeResponse visitStringExpression(YAPLParser.StringExpressionContext ctx) {
        return TypesTable.getInstance().getStringType().toResponse();
    }

    @Override
    public VisitorTypeResponse visitOperatorExpression(YAPLParser.OperatorExpressionContext ctx) {
        VisitorTypeResponse leftType = visit(ctx.left);
        VisitorTypeResponse rightType = visit(ctx.right);
        String operator = ctx.op.getText();

        checkIsIntExpression(leftType, operator, ctx.left.start.getCharPositionInLine(), ctx.left.start.getLine());
        checkIsIntExpression(rightType, operator, ctx.right.start.getCharPositionInLine(), ctx.right.start.getLine());

        return TypesTable.getInstance().getIntType().toResponse();
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
        return currentType.toResponse();
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
            return response.getSymbolFound().getAssociatedType(column, line);
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
        //A function declaration requires a new element in the stack
        SymbolStack stack = SymbolStack.getInstance();
        //The name of the stack if with the posfix of a let
        stack.addLetScope("let");
        for (YAPLParser.Declaration_with_possible_assignationContext d : ctx.declaration_with_possible_assignation()){
            visitDeclaration_with_possible_assignation(d);
        }
        VisitorTypeResponse response = visit(ctx.expr());
        //Pop the scope
        stack.removeCurrentScope();
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

        //Visit then expr
        VisitorTypeResponse thenResponse = visit(ctx.expr(1));
        //Visit else expr
        VisitorTypeResponse elseResponse = visit(ctx.expr(2));

        //For now return void type
        return thenResponse.getMCT(elseResponse);
    }

    @Override
    public VisitorTypeResponse visitClassInstantiationExpression(YAPLParser.ClassInstantiationExpressionContext ctx) {
        return visit(ctx.type_grammar());
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

        return TypesTable.getInstance().getBoolType().toResponse();
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