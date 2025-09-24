

use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, OP_NEGATE, OP_ADD, OP_SUBTRACT, OP_MULTIPLY, OP_DIVIDE, disassemble_opcode, Value};

pub enum InterpretResult {
    Ok,
    CompileError,
    RuntimeError,
}

pub struct VM {
    pub chunk: Chunk,
    pub stack: Vec<Value>,
    ip: usize,
}

impl VM {
    pub fn new() -> Self {
        VM { chunk: Chunk::new(), ip: 0, stack: vec![] }
    }

    /**
        Run the VM
    */
    pub fn run(&mut self) -> InterpretResult {
        loop {
            let instruction = self.chunk.code[self.ip];
            self.ip += 1;
            let (opcode, value) = disassemble_opcode(instruction);

            match opcode {
                OP_RETURN => {
                    let value = self.pop();
                    return InterpretResult::Ok;
                }
                OP_CONSTANT => {
                    let constant_index = value;
                    let constant = self.chunk.constants[constant_index as usize];
                    self.push(constant);
                }
                OP_NEGATE => {
                    let constant_index = value;
                    let constant = self.chunk.constants[constant_index as usize];
                    match constant {
                        Value::Number(number) => self.push(Value::Number(-number)),
                        _ => return InterpretResult::RuntimeError,
                    }
                }
                OP_ADD | OP_SUBTRACT | OP_MULTIPLY | OP_DIVIDE => {
                    self.handle_binary_operation(opcode);
                }
                _ => {
                    return InterpretResult::RuntimeError;
                }
            }
        }
    }

    fn handle_binary_operation(&mut self, opcode: u8) -> Result<(), InterpretResult> {
        let b = self.pop_number()?;
        let a = self.pop_number()?;
        let result = match opcode {
            OP_ADD => Value::Number(a + b),
            OP_SUBTRACT => Value::Number(a - b),
            OP_MULTIPLY => Value::Number(a * b),
            OP_DIVIDE => Value::Number(a / b),
            _ => return Err(InterpretResult::RuntimeError),
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
            _ => Err(InterpretResult::RuntimeError),
        }
    }
}