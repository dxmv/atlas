package ast; 

import tokenizer.Token;
import java.util.List;


public class LogicalExpr extends Expr{
	public Expr left;
	public Token operator;
	public Expr right;



	public LogicalExpr (Expr left, Token operator, Expr right){
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitLogicalExpr(this);
	}
}
