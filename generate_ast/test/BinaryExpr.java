package ast; 

public class BinaryExpr extends Expr{
	private Expr left;
	private Token operator;
	private Expr right;



	public BinaryExpr (Expr left, Token operator, Expr right){
		this.left = left;
		this.operator = operator;
		this.right = right;
	}
}
