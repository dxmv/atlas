package scanner;

import token.Token;

import java.util.List;


public interface IScanner {
    public List<Token> listTokens() throws Exception;
}
