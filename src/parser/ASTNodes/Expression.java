package parser.ASTNodes;

import lowlevel.BasicBlock;
import lowlevel.CodeItem;
import lowlevel.Operation;

public abstract class Expression {
    public abstract void printAST();

    public abstract void genLLCode(BasicBlock currBlock, CodeItem firstItem, boolean isRhs, int currIdx);
}
