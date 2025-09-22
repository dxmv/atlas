

use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, disassemble_opcode, Value};


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
                return InterpretResult::RuntimeError;
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