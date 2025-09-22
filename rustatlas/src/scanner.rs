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
                        return Token::new(TokenType::EOF, self.line, String::new(), None);
                }
                let c = self.advance();
                let token = match c {
                        '(' => return self.createToken(TokenType::LEFT_PAREN, String::new(), None),
                        ')' => return self.createToken(TokenType::RIGHT_PAREN, String::new(), None),
                        '{' => return self.createToken(TokenType::LEFT_BRACE, String::new(), None),
                        '}' => return self.createToken(TokenType::RIGHT_BRACE, String::new(), None),
                        '+' => return self.createToken(TokenType::PLUS, String::new(), None),
                        '-' => return self.createToken(TokenType::MINUS, String::new(), None),
                        '*' => return self.createToken(TokenType::STAR, String::new(), None),
                        '.' => return self.createToken(TokenType::DOT, String::new(), None),
                        ',' => return self.createToken(TokenType::COMMA, String::new(), None),
                        ';' => return self.createToken(TokenType::SEMICOLON, String::new(), None),
                        '/' => return self.createToken(TokenType::SLASH, String::new(), None),
                        _=>self.createErrorToken(format!("Unexpected character: {}", c))
                };
                token
        }

        /**
          Creates a new token
        */
        fn createToken(&self, token_type: TokenType, literal: String, value: Option<String>) -> Token {
                Token::new(token_type, self.line, literal, value)
        }

        /**
        Create error token
        */
        fn createErrorToken(&self, message: String) -> Token {
                Token::new(TokenType::ERROR, self.line, message, None)
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
}