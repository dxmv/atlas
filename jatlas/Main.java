import tokenizer.IScanner;
import tokenizer.Scanner;
import tokenizer.Token;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        if(args.length > 1){
            System.out.printf("Error: Usage atlas <filename>");
            return;
        }

        IScanner scanner = new Scanner("* / + -");
        List<Token> tokens = scanner.tokenize();
        for(Token token : tokens){
            System.out.println(token);
        }
    }

}
