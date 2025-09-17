package ast; 

import tokenizer.Token;
import java.util.List;


public class GroupingExpr extends Expr{
	public Expr expression;



	public GroupingExpr (Expr expression){
		this.expression = expression;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}
}
