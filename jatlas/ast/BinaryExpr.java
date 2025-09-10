package ast; 

import tokenizer.Token;


public class BinaryExpr extends Expr{
	public Expr left;
	public Token operator;
	public Expr right;



	public BinaryExpr (Expr left, Token operator, Expr right){
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitBinaryExpr(this);
	}
}
