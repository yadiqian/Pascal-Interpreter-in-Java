import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class MyVisitor extends PascalBaseVisitor<Object> {

  public static final String REAL_TYPE = "real";
  public static final String BOOL_TYPE = "boolean";

  private HashMap<String, String[]> symbolTable = new HashMap<String, String[]>();
  private Scanner scan = new Scanner(System.in);

  private void updateVar(String id, String value) {
    id = id.toLowerCase();
    if (symbolTable.containsKey(id)) {
      String[] var = symbolTable.get(id);
      var[1] = value;
    } else {
      System.err.println("Variable " + id + " not declared");
    }
  }

  @Override
  public Object visitStatements(PascalParser.StatementsContext ctx) {
    if (ctx.statements() == null)
      return null;
    this.visit(ctx.statement());
    return this.visit(ctx.statements());
  }

  @Override
  public Object visitAssignmentStmt(PascalParser.AssignmentStmtContext ctx) {
    return this.visit(ctx.assignment());
  }

  @Override
  public Object visitCondStmt(PascalParser.CondStmtContext ctx) {
    return this.visit(ctx.condition());
  }

  @Override
  public Object visitPrintStmt(PascalParser.PrintStmtContext ctx) {
    return this.visit(ctx.printStatement());
  }

  @Override
  public Object visitInputStmt(PascalParser.InputStmtContext ctx) {
    return this.visit(ctx.input());
  }

  @Override
  public Object visitCaseExprStmt(PascalParser.CaseExprStmtContext ctx) {
    return this.visit(ctx.caseExpr());
  }

  @Override
  public Object visitWhileStmt(PascalParser.WhileStmtContext ctx) {
    return this.visit(ctx.whileLoop());
  }

  @Override
  public Object visitForStmt(PascalParser.ForStmtContext ctx) {
    return this.visit(ctx.forLoop());
  }

  @Override
  public Object visitDeclaration(PascalParser.DeclarationContext ctx) {
    String[] names = ctx.varName().getText().split(",");
    String type = ctx.varType().getText().toLowerCase();
    for (String s : names) {
      String name = s.toLowerCase();
      String defaultVal = "";

      if (type.equals(REAL_TYPE))
        defaultVal = "0";
      else if (type.equals(BOOL_TYPE))
        defaultVal = "FALSE";
      else
        System.err.println("Type " + type + " is not supported");

      String[] value = new String[] { type, defaultVal };
      symbolTable.put(name, value);
    }
    return null;
  }

  @Override
  public Object visitAssignment(PascalParser.AssignmentContext ctx) {
    String id = ctx.ID().getText();
    updateVar(id, this.visit(ctx.value()).toString());

    return null;
  }

  @Override
  public String visitIdValue(PascalParser.IdValueContext ctx) {
    String name = ctx.ID().getText().toLowerCase();
    String value = "";
    if (symbolTable.containsKey(name)) {
      String val = symbolTable.get(name)[1].toString();
      value = symbolTable.get(name)[0].equals(BOOL_TYPE) ? val.toUpperCase() : val;
    } else {
      System.err.println("Variable " + ctx.ID().getText() + " not declared");
    }
    return value;
  }

  @Override
  public String visitBoolValue(PascalParser.BoolValueContext ctx) {
    Object obj = this.visit(ctx.bool_op());
    return obj.toString().toUpperCase();
  }

  @Override
  public String visitExprValue(PascalParser.ExprValueContext ctx) {
    Object obj = this.visit(ctx.expr());
    return obj.toString();
  }

  @Override
  public Object visitPrintStatement(PascalParser.PrintStatementContext ctx) {
    Object obj = this.visit(ctx.printStrs());
    System.out.println(obj);
    return obj;
  }

  @Override
  public Object visitSinglePrintStrs(PascalParser.SinglePrintStrsContext ctx) {
    return this.visit(ctx.printStr());
  }

  @Override
  public Object visitMulPrintStrs(PascalParser.MulPrintStrsContext ctx) {
    String result = this.visit(ctx.pl).toString() + " " + this.visit(ctx.pr).toString();
    return result;
  }

  @Override
  public String visitStrPrintStr(PascalParser.StrPrintStrContext ctx) {
    String str = ctx.STRING().getText();
    str = str.substring(1, str.length() - 1);
    String output = "";
    for (int i = 0; i < str.length(); i++) {
      output += str.charAt(i);
      if (str.charAt(i) == '\'')
        i++;
    }
    return output;
  }

  @Override
  public Object visitValuePrintStr(PascalParser.ValuePrintStrContext ctx) {
    Object obj = this.visit(ctx.value());
    return obj;
  }

  @Override
  public String visitBoolLogicPrintStr(PascalParser.BoolLogicPrintStrContext ctx) {
    Object obj = this.visit(ctx.bool_logic());
    return obj.toString().toUpperCase();
  }

  @Override
  public String visitNothingPrintStr(PascalParser.NothingPrintStrContext ctx) {
    return "";
  }

  @Override
  public Double visitExpExpr(PascalParser.ExpExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return Math.exp((Double) obj);
  }

  @Override
  public Double visitLnExpr(PascalParser.LnExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return Math.log((Double) obj);
  }

  @Override
  public Double visitCosExpr(PascalParser.CosExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return Math.cos((Double) obj);
  }

  @Override
  public Double visitSinExpr(PascalParser.SinExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return Math.sin((Double) obj);
  }

  @Override
  public Double visitSqrtExpr(PascalParser.SqrtExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return Math.sqrt((Double) obj);
  }

  @Override
  public Double visitParExpr(PascalParser.ParExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return (Double) obj;
  }

  @Override
  public Double visitDivExpr(PascalParser.DivExprContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return (Double) left / (Double) right;
  }

  @Override
  public Double visitMulExpr(PascalParser.MulExprContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return (Double) left * (Double) right;
  }

  @Override
  public Double visitSubExpr(PascalParser.SubExprContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return (Double) left - (Double) right;
  }

  @Override
  public Double visitPlusExpr(PascalParser.PlusExprContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return (Double) left + (Double) right;
  }

  @Override
  public Double visitDoubleExpr(PascalParser.DoubleExprContext ctx) {
    return Double.parseDouble(ctx.DOUBLE().getText());
  }

  @Override
  public Double visitIntExpr(PascalParser.IntExprContext ctx) {
    return Double.parseDouble(ctx.INT().getText());
  }

  @Override
  public Double visitIdExpr(PascalParser.IdExprContext ctx) {
    Object obj = this.visit(ctx.valID());
    return Double.parseDouble(obj.toString());
  }

  @Override
  public Double visitNegExpExpr(PascalParser.NegExpExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return -Math.exp(Double.parseDouble(obj.toString()));
  }

  @Override
  public Double visitNegLnExpr(PascalParser.NegLnExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return -Math.log(Double.parseDouble(obj.toString()));
  }

  @Override
  public Double visitNegCosExpr(PascalParser.NegCosExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return -Math.cos(Double.parseDouble(obj.toString()));
  }

  @Override
  public Double visitNegSinExpr(PascalParser.NegSinExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return -Math.sin(Double.parseDouble(obj.toString()));
  }

  @Override
  public Double visitNegSqrtExpr(PascalParser.NegSqrtExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return -Math.sqrt(Double.parseDouble(obj.toString()));
  }

  @Override
  public Double visitNegParExpr(PascalParser.NegParExprContext ctx) {
    Object obj = this.visit(ctx.e);
    return -Double.parseDouble(obj.toString());
  }

  @Override
  public Double visitNegDoubleExpr(PascalParser.NegDoubleExprContext ctx) {
    return -Double.parseDouble(ctx.DOUBLE().getText());
  }

  @Override
  public Double visitNegIntExpr(PascalParser.NegIntExprContext ctx) {
    return -Double.parseDouble(ctx.INT().getText());
  }

  @Override
  public Double visitNegIdExpr(PascalParser.NegIdExprContext ctx) {
    Object obj = this.visit(ctx.valID());
    return -Double.parseDouble(obj.toString());
  }

  @Override
  public String visitValID(PascalParser.ValIDContext ctx) {
    String name = ctx.ID().getText().toLowerCase();
    String value = "";
    if (symbolTable.containsKey(name)) {
      value = symbolTable.get(name)[1];
    } else {
      System.err.println("Variable " + ctx.ID().getText() + " not declared");
    }
    return value;
  }

  @Override
  public Boolean visitParBool_op(PascalParser.ParBool_opContext ctx) {
    Object obj = this.visit(ctx.e);
    return (Boolean) obj;
  }

  @Override
  public Boolean visitNotBool_op(PascalParser.NotBool_opContext ctx) {
    Object obj = this.visit(ctx.e);
    return !(Boolean) obj;
  }

  @Override
  public Boolean visitAndBool_op(PascalParser.AndBool_opContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return (Boolean) left && (Boolean) right;
  }

  @Override
  public Boolean visitOrBool_op(PascalParser.OrBool_opContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return (Boolean) left || (Boolean) right;
  }

  @Override
  public Boolean visitXorBool_op(PascalParser.XorBool_opContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return (Boolean) left ^ (Boolean) right;
  }

  @Override
  public Boolean visitTrueBool_op(PascalParser.TrueBool_opContext ctx) {
    return true;
  }

  @Override
  public Boolean visitFalseBool_op(PascalParser.FalseBool_opContext ctx) {
    return false;
  }

  @Override
  public Boolean visitIdBool_op(PascalParser.IdBool_opContext ctx) {
    Object obj = this.visit(ctx.valID());
    return Boolean.valueOf(obj.toString());
  }

  @Override
  public Object visitCondition(PascalParser.ConditionContext ctx) {
    Boolean val = Boolean.valueOf(this.visit(ctx.b).toString());
    if (val)
      this.visit(ctx.c1);
    else if (ctx.c2 != null)
      this.visit(ctx.c2);
    return null;
  }

  @Override
  public Boolean visitEqualBool_logic(PascalParser.EqualBool_logicContext ctx) {
    Object left = this.visit(ctx.er);
    Object right = this.visit(ctx.er);
    return left.equals(right);
  }

  @Override
  public Boolean visitGreaterBool_logic(PascalParser.GreaterBool_logicContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return Double.valueOf((String) left) > Double.valueOf((String) right);
  }

  @Override
  public Boolean visitSmallerBool_logic(PascalParser.SmallerBool_logicContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return Double.valueOf((String) left) < Double.valueOf((String) right);
  }

  @Override
  public Boolean visitBool_opBool_logic(PascalParser.Bool_opBool_logicContext ctx) {
    Object obj = this.visit(ctx.bool_op());
    return (Boolean) obj;
  }

  @Override
  public String visitIdExpression(PascalParser.IdExpressionContext ctx) {
    return this.visit(ctx.valID()).toString();
  }

  @Override
  public String visitExprExpression(PascalParser.ExprExpressionContext ctx) {
    return this.visit(ctx.expr()).toString();
  }

  @Override
  public String visitBool_opExpression(PascalParser.Bool_opExpressionContext ctx) {
    return this.visit(ctx.bool_op()).toString();
  }

  @Override
  public Object visitAssignmentClause(PascalParser.AssignmentClauseContext ctx) {
    return this.visit(ctx.assignment());
  }

  @Override
  public Object visitCondClause(PascalParser.CondClauseContext ctx) {
    return this.visit(ctx.condition());
  }

  @Override
  public Object visitPrintClause(PascalParser.PrintClauseContext ctx) {
    return this.visit(ctx.printStatement());
  }

  @Override
  public Object visitInputClause(PascalParser.InputClauseContext ctx) {
    return this.visit(ctx.input());
  }

  @Override
  public Object visitBlockClause(PascalParser.BlockClauseContext ctx) {
    return this.visit(ctx.statements());
  }

  @Override
  public Object visitClauseLoopBlock(PascalParser.ClauseLoopBlockContext ctx) {
    this.visit(ctx.conditionClause());
    return true;
  }

  @Override
  public Object visitStmtLoopBlock(PascalParser.StmtLoopBlockContext ctx) {
    return Boolean.valueOf(this.visit(ctx.loopStmts()).toString());
  }

  public Object visitLoopStmts(PascalParser.LoopStmtsContext ctx) {
    for (PascalParser.LoopStmtContext stmt : ctx.loopStmt()) {
      Integer action = Integer.valueOf(this.visit(stmt).toString());
      if (action == 0)
        continue;
      if (action == 1)
        return false;
      if (action == 2)
        return true;
    }
    return true;
  }

  @Override
  public Object visitStmtLoop(PascalParser.StmtLoopContext ctx) {
    this.visit(ctx.statement());
    return 0;
  }

  @Override
  public Object visitBreakLoop(PascalParser.BreakLoopContext ctx) {
    return 1;
  }

  @Override
  public Object visitContinueLoop(PascalParser.ContinueLoopContext ctx) {
    return 2;
  }

  @Override
  public Object visitWhileLoop(PascalParser.WhileLoopContext ctx) {
    Boolean value = Boolean.valueOf(this.visit(ctx.b).toString());

    while (value) {
      Boolean action = Boolean.valueOf(this.visit(ctx.loopBlock()).toString());
      if (!action)
        break;
      value = Boolean.valueOf(this.visit(ctx.b).toString());
    }
    return null;
  }

  @Override
  public Object visitForLoop(PascalParser.ForLoopContext ctx) {
    String id = ctx.ID().getText();
    Float start = Float.valueOf(this.visit(ctx.start).toString());
    Float end = Float.valueOf(this.visit(ctx.end).toString());
    Integer s = Math.round(start);
    Integer e = Math.round(end);
    updateVar(id, Double.valueOf(s).toString());

    Boolean increasing = Boolean.valueOf(this.visit(ctx.order()).toString());

    if (increasing) {
      for (int i = s; i <= e; i++) {
        Boolean action = Boolean.valueOf(this.visit(ctx.loopBlock()).toString());
        if (!action)
          break;
        updateVar(id, Double.toString(i + 1));
      }
    } else {
      for (int i = s; i >= e; i--) {
        Boolean action = Boolean.valueOf(this.visit(ctx.loopBlock()).toString());
        if (!action)
          break;
        updateVar(id, Double.toString(i - 1));
      }
    }
    return null;
  }

  @Override
  public Object visitIncOrder(PascalParser.IncOrderContext ctx) {
    return true;
  }

  @Override
  public Object visitDecOrder(PascalParser.DecOrderContext ctx) {
    return false;
  }

  @Override
  public Object visitCaseExpr(PascalParser.CaseExprContext ctx) {
    String labelVal = this.visit(ctx.caseId()).toString();
    for (PascalParser.CaseStmtContext stmt : ctx.caseStmt()) {
      String caseVal = this.visit(stmt.caseLabel()).toString();
      if (labelVal.equals(caseVal)) {
        this.visit(stmt.conditionClause());
        return null;
      }
    }
    return this.visit(ctx.caseElse());
  }

  @Override
  public Object visitCaseElse(PascalParser.CaseElseContext ctx) {
    return this.visit(ctx.statements());
  }

  @Override
  public String visitCaseId(PascalParser.CaseIdContext ctx) {
    String id = ctx.ID().getText().toLowerCase();
    String val = "";
    if (symbolTable.containsKey(id)) {
      String value = symbolTable.get(id)[1];
      if (symbolTable.get(id)[0].equals(REAL_TYPE)) {
        value = Double.toString(Math.round(Double.parseDouble(value)));
      }
      val = value.toLowerCase();
    } else {
      System.err.println("Variable " + ctx.ID().getText() + " not declared");
    }
    return val;
  }

  @Override
  public String visitExprCaseLabel(PascalParser.ExprCaseLabelContext ctx) {
    Object obj = this.visit(ctx.e);
    return Double.toString(Math.round((Double) obj));
  }

  @Override
  public String visitBoolCaseLabel(PascalParser.BoolCaseLabelContext ctx) {
    return this.visit(ctx.b).toString();
  }

  @Override
  public Object visitInput(PascalParser.InputContext ctx) {
    String[] arr = ctx.i.getText().split(",");
    for (String s : arr) {
      String value = scan.nextLine();
      String id = s.toLowerCase();

      if (symbolTable.containsKey(id)) {
        symbolTable.get(id)[1] = value;
      } else {
        System.err.println("Variable " + s + " not declared");
      }
    }
    return visitChildren(ctx);
  }

}