package ast;

public class RpnPrinter implements Visitor<String>{
    @Override
    public String visitBinaryExpr(BinaryExpr expr) {
        return expr.left.accept(this) + " " +
                expr.right.accept(this) + " " + expr.operator.getValue();
    }

    @Override
    public String visitGroupingExpr(GroupingExpr expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(LiteralExpr expr) {
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(UnaryExpr expr) {
        return expr.right.accept(this) + " " + expr.operator.getValue();
    }

    @Override
    public String visitPrintStmt(PrintStmt expr) {
        return "";
    }

    @Override
    public String visitExpressionStmt(ExpressionStmt expr) {
        return "";
    }

    public String print(Expr expression) {
        return expression.accept(this);
    }
}
