// Generated from YAPL.g4 by ANTLR 4.10.1

package com.company.yapl;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link YAPLParser}.
 */
public interface YAPLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link YAPLParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(YAPLParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(YAPLParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#class_grammar}.
	 * @param ctx the parse tree
	 */
	void enterClass_grammar(YAPLParser.Class_grammarContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#class_grammar}.
	 * @param ctx the parse tree
	 */
	void exitClass_grammar(YAPLParser.Class_grammarContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#features}.
	 * @param ctx the parse tree
	 */
	void enterFeatures(YAPLParser.FeaturesContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#features}.
	 * @param ctx the parse tree
	 */
	void exitFeatures(YAPLParser.FeaturesContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#type_grammar}.
	 * @param ctx the parse tree
	 */
	void enterType_grammar(YAPLParser.Type_grammarContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#type_grammar}.
	 * @param ctx the parse tree
	 */
	void exitType_grammar(YAPLParser.Type_grammarContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#class_function_declaration}.
	 * @param ctx the parse tree
	 */
	void enterClass_function_declaration(YAPLParser.Class_function_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#class_function_declaration}.
	 * @param ctx the parse tree
	 */
	void exitClass_function_declaration(YAPLParser.Class_function_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#class_property_declaration}.
	 * @param ctx the parse tree
	 */
	void enterClass_property_declaration(YAPLParser.Class_property_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#class_property_declaration}.
	 * @param ctx the parse tree
	 */
	void exitClass_property_declaration(YAPLParser.Class_property_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#class_feature}.
	 * @param ctx the parse tree
	 */
	void enterClass_feature(YAPLParser.Class_featureContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#class_feature}.
	 * @param ctx the parse tree
	 */
	void exitClass_feature(YAPLParser.Class_featureContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(YAPLParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(YAPLParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link YAPLParser#declaration_with_possible_assignation}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration_with_possible_assignation(YAPLParser.Declaration_with_possible_assignationContext ctx);
	/**
	 * Exit a parse tree produced by {@link YAPLParser#declaration_with_possible_assignation}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration_with_possible_assignation(YAPLParser.Declaration_with_possible_assignationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code isVoidExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIsVoidExpression(YAPLParser.IsVoidExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code isVoidExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIsVoidExpression(YAPLParser.IsVoidExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolExpression(YAPLParser.BoolExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolExpression(YAPLParser.BoolExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boolComparisonExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolComparisonExpression(YAPLParser.BoolComparisonExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boolComparisonExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolComparisonExpression(YAPLParser.BoolComparisonExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterWhileExpression(YAPLParser.WhileExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitWhileExpression(YAPLParser.WhileExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code intExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIntExpression(YAPLParser.IntExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code intExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIntExpression(YAPLParser.IntExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesisExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisExpression(YAPLParser.ParenthesisExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesisExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisExpression(YAPLParser.ParenthesisExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code functionInvocationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFunctionInvocationExpression(YAPLParser.FunctionInvocationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code functionInvocationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFunctionInvocationExpression(YAPLParser.FunctionInvocationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpression(YAPLParser.NotExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code notExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpression(YAPLParser.NotExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bracketExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBracketExpression(YAPLParser.BracketExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bracketExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBracketExpression(YAPLParser.BracketExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code scopeInvocationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterScopeInvocationExpression(YAPLParser.ScopeInvocationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code scopeInvocationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitScopeInvocationExpression(YAPLParser.ScopeInvocationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code smallNegationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSmallNegationExpression(YAPLParser.SmallNegationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code smallNegationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSmallNegationExpression(YAPLParser.SmallNegationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code idExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIdExpression(YAPLParser.IdExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code idExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIdExpression(YAPLParser.IdExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code stringExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterStringExpression(YAPLParser.StringExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code stringExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitStringExpression(YAPLParser.StringExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code operatorExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOperatorExpression(YAPLParser.OperatorExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code operatorExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOperatorExpression(YAPLParser.OperatorExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code selfExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSelfExpression(YAPLParser.SelfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code selfExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSelfExpression(YAPLParser.SelfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code letExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLetExpression(YAPLParser.LetExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code letExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLetExpression(YAPLParser.LetExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIfExpression(YAPLParser.IfExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIfExpression(YAPLParser.IfExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code classInstantiationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterClassInstantiationExpression(YAPLParser.ClassInstantiationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code classInstantiationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitClassInstantiationExpression(YAPLParser.ClassInstantiationExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variableValueAsignationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVariableValueAsignationExpression(YAPLParser.VariableValueAsignationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variableValueAsignationExpression}
	 * labeled alternative in {@link YAPLParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVariableValueAsignationExpression(YAPLParser.VariableValueAsignationExpressionContext ctx);
}