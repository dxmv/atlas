import ast.*;
import error.ErrorReporter;
import error.RuntimeError;
import error.ScanError;
import parser.Parser;
import tokenizer.IScanner;
import tokenizer.Scanner;
import tokenizer.Token;
import tokenizer.TokenType;

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

            Parser parser = new Parser(tokens);
            List<Stmt> stmts = parser.parse();
            Interpreter interpreter = new Interpreter();

            // resolve first
            Resolver resolver = new Resolver(interpreter);
            resolver.resolve(stmts);

            interpreter.interpret(stmts);
        }
        catch (RuntimeError err){
            ErrorReporter.error(err.getToken().getLine(), err.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
