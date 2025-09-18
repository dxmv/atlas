package ast; 

import tokenizer.Token;


public class VariableExpr extends Expr{
	public Token identifier;



	public VariableExpr (Token identifier){
		this.identifier = identifier;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitVariableExpr(this);
	}
}
