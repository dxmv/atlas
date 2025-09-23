use crate::token::{is_keyword, Token, TokenType};


pub struct Scanner {
    pub source: String,
    pub current: usize,
    pub start: usize,
    pub line: usize
}

impl Scanner{
        pub fn new(source:String) -> Self {
                Scanner{
                        source,
                        current:0,
                        start:0,
                        line:1,
                }
        }

        pub fn scan_tokens(&mut self) -> Vec<Token> {
                let mut tokens = Vec::new();
                loop {
                        let token = self.scan_token();
                        println!("{:?}", token);
                        if token.token_type == TokenType::Eof {
                                tokens.push(token);
                                break;
                        }
                        tokens.push(token);
                }
                tokens
        }

        pub fn scan_token(&mut self) -> Token {
                self.start = self.current;
                if self.isAtEnd() {
                        return Token::new(TokenType::Eof, self.line, self.current, 0);
                }
                let c = self.advance();
                let token = match c {
                        '(' => return self.create_token(TokenType::LeftParen),
                        ')' => return self.create_token(TokenType::RightParen),
                        '{' => return self.create_token(TokenType::LeftBrace),
                        '}' => return self.create_token(TokenType::RightBrace),
                        '+' => return self.create_token(TokenType::Plus),
                        '-' => return self.create_token(TokenType::Minus),
                        '*' => return self.create_token(TokenType::Star),
                        '.' => return self.create_token(TokenType::Dot),
                        ',' => return self.create_token(TokenType::Comma),
                        ';' => return self.create_token(TokenType::Semicolon),
                        '/' => return self.create_token(TokenType::Slash),
                        '=' => if self.matches_char(self.peek(), '=') { self.advance(); return self.create_token(TokenType::EqualEqual); } else { return self.create_token(TokenType::Equal); },
                        '>' => if self.matches_char(self.peek(), '=') { self.advance(); return self.create_token(TokenType::GreaterEqual); } else { return self.create_token(TokenType::Greater); },
                        '<' => if self.matches_char(self.peek(), '=') { self.advance(); return self.create_token(TokenType::LessEqual); } else { return self.create_token(TokenType::Less); },
                        '!' => if self.matches_char(self.peek(), '=') { self.advance(); return self.create_token(TokenType::BangEqual); } else { return self.create_token(TokenType::Bang); },
                        '"' => return self.handle_string(),
                        '0'..='9' => return self.handle_number(),
                        'a'..='z' | 'A'..='Z' => return self.handle_identifier(),
                        '\n' | '\t' | '\r' | ' ' => return self.scan_token(),
                        _=>self.create_error_token(format!("Unexpected character: {}", c))
                };
                token
        }

        /**
        Handles strings
        */
        fn handle_string(&mut self) -> Token {
                while !self.isAtEnd() && self.peek() != '"' {
                        self.advance();
                }
                if self.isAtEnd() {
                        return self.create_error_token("Unterminated string".to_string());
                }
                self.advance(); // consume closing quote
                return self.create_token(TokenType::String);
        }

        /**
        Handles identifiers
        */
        fn handle_identifier(&mut self) -> Token {
                while !self.isAtEnd() && self.peek().is_alphanumeric() {
                        self.advance();
                }
                // check if the identifier is a keyword
                let identifier = self.source[self.start..self.current].to_string();
                if let Some(token_type) = is_keyword(&identifier) {
                        return self.create_token(token_type);
                }
                return self.create_token(TokenType::Identifier);
        }

        /**
        Handles numbers
        */
        fn handle_number(&mut self) -> Token {
                let mut num_dots: usize = 0;
                while !self.isAtEnd() {
                        // handle 1 decimal point
                        if self.matches_char(self.peek(), '.') {
                                num_dots += 1;
                                if num_dots > 1 {
                                        return self.create_error_token("Invalid number format".to_string());
                                }
                                self.advance();
                        }
                        // if not a digit, break
                        if !self.peek().is_digit(10) {
                                break;
                        }
                        // if a digit, advance
                        self.advance();
                }
                return self.create_token(TokenType::Number);
        }

        /**
          Creates a new token
        */
        fn create_token(&self, token_type: TokenType) -> Token {
                let length = self.current - self.start;
                Token::new(token_type, self.line, self.start, length)
        }

        /**
        Create error token
        */
        fn create_error_token(&self, _message: String) -> Token {
                let length = self.current - self.start;
                Token::new(TokenType::Error, self.line, self.start, length)
        }

        fn isAtEnd(&self) -> bool {
                self.current >= self.source.len()
        }


        /**
        Consumes the current character and returns it
        */
        fn advance(&mut self) -> char {
                let c = self.source.chars().nth(self.current).unwrap();
                self.current += 1;
                c
        }

        /**
        Returns the current character without consuming it
        */
        fn peek(&self) -> char {
                self.source.chars().nth(self.current).unwrap()
        }

        /**
        Returns the next character without consuming it
        */
        fn peek_next(&self) -> char {
                self.source.chars().nth(self.current + 1).unwrap()
        }


        /**
        Check if the given character is the same as the other character
        */
        fn matches_char(&self, c: char, c2: char) -> bool {
                c == c2
        }
}