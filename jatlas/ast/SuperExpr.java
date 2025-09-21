package ast; 

import tokenizer.Token;
import java.util.List;


public class SuperExpr extends Expr{
	public Token keyword;
	public Token method;



	public SuperExpr (Token keyword, Token method){
		this.keyword = keyword;
		this.method = method;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitSuperExpr(this);
	}
}
