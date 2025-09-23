use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, OP_NEGATE};
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
    pub fn new(scanner: Scanner) -> Self {
        Compiler {
                previous_token: Token::new(TokenType::EOF, 0, 0, 0),
                current_token: Token::new(TokenType::EOF, 0, 0, 0),
                had_error: false,
                panic_mode: false,
                scanner: scanner,
        }
    }

    /**
     * Compiles an expression into a chunk of bytecode
     */
    pub fn compile(&mut self, chunk: &mut Chunk) -> bool {
        self.current_token = self.advance();

        // at the end of the expression, emit a return
        self.emit_byte(chunk, OP_RETURN);

        !self.had_error
    }

    pub fn group(&mut self, chunk: &mut Chunk) {
        self.expression(chunk);
        self.consume(TokenType::RIGHT_PAREN, "Expect ')' after expression.");
    }

    /**
     * Emits a number to the chunk
     */
    pub fn number(&mut self, chunk: &mut Chunk) {
        let value = self.current_token.lexeme(&self.scanner.source).parse::<f64>().unwrap();
        let constant_index = chunk.add_constant(value);
        if constant_index > 63 {
            self.error(self.current_token, "Too many constants in one chunk.");
            return;
        }

        self.emit_byte(chunk, self.make_constant_instruction(constant_index));
    }

    pub fn expression(&mut self, chunk: &mut Chunk) {
        return;
    }

    /**
     * Advances the current token
     */
    pub fn advance(&mut self) -> Token {
        self.previous_token = self.current_token;
        let current_token = self.scanner.scan_token();
        if current_token.token_type == TokenType::ERROR {
                let error_message = current_token.lexeme(&self.scanner.source).to_string();
                self.error(current_token, &error_message);
        }

        current_token
    }

    /**
     * Consumes the current token if it is of the given type
     */
    pub fn consume(&mut self, token_type: TokenType, message: &str) -> Token {
        if self.current_token.token_type == token_type {
            return self.advance();
        }
        self.error(self.current_token, message);
        self.current_token
    }

    /**
     * Errors the compiler
     */
    pub fn error(&mut self, token: Token, message: &str) {
        self.had_error = true;
        self.panic_mode = true;
        println!("[line {}] Error: {}", token.line, message);
    }

    /**
     * Emits a byte to the chunk
     */
    pub fn emit_byte(&mut self, chunk: &mut Chunk, byte: u8) {
        chunk.write(byte, self.current_token.line);
    }

    /**
     * Makes a constant instruction
     */
    fn make_constant_instruction(&self, index: u8) -> u8 {
        (index << 2) | OP_CONSTANT
    }
    
    /**
     * Makes a negate instruction
     */
    fn make_negate_instruction(&self, index: u8) -> u8 {
        (index << 2) | OP_NEGATE
    }

}