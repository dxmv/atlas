mod chunk;
mod vm;

use chunk::{Chunk, OP_ADD, OP_SUBTRACT, OP_MULTIPLY, OP_DIVIDE, OP_RETURN, make_negate_instruction, make_constant_instruction};
use vm::VM;

fn main() {
    let mut chunk = Chunk::new();
    let indx = chunk.add_constant(4.0);
    let val1 = make_constant_instruction(indx);
    chunk.write(val1,123);
    let indx = chunk.add_constant(5.0);
    let val2 = make_constant_instruction(indx);
    chunk.write(val2,123);
    chunk.write(OP_ADD,123);
    chunk.write(OP_RETURN,123);
    chunk.print();
    println!("Running VM");
    let mut vm = VM::new();
    vm.chunk = chunk;
    vm.run();
    println!("VM finished");
}
