package ast; 

import tokenizer.Token;
import java.util.List;


public class GetExpr extends Expr{
	public Token name;
	public Expr expr;



	public GetExpr (Token name, Expr expr){
		this.name = name;
		this.expr = expr;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitGetExpr(this);
	}
}
