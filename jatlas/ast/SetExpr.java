package ast; 

import tokenizer.Token;
import java.util.List;


public class SetExpr extends Expr{
	public Expr object;
	public Token name;
	public Expr val;



	public SetExpr (Expr object,Token name,Expr val){
		this.object = object;
		this.name = name;
		this.val = val;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitSetExpr(this);
	}
}
