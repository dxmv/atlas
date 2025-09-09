import error.ScanError;
import tokenizer.IScanner;
import tokenizer.Scanner;
import tokenizer.Token;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length > 1){
            System.out.printf("Error: Usage atlas <filename>");
            return;
        }
        try {
            String source = Files.readString(Paths.get("./test.txt"));
            IScanner scanner = new Scanner(source);
            List<Token> tokens = scanner.tokenize();
            // print out errors
            if(!scanner.getErrors().isEmpty()){
                for(ScanError error : scanner.getErrors()){
                    System.out.println(error);
                }
                return;
            }
            for (Token token : tokens) {
                System.out.println(token);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
