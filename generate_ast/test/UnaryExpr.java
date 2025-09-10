package ast; 

public class UnaryExpr extends Expr{
	private Token operator;
	private Expr right;



	public UnaryExpr (Token operator, Expr right){
		this.operator = operator;
		this.right = right;
	}
}
