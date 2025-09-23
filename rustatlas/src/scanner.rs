use crate::token::{Token, TokenType};


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

        pub fn scan(&mut self) -> Vec<Token> {
                let mut tokens = Vec::new();
                loop {
                        let token = self.scanToken();
                        println!("{:?}", token);
                        if token.token_type == TokenType::EOF {
                                tokens.push(token);
                                break;
                        }
                        tokens.push(token);
                }
                tokens
        }

        pub fn scanToken(&mut self) -> Token {
                self.start = self.current;
                if self.isAtEnd() {
                        return Token::new(TokenType::EOF, self.line, self.current, 0);
                }
                let c = self.advance();
                let token = match c {
                        '(' => return self.createToken(TokenType::LEFT_PAREN),
                        ')' => return self.createToken(TokenType::RIGHT_PAREN),
                        '{' => return self.createToken(TokenType::LEFT_BRACE),
                        '}' => return self.createToken(TokenType::RIGHT_BRACE),
                        '+' => return self.createToken(TokenType::PLUS),
                        '-' => return self.createToken(TokenType::MINUS),
                        '*' => return self.createToken(TokenType::STAR),
                        '.' => return self.createToken(TokenType::DOT),
                        ',' => return self.createToken(TokenType::COMMA),
                        ';' => return self.createToken(TokenType::SEMICOLON),
                        '/' => return self.createToken(TokenType::SLASH),
                        '=' => if self.matches_char(self.peek(), '=') { self.advance(); return self.createToken(TokenType::EQUAL_EQUAL); } else { return self.createToken(TokenType::EQUAL); },
                        '>' => if self.matches_char(self.peek(), '=') { self.advance(); return self.createToken(TokenType::GREATER_EQUAL); } else { return self.createToken(TokenType::GREATER); },
                        '<' => if self.matches_char(self.peek(), '=') { self.advance(); return self.createToken(TokenType::LESS_EQUAL); } else { return self.createToken(TokenType::LESS); },
                        '!' => if self.matches_char(self.peek(), '=') { self.advance(); return self.createToken(TokenType::BANG_EQUAL); } else { return self.createToken(TokenType::BANG); },
                        '"' => return self.handle_string(),
                        _=>self.createErrorToken(format!("Unexpected character: {}", c))
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
                        return self.createErrorToken("Unterminated string".to_string());
                }
                self.advance(); // consume closing quote
                return self.createToken(TokenType::STRING);
        }

        /**
          Creates a new token
        */
        fn createToken(&self, token_type: TokenType) -> Token {
                let length = self.current - self.start;
                Token::new(token_type, self.line, self.start, length)
        }

        /**
        Create error token
        */
        fn createErrorToken(&self, _message: String) -> Token {
                let length = self.current - self.start;
                Token::new(TokenType::ERROR, self.line, self.start, length)
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
        fn peekNext(&self) -> char {
                self.source.chars().nth(self.current + 1).unwrap()
        }


        /**
        Check if the given character is the same as the other character
        */
        fn matches_char(&self, c: char, c2: char) -> bool {
                c == c2
        }
}