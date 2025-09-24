

use crate::chunk::{Chunk, OP_CONSTANT, OP_RETURN, OP_NEGATE, OP_ADD, OP_SUBTRACT, OP_MULTIPLY, OP_DIVIDE, disassemble_opcode, Value};
use crate::compiler::{Compiler};

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

    pub fn interpret(&mut self, source: String) -> InterpretResult {
        let mut compiler = Compiler::new(source);
        if !compiler.compile() {
            return InterpretResult::CompileError;
        }
        self.chunk = compiler.chunk;
        self.ip = 0;
        self.run()
    }

    /**
        Run the VM
    */
    fn run(&mut self) -> InterpretResult {
        loop {
            let instruction = self.chunk.code[self.ip];
            self.ip += 1;
            let (opcode, value) = disassemble_opcode(instruction);

            match opcode {
                OP_RETURN => {
                    let value = self.pop();
                    println!("{}", value);
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
                    self.push(-constant);
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

    fn handle_binary_operation(&mut self, opcode: u8) {
        let b = self.pop();
        let a = self.pop();
        match opcode {
            OP_ADD => self.push(a + b),
            OP_SUBTRACT => self.push(a - b),
            OP_MULTIPLY => self.push(a * b),
            OP_DIVIDE => self.push(a / b),
            _ => (), // Unreachable.
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