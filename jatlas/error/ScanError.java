package error;

public class ScanError {
    private ScanErrorTypes type;
    private int line;
    private String message;

    public ScanError(ScanErrorTypes type, int line, String message) {
        this.type = type;
        this.line = line;
        this.message = message;
    }

    @Override
    public String toString(){
        return line + ": <" + type + "> :" + message;
    }
}
