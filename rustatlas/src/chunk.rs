#[derive(Debug)]
pub enum OpCode {
    Return,
    Constant(usize),
}

type Value = f64;

#[derive(Debug)]
pub struct Chunk {
    code: Vec<OpCode>,
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

    pub fn write(&mut self, code: OpCode, line: usize) {
        self.code.push(code);
        self.lines.push(line);
    }

    pub fn add_constant(&mut self, num: Value) -> usize {
        self.constants.push(num);
        self.constants.len() - 1
    }

    pub fn print(&self) {
        println!("== Chunk ==");

        for (offset, code) in self.code.iter().enumerate() {
            print!("{:04} ", offset);
            let line = self.lines[offset];
            print!("{:<4}", line);
            match code {
                OpCode::Return => {
                    println!("OP_RETURN");
                }
                OpCode::Constant(index) => {
                    let value = self.constants[*index];
                    println!("OP_CONSTANT {} '{}'", index, value);
                }
            }
        }
    }
}
