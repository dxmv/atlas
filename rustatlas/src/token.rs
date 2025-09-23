

#[derive(PartialEq, Debug, Copy, Clone)]
pub enum TokenType{

        // Single-character tokens
        LeftParen, RightParen, LeftBrace, RightBrace,
        Comma, Dot, Minus, Plus, Semicolon, Slash, Star,

        // One or two character tokens
        Bang, BangEqual,
        Equal, EqualEqual,
        Greater, GreaterEqual,
        Less, LessEqual,

        // Literals
        Identifier, String, Number,

        // Keywords
        And, Class, Else, False, Func, For, If, Nil, Or,
        Print, Return, Super, This, True, Var, While,

        // End of file
        Eof,
        // Error tokens
        Error,
}

#[derive(Debug, Copy, Clone)]
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
                "and" => Some(TokenType::And),
                "or" => Some(TokenType::Or),
                "class" => Some(TokenType::Class),
                "if" => Some(TokenType::If),
                "else" => Some(TokenType::Else),
                "for" => Some(TokenType::For),
                "while" => Some(TokenType::While),
                "return" => Some(TokenType::Return),
                "false" => Some(TokenType::False),
                "true" => Some(TokenType::True),
                "fun" => Some(TokenType::Func),
                "var" => Some(TokenType::Var),
                "nil" => Some(TokenType::Nil),
                "this" => Some(TokenType::This),
                "super" => Some(TokenType::Super),
                "print" => Some(TokenType::Print),
                _ => None,
        }
}