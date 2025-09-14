package error;

import tokenizer.Token;

public class ErrorReporter {
    private static boolean hadError = false;

    public static void error(int line, String message) {
        report(line, "", message);
    }

    public static void error(Token token, String message) {
        if (token.getType() == tokenizer.TokenType.EOF) {
            report(token.getLine(), " at end", message);
        } else {
            report(token.getLine(), " at '" + token.getLiteral() + "'", message);
        }
    }

    public static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }

    public static boolean hadError() {
        return hadError;
    }

    public static void reset() {
        hadError = false;
    }
}
