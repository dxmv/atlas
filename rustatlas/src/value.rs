use std::rc::Rc;

#[derive(Debug, Clone)]
pub enum Value {
    Bool(bool),
    Nil,
    Number(f64),
    Obj(ObjRef),
}

#[derive(Debug, Clone)]
pub struct ObjRef(pub Rc<Obj>);

#[derive(Debug)]
pub enum Obj {
    String(ObjString),
}

#[derive(Debug)]
pub struct ObjString {
    pub chars: Box<str>,
}


