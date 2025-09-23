use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, OP_NEGATE, OP_ADD, OP_MULTIPLY, OP_SUBTRACT, OP_DIVIDE};
use crate::scanner::Scanner;
use crate::token::Token;
use crate::token::TokenType;

pub struct Compiler {
    pub previous_token: Token,
    pub current_token: Token,
    pub had_error: bool,
    pub panic_mode: bool,
    pub scanner: Scanner,
}

impl Compiler {
    pub fn new(source: String) -> Self {
        Compiler {
            previous_token: Token::new(TokenType::Eof, 0, 0, 0),
            current_token: Token::new(TokenType::Eof, 0, 0, 0),
            had_error: false,
            panic_mode: false,
            scanner: Scanner::new(source),
        }
    }

    pub fn compile(&mut self, chunk: &mut Chunk) -> bool {
        self.advance();
        self.expression(chunk);
        self.emit_byte(chunk, OP_RETURN);
        !self.had_error
    }

    fn grouping(&mut self, chunk: &mut Chunk) {
        self.expression(chunk);
        self.consume(TokenType::RightParen, "Expected ')' after expression.");
    }

    fn expression(&mut self, chunk: &mut Chunk) {
        self.parse_precedence(Precedence::Assignment, chunk);
    }
    
    fn unary(&mut self, chunk: &mut Chunk) {
        if self.match_token(TokenType::Minus) {
            let operator_type = self.previous_token.token_type;
            self.parse_precedence(Precedence::Unary, chunk);
            match operator_type {
                TokenType::Minus => self.emit_byte(chunk, OP_NEGATE),
                _ => unreachable!(),
            }
        } else {
            self.number(chunk);
        }
    }

    fn binary(&mut self, chunk: &mut Chunk) {
        let operator_type = self.previous_token.token_type;
        let rule = self.get_rule(operator_type);
        self.parse_precedence(rule.precedence.next(), chunk);
        match operator_type {
            TokenType::Plus => self.emit_byte(chunk, OP_ADD),
            TokenType::Minus => self.emit_byte(chunk, OP_SUBTRACT),
            TokenType::Star => self.emit_byte(chunk, OP_MULTIPLY),
            TokenType::Slash => self.emit_byte(chunk, OP_DIVIDE),
            _ => unreachable!(),
        }
    }



    fn number(&mut self, chunk: &mut Chunk) {
        let value = self.previous_token.lexeme(&self.scanner.source).parse::<f64>().unwrap();
        self.emit_constant(chunk, value);
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

    pub fn advance(&mut self) {
        self.previous_token = self.current_token;
        loop {
            let token = self.scanner.scan_token();
            if token.token_type != TokenType::Error {
                self.current_token = token;
                return;
            }
            let error_message = token.lexeme(&self.scanner.source).to_string();
            self.error(token, &error_message);
        }
    }

    pub fn consume(&mut self, token_type: TokenType, message: &str) {
        if self.current_token.token_type == token_type {
            self.advance();
            return;
        }
        self.error(self.current_token, message);
    }

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

    pub fn emit_byte(&mut self, chunk: &mut Chunk, byte: u8) {
        chunk.write(byte, self.previous_token.line);
    }

    pub fn emit_bytes(&mut self, chunk: &mut Chunk, byte1: u8, byte2: u8) {
        self.emit_byte(chunk, byte1);
        self.emit_byte(chunk, byte2);
    }

    pub fn emit_constant(&mut self, chunk: &mut Chunk, value: f64) {
        let constant = chunk.add_constant(value);
        self.emit_bytes(chunk, OP_CONSTANT, constant);
    }

    pub fn emit_return(&mut self, chunk: &mut Chunk) {
        self.emit_byte(chunk, OP_RETURN);
    }

    pub fn parse_precedence(&mut self, precedence: Precedence, chunk: &mut Chunk) {
        self.advance();
        let prefix_rule = self.get_rule(self.previous_token.token_type).prefix;
        if prefix_rule.is_none() {
            self.error(self.previous_token, "Expected expression.");
            return;
        }
        prefix_rule.unwrap()(self, chunk);

        while precedence <= self.get_rule(self.current_token.token_type).precedence {
            self.advance();
            let infix_rule = self.get_rule(self.previous_token.token_type).infix;
            infix_rule.unwrap()(self, chunk);
        }
    }

    /// Get the parse rule for the given token type
    fn get_rule(&self, token_type: TokenType) -> ParseRule {
        match token_type {
            TokenType::LeftParen => ParseRule::new(Some(|c, chunk| c.grouping(chunk)), None, Precedence::None),
            TokenType::Minus => ParseRule::new(Some(|c, chunk| c.unary(chunk)), Some(|c, chunk| c.binary(chunk)), Precedence::Term),
            TokenType::Plus => ParseRule::new(None, Some(|c, chunk| c.binary(chunk)), Precedence::Term),
            TokenType::Slash | TokenType::Star => ParseRule::new(None, Some(|c, chunk| c.binary(chunk)), Precedence::Factor),
            TokenType::Number => ParseRule::new(Some(|c, chunk| c.number(chunk)), None, Precedence::None),
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

type ParseFn = fn(&mut Compiler, &mut Chunk);

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