package callable;

import tokenizer.Token;

public class AtlasClass {
    private Token name;
    public AtlasClass(Token name){
        this.name = name;
    }

    @Override
    public String toString(){
        return "<class " +name.getLiteral() +">";
    }
}
