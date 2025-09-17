package ast; 

import tokenizer.Token;

import java.util.List;


public class BlockStmt extends Stmt{
	public List<Stmt> stmts;



	public BlockStmt (List<Stmt> stmts){
		this.stmts = stmts;
	}

	@Override

	<R> R accept(Visitor<R> visitor) {
		return visitor.visitBlockStmt(this);
	}
}
