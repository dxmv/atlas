package error_report;

public class ConsoleError implements IErrorReport{
    @Override
    public void Report(int line,String message) {
        System.out.println("[line " + line + "] Error: " + message);
    }
}
