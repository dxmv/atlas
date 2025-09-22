

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
                // get the instruction, move the ip forward
                let insturction = self.chunk.code[self.ip];
                self.ip += 1;
                let (opcode,value) = disassemble_opcode(insturction);
                // handle the instruction
                if opcode == OP_RETURN {
                        let value = self.pop();
                        println!("{}", value);
                        return InterpretResult::Ok;
                }
                if opcode == OP_CONSTANT {
                        let constant_index = value; 
                        let constant_value = self.chunk.constants[constant_index as usize];
                        self.push(constant_value);
                        continue;
                }
                if opcode == OP_NEGATE {
                        let constant_index = value; 
                        let constant_value = self.chunk.constants[constant_index as usize];
                        self.push(-constant_value);
                        continue;
                }
                if opcode == OP_ADD {
                        self.handle_binary_operation(opcode);
                        continue;
                }
                if opcode == OP_SUBTRACT {
                        self.handle_binary_operation(opcode);
                        continue;
                }
                if opcode == OP_MULTIPLY {
                        self.handle_binary_operation(opcode);
                        continue;
                }
                if opcode == OP_DIVIDE {
                        self.handle_binary_operation(opcode);
                        continue;
                }
                return InterpretResult::RuntimeError;
        }
    }

    pub fn handle_binary_operation(&mut self, opcode: u8) {
        let value1 = self.pop();
        let value2 = self.pop();
        match opcode {
            OP_ADD => self.push(value1 + value2),
            OP_SUBTRACT => self.push(value1 - value2),
            OP_MULTIPLY => self.push(value1 * value2),
            OP_DIVIDE => self.push(value1 / value2),
            _ => panic!("Invalid opcode"),
        }
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
}