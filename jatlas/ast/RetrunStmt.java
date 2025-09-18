package ast; 

import tokenizer.Token;
import java.util.List;


public class RetrunStmt extends Stmt{
	public Token keyword;
	public Expr value;



	public RetrunStmt (Token keyword, Expr value){
		this.keyword = keyword;
		this.value = value;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitRetrunStmt(this);
	}
}
