package tokenizer;

import java.util.HashMap;

public  class ScannerUtils {
    private static HashMap<String, TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("and", TokenType.AND);
        KEYWORDS.put("or", TokenType.OR);
        KEYWORDS.put("class", TokenType.CLASS);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("false", TokenType.FALSE);
        KEYWORDS.put("true", TokenType.TRUE);
        KEYWORDS.put("fun", TokenType.FUNC);
        KEYWORDS.put("var", TokenType.VAR);
        KEYWORDS.put("nil", TokenType.NIL);
        KEYWORDS.put("this", TokenType.THIS);
        KEYWORDS.put("super", TokenType.SUPER);
        KEYWORDS.put("print",TokenType.PRINT);
    }

    public static boolean isDigit(char c){
        return c=='0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' ||
                c=='6' || c=='7' || c=='8' || c=='9';
    }

    public static boolean isAlpha(char c){
        return (c>='a' && c<='z') || (c>='A' && c<='Z');
    }

    public static boolean isAlphaNum(char c){
        return isAlpha(c) || isDigit(c);
    }


    public static TokenType getTokenType(String token){
        return KEYWORDS.get(token);
    }
}
