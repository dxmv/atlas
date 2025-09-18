package ast; 

import tokenizer.Token;
import java.util.List;


public class CallExpr extends Expr{
	public Expr callee;
	public Token paren;
	public List<Expr> arguments;



	public CallExpr (Expr callee, Token paren, List<Expr> arguments){
		this.callee = callee;
		this.paren = paren;
		this.arguments = arguments;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitCallExpr(this);
	}
}
