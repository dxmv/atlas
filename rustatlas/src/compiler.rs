use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, OP_NEGATE, OP_ADD, OP_MULTIPLY, OP_SUBTRACT, OP_DIVIDE, OP_TRUE, OP_FALSE, OP_NIL, OP_NOT, OP_EQUAL, OP_GREATER, OP_LESS, OP_PRINT, OP_POP, OP_DEFINE_GLOBAL, OP_GET_GLOBAL, OP_SET_GLOBAL};
use crate::scanner::Scanner;
use crate::token::Token;
use crate::token::TokenType;
use crate::value::{Value, ObjRef, Obj};
use std::rc::Rc;

pub struct Compiler {
    pub previous_token: Token,
    pub current_token: Token,
    pub had_error: bool,
    pub panic_mode: bool,
    pub scanner: Scanner,
    pub chunk: Chunk,
}

impl Compiler {
    pub fn new(source: String) -> Self {
        Compiler {
            previous_token: Token::new(TokenType::Eof, 0, 0, 0),
            current_token: Token::new(TokenType::Eof, 0, 0, 0),
            had_error: false,
            panic_mode: false,
            scanner: Scanner::new(source),
            chunk: Chunk::new(),
        }
    }

    pub fn compile(&mut self) -> bool {
        self.advance();
        while !self.match_token(TokenType::Eof) {
            self.declaration();
        }
        self.emit_byte(OP_RETURN);
        !self.had_error
    }

    /**
    Advances the current token
    */
    pub fn advance(&mut self) {
        self.previous_token = self.current_token;
        loop {
            self.current_token = self.scanner.scan_token();
            if self.current_token.token_type != TokenType::Error {
                return;
            }
            let error_message = self.current_token.lexeme(&self.scanner.source).to_string();
            self.error(self.current_token, &error_message);
        }
    }

    fn declaration(&mut self) {
        if self.match_token(TokenType::Var) {
            self.var_declaration();
        }
        else{
            self.statement();
        }
    }

    fn var_declaration(&mut self) {
        self.parse_variable("Expected variable name.");
        if self.match_token(TokenType::Equal) {
            self.expression();
        }
        else{
            self.emit_byte(OP_NIL);
        }
        self.consume(TokenType::Semicolon, "Expected ';' after variable declaration.");
        self.emit_byte(OP_DEFINE_GLOBAL);
    }

    /**
    Parses a variable
    */
    fn parse_variable(&mut self, message: &str) -> u8 {
        self.consume(TokenType::Identifier, message);
        self.identifier_constant()
    }

    fn identifier_constant(&mut self) -> u8 {
        let lexeme = self.previous_token.lexeme(&self.scanner.source);
        let obj_ref = self.chunk.intern_string(&lexeme);
        let constant = self.emit_constant(Value::Obj(obj_ref));
        constant
    }

    fn statement(&mut self) {
        if self.match_token(TokenType::Print) {
            self.print_statement();
        }
        else{
            self.expression_statement();
        }
    }

    fn print_statement(&mut self) {
        self.expression();
        self.consume(TokenType::Semicolon, "Expected ';' after value.");
        self.emit_byte(OP_PRINT);
    }

    fn expression_statement(&mut self) {
        self.expression();
        self.consume(TokenType::Semicolon, "Expected ';' after expression.");
        self.emit_byte(OP_POP);
    }

    /**
    Groups an expression
    */
    fn grouping(&mut self) {
        self.expression();
        self.consume(TokenType::RightParen, "Expected ')' after expression.");
    }


    fn expression(&mut self) {
        self.parse_precedence(Precedence::Assignment);
    }
    

    fn unary(&mut self) {
        let operator_type = self.previous_token.token_type;
        self.parse_precedence(Precedence::Unary);
        match operator_type {
            TokenType::Minus => self.emit_byte(OP_NEGATE),
            TokenType::Bang => self.emit_byte(OP_NOT),
            _ => unreachable!(),
        }
    }

