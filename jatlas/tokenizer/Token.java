package tokenizer;

public class Token {
    private TokenType type;
    private int line;
    private Object value;

    public Token(TokenType type, int line, Object value) {
        this.type = type;
        this.line = line;
        this.value = value;
    }

    @Override
    public String toString() {
        return "<" + type.toString() + "> " + value + " " + line;
    }

    public TokenType getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public Object getValue() {
        return value;
    }
}
