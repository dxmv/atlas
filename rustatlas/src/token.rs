

#[derive(PartialEq, Debug)]
pub enum TokenType{

        // Single-character tokens
        LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
        COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

        // One or two character tokens
        BANG, BANG_EQUAL,
        EQUAL, EQUAL_EQUAL,
        GREATER, GREATER_EQUAL,
        LESS, LESS_EQUAL,

        // Literals
        IDENTIFIER, STRING, NUMBER,

        // Keywords
        AND, CLASS, ELSE, FALSE, FUNC, FOR, IF, NIL, OR,
        PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

        // End of file
        EOF,
        // Error tokens
        ERROR,
}

#[derive(Debug)]
pub struct Token {
        pub token_type: TokenType,
        pub line: usize,
        pub start: usize,
        pub length: usize,
}

impl Token {
        pub fn new(token_type: TokenType, line: usize, start: usize, length: usize) -> Self {
                Token { token_type, line, start, length }
        }

        /// Extract the actual text of this token from the source string
        pub fn lexeme<'a>(&self, source: &'a str) -> &'a str {
                &source[self.start..self.start + self.length]
        }
}

pub fn is_keyword(identifier: &str) -> Option<TokenType> {
        match identifier {
                "and" => Some(TokenType::AND),
                "or" => Some(TokenType::OR),
                "class" => Some(TokenType::CLASS),
                "if" => Some(TokenType::IF),
                "else" => Some(TokenType::ELSE),
                "for" => Some(TokenType::FOR),
                "while" => Some(TokenType::WHILE),
                "return" => Some(TokenType::RETURN),
                "false" => Some(TokenType::FALSE),
                "true" => Some(TokenType::TRUE),
                "fun" => Some(TokenType::FUNC),
                "var" => Some(TokenType::VAR),
                "nil" => Some(TokenType::NIL),
                "this" => Some(TokenType::THIS),
                "super" => Some(TokenType::SUPER),
                "print" => Some(TokenType::PRINT),
                _ => None,
        }
}