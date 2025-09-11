package parser;

import ast.*;
import tokenizer.Token;
import tokenizer.TokenType;

import java.util.List;

import static tokenizer.TokenType.*;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        return expression();
    }

    private Expr expression(){
        return equality();
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().getType() == type;
    }

    private Expr primary(){
        if(match(TRUE)) { return new LiteralExpr(true); }
        if(match(FALSE)) { return new LiteralExpr(false); }
        if(match(NIL)) { return new LiteralExpr(null); }
        if(match(NUMBER,STRING)) {return new LiteralExpr(previous().getValue()); }
        if(match(LEFT_PAREN)){
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new GroupingExpr(expr);
        }
        return null;
    }

    private Expr unary(){
        if(match(BANG,MINUS)){
            Token operator = previous();
            Expr expr = unary();
            return new UnaryExpr(operator, expr);
        }
        return primary();
    }

    private Expr factor(){
        Expr expr = unary();
        while (match(SLASH,STAR)){
            Token operator = previous();
            Expr expr1 = unary();
            expr = new BinaryExpr(expr, operator, expr1);
        }
        return expr;
    }

    private Expr term(){
        Expr expr = factor();
        while (match(PLUS,MINUS)){
            Token operator = previous();
            Expr expr1 = factor();
            expr = new BinaryExpr(expr, operator, expr1);
        }
        return expr;
    }

    private Expr comparison(){
        Expr expr = term();
        while (match(GREATER,GREATER_EQUAL,LESS,LESS_EQUAL)){
            Token operator = previous();
            Expr expr1 = term();
            expr = new BinaryExpr(expr, operator, expr1);
        }
        return expr;
    }

    private Expr equality(){
        Expr expr = comparison();
        while (match(BANG_EQUAL,EQUAL_EQUAL)){
            Token operator = previous();
            Expr expr1 = comparison();
            expr = new BinaryExpr(expr, operator, expr1);
        }
        return expr;
    }

    // helpers
    private void consume(TokenType type, String message) {
        if(check(type)) {
            advance();
        }
        else{
            System.out.println(message);
        }
    }

    private boolean isAtEnd(){
        return peek().getType() == EOF;
    }

    private Token peek(){
        return tokens.get(current);
    }

    private Token previous(){
        return tokens.get(current-1);
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }
}
