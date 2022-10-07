grammar YAPL;
@header {
package com.company.yapl;
}

// Productions
//Main execution the parent of the tree
program
    : (class_grammar)+
    ;
class_grammar
    : CLASS className=CLASS_LEXER (INHERITS parent=CLASS_LEXER)? '{' features '}' ';'
    ; //Done

features: (class_feature)*;

type_grammar: CLASS_LEXER | SELF_TYPE; //Done

class_function_declaration : (id=OBJ_TYPE '(' (param1=declaration (',' params=declaration)*)? ')' ':' return_type=type_grammar '{' (body=expr)* '}'); //Done  again

class_property_declaration : (id=OBJ_TYPE ':' class=type_grammar (ASSIGN value=expr)?); //Done

//It can be either a property or a function
class_feature
    : class_function_declaration ';'
    | class_property_declaration ';'
    ; //Done

declaration
    : id=OBJ_TYPE ':' className=type_grammar
    ; //Done

declaration_with_possible_assignation
    : id=OBJ_TYPE ':' className=type_grammar (ASSIGN expr)?
    ; //Done Again

expr:
//Invocación de función a partir de expresión
invocator=expr (Scope type_grammar)? '.' functionId=OBJ_TYPE PARENTHESIS_START (expr (',' expr)*)? PARENTHESIS_END #scopeInvocationExpression //Done again
    | functionId=OBJ_TYPE PARENTHESIS_START ( expr (',' expr)* )? PARENTHESIS_END #functionInvocationExpression //Done again
    | IF condition=expr THEN expr ELSE expr FI #ifExpression //Done again
    | WHILE condition=expr LOOP block=expr POOL #whileExpression //Done Again
    | '{' (expr ';')+ '}' #bracketExpression //Done  Again
    | LET declaration_with_possible_assignation (',' declaration_with_possible_assignation )* IN expr #letExpression // Done again
    | NEW type_grammar #classInstantiationExpression //Done Again
    | '~' expr #smallNegationExpression //Done Again
    | ISVOID expr #isVoidExpression //Done Again
    | left=expr op=MULTIPLICATION_OPERATOR right=expr #operatorExpression //Done Again
    | left=expr op=DIVISION_OPERATOR right=expr #operatorExpression //Done Again
    | left=expr op=SUM_OPERATOR right=expr #operatorExpression //Done Again
    | left=expr op=REST_OPERATOR right=expr #operatorExpression //Done Again
    | left=expr op=SMALLER_THAN right=expr #boolComparisonExpression //Done Again
    | left=expr op=SMALLER_OR_EQUAL_THAN right=expr #boolComparisonExpression //Done Again
    | left=expr op=EQUAL right=expr #boolComparisonExpression //Done Again
    | NOT expr #notExpression //Done Again
    | PARENTHESIS_START expr PARENTHESIS_END #parenthesisExpression //Done Again
    | OBJ_TYPE #idExpression //Done Again
    | INT #intExpression //Done Again
    | '"'(~(EOF| '"'))*  '"' #stringExpression //Done Again
    | TRUE #boolExpression //Done Again
    | FALSE #boolExpression //Done Again
    | SELF #selfExpression //Done Again
    //Asignación de valor a un id
    | OBJ_TYPE ASSIGN expr #variableValueAsignationExpression //Done Again
    ;


//Fragments


fragment LOWERCASE  : [a-z] ;
fragment UPPERCASE  : [A-Z] ;
fragment NUM_RANGE  : [0-9] ;
fragment C          : ('C'|'c') ;
fragment A         : ('A'|'a') ;
fragment L         : ('L'|'l') ;
fragment S          : ('S'|'s') ;
fragment E          : ('E'|'e') ;
fragment H          : ('H'|'h') ;
fragment F          : ('F'|'f') ;
fragment T          : ('T'|'t') ;
fragment Y          : ('Y'|'y') ;
fragment I          : ('I'|'i') ;
fragment P          : ('P'|'p') ;
fragment O          : ('O'|'o') ;
fragment V         : ('V'|'v') ;
fragment N          : ('N'|'n') ;
fragment W          : ('W'|'w') ;
fragment D          : ('D'|'d') ;
fragment R          : ('R'|'r') ;
fragment U          : ('U'|'u') ;



// Keywords

MULTIPLICATION_OPERATOR: '*';
DIVISION_OPERATOR: '/';
SUM_OPERATOR: '+';
REST_OPERATOR: '-';

//Logical operators
SMALLER_THAN : '<';
SMALLER_OR_EQUAL_THAN : '<=';
EQUAL: '=';

//Groupers
PARENTHESIS_START: '(';
PARENTHESIS_END: ')';

SELF
    : 'self'
    ;

//Special self type keyword
SELF_TYPE
    : 'SELF_TYPE'
    ;
IF
    : I F
    ;
NEW
    : N E W
    ;
ISVOID
    : I S V O I D
    ;
LET
    : L E T
    ;
IN
    : I N
    ;
WHILE
    : W H I L E
    ;
LOOP
    : L O O P
    ;
POOL
    : P O O L
    ;
ELSE
    : E L S E
    ;
FI
    : F I
    ;
THEN
    : T H E N
    ;
INHERITS
    : I N H E R I T S
    ;
NOT
    : N O T
    ;
TRUE
    : 'true'
    ;
FALSE
    : 'false'
    ;
ASSIGN
    : '<-'
    ;

// Lexer rules
CLASS : C L A S S ;

//Refers to a class name a class declaration
CLASS_LEXER
    : UPPERCASE (UPPERCASE | LOWERCASE | '_' | NUM_RANGE)*
    ;


OBJ_TYPE
    : LOWERCASE (UPPERCASE | LOWERCASE | '_' | NUM_RANGE )*;


INT
    : NUM_RANGE+
    ;

COMMENT
    : (('(*' .*? '*)')) -> skip
    ;

LINE_COMMENT : ('--' ~('\n')*) -> skip;

WS
    : ( '\t'| '\r' | '\n' | '\f' | ' ' ) -> skip
    ;

Scope: '@';
StringCharacter : ~["\\\r\n] |   '\\';



//OPAR : '(';
//CPAR : ')';


