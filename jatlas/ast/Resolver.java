package ast;

import tokenizer.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Resolver implements Visitor<Object>{
    private Stack<Map<String,Boolean>> scope = new Stack<>();
    private final Interpreter interpreter;

    public Resolver(Interpreter i){
        interpreter = i;
    }

    private void resolve(Stmt stmt){
        stmt.accept(this);
    }

    private void resolve(Expr expr){
        expr.accept(this);
    }

    private void resolve(List<Stmt> stmtList){
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
        return null;
    }

    @Override
    public Object visitGroupingExpr(GroupingExpr expr) {
        return null;
    }

    @Override
    public Object visitLiteralExpr(LiteralExpr expr) {
        return null;
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr expr) {
        return null;
    }

    @Override
    public Object visitVariableExpr(VariableExpr expr) {
        return null;
    }

    @Override
    public Object visitAssignExpr(AssignExpr expr) {
        return null;
    }

    @Override
    public Object visitPrintStmt(PrintStmt expr) {
        return null;
    }

    @Override
    public Object visitExpressionStmt(ExpressionStmt expr) {
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
        return null;
    }

    @Override
    public Object visitLogicalExpr(LogicalExpr expr) {
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt expr) {
        return null;
    }

    @Override
    public Object visitCallExpr(CallExpr expr) {
        return null;
    }

    @Override
    public Object visitFunctionStmt(FunctionStmt expr) {
        return null;
    }

    @Override
    public Object visitRetrunStmt(RetrunStmt expr) {
        return null;
    }
}
