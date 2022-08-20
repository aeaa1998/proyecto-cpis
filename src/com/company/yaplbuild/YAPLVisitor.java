// Generated from YAPL.g4 by ANTLR 4.10.1

package com.company.yaplbuild;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link YAPLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface YAPLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link YAPLParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(YAPLParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link YAPLParser#class_grammar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_grammar(YAPLParser.Class_grammarContext ctx);
	/**
	 * Visit a parse tree produced by {@link YAPLParser#features}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFeatures(YAPLParser.FeaturesContext ctx);
	/**
	 * Visit a parse tree produced by {@link YAPLParser#type_grammar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType_grammar(YAPLParser.Type_grammarContext ctx);
	/**
	 * Visit a parse tree produced by {@link YAPLParser#class_function_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_function_declaration(YAPLParser.Class_function_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link YAPLParser#class_property_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_property_declaration(YAPLParser.Class_property_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link YAPLParser#class_feature}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClass_feature(YAPLParser.Class_featureContext ctx);
	/**
	 * Visit a parse tree produced by {@link YAPLParser#declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaration(YAPLParser.DeclarationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code isVoidExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsVoidExpression(YAPLParser.IsVoidExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolExpression(YAPLParser.BoolExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boolComparisonExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolComparisonExpression(YAPLParser.BoolComparisonExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code whileExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileExpression(YAPLParser.WhileExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code intExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntExpression(YAPLParser.IntExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parenthesisExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisExpression(YAPLParser.ParenthesisExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code functionInvocationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionInvocationExpression(YAPLParser.FunctionInvocationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpression(YAPLParser.NotExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bracketExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracketExpression(YAPLParser.BracketExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code scopeInvocationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScopeInvocationExpression(YAPLParser.ScopeInvocationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code smallNegationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSmallNegationExpression(YAPLParser.SmallNegationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code idExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdExpression(YAPLParser.IdExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code stringExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringExpression(YAPLParser.StringExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code operatorExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperatorExpression(YAPLParser.OperatorExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code selfExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelfExpression(YAPLParser.SelfExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code letExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetExpression(YAPLParser.LetExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ifExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfExpression(YAPLParser.IfExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code classInstantiationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitClassInstantiationExpression(YAPLParser.ClassInstantiationExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code variableValueAsignationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableValueAsignationExpression(YAPLParser.VariableValueAsignationExpressionContext ctx);
}