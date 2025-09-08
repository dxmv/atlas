package tokenizer;

import java.util.ArrayList;
import java.util.List;

public class Scanner implements IScanner{
    private String source;
    private int current; // current position in doc
    private int start; // current position in doc
    private int line; // current line number
    List<Token> tokens = new ArrayList<>();

    public Scanner(String source){
        this.source = source;
        this.current = 0;
        this.start = 0;
        this.line = 1;
        this.tokens = new ArrayList<>();
    }

    @Override
    public List<Token> tokenize() {
        this.tokens.clear();
        while (!isAtEnd()){
            start = current;
            scanToken();
        }
        addToken(TokenType.EOF,line,"");
        return tokens;
    }

    /**
     *
     */
    private void scanToken(){
        char c = advance();
        switch (c){
            // single chars
            case '(':
                addToken(TokenType.LEFT_PAREN,line,"(");
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN,line,")");
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE,line,"{");
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE,line,"}");
                break;
            case '+':
                addToken(TokenType.PLUS,line,"+");
                break;
            case '-':
                addToken(TokenType.MINUS,line,"-");
                break;
            case '*':
                addToken(TokenType.STAR,line,"*");
                break;
            case '/':
                addToken(TokenType.SLASH,line,"/");
                break;
            case ',':
                addToken(TokenType.COMMA,line,",");
                break;
            case ';':
                addToken(TokenType.SEMICOLON,line,";");
                break;
        }
    }

    /**
     * Gives us the current and moves the current
     * @return the current character
     */
    private char advance(){
        return source.charAt(current++);
    }

    /**
     * Are we at the end of document?
     * @return bool whether we're at the end
     */
    private boolean isAtEnd(){
        return current >= source.length();
    }

    /**
     * Creats new token and adds it to the list
     * @param type
     * @param line
     * @param value
     */
    private void addToken(TokenType type, int line, Object value){
            this.tokens.add(new Token(type, line, value));
    }
}
