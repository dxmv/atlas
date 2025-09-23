pub const OP_RETURN: u8 = 0x00;
pub const OP_CONSTANT: u8 = 0x01;
pub const OP_NEGATE: u8 = 0x02;
pub const OP_ADD: u8 = 0x03;
pub const OP_SUBTRACT: u8 = 0x04;
pub const OP_MULTIPLY: u8 = 0x05;
pub const OP_DIVIDE: u8 = 0x06;

pub type Value = f64;

// Utility functions for bytecode manipulation
pub fn disassemble_opcode(byte: u8) -> (u8,u8) {
    let opcode = byte & 0x03;
    let value = byte >> 2;
    (opcode,value)
}

pub struct Chunk {
    pub code: Vec<u8>,
    pub constants: Vec<Value>,
    lines: Vec<usize>,
}

impl Chunk {
    pub fn new() -> Self {
        Chunk { 
            code: vec![],
            constants:vec![],
            lines:vec![], 
        }
    }

    pub fn write(&mut self, code: u8, line: usize) {
        self.code.push(code);
        self.lines.push(line);
    }

    pub fn add_constant(&mut self, num: Value) -> u8 {
        self.constants.push(num);
        return (self.constants.len() - 1) as u8
    }

    pub fn disassemble(&self, name: &str) {
        println!("== {} ==", name);
        for (offset, code) in self.code.iter().enumerate() {
            print!("{:04} ", offset);
            if offset > 0 && self.lines[offset] == self.lines[offset - 1] {
                print!("   | ");
            } else {
                print!("{:<4} ", self.lines[offset]);
            }
            
            let (opcode,value) = disassemble_opcode(*code);
            match opcode {
                OP_RETURN => println!("OP_RETURN"),
                OP_CONSTANT => {
                    let constant_index = value;
                    let constant_value = self.constants[constant_index as usize];
                    println!("OP_CONSTANT {} '{}'", constant_index, constant_value);
                }
                OP_NEGATE => {
                     let constant_index = value;
                     let constant_value = - self.constants[constant_index as usize];
                     println!("OP_NEGATE {} '{}'", constant_index, constant_value);
                }
                OP_ADD => println!("OP_ADD"),
                OP_SUBTRACT => println!("OP_SUBTRACT"),
                OP_MULTIPLY => println!("OP_MULTIPLY"),
                OP_DIVIDE => println!("OP_DIVIDE"),
                _ => println!("Unknown opcode {}", opcode),
            }
        }
    }
}



