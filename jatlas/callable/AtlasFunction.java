package callable;

import ast.Environment;
import ast.FunctionStmt;
import ast.Interpreter;
import ast.Stmt;

import java.util.List;

public class AtlasFunction implements AtlasCallable{
    private final FunctionStmt declaration;
    public AtlasFunction(FunctionStmt declaration){
        this.declaration = declaration;
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment env = new Environment(interpreter.globals);
        // bind the params and arguments
        for(int i = 0; i<arguments.size(); i++){
            Object arg = arguments.get(i);
            String name = declaration.params.get(i).getLiteral();
            env.put(name,arg);
        }
        // execute block
        interpreter.executeBlock(env,declaration.stmts);
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.getLiteral() + ">";
    }
}
