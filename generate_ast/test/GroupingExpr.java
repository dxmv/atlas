package ast; 

public class GroupingExpr extends Expr{
	private Expr expression;



	public GroupingExpr (Expr expression){
		this.expression = expression;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}
}
