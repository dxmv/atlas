pub const OP_RETURN: u8 = 0x00;
pub const OP_CONSTANT: u8 = 0x01;

type Value = f64;

pub struct Chunk {
    code: Vec<u8>,
    constants: Vec<Value>,
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

    pub fn print(&self) {
        println!("== Chunk ==");
        for (offset, code) in self.code.iter().enumerate() {
            print!("{:04} ", offset);
            let line = self.lines[offset];
            print!("{:<4}", line);
            if (code & 0x03) == OP_RETURN {
                println!("OP_RETURN");
                continue;
            }
            if (code & 0x03) == OP_CONSTANT {
                let constant_index = code >> 2;  // Extract upper 6 bits
                let constant_value = self.constants[constant_index as usize];
                println!("OP_CONSTANT {} '{}'", constant_index, constant_value);
                continue;
            }
            println!("OP_UNKNOWN");
        }
    }
}
