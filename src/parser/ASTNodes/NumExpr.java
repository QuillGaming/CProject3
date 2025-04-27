package parser.ASTNodes;

import lowlevel.BasicBlock;
import lowlevel.Operand;

public class NumExpr extends Expression {
    int num;

    public NumExpr(int n) {
        num = n;
    }

    @Override
    public void printAST() {

    }

    @Override
    public Operand genLLCode(BasicBlock currBlock) {
        return new Operand(Operand.OperandType.INTEGER, num);
    }
}
