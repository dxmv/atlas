package tokenizer;

public class Token {
    private TokenType type;
    private int line;
    private Object value;
    private String literal;

    public Token(TokenType type, int line, String literal, Object value) {
        this.type = type;
        this.line = line;
        this.literal = literal;
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

    public String getLiteral() {
        return literal;
    }

    public Object getValue() {
        return value;
    }
}
