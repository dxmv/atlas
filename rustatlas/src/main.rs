mod chunk;

use chunk::{Chunk, OP_CONSTANT, OP_RETURN};

fn main() {
    let mut chunk = Chunk::new();
    let indx = chunk.add_constant(2.0);
    let constant = indx << 2 | OP_CONSTANT;
    chunk.write(constant,123);
    chunk.write(OP_RETURN,123);
    chunk.print();
}
