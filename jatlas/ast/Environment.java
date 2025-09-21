package ast;

import error.RuntimeError;
import tokenizer.Token;

import java.util.HashMap;

public class Environment {
    private HashMap<String,Object> state;
    public Environment parent;

    public Environment() {
        this.state = new HashMap<>();
    }

    public Environment(Environment parent) {
        this.parent = parent;
        this.state = new HashMap<>();
    }

    public void put(String name,Object value) {
        state.put(name,value);
    }

    public Object get(Token name) {
        if(state.containsKey(name.getLiteral())) {
            return state.get(name.getLiteral());
        }
        if(parent != null) return parent.get(name);
        throw new RuntimeError(name,"The item doesn't exist.");
    }

    public Object get(String name) {
        if(state.containsKey(name)) {
            return state.get(name);
        }
        if(parent != null) return parent.get(name);
        // This should not be reachable in user code because the resolver guarantees it.
        throw new RuntimeException("Undefined variable '" + name + "'. This is an interpreter error.");
    }

    public Object assign(Token name,Object value) {
        if(state.containsKey(name.getLiteral())){
            throw new RuntimeError(name,"Cannot assign to undefined value.");
        }
        state.put(name.getLiteral(),value);
        return state.get(name.getLiteral());
    }

    public Object getAt(int dist,Token name){
        return ancestor(dist).get(name);
    }

    public Object getAt(int dist, String name) {
        return ancestor(dist).get(name);
    }

    private Environment ancestor(int dist){
        Environment env = this;
        for(int i=0;i<dist;i++){
            env = env.parent;
        }
        return env;
    }

    public Object assignAt(int dist,Token name,Object value) {
        return ancestor(dist).assign(name,value);
    }
}
