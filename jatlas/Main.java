import ast.*;
import error.ScanError;
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
//    public static void main(String[] args) throws IOException {
//        if(args.length > 1){
//            System.out.printf("Error: Usage atlas <filename>");
//            return;
//        }
//        try {
//            String source = Files.readString(Paths.get("./test.txt"));
//            IScanner scanner = new Scanner(source);
//            List<Token> tokens = scanner.tokenize();
//            // print out errors
//            if(!scanner.getErrors().isEmpty()){
//                for(ScanError error : scanner.getErrors()){
//                    System.out.println(error);
//                }
//                return;
//            }
//            for (Token token : tokens) {
//                System.out.println(token);
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
        Expr expr = new BinaryExpr(
            new BinaryExpr(
                new LiteralExpr(1),
                new Token(TokenType.PLUS,1,"+"),
                new LiteralExpr(1)
            ),new Token(TokenType.STAR,1,"*"),
                new BinaryExpr(
                        new LiteralExpr(1),
                        new Token(TokenType.STAR,1,"*"),
                        new LiteralExpr(1)
            ));
        Expr expr2 = new BinaryExpr(
                new UnaryExpr(
                        new Token(TokenType.MINUS, 1, "-"),
                        new LiteralExpr(123)),
                new Token(TokenType.STAR, 1,"*"),
                new GroupingExpr(
                        new LiteralExpr(45.67)));
        System.out.println(new RpnPrinter().print(expr));
        System.out.println(new RpnPrinter().print(expr2));
    }

}
