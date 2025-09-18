package ast;

import error.RuntimeError;
import tokenizer.Token;

import java.util.HashMap;

public class Environment {
    private HashMap<String,Object> state;
    private Environment parent;

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
        if(!state.containsKey(name.getLiteral())){
            if(parent != null){
                return parent.get(name);
            }
            throw new RuntimeError(name,"The item doesn't exist.");
        }
        return state.get(name.getLiteral());
    }

    public Object assign(Token name,Object value) {
        if(!state.containsKey(name.getLiteral())){
            if(parent != null){
                return parent.assign(name,value);
            }
            throw new RuntimeError(name,"Cannot assign to undefined value.");
        }
        state.put(name.getLiteral(),value);
        return state.get(name.getLiteral());
    }
}
