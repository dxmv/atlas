package ast; 

public class GroupingExpr extends Expr{
	private Expr expression;



	public GroupingExpr (Expr expression){
		this.expression = expression;
	}
}
