package tokenizer;

import error.ScanError;
import error.ScanErrorTypes;

import java.util.ArrayList;
import java.util.List;

public class Scanner implements IScanner{
    private String source;
    private int current; // current position in doc
    private int start; // current position in doc
    private int line; // current line number
    List<Token> tokens = new ArrayList<>();
    private List<ScanError> errors = new ArrayList<>();

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
            case '.':
                addToken(TokenType.DOT,line,".");
                break;
            case ',':
                addToken(TokenType.COMMA,line,",");
                break;
            case ';':
                addToken(TokenType.SEMICOLON,line,";");
                break;
            // handle double chars
            case '/':
                // handle single-line comments
                if(!isAtEnd() && matches(peek(),'/')){
                    advance();
                    while(!isAtEnd()){
                        if(matches(peek(),'\n')){
                            break;
                        }
                        advance();
                    }
                }
                // handle multiline comments
                else if(!isAtEnd() && matches(peek(),'*')){
                    advance();
                    while(!isAtEnd()){
                        if(matches(peek(),'*')){
                            advance();
                            if(!isAtEnd() && matches(peek(),'/')){
                                advance();
                                break;
                            }
                        }
                        if(matches(peek(),'\n')){
                            line++;
                        }
                        advance();
                    }
                } else {
                    addToken(TokenType.SLASH, line, "/");
                }
                break;
            case '=':
                if(matches(peek(),'=')){
                    addToken(TokenType.EQUAL_EQUAL,line,"==");
                    advance();
                }
                else {
                    addToken(TokenType.EQUAL, line, "=");
                }
                break;
            case '!':
                if(matches(peek(),'=')){
                    addToken(TokenType.BANG_EQUAL,line,"!=");
                    advance();
                } else {
                    addToken(TokenType.BANG, line, "!");
                }
                break;
            case '>':
                if(matches(peek(),'=')){
                    addToken(TokenType.GREATER_EQUAL,line,">=");
                    advance();
                } else{
                    addToken(TokenType.GREATER,line,">");
                }
                break;
            case '<':
                if(matches(peek(),'=')){
                    addToken(TokenType.LESS_EQUAL,line,"<=");
                    advance();
                } else {
                    addToken(TokenType.LESS, line, "<");
                }
                break;
            // handle line breaks
            case '\n':
                line++;
                break;
            case ' ': break;
            case '\t': break;
            case '\r': break;
            // handle string
            case '\"':
                strings();
                break;
            default:
                // handle numbers
                if(ScannerUtils.isDigit(c)){
                    numbers();
                }
                // handle identifiers and keywords
                else if(ScannerUtils.isAlpha(c)){
                    identifiers();
                }
                // unknown token
                else{
                    errors.add(new ScanError(ScanErrorTypes.UNEXPECTED_CHAR,line,"Unknown character " + c));
                }
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

    /**
     * Check if the chars c1 and c2 are the same
     * @param c1
     * @param c2
     * @return bool
     */
    private boolean matches(char c1, char c2){
        return c1 == c2;
    }

    /**
     * Returns current char, without moving pointer
     */
    private char peek(){
        return source.charAt(current);
    }

    /**
     * ONLY returns the next char if possible
     */
    private char peekNext(){
        if (current + 1 >= source.length()){
            return '\0';
        }
        return source.charAt(current);
    }

    /**
     * Handles strings, makes a string until it encounters the next " character
     */
    private void strings(){
        StringBuilder sb = new StringBuilder();
        sb.append('\"'); // append the first "
        while(!isAtEnd()){
            if(matches(peek(),'\n')){
                errors.add(new ScanError(ScanErrorTypes.UNTERMINATED_STRING,line,"You haven't terminated the string"));
                return;
            }
            if(matches(peek(),'\"')){
                sb.append(peek());
                advance();
                break;
            }
            sb.append(peek());
            advance();
        }
        addToken(TokenType.STRING,line, sb.toString());
    }

    /**
     * Handles numbers, makes a number until it encounters the new line
     */
    private void numbers(){
        StringBuilder sb = new StringBuilder();
        sb.append(source.charAt(start));
        int numDots = 1; // we can only have 1 dot in number
        while(!isAtEnd()){
            if(matches(peek(),'\n')){
                break;
            }
            if(matches(peek(),'.')){
                // TODO: throw error here
                if(numDots <= 0){
                    return;
                }
                numDots--;
            }
            sb.append(peek());
            advance();
        }
        addToken(TokenType.NUMBER,line, sb.toString());
    }

    public void identifiers(){
        StringBuilder sb = new StringBuilder();
        sb.append(source.charAt(start));
        while(!isAtEnd() && ScannerUtils.isAlphaNum(peek())){
            sb.append(peek());
            advance();
        }
        TokenType kw = ScannerUtils.getTokenType(sb.toString());
        addToken(kw != null ? kw : TokenType.IDENTIFIER,line, sb.toString());
    }

    public List<ScanError> getErrors() {
        return errors;
    }
}
