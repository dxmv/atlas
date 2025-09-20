package ast;

import callable.AtlasCallable;
import callable.AtlasClass;
import callable.AtlasFunction;
import error.ErrorReporter;
import error.Return;
import error.RuntimeError;
import tokenizer.Token;
import tokenizer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter implements Visitor<Object> {
    public Environment globals = new Environment();
    private Environment environment = globals;
    private Map<Expr,Integer> locals = new HashMap<>();

    public Interpreter() {
        // define clock function
        globals.put("clock",new AtlasCallable(){
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }

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

    @Override
    public Object visitVariableExpr(VariableExpr expr) {
        return lookUpVariable(expr);
    }

    private Object lookUpVariable(VariableExpr expr) {
        Integer distance = locals.get(expr);
        if (distance == null) {
            return globals.get(expr.identifier);
        }
        return environment.getAt(distance,expr.identifier);
    }

    @Override
    public Object visitAssignExpr(AssignExpr expr) {
        Object res = expr.value.accept(this);
        Integer dist = locals.get(expr);
        if (dist == null) {
            globals.assign(expr.name,res);
        }
        else{
            environment.assignAt(dist,expr.name,res);
        }
        return res;
    }

    @Override
    public Object visitPrintStmt(PrintStmt expr) {
        Object value = expr.expression.accept(this);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Object visitExpressionStmt(ExpressionStmt expr) {
        return expr.expression.accept(this);
    }

    @Override
    public Object visitDeclareStmt(DeclareStmt expr) {
        Object value = null;
        if (expr.expression != null) {
            value = expr.expression.accept(this);
        }
        environment.put(expr.name.getLiteral(), value);
        return null;
    }

    @Override
    public Object visitBlockStmt(BlockStmt expr) {
        executeBlock(new Environment(environment), expr.stmts);
        return null;
    }

    public void executeBlock(Environment env,List<Stmt> stmts) {
        Environment previous = this.environment;
        try {
            this.environment = env;
            for (Stmt s : stmts) {
                s.accept(this);
            }
        } finally {
            this.environment = previous;
        }
    }

    @Override
    public Object visitIfStmt(IfStmt expr) {
        // process the condition
        boolean res = isTruthy(expr.condition.accept(this));
        if(res){
            return expr.thenBranch.accept(this);
        }
        else{
            if(expr.elseBranch != null){
                return expr.elseBranch.accept(this);
            }
        }
        return null;
    }

    @Override
    public Object visitLogicalExpr(LogicalExpr expr) {
        Object left = expr.left.accept(this);
        boolean leftBool = isTruthy(left);
        if(expr.operator.getType() == TokenType.AND){
            if(!leftBool){
                return left;
            }
        }
        else{
            if(leftBool){
                return left;
            }
        }
        return expr.right.accept(this);
    }

    @Override
    public Object visitWhileStmt(WhileStmt expr) {
        while(isTruthy(expr.condition.accept(this))){
            expr.body.accept(this);
        }
        return null;
    }

    @Override
    public Object visitCallExpr(CallExpr expr) {
        Object callee = expr.callee.accept(this);

        List<Object> args = new ArrayList<>();
        for(Expr arg : expr.arguments){
            args.add(arg.accept(this));
        }
        if (!(callee instanceof AtlasCallable function)) {
            throw new RuntimeError(expr.paren,
                    "Can only call functions and classes.");
        }
        if(args.size() != function.arity()){
            throw new RuntimeError(expr.paren, "Expected " +
                    function.arity() + " arguments but got " +
                    args.size() + ".");
        }
        return function.call(this,args);
    }

    @Override
    public Object visitFunctionStmt(FunctionStmt expr) {
        Environment previous = this.environment;
        AtlasCallable func = new AtlasFunction(expr,previous);
        environment.put(expr.name.getLiteral(), func);
        return null;
    }

    @Override
    public Object visitRetrunStmt(RetrunStmt expr) {
        Object val = null;
        if(expr.value != null) val = expr.value.accept(this);
        throw new Return(val);
    }

    @Override
    public Object visitClassStmt(ClassStmt expr) {
        environment.put(expr.name.getLiteral(),null);
        AtlasClass klass = new AtlasClass(expr.name);
        environment.put(expr.name.getLiteral(),klass);
        return null;
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

    public void interpret(List<Stmt> stmts) throws RuntimeError {
        for (Stmt stmt : stmts) {
            stmt.accept(this);
        }
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

    public void resolve(int depth, Expr expr) {
        locals.put(expr,depth);
    }
}
