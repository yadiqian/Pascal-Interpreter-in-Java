grammar Pascal;

program: programHeading programBlock '.';

programHeading: PROGRAM ID ';';

programBlock: declarationBlock functionDec BEGIN block END;

declarationBlock: 
// nothing
| VAR (declaration ';')+ declarationBlock
;

declaration: varName ':' varType;

varName: ID (',' ID)*;

varType:
  REAL
  | BOOLEAN
  ;

functionDec:
  // nothing
  | function functionDec
  | procedure functionDec
  ;

block:
  | statements
  | statements BEGIN block END ';' statements block
  ;

statements:
  // nothing
  | statement statements;

statement: 
  assignment ';' #assignmentStmt
  | condition ';' #condStmt
  | printStatement ';' #printStmt
  | input ';' #inputStmt
  | caseExpr ';' #caseExprStmt
  | whileLoop ';' #whileStmt
  | forLoop ';' #forStmt
  | functionCall ';' #functionCallStmt
  ;

assignment: 
  ID ':=' value #valueAssignment
  | ID ':=' functionCall #functionAssignment
  ;

value returns[String s]:
  ID  #idValue
  | bool_op  #boolValue
  | expr  #exprValue
  ;

printStatement: WRITELN '(' printStrs ')';

printStrs returns[String s]:
  printStr #singlePrintStrs 
  | pl=printStr ',' pr=printStrs #mulPrintStrs
  ;

printStr returns[String s]:
  STRING #strPrintStr
  | value #valuePrintStr
  | bool_logic #boolLogicPrintStr
  | #nothingPrintStr
  ;

expr returns[Double d]:
  EXP '(' e=expr ')' #expExpr
  | LN '(' e=expr ')' #lnExpr
  | COS '(' e=expr ')' #cosExpr
  | SIN '(' e=expr ')' #sinExpr
  | SQRT '(' e=expr ')' #sqrtExpr
  |'(' e=expr ')' #parExpr
  | el=expr '/' er=expr #divExpr
  | el=expr '*' er=expr #mulExpr
  | el=expr '-' er=expr #subExpr
  | el=expr '+' er=expr #plusExpr
  | DOUBLE #doubleExpr
  | INT #intExpr
  | valID #idExpr
  | '-' EXP '(' e=expr ')' #negExpExpr
  | '-' LN '(' e=expr ')' #negLnExpr
  | '-' COS '(' e=expr ')' #negCosExpr
  | '-' SIN '(' e=expr ')' #negSinExpr
  | '-' SQRT '(' e=expr ')' #negSqrtExpr
  | '-' '(' e=expr ')' #negParExpr
  | '-' DOUBLE #negDoubleExpr
  | '-' INT #negIntExpr
  | '-' valID #negIdExpr
  ;

valID returns [String s]: ID;

bool_op returns[boolean b]:
  '(' e=bool_op ')' #parBool_op
  | NOT e=bool_op #notBool_op
  | el=bool_op AND er=bool_op #andBool_op
  | el=bool_op OR er=bool_op #orBool_op
  | el=bool_op XOR er=bool_op #xorBool_op
  | TRUE #trueBool_op
  | FALSE #falseBool_op
  | valID #idBool_op
  ;

condition: IF '('? b=bool_logic ')'? THEN c1=conditionClause (ELSE c2=conditionClause)?;

bool_logic returns [boolean b]:
  el=expression '=' er=expression #equalBool_logic
  | el=expression '>' er=expression #greaterBool_logic
  | el=expression '<' er=expression #smallerBool_logic
  | bool_op #bool_opBool_logic
  ;

expression returns [String s]: 
  valID #idExpression
  | expr #exprExpression
  | bool_op #bool_opExpression
  ;

conditionClause:
  assignment #assignmentClause
  | condition #condClause
  | printStatement #printClause
  | input #inputClause
  | BEGIN statements END #blockClause
  ;

loopBlock:
  conditionClause #clauseLoopBlock
  | BEGIN loopStmts END #stmtLoopBlock
  ;

loopStmts: loopStmt*;

loopStmt: 
  statement #stmtLoop
  | BREAK ';' #breakLoop
  | CONTINUE ';' #continueLoop
  ;

whileLoop: WHILE '('? b=bool_logic ')'? DO loopBlock;

forLoop: FOR ID ':=' start=expr order end=expr DO loopBlock;

order:
  TO #incOrder
  | DOWNTO #decOrder
  ;

caseExpr: CASE '(' caseId ')' OF caseStmt* ELSE caseElse END;

caseStmt: caseLabel ':' conditionClause ';';

caseElse: BEGIN? statements (END ';')?;

caseId returns [String s]: ID;

caseLabel returns [String s]:
  e=expr #exprCaseLabel
  | b=bool_op #boolCaseLabel
  ;

input: READLN '(' i=inputIDs ')';

inputIDs:
  | ID
  | ID ',' inputIDs
  ;

function: functionHeader ';' body ';';

functionHeader: FUNCTION ID '(' param? ')' ':' varType;

body: declarationBlock BEGIN block END;

param: declaration (';' declaration)*;

functionCall: ID '(' paramCall? ')';

paramCall: value (',' value)*;

procedure: procedureHeader ';' body ';';

procedureHeader: PROCEDURE ID ('(' paramList ')')?;

paramList: 
  param (';' varDeclare)?
  | varDeclare (';' param)?
  ;

varDeclare: VAR declaration (';' VAR declaration)*;

PROGRAM: P R O G R A M;
BEGIN: B E G I N;
END: E N D;
VAR: V A R;
REAL: R E A L;
BOOLEAN: B O O L E A N;
TRUE: T R U E;
FALSE: F A L S E;
AND: A N D;
OR: O R;
XOR: X O R;
NOT: N O T;
WRITELN: W R I T E L N;
SQRT: S Q R T;
SIN: S I N;
COS: C O S;
LN: L N;
EXP: E X P;
IF: I F;
THEN: T H E N;
ELSE: E L S E;
CASE: C A S E;
OF: O F;
READLN: R E A D L N;
WHILE: W H I L E;
DO: D O;
FOR: F O R;
TO: T O;
DOWN: D O W N;
DOWNTO: D O W N T O;
BREAK: B R E A K;
CONTINUE: C O N T I N U E;
FUNCTION: F U N C T I O N;
PROCEDURE: P R O C E D U R E;
ID: [A-Za-z_][A-Za-z0-9_]*;
STRING: '\'' ('\'\'' | ~ ('\''))* '\'';
INT: [0-9]+ ;
DOUBLE: INT ('.' INT)?;
COMMENT: '(*' .*? '*)' -> skip;
WS: [ \t\r\n]+ -> skip;

fragment A: ('a'|'A');
fragment B: ('b'|'B');
fragment C: ('c'|'C');
fragment D: ('d'|'D');
fragment E: ('e'|'E');
fragment F: ('f'|'F');
fragment G: ('g'|'G');
fragment H: ('h'|'H');
fragment I: ('i'|'I');
fragment J: ('j'|'J');
fragment K: ('k'|'K');
fragment L: ('l'|'L');
fragment M: ('m'|'M');
fragment N: ('n'|'N');
fragment O: ('o'|'O');
fragment P: ('p'|'P');
fragment Q: ('q'|'Q');
fragment R: ('r'|'R');
fragment S: ('s'|'S');
fragment T: ('t'|'T');
fragment U: ('u'|'U');
fragment V: ('v'|'V');
fragment W: ('w'|'W');
fragment X: ('x'|'X');
fragment Y: ('y'|'Y');
fragment Z: ('z'|'Z');
