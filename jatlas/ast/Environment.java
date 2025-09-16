package ast;

import error.RuntimeError;

import java.util.HashMap;

public class Environment {
    private HashMap<String,Object> state;

    public Environment() {
        state = new HashMap<>();
    }

    public void put(String name,Object value) {
        state.put(name,value);
    }

    public Object get(String name) {
        if(!state.containsKey(name)){
            throw new RuntimeError(null,"");
        }
        return state.get(name);
    }

    public Object assign(String name,Object value) {
        if(!state.containsKey(name)){
            throw new RuntimeError(null,"Cannot assign to undefined value.");
        }
        state.put(name,value);
        return state.get(name);
    }
}
