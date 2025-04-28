package parser.ASTNodes;

import lowlevel.BasicBlock;
import lowlevel.CodeItem;
import lowlevel.Operand;
import lowlevel.Operation;

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
    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, boolean isRhs, int currIdx) {
        if (currIdx == -1) {
            Operation dummyOper = new Operation(Operation.OperationType.UNKNOWN, currBlock);
            dummyOper.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, num));
            currBlock.appendOper(dummyOper);
            return;
        }

        Operation lastOper = currBlock.getLastOper();
        lastOper.setSrcOperand(currIdx, new Operand(Operand.OperandType.INTEGER, num));
    }
}
