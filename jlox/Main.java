import error_report.ConsoleError;
import error_report.IErrorReport;

import java.io.*;
import java.util.Scanner;

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

    public static void runFile(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("./" + filename));

        while(scanner.hasNext()){
            String line = scanner.next();
            System.out.println(line);
        }
    }

    public static void runPrompt() throws IOException {
        System.out.println("Enter a file name:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String file = br.readLine();
        runFile(file);
    }


}
