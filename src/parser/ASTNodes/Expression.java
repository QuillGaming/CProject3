package parser.ASTNodes;

import lowlevel.BasicBlock;
import lowlevel.CodeItem;

public abstract class Expression {
    public abstract void printAST();

    public abstract void genLLCode(BasicBlock currBlock, CodeItem firstItem, int currDestIdx);
}
