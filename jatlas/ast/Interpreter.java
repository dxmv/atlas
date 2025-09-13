package ast;

import tokenizer.Token;

import java.beans.Expression;

public class Interpreter implements Visitor<Object> {
    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        Token op = expr.operator;
        Object left = expr.left.accept(this);
        Object right = expr.right.accept(this);
        return switch (op.getType()){
            case MINUS -> (double) left - (double) right;
            case SLASH -> (double) left / (double) right;
            case STAR -> (double) left * (double) right;
            case GREATER -> (double) left > (double) right;
            case LESS -> (double) left < (double) right;
            case GREATER_EQUAL -> (double) left >= (double) right;
            case LESS_EQUAL -> (double) left <= (double) right;
            case PLUS -> handlePlus(left, right);
            case EQUAL_EQUAL -> isEqual(left,right);
            case BANG_EQUAL -> !isEqual(left,right);
            default -> null;
        };
    }


    @Override
    public Object visitGroupingExpr(GroupingExpr expr) {
        return expr.expression.accept(this);
    }

    @Override
    public Object visitLiteralExpr(LiteralExpr expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr expr) {
        Token op = expr.operator;
        Object res = expr.accept(this);
        return switch (op.getType()) {
            case MINUS -> (double) res * -1;
            case BANG -> !isTruthy(res);
            default -> null;
        };
    }

    /**
     * Only false & null values are false, everything else is true
     * @param res
     * @return
     */
    public boolean isTruthy(Object res) {
        if (res == null) return false;
        if (res instanceof Boolean) return (boolean) res;
        return true;
    }


    /**
     * Handles the plus operation for 2 values
     * @param left
     * @param right
     * @return
     */
    public Object handlePlus(Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return  (double)left + (double)right;
        }
        if(left instanceof String && right instanceof String) {
            return left.toString() + right.toString();
        }
        if(left instanceof Double && right instanceof String || left instanceof String && right instanceof Double) {
            return left.toString() + right.toString();
        }
        return null;
    }

    /**
     * Check if 2 objects are equal
     * @param left
     * @param right
     * @return
     */
    public boolean isEqual(Object left, Object right) {
        if(left == null && right == null) return true;
        if(left == null || right == null) return false;
        return left.equals(right);
    }

    public void interpret(Expr expression){
        Object result = expression.accept(this);
        System.out.printf(result.toString());
    }
}
