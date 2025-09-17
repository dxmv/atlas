package ast; 

import tokenizer.Token;
import java.util.List;


public class DeclareStmt extends Stmt{
	public Expr expression;
	public String name;



	public DeclareStmt (Expr expression, String name){
		this.expression = expression;
		this.name = name;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitDeclareStmt(this);
	}
}
