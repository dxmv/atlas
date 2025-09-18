package callable;

import ast.Interpreter;

import java.util.List;

public interface AtlasCallable {
    /**
     *
     * @return number of arguments
     */
    int arity();

    /**
     * Calls the function with the given arguments using interpreter
     * @param interpreter
     * @param arguments
     * @return
     */
    Object call(Interpreter interpreter, List<Object> arguments);
}
