package callable;

import error.RuntimeError;
import tokenizer.Token;

import java.util.HashMap;
import java.util.Map;

public class AtlasInstance {
    private AtlasClass klass;
    private Map<String,Object> fields = new HashMap<>();
    public AtlasInstance(AtlasClass klass){
        this.klass = klass;
    }

    @Override
    public String toString() {
        return "<instance of " + klass.getName().getLiteral() + ">";
    }

    public Object get(Token name) {
        String nameStr = name.getLiteral();
        if(fields.containsKey(nameStr)){
            return fields.get(nameStr);
        }
        AtlasFunction func = klass.getMethod(nameStr);
        if(func != null){
            return func;
        }
        throw new RuntimeError(name,"Undefined property '" + name.getLiteral() + "'.");
    }

    public void set(Token name, Object val) {
        String nameStr = name.getLiteral();
        fields.put(nameStr,val);
    }
}
