package ast; 

import tokenizer.Token;
import java.util.List;


public class ClassStmt extends Stmt{
	public Token name;
	public VariableExpr superclass;
	public List<FunctionStmt> functions;



	public ClassStmt (Token name, VariableExpr superclass, List<FunctionStmt> functions){
		this.name = name;
		this.superclass = superclass;
		this.functions = functions;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitClassStmt(this);
	}
}