    fn binary(&mut self) {
        let operator_type = self.previous_token.token_type;
        let rule = self.get_rule(operator_type);
        self.parse_precedence(rule.precedence.next());
        match operator_type {
            TokenType::Plus => self.emit_byte(OP_ADD),
            TokenType::Minus => self.emit_byte(OP_SUBTRACT),
            TokenType::Star => self.emit_byte(OP_MULTIPLY),
            TokenType::Slash => self.emit_byte(OP_DIVIDE),
            TokenType::EqualEqual => self.emit_byte(OP_EQUAL),
            TokenType::Greater => self.emit_byte(OP_GREATER),
            TokenType::Less => self.emit_byte(OP_LESS),
            TokenType::BangEqual => self.emit_bytes(OP_EQUAL, OP_NOT),
            TokenType::GreaterEqual => self.emit_bytes(OP_GREATER, OP_EQUAL),
            TokenType::LessEqual => self.emit_bytes(OP_LESS, OP_EQUAL),
            _ => unreachable!(),
        }
    }

    fn variable(&mut self) {
        self.identifier_constant();
        if self.match_token(TokenType::Equal) {
            self.expression();
            self.emit_byte(OP_SET_GLOBAL);
        }
        else{
            self.emit_byte(OP_GET_GLOBAL);
        }
    }

    /**
    Emits a literal to the chunk
    */
    fn literal(&mut self) {
        match self.previous_token.token_type {
            TokenType::True => self.emit_byte(OP_TRUE),
            TokenType::False => self.emit_byte(OP_FALSE),
            TokenType::Nil => self.emit_byte(OP_NIL),
            _ => unreachable!(),
        }
    }

    fn string(&mut self) {
        let lexeme = self.previous_token.lexeme(&self.scanner.source);
        let value = &lexeme[1..lexeme.len()-1];

        let obj_ref = self.chunk.intern_string(value);
        self.emit_constant(Value::Obj(obj_ref));
    }



    fn number(&mut self) {
        let value = self.previous_token.lexeme(&self.scanner.source).parse::<f64>().unwrap();
        self.emit_constant(Value::Number(value));
    }
    
    fn match_token(&mut self, token_type: TokenType) -> bool {
        if !self.check(token_type) {
            return false;
        }
        self.advance();
        true
    }

    fn check(&self, token_type: TokenType) -> bool {
        self.current_token.token_type == token_type
    }


    /**
    Consumes the current token if it matches the given token type
    */
    pub fn consume(&mut self, token_type: TokenType, message: &str) {
        if self.current_token.token_type == token_type {
            self.advance();
            return;
        }
        self.error(self.current_token, message);
    }

    /**
    Prints an error message
    */
    pub fn error(&mut self, token: Token, message: &str) {
        if self.panic_mode {
            return;
        }
        self.panic_mode = true;
        self.had_error = true;
        eprintln!("[line {}] Error", token.line);
        if token.token_type == TokenType::Eof {
            eprintln!(" at end");
        } else if token.token_type == TokenType::Error {
            // Nothing.
        } else {
            eprintln!(" at '{}'", token.lexeme(&self.scanner.source));
        }
        eprintln!(": {}", message);
    }

    /**
    Emits a byte to the chunk
    */
    pub fn emit_byte(&mut self, byte: u8) {
        self.chunk.write(byte, self.previous_token.line);
    }

    /**
    Emits two bytes to the chunk
    */
    pub fn emit_bytes(&mut self, byte1: u8, byte2: u8) {
        self.emit_byte(byte1);
        self.emit_byte(byte2);
    }

    /**
    Emits a constant to the chunk
    */
    pub fn emit_constant(&mut self, value: Value) -> u8 {
        let constant = self.chunk.add_constant(value);
        self.emit_bytes(OP_CONSTANT, constant);
        constant
    }

