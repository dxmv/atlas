package parser;

import ast.*;
import error.ErrorReporter;
import tokenizer.Token;
import tokenizer.TokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static tokenizer.TokenType.*;

public class Parser {
    private static class ParseError extends RuntimeException {}
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        // go until the at end of file
        while(!isAtEnd()){
            Stmt stmt = declare();
            statements.add(stmt);
        }
        return statements;
    }

    /**
     *
     * @return
     */
    private Stmt declare() {
        if(match(VAR)) return declareStmt();
        return statement();
    }

    private DeclareStmt declareStmt() {
        Token id = consume(IDENTIFIER,"Expected identifier");
        Expr expr;
        if(match(EQUAL)) {
            expr = expression();
        }
        else{
            expr = new LiteralExpr("null");
        }
        consume(SEMICOLON,"Expect ';'.");
        return new DeclareStmt(expr, id.getLiteral());
    }

    /**
     * Top level of statment tree
     * @return the appropriate statement
     */
    private Stmt statement(){
        if(match(PRINT)) return printStatement();
        if(match(LEFT_BRACE)) return blockStatement();
        if(match(IF)) return ifStatement();
        if(match(WHILE)) return whileStatement();
        if(match(FOR)) return forStatement();
        return expressionStatement();
    }

    private WhileStmt whileStatement() {
        // consume '('
        consume(LEFT_PAREN,"Expected \"(\" after while.");
        // get the condition
        Expr condition = expression();
        // consume '('
        consume(RIGHT_PAREN,"Expected \")\" after condition.");

        // get the body
        Stmt body = statement();
        return new WhileStmt(condition, body);
    }

    private Stmt forStatement() {
        // consume '('
        consume(LEFT_PAREN,"Expected \"(\" after for.");
        // try to get the initializer
        Stmt initializer = null;
        if(match(SEMICOLON)) {
            // no init
            initializer = null;
        } else if(match(VAR)){
            initializer = declareStmt();
        } else{
            initializer = expressionStatement();
        }

        // get the condition
        Expr condition = null;
        if(!check(SEMICOLON)) condition = expression();
        consume(SEMICOLON, "Expect ';' after loop condition.");

        // get the increment
        Expr increment = null;
        if(!check(RIGHT_PAREN)) increment = expression();
        consume(RIGHT_PAREN, "Expect ')' after for clauses.");

        Stmt body = statement();
        // add increment to end of body
        if(increment != null) {
            body = new BlockStmt(Arrays.asList(body,new ExpressionStmt(increment)));
        }
        // if no condition, then always true
        if(condition == null) {
            condition = new LiteralExpr(true);
        }
        body = new WhileStmt(condition, body);
        // add the initializer to start of body
        if(initializer != null) {
            body = new BlockStmt(Arrays.asList(initializer,body));
        }
        return body;
    }

    private IfStmt ifStatement() {
        // consume '('
        consume(LEFT_PAREN,"Expected \"(\" after if.");
        // get the condition
        Expr condition = expression();
        // consume '('
        consume(RIGHT_PAREN,"Expected \")\" after condition.");

        // get the thenBranch
        Stmt thenBranch = statement();

        Stmt elseBranch = null;
        if(match(ELSE)) elseBranch = statement();

        return new IfStmt(condition,thenBranch,elseBranch);
    }

    private BlockStmt blockStatement() {
        List<Stmt> statements = new ArrayList<>();
        while(!isAtEnd() && !check(RIGHT_BRACE)){
            statements.add(declare());
        }

        consume(RIGHT_BRACE,"Expect '}' after block.");
        return new BlockStmt(statements);
    }

    private PrintStmt printStatement(){
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new PrintStmt(value);
    }


    private ExpressionStmt expressionStatement() {
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStmt(value);
    }

    /**
     * Top level of expressions
     * @param
     * @return
     */
    private Expr expression(){
        return assignment();
    }

    private Expr assignment(){
        Expr expr = or();

        if(match(EQUAL)){
            Token equal = previous();
            Expr right = assignment();
            if(expr instanceof VariableExpr ve){
                String name = ve.identifier;
                return new AssignExpr(name,right);
            }
            error(equal,"Invalid assignment target.");
        }
        return expr;
    }

    private Expr or(){
        Expr expr = and();

        while (match(OR)){
            Token operator = previous();
            Expr expr1 = and();
            expr = new LogicalExpr(expr, operator, expr1);
        }
        return expr;
    }

    private Expr and(){
        Expr expr = equality();

        while (match(AND)){
            Token operator = previous();
            Expr expr1 = equality();
            expr = new LogicalExpr(expr, operator, expr1);
        }
        return expr;
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

    // expressions handling
    private Expr primary(){
        if(match(TRUE)) { return new LiteralExpr(true); }
        if(match(FALSE)) { return new LiteralExpr(false); }
        if(match(NIL)) { return new LiteralExpr(null); }
        if(match(NUMBER,STRING)) { return new LiteralExpr(previous().getValue()); }
        if(match(IDENTIFIER)) { return new VariableExpr(previous().getLiteral()); }
        if(match(LEFT_PAREN)){
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new GroupingExpr(expr);
        }

        throw error(peek(), "Expect expression.");
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
    private Token consume(TokenType type, String message) {
        if(check(type)) {
            return advance();
        }

        throw error(peek(), message);
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

    private ParseError error(Token token, String message) {
        ErrorReporter.error(token, message);
        return new ParseError();
    }
}
