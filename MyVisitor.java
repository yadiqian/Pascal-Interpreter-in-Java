import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import javafx.util.Pair;
import java.util.ArrayList;

public class MyVisitor extends PascalBaseVisitor<Object> {

  public static final String REAL_TYPE = "real";
  public static final String BOOL_TYPE = "boolean";

  private Scanner scan;
  private Stack<Scope> scopes;
  private HashMap<String, HashMap<String, Pair<String, PascalParser.FunctionContext>>> functions;
  private HashMap<String, HashMap<String, Pair<String, PascalParser.ProcedureContext>>> procedures;

  public MyVisitor() {
    scan = new Scanner(System.in);
    scopes = new Stack<Scope>();
    scopes.push(new Scope(null)); // create global scope
    functions = new HashMap<String, HashMap<String, Pair<String, PascalParser.FunctionContext>>>();
    procedures = new HashMap<String, HashMap<String, Pair<String, PascalParser.ProcedureContext>>>();
  }

  private void updateVar(String id, String value) {
    id = id.toLowerCase();

    Scope scope = scopes.peek();
    int inScope = scope.inScope(id, scopes.size() - 1);
    if (inScope != -1) {
      scopes.get(inScope).get(id)[1] = value;
    } else {
      System.out.println("Varialbe " + id + " not in scope");
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
  public Object visitFunctionCallStmt(PascalParser.FunctionCallStmtContext ctx) {
    return this.visit(ctx.functionCall());
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
      Scope scope = scopes.peek();
      scope.put(name, value);
    }
    return null;
  }

  @Override
  public Object visitAssignment(PascalParser.AssignmentContext ctx) {
    String id = ctx.ID().getText();
    String value = this.visit(ctx.value()).toString();
    updateVar(id, value);

    return null;
  }

  @Override
  public Object visitFunctionValue(PascalParser.FunctionValueContext ctx) {
    return this.visit(ctx.functionCall()).toString();
  }

  @Override
  public String visitIdValue(PascalParser.IdValueContext ctx) {
    String name = ctx.ID().getText().toLowerCase();
    String value = "";

    Scope scope = scopes.peek();
    int inScope = scope.inScope(name, scopes.size() - 1);
    if (inScope != -1) {
      Scope cur = scopes.get(inScope);
      String val = cur.get(name)[1].toString();
      value = cur.get(name)[0].equals(BOOL_TYPE) ? val.toUpperCase() : val;
    } else {
      System.err.println("Variable " + ctx.ID().getText() + " not in scope");
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
    String result = this.visit(ctx.pl).toString() + this.visit(ctx.pr).toString();
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
    if (!obj.toString().toLowerCase().equals("true") && !obj.toString().toLowerCase().equals("false")
        && !obj.toString().isEmpty()) {
      Float d = Float.valueOf(obj.toString());
      Integer i = Math.round(d);
      if (i - d == 0)
        return i.toString();
    }
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
  public Object visitFuncExpr(PascalParser.FuncExprContext ctx) {
    Object obj = this.visit(ctx.functionCall());
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

    Scope scope = scopes.peek();
    int inScope = scope.inScope(name, scopes.size() - 1);
    if (inScope != -1) {
      Scope cur = scopes.get(inScope);
      value = cur.get(name)[1];
    } else {
      System.err.println("Variable " + ctx.ID().getText() + " not in scope");
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
    Object left = this.visit(ctx.el);
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
  public Object visitGreaterEqualBool_logic(PascalParser.GreaterEqualBool_logicContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return Double.valueOf((String) left) >= Double.valueOf((String) right);
  }

  @Override
  public Object visitSmallerEqualBool_logic(PascalParser.SmallerEqualBool_logicContext ctx) {
    Object left = this.visit(ctx.el);
    Object right = this.visit(ctx.er);
    return Double.valueOf((String) left) <= Double.valueOf((String) right);
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
  public Object visitConditionLoop(PascalParser.ConditionLoopContext ctx) {
    Boolean val = Boolean.valueOf(this.visit(ctx.b).toString());
    if (val)
      return this.visit(ctx.c1);
    else if (ctx.c2 != null)
      return this.visit(ctx.c2);
    return 0;
  }

  @Override
  public Object visitConditionClauseLoop(PascalParser.ConditionClauseLoopContext ctx) {
    this.visit(ctx.conditionClause());
    return 0;
  }

  @Override
  public Object visitContinueClauseLoop(PascalParser.ContinueClauseLoopContext ctx) {
    return 2;
  }

  @Override
  public Object visitBreakClauseLoop(PascalParser.BreakClauseLoopContext ctx) {
    return 1;
  }

  @Override
  public Object visitBlockClauseLoop(PascalParser.BlockClauseLoopContext ctx) {
    return this.visit(ctx.loopStmts());
  }

  @Override
  public Object visitClauseLoopBlock(PascalParser.ClauseLoopBlockContext ctx) {
    return this.visit(ctx.conditionLoopClause());
  }

  @Override
  public Object visitStmtLoopBlock(PascalParser.StmtLoopBlockContext ctx) {
    String action = this.visit(ctx.loopStmts()).toString();
    if (action == "1")
      return false;
    return true;
  }

  public Object visitLoopStmts(PascalParser.LoopStmtsContext ctx) {
    for (PascalParser.LoopStmtContext stmt : ctx.loopStmt()) {
      Integer action = Integer.valueOf(this.visit(stmt).toString());
      if (action == 0)
        continue;
      if (action == 1)
        return 1;
      if (action == 2)
        return 2;
    }
    return 0;
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
  public Object visitConditionLoopStmt(PascalParser.ConditionLoopStmtContext ctx) {
    return this.visit(ctx.conditionLoop());
  }

  @Override
  public Object visitWhileLoop(PascalParser.WhileLoopContext ctx) {
    Boolean value = Boolean.valueOf(this.visit(ctx.b).toString());

    while (value) {
      Boolean action = true;
      String s = this.visit(ctx.loopBlock()).toString();
      if (s.equals("1"))
        action = false;
      else if (!s.equals("0") && !s.equals("2"))
        action = Boolean.valueOf(s);

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
        Boolean action = true;
        String ret = this.visit(ctx.loopBlock()).toString();
        if (ret.equals("1"))
          action = false;
        else if (!ret.equals("0") && !ret.equals("2"))
          action = Boolean.valueOf(ret);

        if (!action)
          break;

        if (i != e)
          updateVar(id, Double.toString(i + 1));
      }
    } else {
      for (int i = s; i >= e; i--) {
        Boolean action = true;
        String ret = this.visit(ctx.loopBlock()).toString();
        if (ret.equals("1"))
          action = false;
        else if (!ret.equals("0") && !ret.equals("2"))
          action = Boolean.valueOf(ret);

        if (!action)
          break;
        if (i != e)
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

    Scope scope = scopes.peek();
    int inScope = scope.inScope(id, scopes.size() - 1);
    if (inScope != -1) {
      Scope cur = scopes.get(inScope);
      String value = cur.get(id)[1];
      if (cur.get(id)[0].equals(REAL_TYPE)) {
        value = Double.toString(Math.round(Double.parseDouble(value)));
      }
      val = value.toLowerCase();
    } else {
      System.err.println("Variable " + ctx.ID().getText() + " not in scope");
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

      Scope scope = scopes.peek();
      int inScope = scope.inScope(id, scopes.size() - 1);
      if (inScope != -1) {
        Scope cur = scopes.get(inScope);
        cur.get(id)[1] = value;
      } else {
        System.err.println("Variable " + s + " not in scope");
      }

    }
    return null;
  }

  @Override
  public Object visitFunction(PascalParser.FunctionContext ctx) {
    String name = ctx.ID().getText().toLowerCase();
    String param = "";
    String valName = "";

    if (ctx.param() != null) {
      String params = ctx.param().getText();
      String[] expr = params.split(";");
      for (String s : expr) {
        if (!param.isEmpty())
          param += ",";
        if (!valName.isEmpty())
          valName += ",";
        String[] valType = s.split(":");
        valName += valType[0];
        int num = valType[0].split(",").length;
        for (int j = 0; j < num; j++) {
          if (!param.isEmpty() && j != 0)
            param += ",";
          param += valType[1].toLowerCase();
        }
      }
    }

    String id = name + param;

    if (!functions.containsKey(id) || !functions.get(id).containsKey(param)) {
      Pair<String, PascalParser.FunctionContext> p = new Pair<>(valName, ctx);
      HashMap<String, Pair<String, PascalParser.FunctionContext>> map = new HashMap<>();
      map.put(param, p);
      functions.put(id, map);
    } else {
      // Set return value
      String type = ctx.varType().getText();
      String defaultVal = "";

      String[] value = new String[] { type, defaultVal };
      Scope scope = scopes.peek();
      scope.put(name, value);

      // Execute function
      this.visit(ctx.body());
    }
    return null;
  }

  @Override
  public Object visitFunctionCall(PascalParser.FunctionCallContext ctx) {
    // Get argument types
    String id = ctx.ID().getText().toLowerCase();
    String param = "";
    if (ctx.paramCall() != null) {
      String s = this.visit(ctx.paramCall()).toString();
      String[] params = s.split(",");
      for (String p : params) {
        if (!param.isEmpty())
          param += ",";
        if (p.toLowerCase().equals("true") || p.toLowerCase().equals("false")) {
          param += "boolean";
        } else {
          param += "real";
        }
      }
    }

    String ret = "";
    String name = id + param;

    if (functions.containsKey(name)) {
      if (functions.get(name).containsKey(param)) {
        // Create new scope
        scopes.push(new Scope(scopes.peek()));

        Scope scope = scopes.peek();
        String s0 = functions.get(name).get(param).getKey();
        String[] paramName = s0.split(",");
        String s1 = "";
        if (ctx.paramCall() != null)
          s1 = this.visit(ctx.paramCall()).toString();
        String[] paramValue = s1.split(",");

        PascalParser.FunctionContext functionCtx = functions.get(name).get(param).getValue();

        // Add parameter values to the new scope
        if (functionCtx.param() != null)
          this.visit(functionCtx.param());
        for (int i = 0; i < paramName.length; i++) {
          if (paramName[i].isEmpty())
            continue;
          updateVar(paramName[i], paramValue[i]);
        }

        this.visit(functionCtx);

        if (scope.containsKey(id) && !scope.get(id)[1].isEmpty()) {
          ret = scope.get(id)[1];
        } else {
          System.out.println("Functinon has no return value");
        }

        // Function ends, destroy function scope
        scopes.pop();
      }
    } else if (procedures.containsKey(name)) {
      if (procedures.get(name).containsKey(param)) {
        // Create new scope
        scopes.push(new Scope(scopes.peek()));

        Scope scope = scopes.peek();
        String s0 = procedures.get(name).get(param).getKey();
        String[] paramName = s0.split(",");
        String s1 = "";
        if (ctx.paramCall() != null)
          s1 = this.visit(ctx.paramCall()).toString();
        String[] paramValue = s1.split(",");

        PascalParser.ProcedureContext procedureCtx = procedures.get(name).get(param).getValue();

        ArrayList<String> ref = new ArrayList<String>();
        // Add parameter values to the new scope
        if (procedureCtx.paramList() != null)
          this.visit(procedureCtx.paramList());
        for (int i = 0; i < paramName.length; i++) {
          if (paramName[i].isEmpty())
            continue;
          if (!scope.containsKey(paramName[i])) {
            paramName[i] = paramName[i].substring(3);
            ref.add(paramName[i]);
          }
          updateVar(paramName[i], paramValue[i]);
        }

        this.visit(procedureCtx);

        for (int i = 0; i < ref.size(); i++) {
          String value = scope.get(ref.get(i))[1];
          ref.set(i, value);
        }

        // Function ends, destroy function scope
        scopes.pop();

        if (ctx.paramCall() != null) {
          String[] callVars = ctx.paramCall().getText().split(",");
          for (int i = callVars.length - 1, j = ref.size() - 1; j >= 0; i--, j--) {
            Scope cur = scopes.peek();
            cur.get(callVars[i])[1] = ref.get(j);
          }
        }

      }
    } else {
      System.out.println("Function or procedure not declared");
    }

    return ret;
  }

  @Override
  public Object visitBody(PascalParser.BodyContext ctx) {
    this.visit(ctx.declarationBlock());
    this.visit(ctx.block());
    Scope scope = scopes.peek();
    return null;
  }

  @Override
  public Object visitParamCall(PascalParser.ParamCallContext ctx) {
    String v = "";
    for (PascalParser.ValueContext value : ctx.value()) {
      if (!v.isEmpty())
        v += ",";
      v += this.visit(value).toString();
    }
    return v;
  }

  @Override
  public Object visitProcedure(PascalParser.ProcedureContext ctx) {
    String name = ctx.ID().getText().toLowerCase();
    String param = "";
    String valName = "";

    if (ctx.paramList() != null) {
      String params = ctx.paramList().getText();
      String[] expr = params.split(";");
      for (String s : expr) {
        if (!param.isEmpty())
          param += ",";
        if (!valName.isEmpty())
          valName += ",";
        String[] valType = s.split(":");
        valName += valType[0];
        int num = valType[0].split(",").length;
        for (int j = 0; j < num; j++) {
          if (!param.isEmpty() && j != 0)
            param += ",";
          param += valType[1].toLowerCase();
        }
      }
    }

    name = name + param;

    if (!procedures.containsKey(name) || !procedures.get(name).containsKey(param)) {
      Pair<String, PascalParser.ProcedureContext> p = new Pair<>(valName, ctx);
      HashMap<String, Pair<String, PascalParser.ProcedureContext>> map = new HashMap<>();
      map.put(param, p);
      procedures.put(name, map);
    } else {
      // Execute function
      this.visit(ctx.body());
    }

    return null;
  }

}