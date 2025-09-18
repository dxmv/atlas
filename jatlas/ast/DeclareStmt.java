package ast; 

import tokenizer.Token;


public class DeclareStmt extends Stmt{
	public Expr expression;
	public Token name;



	public DeclareStmt (Expr expression, Token name){
		this.expression = expression;
		this.name = name;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitDeclareStmt(this);
	}
}
