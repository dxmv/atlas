use std::rc::Rc;

#[derive(Debug, Clone)]
pub enum Value {
    Bool(bool),
    Nil,
    Number(f64),
    Obj(ObjRef),
}

#[derive(Debug, Clone, PartialEq)]
pub struct ObjRef(pub Rc<Obj>);


#[derive(Debug, Eq, PartialEq)]
pub enum Obj {
    String(ObjString),
}


#[derive(Debug, Eq, PartialEq)]
pub struct ObjString {
    pub chars: Box<str>,
}


