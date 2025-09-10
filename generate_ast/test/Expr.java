package ast; 

public abstract class Expr{
	abstract <R> R accept(Visitor<R> visitor);
}
