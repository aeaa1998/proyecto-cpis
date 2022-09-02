// Generated from YAPL.g4 by ANTLR 4.10.1

package com.company.visitor;

import com.company.utils.Constants;
import com.company.errors.TableErrorsContainer;
import com.company.tables.Attribute;
import com.company.tables.Method;
import com.company.tables.Type;
import com.company.tables.TypesTable;
import com.company.yapl.YAPLParser;
import com.company.yapl.YAPLVisitor;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * This class provides an empty implementation of {@link YAPLVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public class YAPLTypeVisitor<T> extends AbstractParseTreeVisitor<T> implements YAPLVisitor<T> {
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitProgram(YAPLParser.ProgramContext ctx) {
		return visitChildren(ctx);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClass_grammar(YAPLParser.Class_grammarContext ctx) {
		TypesTable typesTable = TypesTable.getInstance();
		String className = ctx.className.getText();
		int classLine = ctx.className.getLine();
		int classColumn = ctx.className.getCharPositionInLine();
		String parentName = null;
		//Parent exists
		if (ctx.parent != null){
			parentName = ctx.parent.getText();
		}
		Type typeScope = typesTable.storeType(new Type(className, parentName, classColumn, classLine));
		YAPLParser.FeaturesContext features = ctx.features();
		if (features == null){
			return null;
		}
		features.typeScope = typeScope;
		visit(features);
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFeatures(YAPLParser.FeaturesContext ctx) {
		for (YAPLParser.Class_featureContext classFeature : ctx.class_feature()){
			classFeature.typeScope = ctx.typeScope;
			visit(classFeature);
		};
		return null;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitType_grammar(YAPLParser.Type_grammarContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClass_function_declaration(YAPLParser.Class_function_declarationContext ctx) {
		//Check all items are present
		if (ctx.id == null || ctx.return_type == null) {
			return null;
		}

		String id = ctx.id.getText();
		String returnType = ctx.return_type.getText();
		int column = ctx.start.getCharPositionInLine();
		int line = ctx.start.getLine();
		//If it is self type set the type scope
//		if (returnType.equals(Constants.SELF_TYPE)){
//			returnType = ctx.typeScope.getId();
//		}
		Method method = new Method(id, returnType, column, line);
		for (YAPLParser.DeclarationContext paramDeclaration : ctx.declaration()){
			paramDeclaration.method = method;
			paramDeclaration.typeScope = ctx.typeScope;
		}

		//It has to bee after so the parameters have been passed
		T res = visitChildren(ctx);
		ctx.typeScope.setMethod(method);
		return res;
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClass_property_declaration(YAPLParser.Class_property_declarationContext ctx) {
		String id = ctx.id.getText();
		//In a property declaration we add
		String type = ctx.class_.getText();
		int column = ctx.start.getCharPositionInLine();
		int line = ctx.start.getLine();
		//If it is self type set the type scope
//		if (type.equals(Constants.SELF_TYPE)){
//			type = ctx.typeScope.getId();
//		}
		Attribute attr = new Attribute(id, type, column, line);
		//Add the attribute
		ctx.typeScope.setAttribute(attr);
		return visitChildren(ctx);
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClass_feature(YAPLParser.Class_featureContext ctx) {
		ParseTree visitTree;
		YAPLParser.Class_property_declarationContext property = ctx.class_property_declaration();
		if (property != null){
			property.typeScope = ctx.typeScope;
			visitTree = property;
		}else{
			YAPLParser.Class_function_declarationContext functionDeclaration = ctx.class_function_declaration();
			functionDeclaration.typeScope = ctx.typeScope;
			visitTree = functionDeclaration;
		}
		return visit(visitTree);
	}



	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitDeclaration(YAPLParser.DeclarationContext ctx) {
		String id = ctx.id.getText();
		String className = ctx.className.getText();
		if (className.equals(Constants.SELF_TYPE)){
			className = ctx.typeScope.getId();
		}
		YAPLParser.Type_grammarContext typeGrammarCtx = ctx.type_grammar();
		int column = ctx.start.getCharPositionInLine();
		int line = ctx.start.getLine();
		if (typeGrammarCtx != null){
			ctx.method.addParamString(id, className, column, line);
		}else{
			TableErrorsContainer.getInstance().addError(
					"El parametro " + id + " no se le declaro un tipo.",
				column,
				line
			);
		}
		return visitChildren(ctx);
	}

	@Override
	public T visitDeclaration_with_possible_assignation(YAPLParser.Declaration_with_possible_assignationContext ctx) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIsVoidExpression(YAPLParser.IsVoidExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBoolExpression(YAPLParser.BoolExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitWhileExpression(YAPLParser.WhileExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIntExpression(YAPLParser.IntExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitParenthesisExpression(YAPLParser.ParenthesisExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	public T visitScopeInvocationExpression(YAPLParser.ScopeInvocationExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitFunctionInvocationExpression(YAPLParser.FunctionInvocationExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitNotExpression(YAPLParser.NotExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBracketExpression(YAPLParser.BracketExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitVariableValueAsignationExpression(YAPLParser.VariableValueAsignationExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSmallNegationExpression(YAPLParser.SmallNegationExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitStringExpression(YAPLParser.StringExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitOperatorExpression(YAPLParser.OperatorExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitSelfExpression(YAPLParser.SelfExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIdExpression(YAPLParser.IdExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitLetExpression(YAPLParser.LetExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitIfExpression(YAPLParser.IfExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitClassInstantiationExpression(YAPLParser.ClassInstantiationExpressionContext ctx) { return visitChildren(ctx); }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation returns the result of calling
	 * {@link #visitChildren} on {@code ctx}.</p>
	 */
	@Override public T visitBoolComparisonExpression(YAPLParser.BoolComparisonExpressionContext ctx) {


		return visitChildren(ctx);
	}
}