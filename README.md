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
- [x] Resolution errors
- [ ] Extend the resolver to report an error if a local variable is never used
- [ ] Extend the resolver to associate a unique index for each local variable declared in a scope. When resolving a variable access, look up both the scope the variable is in and its index and store that. In the interpreter, use that to quickly access a variable by its index instead of using a map.

### Phase 10 - Classes
- [x] Class declarations
- [x] Creating instances
- [x] Properties on instances
- [x] Methods on classes
- [x] This
- [x] Constructors and initializers
- [ ] Implement static methods
- [ ] Implement getters and setters

### Phase 11 - Inheritance
- [x] Superclasses and subclasses
- [x] Inheriting methods
- [x] Calling superclass methods

### Phase 12 - Chunks of Bytecode
- [x] Chunk struct
- [x] Constants 
- [x] Line info

### Phase 13 - Virtual machine
- [x] An Instruction Execution Machine
- [x] A value stack manipulator
- [x] A simple calculator 

### Phase 14 - Scanning on demand
- [x] Simple scanner loop
- [x] Single char, double char tokens
- [x] Literals
- [x] Numbers
- [x] Identifiers
- [x] Keywords
- [x] spaces, new lines etc.

### Phase 15 - Compilling expressions
- [x] Parse token by token
- [x] Parse numbers
- [x] Parse grouping and unary
- [x] Presednce parsing
- [x] Parse infix expressions
- [x] Pratt parsing

### Phase 16 - Types of Values
- [x] Modify the Value type so it supports bool, nils and numbers
- [x] Correct the Value type usage in compiler and vm
- [x] Add support for true, false and nil values in both compiler and vm
- [x] Add support for '!', NOT operator
- [x] Add support for equality operators

### Phase 17 - Strings
- [x] Add object to the Value type and strings to the object
- [x] Strings in compiler
- [x] Operations on Strings

### Phase 18 - Hash Tables
- [x] Add interning to chunk
- [x] When we encounter a string in compiler intern it
- [x] Opeartions on Strings faster

### Phase 19 - Global Variables
- [x] Print & Expression statement
- [x] Varaible declarations
- [x] Reading varaibles
- [ ] Assignment




