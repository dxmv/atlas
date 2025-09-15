package ast; 

import tokenizer.Token;


public abstract class Stmt{
	abstract <R> R accept(Visitor<R> visitor);
}