    /**
    Parses the precedence
    */
    pub fn parse_precedence(&mut self, precedence: Precedence) {
        self.advance();
        let prefix_rule = self.get_rule(self.previous_token.token_type).prefix;
        if prefix_rule.is_none() {
            self.error(self.previous_token, "Expected expression.");
            return;
        }
        prefix_rule.unwrap()(self);
        while precedence <= self.get_rule(self.current_token.token_type).precedence {
            self.advance();
            let infix_rule = self.get_rule(self.previous_token.token_type).infix;
            infix_rule.unwrap()(self);
        }

    }

    /// Get the parse rule for the given token type
    fn get_rule(&self, token_type: TokenType) -> ParseRule {
        match token_type {
            TokenType::LeftParen => ParseRule::new(Some(|c| c.grouping()), None, Precedence::None),
            TokenType::Minus => ParseRule::new(Some(|c| c.unary()), Some(|c| c.binary()), Precedence::Term),
            TokenType::Plus => ParseRule::new(None, Some(|c| c.binary()), Precedence::Term),
            TokenType::Slash => ParseRule::new(None, Some(|c| c.binary()), Precedence::Factor),
            TokenType::Star => ParseRule::new(None, Some(|c| c.binary()), Precedence::Factor),
            TokenType::Number => ParseRule::new(Some(|c| c.number()), None, Precedence::None),
            TokenType::Eof => ParseRule::new(None, None, Precedence::None),
            TokenType::True => ParseRule::new(Some(|c| c.literal()), None, Precedence::None),
            TokenType::False => ParseRule::new(Some(|c| c.literal()), None, Precedence::None),
            TokenType::Nil => ParseRule::new(Some(|c| c.literal()), None, Precedence::None),
            TokenType::Bang => ParseRule::new(Some(|c| c.unary()), None, Precedence::None),
            TokenType::BangEqual => ParseRule::new(None, Some(|c| c.binary()), Precedence::Equality),
            TokenType::EqualEqual => ParseRule::new(None, Some(|c| c.binary()), Precedence::Equality),
            TokenType::Greater => ParseRule::new(None, Some(|c| c.binary()), Precedence::Comparison),
            TokenType::Less => ParseRule::new(None, Some(|c| c.binary()), Precedence::Comparison),
            TokenType::GreaterEqual => ParseRule::new(None, Some(|c| c.binary()), Precedence::Comparison),
            TokenType::LessEqual => ParseRule::new(None, Some(|c| c.binary()), Precedence::Comparison),
            TokenType::String => ParseRule::new(Some(|c| c.string()), None, Precedence::None),
            TokenType::Identifier => ParseRule::new(Some(|c| c.variable()), None, Precedence::None),
            _ => ParseRule::new(None, None, Precedence::None),
        }
    }

}

#[derive(Debug, Copy, Clone, PartialEq, PartialOrd)]
enum Precedence {
    None,
    Assignment,
    Or,
    And,
    Equality,
    Comparison,
    Term,
    Factor,
    Unary,
    Call,
    Primary,
}

impl Precedence {
    fn next(&self) -> Self {
        match self {
            Precedence::None => Precedence::Assignment,
            Precedence::Assignment => Precedence::Or,
            Precedence::Or => Precedence::And,
            Precedence::And => Precedence::Equality,
            Precedence::Equality => Precedence::Comparison,
            Precedence::Comparison => Precedence::Term,
            Precedence::Term => Precedence::Factor,
            Precedence::Factor => Precedence::Unary,
            Precedence::Unary => Precedence::Call,
            Precedence::Call => Precedence::Primary,
            Precedence::Primary => Precedence::None,
        }
    }
}

type ParseFn = fn(&mut Compiler);

struct ParseRule {
    prefix: Option<ParseFn>,
    infix: Option<ParseFn>,
    precedence: Precedence,
}

impl ParseRule {
    fn new(prefix: Option<ParseFn>, infix: Option<ParseFn>, precedence: Precedence) -> Self {
        ParseRule {
            prefix,
            infix,
            precedence,
        }
    }
}