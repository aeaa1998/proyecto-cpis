// Generated from YAPL.g4 by ANTLR 4.10.1

package com.company.yapl;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class YAPLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, MULTIPLICATION_OPERATOR=9, 
		DIVISION_OPERATOR=10, SUM_OPERATOR=11, REST_OPERATOR=12, SMALLER_THAN=13, 
		SMALLER_OR_EQUAL_THAN=14, EQUAL=15, PARENTHESIS_START=16, PARENTHESIS_END=17, 
		SELF=18, SELF_TYPE=19, IF=20, NEW=21, ISVOID=22, LET=23, IN=24, WHILE=25, 
		LOOP=26, POOL=27, ELSE=28, FI=29, THEN=30, INHERITS=31, NOT=32, TRUE=33, 
		FALSE=34, ASSIGN=35, CLASS=36, CLASS_LEXER=37, OBJ_TYPE=38, INT=39, COMMENT=40, 
		LINE_COMMENT=41, WS=42, Scope=43, StringCharacter=44;
	public static final int
		RULE_program = 0, RULE_class_grammar = 1, RULE_features = 2, RULE_type_grammar = 3, 
		RULE_class_function_declaration = 4, RULE_class_property_declaration = 5, 
		RULE_class_feature = 6, RULE_declaration = 7, RULE_declaration_with_possible_assignation = 8, 
		RULE_expr = 9;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "class_grammar", "features", "type_grammar", "class_function_declaration", 
			"class_property_declaration", "class_feature", "declaration", "declaration_with_possible_assignation", 
			"expr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "';'", "','", "':'", "'.'", "'~'", "'\"'", "'*'", 
			"'/'", "'+'", "'-'", "'<'", "'<='", "'='", "'('", "')'", "'self'", "'SELF_TYPE'", 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "'true'", "'false'", "'<-'", null, null, null, null, null, null, 
			null, "'@'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "MULTIPLICATION_OPERATOR", 
			"DIVISION_OPERATOR", "SUM_OPERATOR", "REST_OPERATOR", "SMALLER_THAN", 
			"SMALLER_OR_EQUAL_THAN", "EQUAL", "PARENTHESIS_START", "PARENTHESIS_END", 
			"SELF", "SELF_TYPE", "IF", "NEW", "ISVOID", "LET", "IN", "WHILE", "LOOP", 
			"POOL", "ELSE", "FI", "THEN", "INHERITS", "NOT", "TRUE", "FALSE", "ASSIGN", 
			"CLASS", "CLASS_LEXER", "OBJ_TYPE", "INT", "COMMENT", "LINE_COMMENT", 
			"WS", "Scope", "StringCharacter"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "YAPL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public YAPLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends com.company.visitor.YAPLVisitorContext {
		public List<Class_grammarContext> class_grammar() {
			return getRuleContexts(Class_grammarContext.class);
		}
		public Class_grammarContext class_grammar(int i) {
			return getRuleContext(Class_grammarContext.class,i);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitProgram(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitProgram(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(21); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(20);
				class_grammar();
				}
				}
				setState(23); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==CLASS );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Class_grammarContext extends com.company.visitor.YAPLVisitorContext {
		public Token className;
		public Token parent;
		public TerminalNode CLASS() { return getToken(YAPLParser.CLASS, 0); }
		public FeaturesContext features() {
			return getRuleContext(FeaturesContext.class,0);
		}
		public List<TerminalNode> CLASS_LEXER() { return getTokens(YAPLParser.CLASS_LEXER); }
		public TerminalNode CLASS_LEXER(int i) {
			return getToken(YAPLParser.CLASS_LEXER, i);
		}
		public TerminalNode INHERITS() { return getToken(YAPLParser.INHERITS, 0); }
		public Class_grammarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_grammar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterClass_grammar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitClass_grammar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitClass_grammar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Class_grammarContext class_grammar() throws RecognitionException {
		Class_grammarContext _localctx = new Class_grammarContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_class_grammar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			match(CLASS);
			setState(26);
			((Class_grammarContext)_localctx).className = match(CLASS_LEXER);
			setState(29);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INHERITS) {
				{
				setState(27);
				match(INHERITS);
				setState(28);
				((Class_grammarContext)_localctx).parent = match(CLASS_LEXER);
				}
			}

			setState(31);
			match(T__0);
			setState(32);
			features();
			setState(33);
			match(T__1);
			setState(34);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FeaturesContext extends com.company.visitor.YAPLVisitorContext {
		public List<Class_featureContext> class_feature() {
			return getRuleContexts(Class_featureContext.class);
		}
		public Class_featureContext class_feature(int i) {
			return getRuleContext(Class_featureContext.class,i);
		}
		public FeaturesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_features; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterFeatures(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitFeatures(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitFeatures(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FeaturesContext features() throws RecognitionException {
		FeaturesContext _localctx = new FeaturesContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_features);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==OBJ_TYPE) {
				{
				{
				setState(36);
				class_feature();
				}
				}
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Type_grammarContext extends com.company.visitor.YAPLVisitorContext {
		public TerminalNode CLASS_LEXER() { return getToken(YAPLParser.CLASS_LEXER, 0); }
		public TerminalNode SELF_TYPE() { return getToken(YAPLParser.SELF_TYPE, 0); }
		public Type_grammarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type_grammar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterType_grammar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitType_grammar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitType_grammar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Type_grammarContext type_grammar() throws RecognitionException {
		Type_grammarContext _localctx = new Type_grammarContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_type_grammar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(42);
			_la = _input.LA(1);
			if ( !(_la==SELF_TYPE || _la==CLASS_LEXER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Class_function_declarationContext extends com.company.visitor.YAPLVisitorContext {
		public Token id;
		public DeclarationContext param1;
		public DeclarationContext params;
		public Type_grammarContext return_type;
		public ExprContext body;
		public TerminalNode PARENTHESIS_START() { return getToken(YAPLParser.PARENTHESIS_START, 0); }
		public TerminalNode PARENTHESIS_END() { return getToken(YAPLParser.PARENTHESIS_END, 0); }
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public Type_grammarContext type_grammar() {
			return getRuleContext(Type_grammarContext.class,0);
		}
		public List<DeclarationContext> declaration() {
			return getRuleContexts(DeclarationContext.class);
		}
		public DeclarationContext declaration(int i) {
			return getRuleContext(DeclarationContext.class,i);
		}
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Class_function_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_function_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterClass_function_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitClass_function_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitClass_function_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Class_function_declarationContext class_function_declaration() throws RecognitionException {
		Class_function_declarationContext _localctx = new Class_function_declarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_class_function_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(44);
			((Class_function_declarationContext)_localctx).id = match(OBJ_TYPE);
			setState(45);
			match(PARENTHESIS_START);
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==OBJ_TYPE) {
				{
				setState(46);
				((Class_function_declarationContext)_localctx).param1 = declaration();
				setState(51);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(47);
					match(T__3);
					setState(48);
					((Class_function_declarationContext)_localctx).params = declaration();
					}
					}
					setState(53);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(56);
			match(PARENTHESIS_END);
			setState(57);
			match(T__4);
			setState(58);
			((Class_function_declarationContext)_localctx).return_type = type_grammar();
			setState(59);
			match(T__0);
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__6) | (1L << T__7) | (1L << PARENTHESIS_START) | (1L << SELF) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << LET) | (1L << WHILE) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << OBJ_TYPE) | (1L << INT))) != 0)) {
				{
				{
				setState(60);
				((Class_function_declarationContext)_localctx).body = expr(0);
				}
				}
				setState(65);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(66);
			match(T__1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Class_property_declarationContext extends com.company.visitor.YAPLVisitorContext {
		public Token id;
		public Type_grammarContext class_;
		public ExprContext value;
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public Type_grammarContext type_grammar() {
			return getRuleContext(Type_grammarContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(YAPLParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Class_property_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_property_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterClass_property_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitClass_property_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitClass_property_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Class_property_declarationContext class_property_declaration() throws RecognitionException {
		Class_property_declarationContext _localctx = new Class_property_declarationContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_class_property_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(68);
			((Class_property_declarationContext)_localctx).id = match(OBJ_TYPE);
			setState(69);
			match(T__4);
			setState(70);
			((Class_property_declarationContext)_localctx).class_ = type_grammar();
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(71);
				match(ASSIGN);
				setState(72);
				((Class_property_declarationContext)_localctx).value = expr(0);
				}
			}

			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Class_featureContext extends com.company.visitor.YAPLVisitorContext {
		public Class_function_declarationContext class_function_declaration() {
			return getRuleContext(Class_function_declarationContext.class,0);
		}
		public Class_property_declarationContext class_property_declaration() {
			return getRuleContext(Class_property_declarationContext.class,0);
		}
		public Class_featureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class_feature; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterClass_feature(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitClass_feature(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitClass_feature(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Class_featureContext class_feature() throws RecognitionException {
		Class_featureContext _localctx = new Class_featureContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_class_feature);
		try {
			setState(81);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(75);
				class_function_declaration();
				setState(76);
				match(T__2);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(78);
				class_property_declaration();
				setState(79);
				match(T__2);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclarationContext extends com.company.visitor.YAPLVisitorContext {
		public Token id;
		public Type_grammarContext className;
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public Type_grammarContext type_grammar() {
			return getRuleContext(Type_grammarContext.class,0);
		}
		public DeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclarationContext declaration() throws RecognitionException {
		DeclarationContext _localctx = new DeclarationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(83);
			((DeclarationContext)_localctx).id = match(OBJ_TYPE);
			setState(84);
			match(T__4);
			setState(85);
			((DeclarationContext)_localctx).className = type_grammar();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Declaration_with_possible_assignationContext extends com.company.visitor.YAPLVisitorContext {
		public Token id;
		public Type_grammarContext className;
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public Type_grammarContext type_grammar() {
			return getRuleContext(Type_grammarContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(YAPLParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public Declaration_with_possible_assignationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaration_with_possible_assignation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterDeclaration_with_possible_assignation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitDeclaration_with_possible_assignation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitDeclaration_with_possible_assignation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Declaration_with_possible_assignationContext declaration_with_possible_assignation() throws RecognitionException {
		Declaration_with_possible_assignationContext _localctx = new Declaration_with_possible_assignationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_declaration_with_possible_assignation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			((Declaration_with_possible_assignationContext)_localctx).id = match(OBJ_TYPE);
			setState(88);
			match(T__4);
			setState(89);
			((Declaration_with_possible_assignationContext)_localctx).className = type_grammar();
			setState(92);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASSIGN) {
				{
				setState(90);
				match(ASSIGN);
				setState(91);
				expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends com.company.visitor.YAPLVisitorContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class IsVoidExpressionContext extends ExprContext {
		public TerminalNode ISVOID() { return getToken(YAPLParser.ISVOID, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public IsVoidExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterIsVoidExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitIsVoidExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitIsVoidExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolExpressionContext extends ExprContext {
		public TerminalNode TRUE() { return getToken(YAPLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(YAPLParser.FALSE, 0); }
		public BoolExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterBoolExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitBoolExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitBoolExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolComparisonExpressionContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode SMALLER_THAN() { return getToken(YAPLParser.SMALLER_THAN, 0); }
		public TerminalNode SMALLER_OR_EQUAL_THAN() { return getToken(YAPLParser.SMALLER_OR_EQUAL_THAN, 0); }
		public TerminalNode EQUAL() { return getToken(YAPLParser.EQUAL, 0); }
		public BoolComparisonExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterBoolComparisonExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitBoolComparisonExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitBoolComparisonExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileExpressionContext extends ExprContext {
		public ExprContext condition;
		public ExprContext block;
		public TerminalNode WHILE() { return getToken(YAPLParser.WHILE, 0); }
		public TerminalNode LOOP() { return getToken(YAPLParser.LOOP, 0); }
		public TerminalNode POOL() { return getToken(YAPLParser.POOL, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public WhileExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterWhileExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitWhileExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitWhileExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntExpressionContext extends ExprContext {
		public TerminalNode INT() { return getToken(YAPLParser.INT, 0); }
		public IntExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterIntExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitIntExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitIntExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParenthesisExpressionContext extends ExprContext {
		public TerminalNode PARENTHESIS_START() { return getToken(YAPLParser.PARENTHESIS_START, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode PARENTHESIS_END() { return getToken(YAPLParser.PARENTHESIS_END, 0); }
		public ParenthesisExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterParenthesisExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitParenthesisExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitParenthesisExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class FunctionInvocationExpressionContext extends ExprContext {
		public Token functionId;
		public TerminalNode PARENTHESIS_START() { return getToken(YAPLParser.PARENTHESIS_START, 0); }
		public TerminalNode PARENTHESIS_END() { return getToken(YAPLParser.PARENTHESIS_END, 0); }
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public FunctionInvocationExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterFunctionInvocationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitFunctionInvocationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitFunctionInvocationExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotExpressionContext extends ExprContext {
		public TerminalNode NOT() { return getToken(YAPLParser.NOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NotExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterNotExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitNotExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitNotExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BracketExpressionContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public BracketExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterBracketExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitBracketExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitBracketExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ScopeInvocationExpressionContext extends ExprContext {
		public ExprContext invocator;
		public Token functionId;
		public TerminalNode PARENTHESIS_START() { return getToken(YAPLParser.PARENTHESIS_START, 0); }
		public TerminalNode PARENTHESIS_END() { return getToken(YAPLParser.PARENTHESIS_END, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public TerminalNode Scope() { return getToken(YAPLParser.Scope, 0); }
		public Type_grammarContext type_grammar() {
			return getRuleContext(Type_grammarContext.class,0);
		}
		public ScopeInvocationExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterScopeInvocationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitScopeInvocationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitScopeInvocationExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SmallNegationExpressionContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public SmallNegationExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterSmallNegationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitSmallNegationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitSmallNegationExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdExpressionContext extends ExprContext {
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public IdExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterIdExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitIdExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitIdExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringExpressionContext extends ExprContext {
		public List<TerminalNode> EOF() { return getTokens(YAPLParser.EOF); }
		public TerminalNode EOF(int i) {
			return getToken(YAPLParser.EOF, i);
		}
		public StringExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterStringExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitStringExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitStringExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OperatorExpressionContext extends ExprContext {
		public ExprContext left;
		public Token op;
		public ExprContext right;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode MULTIPLICATION_OPERATOR() { return getToken(YAPLParser.MULTIPLICATION_OPERATOR, 0); }
		public TerminalNode DIVISION_OPERATOR() { return getToken(YAPLParser.DIVISION_OPERATOR, 0); }
		public TerminalNode SUM_OPERATOR() { return getToken(YAPLParser.SUM_OPERATOR, 0); }
		public TerminalNode REST_OPERATOR() { return getToken(YAPLParser.REST_OPERATOR, 0); }
		public OperatorExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterOperatorExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitOperatorExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitOperatorExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SelfExpressionContext extends ExprContext {
		public TerminalNode SELF() { return getToken(YAPLParser.SELF, 0); }
		public SelfExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterSelfExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitSelfExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitSelfExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LetExpressionContext extends ExprContext {
		public TerminalNode LET() { return getToken(YAPLParser.LET, 0); }
		public List<Declaration_with_possible_assignationContext> declaration_with_possible_assignation() {
			return getRuleContexts(Declaration_with_possible_assignationContext.class);
		}
		public Declaration_with_possible_assignationContext declaration_with_possible_assignation(int i) {
			return getRuleContext(Declaration_with_possible_assignationContext.class,i);
		}
		public TerminalNode IN() { return getToken(YAPLParser.IN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public LetExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterLetExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitLetExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitLetExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfExpressionContext extends ExprContext {
		public ExprContext condition;
		public TerminalNode IF() { return getToken(YAPLParser.IF, 0); }
		public TerminalNode THEN() { return getToken(YAPLParser.THEN, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(YAPLParser.ELSE, 0); }
		public TerminalNode FI() { return getToken(YAPLParser.FI, 0); }
		public IfExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterIfExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitIfExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitIfExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ClassInstantiationExpressionContext extends ExprContext {
		public TerminalNode NEW() { return getToken(YAPLParser.NEW, 0); }
		public Type_grammarContext type_grammar() {
			return getRuleContext(Type_grammarContext.class,0);
		}
		public ClassInstantiationExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterClassInstantiationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitClassInstantiationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitClassInstantiationExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariableValueAsignationExpressionContext extends ExprContext {
		public TerminalNode OBJ_TYPE() { return getToken(YAPLParser.OBJ_TYPE, 0); }
		public TerminalNode ASSIGN() { return getToken(YAPLParser.ASSIGN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public VariableValueAsignationExpressionContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).enterVariableValueAsignationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof YAPLListener ) ((YAPLListener)listener).exitVariableValueAsignationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof YAPLVisitor ) return ((YAPLVisitor<? extends T>)visitor).visitVariableValueAsignationExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				_localctx = new FunctionInvocationExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(95);
				((FunctionInvocationExpressionContext)_localctx).functionId = match(OBJ_TYPE);
				setState(96);
				match(PARENTHESIS_START);
				setState(105);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__6) | (1L << T__7) | (1L << PARENTHESIS_START) | (1L << SELF) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << LET) | (1L << WHILE) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << OBJ_TYPE) | (1L << INT))) != 0)) {
					{
					setState(97);
					expr(0);
					setState(102);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__3) {
						{
						{
						setState(98);
						match(T__3);
						setState(99);
						expr(0);
						}
						}
						setState(104);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(107);
				match(PARENTHESIS_END);
				}
				break;
			case 2:
				{
				_localctx = new IfExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(108);
				match(IF);
				setState(109);
				((IfExpressionContext)_localctx).condition = expr(0);
				setState(110);
				match(THEN);
				setState(111);
				expr(0);
				setState(112);
				match(ELSE);
				setState(113);
				expr(0);
				setState(114);
				match(FI);
				}
				break;
			case 3:
				{
				_localctx = new WhileExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(116);
				match(WHILE);
				setState(117);
				((WhileExpressionContext)_localctx).condition = expr(0);
				setState(118);
				match(LOOP);
				setState(119);
				((WhileExpressionContext)_localctx).block = expr(0);
				setState(120);
				match(POOL);
				}
				break;
			case 4:
				{
				_localctx = new BracketExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(122);
				match(T__0);
				setState(126); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(123);
					expr(0);
					setState(124);
					match(T__2);
					}
					}
					setState(128); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__6) | (1L << T__7) | (1L << PARENTHESIS_START) | (1L << SELF) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << LET) | (1L << WHILE) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << OBJ_TYPE) | (1L << INT))) != 0) );
				setState(130);
				match(T__1);
				}
				break;
			case 5:
				{
				_localctx = new LetExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(132);
				match(LET);
				setState(133);
				declaration_with_possible_assignation();
				setState(138);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__3) {
					{
					{
					setState(134);
					match(T__3);
					setState(135);
					declaration_with_possible_assignation();
					}
					}
					setState(140);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(141);
				match(IN);
				setState(142);
				expr(20);
				}
				break;
			case 6:
				{
				_localctx = new ClassInstantiationExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(144);
				match(NEW);
				setState(145);
				type_grammar();
				}
				break;
			case 7:
				{
				_localctx = new SmallNegationExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(146);
				match(T__6);
				setState(147);
				expr(18);
				}
				break;
			case 8:
				{
				_localctx = new IsVoidExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(148);
				match(ISVOID);
				setState(149);
				expr(17);
				}
				break;
			case 9:
				{
				_localctx = new NotExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(150);
				match(NOT);
				setState(151);
				expr(9);
				}
				break;
			case 10:
				{
				_localctx = new ParenthesisExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(152);
				match(PARENTHESIS_START);
				setState(153);
				expr(0);
				setState(154);
				match(PARENTHESIS_END);
				}
				break;
			case 11:
				{
				_localctx = new IdExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(156);
				match(OBJ_TYPE);
				}
				break;
			case 12:
				{
				_localctx = new IntExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(157);
				match(INT);
				}
				break;
			case 13:
				{
				_localctx = new StringExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(158);
				match(T__7);
				setState(162);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << MULTIPLICATION_OPERATOR) | (1L << DIVISION_OPERATOR) | (1L << SUM_OPERATOR) | (1L << REST_OPERATOR) | (1L << SMALLER_THAN) | (1L << SMALLER_OR_EQUAL_THAN) | (1L << EQUAL) | (1L << PARENTHESIS_START) | (1L << PARENTHESIS_END) | (1L << SELF) | (1L << SELF_TYPE) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << LET) | (1L << IN) | (1L << WHILE) | (1L << LOOP) | (1L << POOL) | (1L << ELSE) | (1L << FI) | (1L << THEN) | (1L << INHERITS) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << ASSIGN) | (1L << CLASS) | (1L << CLASS_LEXER) | (1L << OBJ_TYPE) | (1L << INT) | (1L << COMMENT) | (1L << LINE_COMMENT) | (1L << WS) | (1L << Scope) | (1L << StringCharacter))) != 0)) {
					{
					{
					setState(159);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==EOF || _la==T__7) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(164);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(165);
				match(T__7);
				}
				break;
			case 14:
				{
				_localctx = new BoolExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(166);
				match(TRUE);
				}
				break;
			case 15:
				{
				_localctx = new BoolExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(167);
				match(FALSE);
				}
				break;
			case 16:
				{
				_localctx = new SelfExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(168);
				match(SELF);
				}
				break;
			case 17:
				{
				_localctx = new VariableValueAsignationExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(169);
				match(OBJ_TYPE);
				setState(170);
				match(ASSIGN);
				setState(171);
				expr(1);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(216);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(214);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
					case 1:
						{
						_localctx = new OperatorExpressionContext(new ExprContext(_parentctx, _parentState));
						((OperatorExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(174);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(175);
						((OperatorExpressionContext)_localctx).op = match(MULTIPLICATION_OPERATOR);
						setState(176);
						((OperatorExpressionContext)_localctx).right = expr(17);
						}
						break;
					case 2:
						{
						_localctx = new OperatorExpressionContext(new ExprContext(_parentctx, _parentState));
						((OperatorExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(177);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(178);
						((OperatorExpressionContext)_localctx).op = match(DIVISION_OPERATOR);
						setState(179);
						((OperatorExpressionContext)_localctx).right = expr(16);
						}
						break;
					case 3:
						{
						_localctx = new OperatorExpressionContext(new ExprContext(_parentctx, _parentState));
						((OperatorExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(180);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(181);
						((OperatorExpressionContext)_localctx).op = match(SUM_OPERATOR);
						setState(182);
						((OperatorExpressionContext)_localctx).right = expr(15);
						}
						break;
					case 4:
						{
						_localctx = new OperatorExpressionContext(new ExprContext(_parentctx, _parentState));
						((OperatorExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(183);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(184);
						((OperatorExpressionContext)_localctx).op = match(REST_OPERATOR);
						setState(185);
						((OperatorExpressionContext)_localctx).right = expr(14);
						}
						break;
					case 5:
						{
						_localctx = new BoolComparisonExpressionContext(new ExprContext(_parentctx, _parentState));
						((BoolComparisonExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(186);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(187);
						((BoolComparisonExpressionContext)_localctx).op = match(SMALLER_THAN);
						setState(188);
						((BoolComparisonExpressionContext)_localctx).right = expr(13);
						}
						break;
					case 6:
						{
						_localctx = new BoolComparisonExpressionContext(new ExprContext(_parentctx, _parentState));
						((BoolComparisonExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(189);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(190);
						((BoolComparisonExpressionContext)_localctx).op = match(SMALLER_OR_EQUAL_THAN);
						setState(191);
						((BoolComparisonExpressionContext)_localctx).right = expr(12);
						}
						break;
					case 7:
						{
						_localctx = new BoolComparisonExpressionContext(new ExprContext(_parentctx, _parentState));
						((BoolComparisonExpressionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(192);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(193);
						((BoolComparisonExpressionContext)_localctx).op = match(EQUAL);
						setState(194);
						((BoolComparisonExpressionContext)_localctx).right = expr(11);
						}
						break;
					case 8:
						{
						_localctx = new ScopeInvocationExpressionContext(new ExprContext(_parentctx, _parentState));
						((ScopeInvocationExpressionContext)_localctx).invocator = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(195);
						if (!(precpred(_ctx, 25))) throw new FailedPredicateException(this, "precpred(_ctx, 25)");
						setState(198);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if (_la==Scope) {
							{
							setState(196);
							match(Scope);
							setState(197);
							type_grammar();
							}
						}

						setState(200);
						match(T__5);
						setState(201);
						((ScopeInvocationExpressionContext)_localctx).functionId = match(OBJ_TYPE);
						setState(202);
						match(PARENTHESIS_START);
						setState(211);
						_errHandler.sync(this);
						_la = _input.LA(1);
						if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__6) | (1L << T__7) | (1L << PARENTHESIS_START) | (1L << SELF) | (1L << IF) | (1L << NEW) | (1L << ISVOID) | (1L << LET) | (1L << WHILE) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << OBJ_TYPE) | (1L << INT))) != 0)) {
							{
							setState(203);
							expr(0);
							setState(208);
							_errHandler.sync(this);
							_la = _input.LA(1);
							while (_la==T__3) {
								{
								{
								setState(204);
								match(T__3);
								setState(205);
								expr(0);
								}
								}
								setState(210);
								_errHandler.sync(this);
								_la = _input.LA(1);
							}
							}
						}

						setState(213);
						match(PARENTHESIS_END);
						}
						break;
					}
					} 
				}
				setState(218);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 9:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 16);
		case 1:
			return precpred(_ctx, 15);
		case 2:
			return precpred(_ctx, 14);
		case 3:
			return precpred(_ctx, 13);
		case 4:
			return precpred(_ctx, 12);
		case 5:
			return precpred(_ctx, 11);
		case 6:
			return precpred(_ctx, 10);
		case 7:
			return precpred(_ctx, 25);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001,\u00dc\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0001\u0000\u0004\u0000\u0016\b\u0000\u000b"+
		"\u0000\f\u0000\u0017\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0003"+
		"\u0001\u001e\b\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0005\u0002&\b\u0002\n\u0002\f\u0002)\t\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0005\u00042\b\u0004\n\u0004\f\u00045\t\u0004\u0003\u00047\b\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004"+
		">\b\u0004\n\u0004\f\u0004A\t\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005J\b\u0005"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0003\u0006R\b\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b]\b\b\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0005\te\b\t\n\t\f\th\t\t\u0003\tj\b"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0004\t\u007f\b\t\u000b\t\f\t\u0080\u0001\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0005\t\u0089\b\t\n\t\f\t\u008c\t\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0005"+
		"\t\u00a1\b\t\n\t\f\t\u00a4\t\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0003\t\u00ad\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0003\t\u00c7\b\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0005\t\u00cf\b\t\n\t\f\t\u00d2\t\t\u0003\t\u00d4\b\t\u0001\t\u0005"+
		"\t\u00d7\b\t\n\t\f\t\u00da\t\t\u0001\t\u0000\u0001\u0012\n\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0000\u0002\u0002\u0000\u0013\u0013"+
		"%%\u0001\u0001\b\b\u00fa\u0000\u0015\u0001\u0000\u0000\u0000\u0002\u0019"+
		"\u0001\u0000\u0000\u0000\u0004\'\u0001\u0000\u0000\u0000\u0006*\u0001"+
		"\u0000\u0000\u0000\b,\u0001\u0000\u0000\u0000\nD\u0001\u0000\u0000\u0000"+
		"\fQ\u0001\u0000\u0000\u0000\u000eS\u0001\u0000\u0000\u0000\u0010W\u0001"+
		"\u0000\u0000\u0000\u0012\u00ac\u0001\u0000\u0000\u0000\u0014\u0016\u0003"+
		"\u0002\u0001\u0000\u0015\u0014\u0001\u0000\u0000\u0000\u0016\u0017\u0001"+
		"\u0000\u0000\u0000\u0017\u0015\u0001\u0000\u0000\u0000\u0017\u0018\u0001"+
		"\u0000\u0000\u0000\u0018\u0001\u0001\u0000\u0000\u0000\u0019\u001a\u0005"+
		"$\u0000\u0000\u001a\u001d\u0005%\u0000\u0000\u001b\u001c\u0005\u001f\u0000"+
		"\u0000\u001c\u001e\u0005%\u0000\u0000\u001d\u001b\u0001\u0000\u0000\u0000"+
		"\u001d\u001e\u0001\u0000\u0000\u0000\u001e\u001f\u0001\u0000\u0000\u0000"+
		"\u001f \u0005\u0001\u0000\u0000 !\u0003\u0004\u0002\u0000!\"\u0005\u0002"+
		"\u0000\u0000\"#\u0005\u0003\u0000\u0000#\u0003\u0001\u0000\u0000\u0000"+
		"$&\u0003\f\u0006\u0000%$\u0001\u0000\u0000\u0000&)\u0001\u0000\u0000\u0000"+
		"\'%\u0001\u0000\u0000\u0000\'(\u0001\u0000\u0000\u0000(\u0005\u0001\u0000"+
		"\u0000\u0000)\'\u0001\u0000\u0000\u0000*+\u0007\u0000\u0000\u0000+\u0007"+
		"\u0001\u0000\u0000\u0000,-\u0005&\u0000\u0000-6\u0005\u0010\u0000\u0000"+
		".3\u0003\u000e\u0007\u0000/0\u0005\u0004\u0000\u000002\u0003\u000e\u0007"+
		"\u00001/\u0001\u0000\u0000\u000025\u0001\u0000\u0000\u000031\u0001\u0000"+
		"\u0000\u000034\u0001\u0000\u0000\u000047\u0001\u0000\u0000\u000053\u0001"+
		"\u0000\u0000\u00006.\u0001\u0000\u0000\u000067\u0001\u0000\u0000\u0000"+
		"78\u0001\u0000\u0000\u000089\u0005\u0011\u0000\u00009:\u0005\u0005\u0000"+
		"\u0000:;\u0003\u0006\u0003\u0000;?\u0005\u0001\u0000\u0000<>\u0003\u0012"+
		"\t\u0000=<\u0001\u0000\u0000\u0000>A\u0001\u0000\u0000\u0000?=\u0001\u0000"+
		"\u0000\u0000?@\u0001\u0000\u0000\u0000@B\u0001\u0000\u0000\u0000A?\u0001"+
		"\u0000\u0000\u0000BC\u0005\u0002\u0000\u0000C\t\u0001\u0000\u0000\u0000"+
		"DE\u0005&\u0000\u0000EF\u0005\u0005\u0000\u0000FI\u0003\u0006\u0003\u0000"+
		"GH\u0005#\u0000\u0000HJ\u0003\u0012\t\u0000IG\u0001\u0000\u0000\u0000"+
		"IJ\u0001\u0000\u0000\u0000J\u000b\u0001\u0000\u0000\u0000KL\u0003\b\u0004"+
		"\u0000LM\u0005\u0003\u0000\u0000MR\u0001\u0000\u0000\u0000NO\u0003\n\u0005"+
		"\u0000OP\u0005\u0003\u0000\u0000PR\u0001\u0000\u0000\u0000QK\u0001\u0000"+
		"\u0000\u0000QN\u0001\u0000\u0000\u0000R\r\u0001\u0000\u0000\u0000ST\u0005"+
		"&\u0000\u0000TU\u0005\u0005\u0000\u0000UV\u0003\u0006\u0003\u0000V\u000f"+
		"\u0001\u0000\u0000\u0000WX\u0005&\u0000\u0000XY\u0005\u0005\u0000\u0000"+
		"Y\\\u0003\u0006\u0003\u0000Z[\u0005#\u0000\u0000[]\u0003\u0012\t\u0000"+
		"\\Z\u0001\u0000\u0000\u0000\\]\u0001\u0000\u0000\u0000]\u0011\u0001\u0000"+
		"\u0000\u0000^_\u0006\t\uffff\uffff\u0000_`\u0005&\u0000\u0000`i\u0005"+
		"\u0010\u0000\u0000af\u0003\u0012\t\u0000bc\u0005\u0004\u0000\u0000ce\u0003"+
		"\u0012\t\u0000db\u0001\u0000\u0000\u0000eh\u0001\u0000\u0000\u0000fd\u0001"+
		"\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000gj\u0001\u0000\u0000\u0000"+
		"hf\u0001\u0000\u0000\u0000ia\u0001\u0000\u0000\u0000ij\u0001\u0000\u0000"+
		"\u0000jk\u0001\u0000\u0000\u0000k\u00ad\u0005\u0011\u0000\u0000lm\u0005"+
		"\u0014\u0000\u0000mn\u0003\u0012\t\u0000no\u0005\u001e\u0000\u0000op\u0003"+
		"\u0012\t\u0000pq\u0005\u001c\u0000\u0000qr\u0003\u0012\t\u0000rs\u0005"+
		"\u001d\u0000\u0000s\u00ad\u0001\u0000\u0000\u0000tu\u0005\u0019\u0000"+
		"\u0000uv\u0003\u0012\t\u0000vw\u0005\u001a\u0000\u0000wx\u0003\u0012\t"+
		"\u0000xy\u0005\u001b\u0000\u0000y\u00ad\u0001\u0000\u0000\u0000z~\u0005"+
		"\u0001\u0000\u0000{|\u0003\u0012\t\u0000|}\u0005\u0003\u0000\u0000}\u007f"+
		"\u0001\u0000\u0000\u0000~{\u0001\u0000\u0000\u0000\u007f\u0080\u0001\u0000"+
		"\u0000\u0000\u0080~\u0001\u0000\u0000\u0000\u0080\u0081\u0001\u0000\u0000"+
		"\u0000\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u0083\u0005\u0002\u0000"+
		"\u0000\u0083\u00ad\u0001\u0000\u0000\u0000\u0084\u0085\u0005\u0017\u0000"+
		"\u0000\u0085\u008a\u0003\u0010\b\u0000\u0086\u0087\u0005\u0004\u0000\u0000"+
		"\u0087\u0089\u0003\u0010\b\u0000\u0088\u0086\u0001\u0000\u0000\u0000\u0089"+
		"\u008c\u0001\u0000\u0000\u0000\u008a\u0088\u0001\u0000\u0000\u0000\u008a"+
		"\u008b\u0001\u0000\u0000\u0000\u008b\u008d\u0001\u0000\u0000\u0000\u008c"+
		"\u008a\u0001\u0000\u0000\u0000\u008d\u008e\u0005\u0018\u0000\u0000\u008e"+
		"\u008f\u0003\u0012\t\u0014\u008f\u00ad\u0001\u0000\u0000\u0000\u0090\u0091"+
		"\u0005\u0015\u0000\u0000\u0091\u00ad\u0003\u0006\u0003\u0000\u0092\u0093"+
		"\u0005\u0007\u0000\u0000\u0093\u00ad\u0003\u0012\t\u0012\u0094\u0095\u0005"+
		"\u0016\u0000\u0000\u0095\u00ad\u0003\u0012\t\u0011\u0096\u0097\u0005 "+
		"\u0000\u0000\u0097\u00ad\u0003\u0012\t\t\u0098\u0099\u0005\u0010\u0000"+
		"\u0000\u0099\u009a\u0003\u0012\t\u0000\u009a\u009b\u0005\u0011\u0000\u0000"+
		"\u009b\u00ad\u0001\u0000\u0000\u0000\u009c\u00ad\u0005&\u0000\u0000\u009d"+
		"\u00ad\u0005\'\u0000\u0000\u009e\u00a2\u0005\b\u0000\u0000\u009f\u00a1"+
		"\b\u0001\u0000\u0000\u00a0\u009f\u0001\u0000\u0000\u0000\u00a1\u00a4\u0001"+
		"\u0000\u0000\u0000\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a3\u00a5\u0001\u0000\u0000\u0000\u00a4\u00a2\u0001"+
		"\u0000\u0000\u0000\u00a5\u00ad\u0005\b\u0000\u0000\u00a6\u00ad\u0005!"+
		"\u0000\u0000\u00a7\u00ad\u0005\"\u0000\u0000\u00a8\u00ad\u0005\u0012\u0000"+
		"\u0000\u00a9\u00aa\u0005&\u0000\u0000\u00aa\u00ab\u0005#\u0000\u0000\u00ab"+
		"\u00ad\u0003\u0012\t\u0001\u00ac^\u0001\u0000\u0000\u0000\u00acl\u0001"+
		"\u0000\u0000\u0000\u00act\u0001\u0000\u0000\u0000\u00acz\u0001\u0000\u0000"+
		"\u0000\u00ac\u0084\u0001\u0000\u0000\u0000\u00ac\u0090\u0001\u0000\u0000"+
		"\u0000\u00ac\u0092\u0001\u0000\u0000\u0000\u00ac\u0094\u0001\u0000\u0000"+
		"\u0000\u00ac\u0096\u0001\u0000\u0000\u0000\u00ac\u0098\u0001\u0000\u0000"+
		"\u0000\u00ac\u009c\u0001\u0000\u0000\u0000\u00ac\u009d\u0001\u0000\u0000"+
		"\u0000\u00ac\u009e\u0001\u0000\u0000\u0000\u00ac\u00a6\u0001\u0000\u0000"+
		"\u0000\u00ac\u00a7\u0001\u0000\u0000\u0000\u00ac\u00a8\u0001\u0000\u0000"+
		"\u0000\u00ac\u00a9\u0001\u0000\u0000\u0000\u00ad\u00d8\u0001\u0000\u0000"+
		"\u0000\u00ae\u00af\n\u0010\u0000\u0000\u00af\u00b0\u0005\t\u0000\u0000"+
		"\u00b0\u00d7\u0003\u0012\t\u0011\u00b1\u00b2\n\u000f\u0000\u0000\u00b2"+
		"\u00b3\u0005\n\u0000\u0000\u00b3\u00d7\u0003\u0012\t\u0010\u00b4\u00b5"+
		"\n\u000e\u0000\u0000\u00b5\u00b6\u0005\u000b\u0000\u0000\u00b6\u00d7\u0003"+
		"\u0012\t\u000f\u00b7\u00b8\n\r\u0000\u0000\u00b8\u00b9\u0005\f\u0000\u0000"+
		"\u00b9\u00d7\u0003\u0012\t\u000e\u00ba\u00bb\n\f\u0000\u0000\u00bb\u00bc"+
		"\u0005\r\u0000\u0000\u00bc\u00d7\u0003\u0012\t\r\u00bd\u00be\n\u000b\u0000"+
		"\u0000\u00be\u00bf\u0005\u000e\u0000\u0000\u00bf\u00d7\u0003\u0012\t\f"+
		"\u00c0\u00c1\n\n\u0000\u0000\u00c1\u00c2\u0005\u000f\u0000\u0000\u00c2"+
		"\u00d7\u0003\u0012\t\u000b\u00c3\u00c6\n\u0019\u0000\u0000\u00c4\u00c5"+
		"\u0005+\u0000\u0000\u00c5\u00c7\u0003\u0006\u0003\u0000\u00c6\u00c4\u0001"+
		"\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000\u0000\u0000\u00c7\u00c8\u0001"+
		"\u0000\u0000\u0000\u00c8\u00c9\u0005\u0006\u0000\u0000\u00c9\u00ca\u0005"+
		"&\u0000\u0000\u00ca\u00d3\u0005\u0010\u0000\u0000\u00cb\u00d0\u0003\u0012"+
		"\t\u0000\u00cc\u00cd\u0005\u0004\u0000\u0000\u00cd\u00cf\u0003\u0012\t"+
		"\u0000\u00ce\u00cc\u0001\u0000\u0000\u0000\u00cf\u00d2\u0001\u0000\u0000"+
		"\u0000\u00d0\u00ce\u0001\u0000\u0000\u0000\u00d0\u00d1\u0001\u0000\u0000"+
		"\u0000\u00d1\u00d4\u0001\u0000\u0000\u0000\u00d2\u00d0\u0001\u0000\u0000"+
		"\u0000\u00d3\u00cb\u0001\u0000\u0000\u0000\u00d3\u00d4\u0001\u0000\u0000"+
		"\u0000\u00d4\u00d5\u0001\u0000\u0000\u0000\u00d5\u00d7\u0005\u0011\u0000"+
		"\u0000\u00d6\u00ae\u0001\u0000\u0000\u0000\u00d6\u00b1\u0001\u0000\u0000"+
		"\u0000\u00d6\u00b4\u0001\u0000\u0000\u0000\u00d6\u00b7\u0001\u0000\u0000"+
		"\u0000\u00d6\u00ba\u0001\u0000\u0000\u0000\u00d6\u00bd\u0001\u0000\u0000"+
		"\u0000\u00d6\u00c0\u0001\u0000\u0000\u0000\u00d6\u00c3\u0001\u0000\u0000"+
		"\u0000\u00d7\u00da\u0001\u0000\u0000\u0000\u00d8\u00d6\u0001\u0000\u0000"+
		"\u0000\u00d8\u00d9\u0001\u0000\u0000\u0000\u00d9\u0013\u0001\u0000\u0000"+
		"\u0000\u00da\u00d8\u0001\u0000\u0000\u0000\u0014\u0017\u001d\'36?IQ\\"+
		"fi\u0080\u008a\u00a2\u00ac\u00c6\u00d0\u00d3\u00d6\u00d8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}