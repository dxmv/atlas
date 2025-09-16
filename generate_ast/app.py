import os

GENERATE_FILE="generate.txt"
FOLDER_PATH = "../jatlas/ast"
PACKAGE_NAME = "ast"

def write_package_name(f):
        '''
        Writes package name as first line
        '''
        f.write(f"package {PACKAGE_NAME}; \n\nimport tokenizer.Token;\n")


def generate_root(name:str):
        '''
        Generates the abstract class
        '''
        with open(f"{FOLDER_PATH}/{name}.java","w") as f:
                write_package_name(f)
                f.write(f"\n\npublic abstract class {name}{{\n")
                f.write(f"\tabstract <R> R accept(Visitor<R> visitor);\n")
                f.write("}\n")

def generate_class(base:str,class_name:str,args:str):
        '''
        Generate a class, based on the line from file
        line format:
        NAME: ARG_TYPE ARG_NAME, ARG_TYPE ARG_NAME ...
        '''
        complete_args = [arg.strip() for arg in args.split(",")]
        # crete a file
        with open(f"{FOLDER_PATH}/{class_name}.java","w") as f:
                write_package_name(f)
                f.write(f"\n\npublic class {class_name} extends {base}{{\n")
                # generate fields
                for arg in complete_args:
                        f.write(f"\tpublic {arg};\n")
                f.write("\n\n\n")
                # generate constructor
                f.write(f"\tpublic {class_name} ({args}){{\n")
                for arg in complete_args:
                        name = arg.split(" ")[1].strip()
                        f.write(f"\t\tthis.{name} = {name};\n")
                f.write(f"\t}}\n")

                # write abstract method
                f.write(f"\n\t@Override\n")
                f.write(f"\n\t<R> R accept(Visitor<R> visitor) {{\n")
                f.write(f"\t\treturn visitor.visit{class_name}(this);\n")
                f.write(f"\t}}\n")
                # eof
                f.write("}\n")

def generate_visitor(class_names):
        '''
        Generates the visitor interface
        '''
        with open(f"{FOLDER_PATH}/Visitor.java","w") as f:
                write_package_name(f)
                f.write("\n\npublic interface Visitor<R>{\n\n")
                for name in class_names:
                        f.write(f"\tR visit{name}({name} expr);\n")
                f.write("\n}\n")

def main():
        # create a folder if needed
        try:
                os.mkdir(FOLDER_PATH)
        except FileExistsError:
                print(f"Folder {FOLDER_PATH} aleady exists")
        # generate super class
        generate_root("Expr")
        generate_root("Stmt")
        # generatoes visitor interface
        # open the generation file and for each line generate a file
        with open(GENERATE_FILE,"r") as f:
                class_names = []
                for line in f.readlines():
                        normal_name,base = line.split(";")
                        base = base.strip()
                        name,args = normal_name.split(":")
                        name = name.strip()
                        args = args.strip()
                        class_name = f"{name}{base}"
                        generate_class(base,class_name,args)
                        class_names.append(class_name)
        # generate visitor
        generate_visitor(class_names)
        
        
        

if __name__ == "__main__":
        main()