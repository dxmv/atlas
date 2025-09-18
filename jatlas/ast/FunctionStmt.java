package ast; 

import tokenizer.Token;
import java.util.List;


public class FunctionStmt extends Stmt{
	public Token name;
	public List<Token> params;
	public List<Stmt> stmts;



	public FunctionStmt (Token name,List<Token> params,List<Stmt> stmts){
		this.name = name;
		this.params = params;
		this.stmts = stmts;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitFunctionStmt(this);
	}
}
