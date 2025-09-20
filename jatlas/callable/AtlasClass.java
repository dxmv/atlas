package callable;

import ast.Interpreter;
import tokenizer.Token;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtlasClass implements AtlasCallable{
    private Token name;
    private Map<String,AtlasCallable> methods;
    public AtlasClass(Token name, Map<String,AtlasCallable> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString(){
        return "<class " +name.getLiteral() +">";
    }

    @Override
    public int arity() {
        AtlasFunction func = getMethod("init");
        return func == null ? 0 : func.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        AtlasInstance instance = new AtlasInstance(this);
        AtlasFunction init = getMethod("init");
        if(init != null){
            init.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    public AtlasFunction getMethod(String name){
        if(methods.containsKey(name)){
            return (AtlasFunction) methods.get(name);
        }
        return null;
    }

    public Token getName() {
        return name;
    }

    public Map<String, AtlasCallable> getMethods() {
        return methods;
    }
}
