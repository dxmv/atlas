package callable;

import ast.Environment;
import ast.FunctionStmt;
import ast.Interpreter;
import ast.Stmt;
import error.Return;
import tokenizer.Token;
import tokenizer.TokenType;

import java.util.List;

public class AtlasFunction implements AtlasCallable{
    private final FunctionStmt declaration;
    private final Environment closure;
    private final boolean isInit;
    public AtlasFunction(FunctionStmt declaration,Environment closure){
        this.declaration = declaration;
        this.closure = closure;
        this.isInit = false;
    }

    public AtlasFunction(FunctionStmt declaration,Environment closure, boolean isInit){
        this.declaration = declaration;
        this.closure = closure;
        this.isInit = isInit;
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
            if(isInit){
                return closure.get(new Token(TokenType.THIS,1,"this","this"));
            }
            return r.getValue();
        }
        if(isInit){
            return closure.get(new Token(TokenType.THIS,1,"this","this"));
        }
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.getLiteral() + ">";
    }

    public AtlasFunction bind(AtlasInstance atlasInstance) {
        Environment environment = new Environment(closure);
        environment.put("this", atlasInstance);
        return new AtlasFunction(declaration, environment,isInit);
    }
}
