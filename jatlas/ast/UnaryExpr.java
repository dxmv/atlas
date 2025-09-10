package ast; 

import tokenizer.Token;


public class UnaryExpr extends Expr{
	public Token operator;
	public Expr right;



	public UnaryExpr (Token operator, Expr right){
		this.operator = operator;
		this.right = right;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitUnaryExpr(this);
	}
}
