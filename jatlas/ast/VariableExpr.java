package ast; 

import tokenizer.Token;


public class VariableExpr extends Expr{
	public String identifier;



	public VariableExpr (String identifier){
		this.identifier = identifier;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitVariableExpr(this);
	}
}
