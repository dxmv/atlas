package ast; 

import tokenizer.Token;
import java.util.List;


public class LiteralExpr extends Expr{
	public Object value;



	public LiteralExpr (Object value){
		this.value = value;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitLiteralExpr(this);
	}
}
