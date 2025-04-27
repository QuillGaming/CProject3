package parser.ASTNodes;

import lowlevel.BasicBlock;

public abstract class Expression {
    public abstract void printAST();

    public abstract Object genLLCode(BasicBlock currBlock);
}
