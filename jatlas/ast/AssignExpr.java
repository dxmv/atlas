package ast; 

import tokenizer.Token;
import java.util.List;


public class AssignExpr extends Expr{
	public String name;
	public Expr value;



	public AssignExpr (String name, Expr value){
		this.name = name;
		this.value = value;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitAssignExpr(this);
	}
}
