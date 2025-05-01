package parser.ASTNodes;

import lowlevel.*;

public class NumExpr extends Expression {
    int num;

    public NumExpr(int n) {
        num = n;
    }

    public int getNum() {
        return num;
    }

    @Override
    public void printAST() {

    }

    @Override
    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, int currDestIdx) {
        Operation lastOper = currBlock.getLastOper();
        lastOper.setSrcOperand(currDestIdx, new Operand(Operand.OperandType.INTEGER, num));
    }
}
