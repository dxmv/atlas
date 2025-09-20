package callable;

import ast.Interpreter;
import tokenizer.Token;

import java.util.List;

public class AtlasClass implements AtlasCallable{
    private Token name;
    public AtlasClass(Token name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "<class " +name.getLiteral() +">";
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        AtlasInstance instance = new AtlasInstance(this.name);
        return instance;
    }
}
