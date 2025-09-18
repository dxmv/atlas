package callable;

import ast.Environment;
import ast.FunctionStmt;
import ast.Interpreter;
import ast.Stmt;
import error.Return;

import java.util.List;

public class AtlasFunction implements AtlasCallable{
    private final FunctionStmt declaration;
    private final Environment closure;
    public AtlasFunction(FunctionStmt declaration,Environment closure){
        this.declaration = declaration;
        this.closure = closure;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment env = new Environment(this.closure);
        // bind the params and arguments
        for(int i = 0; i<arguments.size(); i++){
            Object arg = arguments.get(i);
            String name = declaration.params.get(i).getLiteral();
            env.put(name,arg);
        }
        // execute block
        try {
            interpreter.executeBlock(env, declaration.stmts);
        } catch (Return r){
            return r.getValue();
        }
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.getLiteral() + ">";
    }
}
