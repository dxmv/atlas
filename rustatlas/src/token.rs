

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
        pub literal: String,
        pub value: Option<String>,
}

impl Token {
        pub fn new(token_type: TokenType, line: usize, literal: String, value: Option<String>) -> Self {
                Token { token_type, line, literal, value }
        }
}