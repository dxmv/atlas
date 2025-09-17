package ast; 

import tokenizer.Token;
import java.util.List;


public abstract class Stmt{
	abstract <R> R accept(Visitor<R> visitor);
}
