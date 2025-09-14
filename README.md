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
- [x] Tokenizer errors

### Phase 3 - Representing code
- [x] Make a script that will generate the base expression class and other classes that extend it
- [x] Make the script also generate visitor stuff
- [x] Write an AST printer
- [x] Define a visitor class for our syntax tree classes that takes an expression, converts it to RPN and returns the resulting string

### Phase 4 - Parser
- [x] Build a recursive desent parser
- [x] Add support for comma expressions
- [ ] Add support for ternary '?' operator
- [ ] Parser errors

### Phase 5 - Evaluating Expressions
- [x] Make tokenizer use literals and values
- [x] Build an interpreter class
- [x] For '+', if one of operands is a string and the other is number, the result should be a string, like 'test' + 123 -> 'test123'
- [x] Division by 0 error
- [ ] Better number handling
- [ ] Runtime errors

### Phase 6 - Statment & State


