package ast; 

import tokenizer.Token;
import java.util.List;


public class WhileStmt extends Stmt{
	public Expr condition;
	public Stmt body;



	public WhileStmt (Expr condition, Stmt body){
		this.condition = condition;
		this.body = body;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitWhileStmt(this);
	}
}
