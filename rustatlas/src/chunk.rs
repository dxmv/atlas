use crate::value::{Value, ObjString, ObjRef, Obj};
use std::rc::Rc;
use std::collections::HashMap;

// opcodes
pub const OP_RETURN: u8 = 0x00;
pub const OP_CONSTANT: u8 = 0x01;
pub const OP_NEGATE: u8 = 0x02;
pub const OP_ADD: u8 = 0x03;
pub const OP_SUBTRACT: u8 = 0x04;
pub const OP_MULTIPLY: u8 = 0x05;
pub const OP_DIVIDE: u8 = 0x06;
pub const OP_TRUE: u8 = 0x07;
pub const OP_FALSE: u8 = 0x08;
pub const OP_NIL: u8 = 0x09;
pub const OP_NOT: u8 = 0x0A;
pub const OP_EQUAL: u8 = 0x0B;
pub const OP_GREATER: u8 = 0x0C;
pub const OP_LESS: u8 = 0x0D;
pub const OP_PRINT: u8 = 0x0E;
pub const OP_POP: u8 = 0x0F;
pub const OP_DEFINE_GLOBAL: u8 = 0x10;
pub const OP_GET_GLOBAL: u8 = 0x11;



pub struct Chunk {
    pub code: Vec<u8>,
    pub constants: Vec<Value>,
    pub interned_strings: HashMap<String, ObjRef>,
    lines: Vec<usize>,
}

impl Chunk {
    pub fn new() -> Self {
        Chunk { 
            code: vec![],
            constants:vec![],
            lines:vec![], 
            interned_strings: HashMap::new(),
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

    pub fn disassemble(&self, name: &str) {
        println!("==== {} ====", name);
        let mut offset: usize = 0;
        while offset < self.code.len() {
            print!("{:04} | ", offset);

            let line = self.lines[offset];
            print!("{:04} |", line);

            let opcode = self.code[offset];
            offset += 1;
            match opcode {
                OP_RETURN => println!("OP_RETURN"),
                OP_CONSTANT => {
                    if offset >= self.code.len() {
                        println!("OP_CONSTANT <missing operand>");
                        break;
                    }
                    let constant_index = self.code[offset];
                    offset += 1;
                    let constant_value = self.constants[constant_index as usize].clone();
                    println!("OP_CONSTANT {} {:?}", constant_index, constant_value);
                }
                OP_NEGATE => {
                    println!("OP_NEGATE");
                }
                OP_ADD => println!("OP_ADD"),
                OP_SUBTRACT => println!("OP_SUBTRACT"),
                OP_MULTIPLY => println!("OP_MULTIPLY"),
                OP_DIVIDE => println!("OP_DIVIDE"),
                OP_TRUE => println!("OP_TRUE"),
                OP_FALSE => println!("OP_FALSE"),
                OP_NIL => println!("OP_NIL"),
                OP_NOT => println!("OP_NOT"),
                OP_EQUAL => println!("OP_EQUAL"),
                OP_GREATER => println!("OP_GREATER"),
                OP_LESS => println!("OP_LESS"),
                OP_PRINT => println!("OP_PRINT"),
                OP_POP => println!("OP_POP"),
                OP_DEFINE_GLOBAL => println!("OP_DEFINE_GLOBAL"),
                OP_GET_GLOBAL => println!("OP_GET_GLOBAL"),
                other => println!("Unknown opcode {}", other),
            }
        }
    }

    pub fn intern_string(&mut self, string:&str) -> ObjRef {
        if let Some(obj_ref) = self.interned_strings.get(string) {
            return obj_ref.clone();
        }
        let obj_string = ObjString{chars: string.to_string().into_boxed_str()};
        let obj_ref = ObjRef(Rc::new(Obj::String(obj_string)));
        self.interned_strings.insert(string.to_string(), obj_ref.clone());
        obj_ref
    }
}



