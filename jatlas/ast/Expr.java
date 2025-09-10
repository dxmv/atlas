package ast; 

import tokenizer.Token;


public abstract class Expr{
	abstract <R> R accept(Visitor<R> visitor);
}
