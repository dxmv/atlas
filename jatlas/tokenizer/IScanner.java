package tokenizer;

import error.ScanError;

import java.util.List;

public interface IScanner {
    public List<Token> tokenize();
    public List<ScanError> getErrors();
}
