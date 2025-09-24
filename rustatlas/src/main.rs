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
use vm::VM;

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
    let success = compiler.compile();
    if !success {
        println!("Compilation failed");
        return;
    }
    compiler.chunk.disassemble("chunk");
    let mut vm = VM::new();
    vm.chunk = compiler.chunk;
    vm.run();
}
