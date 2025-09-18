package ast; 

import tokenizer.Token;


public class AssignExpr extends Expr{
	public Token name;
	public Expr value;



	public AssignExpr (Token name, Expr value){
		this.name = name;
		this.value = value;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitAssignExpr(this);
	}
}
