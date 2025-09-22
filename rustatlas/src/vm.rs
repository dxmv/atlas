

use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, get_opcode};


pub enum InterpretResult {
    Ok,
    CompileError,
    RuntimeError,
}

pub struct VM {
    pub chunk: Chunk,
    ip: usize,
}

impl VM {
    pub fn new() -> Self {
        VM { chunk: Chunk::new(), ip: 0 }
    }

    pub fn run(&mut self) -> InterpretResult {
        loop {
                // get the instruction, move the ip forward
                let insturction = self.chunk.code[self.ip];
                self.ip += 1;
                let opcode = get_opcode(insturction);
                // handle the instruction
                if opcode == OP_RETURN {
                    return InterpretResult::Ok;
                }
                if opcode == OP_CONSTANT {
                        let constant_index = insturction >> 2;  // Extract upper 6 bits
                        let constant_value = self.chunk.constants[constant_index as usize];
                    println!("{}\n", constant_value);
                    continue;
                }
                return InterpretResult::RuntimeError;
        }
    }
}