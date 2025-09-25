

use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, OP_NEGATE, OP_ADD, OP_SUBTRACT, OP_MULTIPLY, OP_DIVIDE, OP_TRUE, OP_FALSE, OP_NIL, OP_NOT, OP_EQUAL, OP_GREATER, OP_LESS, OP_POP, OP_PRINT, OP_DEFINE_GLOBAL, OP_GET_GLOBAL};
use crate::value::{Value, ObjRef, Obj, ObjString};
use std::collections::HashMap;

#[derive(Debug)]
pub enum InterpretResult {
    Ok(Value),
    CompileError,
    RuntimeError(String),
}

pub struct VM {
    pub chunk: Chunk,
    pub stack: Vec<Value>,
    ip: usize,
    globals: HashMap<Box<str>, Value>,
}

impl VM {
    pub fn new() -> Self {
        VM { chunk: Chunk::new(), ip: 0, stack: vec![], globals: HashMap::new() }
    }

    /**
        Run the VM
    */
    pub fn run(&mut self) -> InterpretResult {
        loop {
            let opcode = self.chunk.code[self.ip];
            self.ip += 1;

            match opcode {
                OP_RETURN => {
                    return InterpretResult::Ok(Value::Nil);
                }
                OP_CONSTANT => {
                    let constant_index = self.chunk.code[self.ip];
                    self.ip += 1;
                    let constant = self.chunk.constants[constant_index as usize].clone();
                    self.push(constant);
                }
                OP_NEGATE => {
                    let constant = self.pop();
                    match constant {
                        Value::Number(number) => self.push(Value::Number(-number)),
                        _ => return InterpretResult::RuntimeError("Expected number".to_string()),
                    }
                }
                OP_ADD => {
                    let result = self.handle_add_operation(opcode);
                    
                }
                OP_SUBTRACT | OP_MULTIPLY | OP_DIVIDE | OP_GREATER | OP_LESS => {
                    let result = self.handle_binary_operation(opcode);
                }
                OP_TRUE => {
                    self.push(Value::Bool(true));
                }
                OP_FALSE => {
                    self.push(Value::Bool(false));
                }
                OP_NIL => {
                    self.push(Value::Nil);
                }
                OP_NOT => {
                    let constant = self.pop();
                    self.push(Value::Bool(!self.is_truthy(constant)));
                }
                OP_EQUAL => {
                    let b = self.pop();
                    let a = self.pop();
                    self.push(Value::Bool(self.is_equal(a, b)));
                }
                OP_PRINT => {
                    let value = self.pop();
                    self.print_value(value);
                    println!("");
                }
                OP_POP => {
                    self.pop();
                }
                OP_DEFINE_GLOBAL => {
                    let value = self.pop(); // value to be assigned
                    let key = match self.pop() {
                        Value::Obj(obj) => match obj.0.as_ref() {
                            Obj::String(string) => string.chars.clone(), 
                            _ => return InterpretResult::RuntimeError("Expected string".to_string()),
                        },
                        _ => return InterpretResult::RuntimeError("Expected string".to_string()),
                    };
                    self.globals.insert(key, value);
                }
                OP_GET_GLOBAL => {
                    let key = match self.pop() {
                        Value::Obj(obj) => match obj.0.as_ref() {
                            Obj::String(string) => string.chars.clone(), 
                            _ => return InterpretResult::RuntimeError("Expected string".to_string()),
                        },
                        _ => return InterpretResult::RuntimeError("Expected string".to_string()),
                    };
                    let value = match self.globals.get(&key) {
                        Some(value) => value.clone(),
                        None => return InterpretResult::RuntimeError("Undefined variable".to_string()),
                    };
                    self.push(value);
                }
                _ => {
                    return InterpretResult::RuntimeError("Unknown opcode".to_string());
                }
            }
        }
    }

    fn handle_add_operation(&mut self, _opcode: u8) -> Result<(), InterpretResult> {
        let b = self.pop();
        let a = self.pop();
        let result = match (a, b) {
            (Value::Number(a), Value::Number(b)) => Value::Number(a + b),
            (Value::Obj(a), Value::Obj(b)) => match (a.0.as_ref(), b.0.as_ref()) {
                (Obj::String(sa), Obj::String(sb)) => {
                    let mut s = String::with_capacity(sa.chars.len() + sb.chars.len());
                    s.push_str(&sa.chars);
                    s.push_str(&sb.chars);
                    let obj_ref = self.chunk.intern_string(&s);
                    Value::Obj(obj_ref)
                }
                _ => return Err(InterpretResult::RuntimeError("Operands must be two strings or two numbers".to_string())),
            },
            _ => return Err(InterpretResult::RuntimeError("Operands must be two strings or two numbers".to_string())),
        };
        self.push(result);
        Ok(())
    }

    fn handle_binary_operation(&mut self, opcode: u8) -> Result<(), InterpretResult> {
        let b = self.pop_number()?;
        let a = self.pop_number()?;
        let result = match opcode {
            OP_ADD => Value::Number(a + b),
            OP_SUBTRACT => Value::Number(a - b),
            OP_MULTIPLY => Value::Number(a * b),
            OP_DIVIDE => Value::Number(a / b),
            OP_GREATER => Value::Bool(a > b),
            OP_LESS => Value::Bool(a < b),
            _ => return Err(InterpretResult::RuntimeError("Unknown opcode".to_string())),
        };
        self.push(result);
        Ok(())
    }

    /**
        Push a value to the stack
    */
    pub fn push(&mut self, value: Value) {
        self.stack.push(value);
    }

    /**
        Pop a value from the stack and decrement the stack pointer
    */
    pub fn pop(&mut self) -> Value {
        if self.stack.is_empty() {
            panic!("Stack is empty");
        }
        self.stack.pop().unwrap()
    }

    /**
    Tries to pop the number from the stack
    */
    pub fn pop_number(&mut self) -> Result<f64, InterpretResult> {
        let value = self.pop();
        match value {
            Value::Number(number) => Ok(number),
            _ => Err(InterpretResult::RuntimeError("Expected number".to_string())),
        }
    }

    /**
     The false values are:
     - false
     - nil
     Everything else is true
    */
    fn is_truthy(&self, value: Value) -> bool {
        match value {
            Value::Bool(bool) => bool,
            Value::Nil => false,
            _ => true,
        }
    }

    /**
    Check if 2 values are equal
    */
    fn is_equal(&self, a: Value, b: Value) -> bool {
        match (a, b) {
            (Value::Number(a), Value::Number(b)) => a == b,
            (Value::Bool(a), Value::Bool(b)) => a == b,
            (Value::Obj(a), Value::Obj(b)) => a == b,
            (Value::Nil, Value::Nil) => true,
            _ => false,
        }
    }

    fn print_value(&self, value: Value) {
        match value {
            Value::Number(number) => print!("{}", number),
            Value::Bool(bool) => print!("{}", bool),
            Value::Nil => print!("nil"),
            Value::Obj(obj) => print!("{:#?}", obj),
            _ => print!("{:#?}", value),
        }
    }
}