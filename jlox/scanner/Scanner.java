package scanner;

import token.Token;
import token.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Scanner implements IScanner {
    private String source;
    private int start = 0;
    private int current = 0;
    private List<Token> tokens;
    private int line = 1;

    public Scanner(String source){
        this.source = source;
        this.tokens = new ArrayList<Token>();
    }


    @Override
    public List<Token> listTokens() throws Exception {
        // delete previous tokens
        this.tokens.clear();
        while(!isAtEnd()){
            start = current;
            scanToken();
        }
        addToken(TokenType.EOF,"");
        return tokens;
    }

    /**
     *
     */
    private void scanToken() throws Exception{
        // based on first character create token
        char c = advance();
        switch(c){
            // handle single chars
            case '(':
                addToken(TokenType.LEFT_PAREN,"(");
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN,")");
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE,"{");
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE,"}");
                break;
            case '+':
                addToken(TokenType.PLUS,"+");
                break;
            case '-':
                addToken(TokenType.MINUS,"-");
                break;
            case '.':
                addToken(TokenType.COMMA,".");
                break;
            case '*':
                addToken(TokenType.STAR,"*");
                break;
            case ';':
                addToken(TokenType.SEMICOLON,";");
                break;
            case ',':
                addToken(TokenType.COMMA,",");
                break;
            // special case cause of comments
            case '/':
                if(matches(peek(), '/')){
                    // skip all chars until new line
                    while(advance() != '\n'){}
                    break;
                }
                addToken(TokenType.SLASH,"/");
                break;
            // 1 or 2 chars
            case '=':
                if(matches(peek(), '=')){
                    addToken(TokenType.EQUAL_EQUAL,"==");
                    current++;
                }
                else{
                    addToken(TokenType.EQUAL,"=");
                }
                break;
            case '!':
                if(matches(peek(), '=')){
                    addToken(TokenType.BANG_EQUAL,"!=");
                    current++;

                }
                else{
                    addToken(TokenType.BANG,"=");
                }
                break;
            case '>':
                if(matches(peek(), '=')){
                    addToken(TokenType.GREATER_EQUAL,">=");
                    current++;
                }
                else{
                    addToken(TokenType.GREATER,">");
                }
                break;
            case '<':
                if(matches(peek(), '=')){
                    addToken(TokenType.LESS_EQUAL,"<=");
                    current++;
                }
                else{
                    addToken(TokenType.LESS,"<");
                }
                break;
            // skip unecessary characters
            case ' ':
                break;
            case '\n':
                line++;
                break;
            case '\t':
                break;
            case '\r':
               break;
            default:
                // handle string literals
                if(c=='"') {
                    strings();
                }
                // handle numbers
                if(Character.isDigit(c)) {
                    numbers();
                }
                // handle keywords
                break;
        }
    }

    private void strings(){
        StringBuilder sb = new StringBuilder();
        while (!isAtEnd() && !matches(peek(), '"')){
            if(peek() == '\n'){
                sb.append(' ');
                line++;
            }
            else{
                sb.append(peek());
            }
            advance();
        }
        addToken(TokenType.STRING,sb.toString());
        advance();
    }

    private void numbers() throws Exception {
        StringBuilder sb = new StringBuilder();
        boolean hasDot = false;
        // add the first digit
        sb.append(source.charAt(start));
        while (!isAtEnd() && !matches(peek(),'\n') && (Character.isDigit(peek()) || matches(peek(),'.'))){
            if(matches(peek(),'.')){
                if(hasDot){
                    throw new Exception("Number exception");
                }
                hasDot = true;
            }
            sb.append(peek());
            advance();
        }
        addToken(TokenType.NUMBER,sb.toString());
    }

    private boolean isAtEnd(){
        return current >= source.length();
    }

    private void addToken(TokenType type, Object value){
        Token token = new Token(type,value,line);
        this.tokens.add(token);
    }

    private char advance(){
        return source.charAt(current++);
    }

    private boolean matches(char c1, char c2){
        return c1 == c2;
    }

    private char peek(){
        if(isAtEnd()){
            return '\0';
        }
        return source.charAt(current);
    }
}
