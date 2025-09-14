package ast;

import error.RuntimeError;
import tokenizer.Token;

public class Interpreter implements Visitor<Object> {
    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        Token op = expr.operator;
        Object left = expr.left.accept(this);
        Object right = expr.right.accept(this);

        switch (op.getType()){
            case MINUS:
                checkNumberOperands(op, left, right);
                return (double) left - (double) right;
            case SLASH:
                checkNumberOperands(op, left, right);
                return handleDivision(op, (double) left, (double) right);
            case STAR:
                checkNumberOperands(op, left, right);
                return (double) left * (double) right;
            case GREATER:
                checkNumberOperands(op, left, right);
                return (double) left > (double) right;
            case LESS:
                checkNumberOperands(op, left, right);
                return (double) left < (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(op, left, right);
                return (double) left >= (double) right;
            case LESS_EQUAL:
                checkNumberOperands(op, left, right);
                return (double) left <= (double) right;
            case PLUS:
                return handlePlus(op, left, right);
            case EQUAL_EQUAL:
                return isEqual(left,right);
            case BANG_EQUAL:
                return !isEqual(left,right);
            default:
                // Unreachable.
                return null;
        }
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
        Object right = expr.right.accept(this);

        return switch (op.getType()) {
            case MINUS -> {
                checkNumberOperand(op, right);
                yield -(double) right;
            }
            case BANG -> !isTruthy(right);
            default ->
                // Unreachable.
                    null;
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
    public Object handlePlus(Token op, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return  (double)left + (double)right;
        }
        if(left instanceof String && right instanceof String) {
            return left + (String)right;
        }
        if(left instanceof Double && right instanceof String || left instanceof String && right instanceof Double) {
            return stringify(left) + stringify(right);
        }
        throw new RuntimeError(op, "Operands must be two numbers or two strings.");
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

    public double handleDivision(Token op, double left, double right) {
        if(right == 0.0){
            throw new RuntimeError(op, "Division by zero.");
        }
        return left / right;
    }

    public void interpret(Expr expression) throws RuntimeError {
        Object result = expression.accept(this);
        System.out.println(stringify(result));
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    /**
     * Used to turn object into string in interpreter
     * @param res
     * @return
     */
    private String stringify(Object res){
        if(res == null) return "nil";
        // check if a decimal result is a whole number
        if((res instanceof Double r) && r%1==0){
            return (int)Math.floor(r) + "";
        }
        return res.toString();
    }
}
