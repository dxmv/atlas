package ast;

public class AstPrinter implements Visitor<String> {
    @Override
    public String visitBinaryExpr(BinaryExpr expr) {
        return parenthesize(expr.operator.toString(),expr.left,expr.right);
    }

    @Override
    public String visitGroupingExpr(GroupingExpr expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(LiteralExpr expr) {
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(UnaryExpr expr) {
        return parenthesize(expr.operator.toString(), expr.right);
    }

    public String print(Expr expression) {
        return expression.accept(this);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder(100);

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
