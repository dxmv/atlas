package ast; 

import tokenizer.Token;
import java.util.List;


public class ThisExpr extends Expr{
	public Token keyword;



	public ThisExpr (Token keyword){
		this.keyword = keyword;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitThisExpr(this);
	}
}
