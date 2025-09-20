package callable;

import tokenizer.Token;

public class AtlasInstance {
    private Token className;
    public AtlasInstance(Token className){
        this.className = className;
    }

    @Override
    public String toString() {
        return "<instance of " + className.getLiteral() + ">";
    }
}
