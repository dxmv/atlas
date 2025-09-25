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
impl PartialEq for ObjRef {
    fn eq(&self, other: &Self) -> bool {
        Rc::ptr_eq(&self.0, &other.0)
    }
}


#[derive(Debug, Eq, PartialEq)]
pub enum Obj {
    String(ObjString),
}


#[derive(Debug, Eq, PartialEq)]
pub struct ObjString {
    pub chars: Box<str>,
}


