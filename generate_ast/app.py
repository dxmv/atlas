import os

GENERATE_FILE="generate.txt"
FOLDER_PATH = "./test"
PACKAGE_NAME = "ast"

def write_package_name(f):
        '''
        Writes package name as first line
        '''
        f.write(f"package {PACKAGE_NAME}; ")


def generate_root():
        '''
        Generates the abstract Expr class
        '''
        with open(f"{FOLDER_PATH}/Expr.java","w") as f:
                write_package_name(f)
                f.write("\n\npublic abstract class Expr{\n")
                f.write("}\n")

def generate_class(line:str):
        '''
        Generate a class, based on the line from file
        line format:
        NAME: ARG_TYPE ARG_NAME, ARG_TYPE ARG_NAME ...
        '''
        name,args = line.split(":")
        name = name.strip()
        args = args.strip()
        class_name = f"{name}Expr"
        complete_args = [arg.strip() for arg in args.split(",")]
        # crete a file
        with open(f"{FOLDER_PATH}/{class_name}.java","w") as f:
                write_package_name(f)
                f.write(f"\n\npublic class {class_name} extends Expr{{\n")
                # generate fields
                for arg in complete_args:
                        f.write(f"\tprivate {arg};\n")
                f.write("\n\n\n")
                # generate constructor
                f.write(f"\tpublic {class_name} ({args}){{\n")
                for arg in complete_args:
                        name = arg.split(" ")[1].strip()
                        f.write(f"\t\tthis.{name} = {name};\n")
                f.write(f"\t}}\n")
                # eof
                f.write("}\n")


def main():
        # create a folder if needed
        try:
                os.mkdir(FOLDER_PATH)
        except FileExistsError:
                print(f"Folder {FOLDER_PATH} aleady exists")
        # generate super class
        generate_root()
        # open the generation file and for each line generate a file
        with open(GENERATE_FILE,"r") as f:
                for line in f.readlines():
                        generate_class(line)
        
        
        

if __name__ == "__main__":
        main()