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
- [ ] Add support for comma expressions
- [ ] Add support for ternary '?' operator
- [x] Parser errors

### Phase 5 - Evaluating Expressions
- [x] Make tokenizer use literals and values
- [x] Build an interpreter class
- [x] For '+', if one of operands is a string and the other is number, the result should be a string, like 'test' + 123 -> 'test123'
- [x] Division by 0 error
- [x] Better number handling
- [x] Runtime errors

### Phase 6 - Statment & State
- [x] Generate statment classes
- [x] Implement print statements
- [x] Implement declaration statements
- [x] Implement global state
- [x] Implement assigment statemnts
- [x] Implement block scope
- [ ] REPL no longer supports entering a single expression and printing it's result value, fix it
- [ ] Make it a runtime error to acess a varaible that has not been initiliazed

### Phase 7 - Control Flow
- [x] If statments
- [x] add 'and' and 'or' operatiors
- [x] While loop
- [x] For loop
- [ ] 'break' in loops

### Phase 8 - Functions
- [x] Parse and interpret function calls
- [x] Global clock function
- [x] Function declarations
- [x] Return statements
- [x] Local Functions and Closures

### Phase 9 - Resolving and Binding
- [x] Resolver class
- [x] Interpreting resolved variables
- [ ] Resolution errors



