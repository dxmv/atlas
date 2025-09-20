package callable;

import error.RuntimeError;
import tokenizer.Token;

import java.util.HashMap;
import java.util.Map;

public class AtlasInstance {
    private Token className;
    private Map<String,Object> fields = new HashMap<>();
    public AtlasInstance(Token className){
        this.className = className;
    }

    @Override
    public String toString() {
        return "<instance of " + className.getLiteral() + ">";
    }

    public Object get(Token name) {
        String nameStr = name.getLiteral();
        if(!fields.containsKey(nameStr)){
            throw new RuntimeError(name,"Undefined property '" + name.getLiteral() + "'.");
        }
        return fields.get(nameStr);
    }

    public void set(Token name, Object val) {
        String nameStr = name.getLiteral();
        fields.put(nameStr,val);
    }
}
