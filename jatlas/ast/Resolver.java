package ast;

import callable.ClassType;
import callable.FunctionType;
import error.ErrorReporter;
import tokenizer.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Resolver implements Visitor<Object>{
    private Stack<Map<String,Boolean>> scope = new Stack<>();
    private final Interpreter interpreter;
    private FunctionType currentFunction = FunctionType.NONE;
    private ClassType currentClass = ClassType.NONE;

    public Resolver(Interpreter i){
        interpreter = i;
    }

    public void resolve(Stmt stmt){
        stmt.accept(this);
    }

    public void resolve(Expr expr){
        expr.accept(this);
    }

    public void resolve(List<Stmt> stmtList){
        for(Stmt stmt : stmtList){
            resolve(stmt);
        }
    }

    private void declareScope(){
        scope.push(new HashMap<>());
    }

    private void endScope(){
        scope.pop();
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitGroupingExpr(GroupingExpr expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Object visitLiteralExpr(LiteralExpr expr) {
        return null;
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr expr) {
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitVariableExpr(VariableExpr expr) {
        if(!scope.isEmpty() && scope.peek().get(expr.identifier.getLiteral()) == Boolean.FALSE ){
            ErrorReporter.error(expr.identifier,"Can't read local variable in its own initializer.");
        }
        resolveLocal(expr.identifier,expr);
        return null;
    }

    private void resolveLocal(Token identifier, Expr expr){
        for(int i = scope.size() - 1; i >= 0; i--){
            if(scope.get(i).containsKey(identifier.getLiteral())){
                interpreter.resolve(scope.size() - 1 - i,expr);
                return;
            }
        }
    }

    @Override
    public Object visitAssignExpr(AssignExpr expr) {
        resolve(expr.value);
        resolveLocal(expr.name,expr);
        return null;
    }

    @Override
    public Object visitPrintStmt(PrintStmt expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Object visitExpressionStmt(ExpressionStmt expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Object visitDeclareStmt(DeclareStmt expr) {
        declareVar(expr.name);
        if(expr.expression != null){
            resolve(expr.expression);
        }
        defineVar(expr.name);
        return null;
    }

    private void declareVar(Token name){
        if(scope.isEmpty()) return;
        Map<String, Boolean> scp = scope.peek();
        if(scp.containsKey(name.getLiteral())){
            ErrorReporter.error(name,"Already a variable with this name in this scope.");
        }
        scp.put(name.getLiteral(), false);
    }

    private void defineVar(Token name){
        if(scope.isEmpty()) return;
        Map<String, Boolean> scp = scope.peek();
        scp.put(name.getLiteral(), true);
    }

    @Override
    public Object visitBlockStmt(BlockStmt expr) {
        declareScope();
        resolve(expr.stmts);
        endScope();
        return null;
    }

    @Override
    public Object visitIfStmt(IfStmt expr) {
        resolve(expr.condition);
        resolve(expr.thenBranch);
        if(expr.elseBranch != null) resolve(expr.elseBranch);
        return null;
    }

    @Override
    public Object visitLogicalExpr(LogicalExpr expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt expr) {
        resolve(expr.condition);
        resolve(expr.body);
        return null;
    }

    @Override
    public Object visitCallExpr(CallExpr expr) {
        resolve(expr.callee);
        for(Expr e:expr.arguments){
            resolve(e);
        }
        return null;
    }

    @Override
    public Object visitFunctionStmt(FunctionStmt expr) {
        declareVar(expr.name);
        defineVar(expr.name);

        resolveFunction(expr,FunctionType.FUNCTION);
        return null;
    }

    private void resolveFunction(FunctionStmt stmt,FunctionType f){
        FunctionType prevType = currentFunction;
        currentFunction = f;
        declareScope();
        for (Token param : stmt.params) {
            declareVar(param);
            defineVar(param);
        }
        resolve(stmt.stmts);
        endScope();
        currentFunction = prevType;
    }

    @Override
    public Object visitRetrunStmt(RetrunStmt expr) {
        if(currentFunction == FunctionType.NONE){
            ErrorReporter.error(expr.keyword,"Can't return from top-level code.");
        }

        if (expr.value != null) {
            if(currentFunction == FunctionType.INITIALIZER){
                ErrorReporter.error(expr.keyword,"Can't return a value from an initializer. ");
            }
            resolve(expr.value);
        }
        return null;
    }

    @Override
    public Object visitClassStmt(ClassStmt expr) {
        ClassType prevType = currentClass;
        currentClass = ClassType.CLASS;
        declareVar(expr.name);
        declareScope();
        endScope();

        scope.peek().put("this",true);
        for(FunctionStmt stmt:expr.functions){
            FunctionType declaration = FunctionType.METHOD;
            if (stmt.name.getLiteral().equals("init")) {
                declaration = FunctionType.INITIALIZER;
            }
            resolveFunction(stmt,declaration);
        }
        defineVar(expr.name);
        currentClass = prevType;
        return null;
    }

    @Override
    public Object visitGetExpr(GetExpr expr) {
        resolve(expr.expr);
        return null;
    }

    @Override
    public Object visitSetExpr(SetExpr expr) {
        resolve(expr.object);
        resolve(expr.val);
        return null;
    }

    @Override
    public Object visitThisExpr(ThisExpr expr) {
        if(currentClass == ClassType.NONE){
            ErrorReporter.error(expr.keyword,"Can't use 'this' outside of a class.");
            return null;
        }
        resolveLocal(expr.keyword,expr);
        return null;
    }
}
