package ast; 

public interface Visitor<R>{

	R visitBinaryExpr(BinaryExpr expr);
	R visitGroupingExpr(GroupingExpr expr);
	R visitLiteralExpr(LiteralExpr expr);
	R visitUnaryExpr(UnaryExpr expr);

}
