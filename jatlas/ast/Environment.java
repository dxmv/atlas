package ast;

import error.RuntimeError;

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

    public Object get(String name) {
        if(!state.containsKey(name)){
            if(parent != null){
                return parent.get(name);
            }
            throw new RuntimeError(null,"The item doesn't exist.");
        }
        return state.get(name);
    }

    public Object assign(String name,Object value) {
        if(!state.containsKey(name)){
            if(parent != null){
                return parent.assign(name,value);
            }
            throw new RuntimeError(null,"Cannot assign to undefined value.");
        }
        state.put(name,value);
        return state.get(name);
    }
}
