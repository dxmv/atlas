mod chunk;
mod vm;
mod scanner;

// file stuff
use std::fs::File;
use std::io::prelude::*;
use std::path::Path;

use chunk::{Chunk, OP_ADD, OP_SUBTRACT, OP_MULTIPLY, OP_DIVIDE, OP_RETURN, make_negate_instruction, make_constant_instruction};
use vm::VM;
use scanner::Scanner;

fn main() {
    let path = Path::new("./test.txt");
    let display = path.display();

    let mut file = match File::open(path) {
        Ok(file) => file,
        Err(e) => panic!("Error opening file: {}", e),
    };

    let mut source = String::new();
    match file.read_to_string(&mut source) {
        Ok(_) => println!("Read file succesffully"),
        Err(e) => panic!("Error reading file: {}", e),
    }
    let mut scanner = Scanner::new(source);
    
}
