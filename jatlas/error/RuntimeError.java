package error;

import tokenizer.Token;

public class RuntimeError extends java.lang.RuntimeException {
    final Token token;

    public RuntimeError(Token token, String message) {
        super(message);
        this.token = token;
    }

    public Token getToken() {
        return token;
    }
}
