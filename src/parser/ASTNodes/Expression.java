package parser.ASTNodes;

import lowlevel.BasicBlock;
import lowlevel.Operation;

public abstract class Expression {
    public abstract void printAST();

    public abstract void genLLCode(BasicBlock currBlock, boolean isRhs, int currIdx);
}
