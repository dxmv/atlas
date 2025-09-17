package ast; 

import tokenizer.Token;
import java.util.List;


public class ExpressionStmt extends Stmt{
	public Expr expression;



	public ExpressionStmt (Expr expression){
		this.expression = expression;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitExpressionStmt(this);
	}
}
