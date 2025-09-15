package ast; 

import tokenizer.Token;


public class PrintStmt extends Stmt{
	public Expr expression;



	public PrintStmt (Expr expression){
		this.expression = expression;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitPrintStmt(this);
	}
}
