pub struct Scanner {
    pub source: String,
    pub current: usize,
    pub start: usize,
    pub line: usize
}

impl Scanner{
        pub fn new(source:String) -> Self {
                Scanner{
                        source,
                        current:0,
                        start:0,
                        line:1,
                }
        }
}