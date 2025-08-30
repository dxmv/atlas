package token;

public class Token {
    private Object value;
    private TokenType type;
    private int line;
    public Token(TokenType type, Object value,int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public String toString() {
        return "<"+type+"> "+value + " line: " + line;
    }
}
