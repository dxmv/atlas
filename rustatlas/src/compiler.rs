use crate::chunk::Chunk;
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

        !self.had_error
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

}