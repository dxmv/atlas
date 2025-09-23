mod chunk;
mod vm;
mod scanner;
mod token;
mod compiler;

// file stuff
use std::fs::File;
use std::io::prelude::*;
use std::path::Path;

use compiler::Compiler;
use chunk::Chunk;

fn main() {
    let path = Path::new("./test.txt");

    let mut file = match File::open(path) {
        Ok(file) => file,
        Err(e) => panic!("Error opening file: {}", e),
    };

    let mut source = String::new();
    match file.read_to_string(&mut source) {
        Ok(_) => println!("Read file successfully"),
        Err(e) => panic!("Error reading file: {}", e),
    }

    let mut compiler = Compiler::new(source);
    let mut chunk = Chunk::new();
    let success = compiler.compile(&mut chunk);
    if !success {
        println!("Compilation failed");
        return;
    }
    chunk.disassemble("chunk");
}
