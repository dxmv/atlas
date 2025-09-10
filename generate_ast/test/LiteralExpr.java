package ast; 

public class LiteralExpr extends Expr{
	private Object value;



	public LiteralExpr (Object value){
		this.value = value;
	}
}
