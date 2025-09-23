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
            previous_token: Token::new(TokenType::EOF, 0, 0, 0),
            current_token: Token::new(TokenType::EOF, 0, 0, 0),
            had_error: false,
            panic_mode: false,
            scanner: Scanner::new(source),
        }
    }

    pub fn compile(&mut self, chunk: &mut Chunk) -> bool {
        self.current_token = self.advance();
        self.expression(chunk);
        self.consume(TokenType::EOF, "Expect end of expression.");
        self.emit_byte(chunk, OP_RETURN);
        !self.had_error
    }

    fn expression(&mut self, chunk: &mut Chunk) {
        return;
    }
    
    fn unary(&mut self, chunk: &mut Chunk) {
        if self.match_token(TokenType::MINUS) {
            let operator_type = self.previous_token.token_type;
            self.unary(chunk);
            match operator_type {
                TokenType::MINUS => self.emit_byte(chunk, OP_NEGATE),
                _ => unreachable!(),
            }
        } else {
            self.number(chunk);
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
        self.current_token = self.advance();
        true
    }

    fn check(&self, token_type: TokenType) -> bool {
        self.current_token.token_type == token_type
    }

    pub fn advance(&mut self) -> Token {
        self.previous_token = self.current_token;
        loop {
            let token = self.scanner.scan_token();
            if token.token_type != TokenType::ERROR {
                return token;
            }
            let error_message = token.lexeme(&self.scanner.source).to_string();
            self.error(token, &error_message);
        }
    }

    pub fn consume(&mut self, token_type: TokenType, message: &str) {
        if self.current_token.token_type == token_type {
            self.current_token = self.advance();
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
        if token.token_type == TokenType::EOF {
            eprintln!(" at end");
        } else if token.token_type == TokenType::ERROR {
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
}