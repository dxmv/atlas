package ast; 

import tokenizer.Token;
import java.util.List;


public interface Visitor<R>{

	R visitBinaryExpr(BinaryExpr expr);
	R visitGroupingExpr(GroupingExpr expr);
	R visitLiteralExpr(LiteralExpr expr);
	R visitUnaryExpr(UnaryExpr expr);
	R visitVariableExpr(VariableExpr expr);
	R visitAssignExpr(AssignExpr expr);
	R visitPrintStmt(PrintStmt expr);
	R visitExpressionStmt(ExpressionStmt expr);
	R visitDeclareStmt(DeclareStmt expr);
	R visitBlockStmt(BlockStmt expr);
	R visitIfStmt(IfStmt expr);
	R visitLogicalExpr(LogicalExpr expr);
	R visitWhileStmt(WhileStmt expr);
	R visitCallExpr(CallExpr expr);
	R visitFunctionStmt(FunctionStmt expr);

}
