# 08.09.2025
- [x] Tokenize single chars like '*','+'...
- [x] Tokenize multiple chars like '==','!='...
- [x] Tokenize comments '//','/*'...

# 09.09.2025
- [x] Tokenize strings
- [x] Tokenize numbers
- [x] Tokenize identifiers
- [x] Tokenize keywords
- [x] Allow nested multiline comments
- [x] Error handling and cleaner code

# 10.09.2025
- [x] Make a script that will generate the base expression class and other classes that extend it
- [x] Make the script also generate visitor stuff
- [x] Write an AST printer

# 11.09.2025
- [x] Define a visitor class for our syntax tree classes that takes an expression, converts it to RPN and returns the resulting string
- [x] Build a recursive desent parser
- [x] Add support for comma expressions

# 13.09.2025
- [x] Make tokenizer use literals and values
- [x] Build an interpreter class
- [x] For '+' if one of operands is a string and the other is number, the result should be a string, like 'test' + 123 -> 'test123'
- [x] Division by 0 error

# 14.09.2025
- [x] Tokenizer errors
- [x] Parser errors
- [x] Runtime errors
- [x] Better number handling in interpreter

# 15.09.2025
- [x] Generate statment classes
- [x] Implement print statements
- [x] Implement declaration statement
- [x] Implement global state

# 16.09.2025
- [x] Implement assigment statemnts
- [x] Implement block scope

# 17.09.2025
- [x] If statments
- [x] add 'and' and 'or' operatiors
- [x] While loop
- [x] For loop

# 18.09.2025
- [x] Use tokens instead of strings in parser
- [x] Parse and interpret function calls
- [x] Global clock function
- [x] Function declarations
- [x] Return statements
- [x] Local functions and clousers

# 19.09.2025

# 20.09.2025
- [x] Resolver class
- [x] Interpreting resolved variables
- [x] Resolution errors
- [x] Class declarations
- [x] Properties on instances
- [x] Methods on classes
- [x] This
- [x] Constructors and initializers

# 21.09.2025
- [x] Superclasses and subclasses
- [x] Inheriting methods
- [x] Calling superclass methods
- [x] Chunks
- [x] Constants 
- [x] Line info

# 23.09.2025
- [x] Single char, double char tokens
- [x] Literals
- [x] Numbers
- [x] Identifiers
- [x] Keywords
- [x] spaces, new lines etc.
- [x] Parse token by token

# 24.09.2025
- [x] Fix dissasemble
- [x] Modify the Value type so it supports bool, nils and numbers
- [x] Correct the Value type usage in compiler and vm
- [x] Add support for true, false and nil values in both compiler and vm
- [x] Add support for '!', NOT operator
- [x] Add support for equality operators
- [x] Add object to the Value type and strings to the object
- [x] Strings in compiler
- [x] Operations on Strings

# 25.09.2025
- [x] Add interning to chunk
- [x] When we encounter a string in compiler intern it
- [x] Opeartions on Strings faster
- [x] Print & Expression statement
- [x] Varaible declarations
- [x] Reading varaibles
- [x] Assignment

# 26.09.2025
- [x] Representing local variables in compiler
- [x] Block statments
- [ ] Declaring local variables
- [ ] Using locals




