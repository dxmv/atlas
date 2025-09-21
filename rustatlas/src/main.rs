mod chunk;

use chunk::{Chunk, OpCode};

fn main() {
    let mut chunk = Chunk::new();
    let indx = chunk.add_constant(2.0);
    chunk.write(OpCode::Constant(indx),123);
    chunk.write(OpCode::Return,123);
    chunk.print();
}
