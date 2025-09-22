mod chunk;
mod vm;

use chunk::{Chunk, OP_RETURN, make_constant_instruction};
use vm::VM;

fn main() {
    let mut chunk = Chunk::new();
    let indx = chunk.add_constant(1.45);
    let constant = make_constant_instruction(indx);
    chunk.write(constant,123);
    chunk.write(OP_RETURN,123);
    chunk.print();
    println!("Running VM");
    let mut vm = VM::new();
    vm.chunk = chunk;
    vm.run();
    println!("VM finished");
}
