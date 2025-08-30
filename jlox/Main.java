import error_report.ConsoleError;
import error_report.IErrorReport;
import scanner.IScanner;
import scanner.Scanner;
import token.Token;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        IErrorReport er = new ConsoleError();
        if(args.length >= 2){
            System.out.println("Usage: jlox [filename]");
            return;
        }
        try{
            if(args.length == 1){
                runFile(args[0]);
            } else {
                runPrompt();
            }
        }
        catch (IOException e){
            er.Report(0,e.getMessage());
        }
        catch(Exception e){
            er.Report(0,"Unknown error: " + e.getMessage());
        }
    }

    /**
     * Tokenizes the file and prints the tokens
     * @param filename - file we want to read
     * @throws FileNotFoundException
     */
    public static void runFile(String filename) throws Exception {
        String source = Files.readString(Paths.get(filename));

        IScanner scanner = new Scanner(source);
        List<Token> tokens = scanner.listTokens();
        for(Token token : tokens){
            System.out.println(token.toString());
        }
    }

    /**
     * Prompts user to enter the file name and runs the runFile method
     * @throws IOException
     */
    public static void runPrompt() throws Exception {
        System.out.println("Enter a file name:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String file = br.readLine();
        runFile(file);
    }


}
