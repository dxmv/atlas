# Atlas 

## Roadmap

### Phase 1 - Tokenization
- [x] Tokenize single chars like '*','+'...
- [x] Tokenize multiple chars like '==','!='...
- [x] Tokenize comments '//','/*'...
- [x] Tokenize strings
- [x] Tokenize numbers
- [x] Tokenize identifiers
- [x] Tokenize keywords
- [x] Allow nested multiline comments
- [x] Error handling

### Phase 3 - Representing code
- [x] Make a script that will generate the base expression class and other classes that extend it
- [x] Make the script also generate visitor stuff
- [x] Write an AST printer
- [x] Define a visitor class for our syntax tree classes that takes an expression, converts it to RPN and returns the resulting string
