#[derive(Debug)]
pub enum OpCode {
    OpReturn,
}

#[derive(Debug)]
pub struct Chunk {
    code: Vec<OpCode>,
}

impl Chunk {
    pub fn new() -> Self {
        Chunk { code: vec![] }
    }

    pub fn write(&mut self, code: OpCode) {
        self.code.push(code);
    }

    pub fn print(&self, ) {
        println!("== Chunk ==");
        for code in &self.code{
            if matches!(code,OpCode::OpReturn) {
                println!("return");
            }
            else{
                println!("unknown");
            }
        }
    }
}
