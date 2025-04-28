package parser.ASTNodes;

import lowlevel.BasicBlock;
import lowlevel.Operation;

public class CallExpr extends Expression {
    String ID;
    Args args;

    public CallExpr(String i, Args a) {
        ID = i;
        args = a;
    }

    @Override
    public void printAST() {

    }

    @Override
    public void genLLCode(BasicBlock currBlock, boolean isRhs, int currIdx) {
        Operation callOper = new Operation(Operation.OperationType.CALL, currBlock);
        Operation nextOper = currBlock.getLastOper();
        Operation prevOper = nextOper.getPrevOper();
        Operation firstPass = args.genLLCode(currBlock);

        Operation prevPass = null;
        Operation lastPass = firstPass;
        while (lastPass != null) {
            prevPass = lastPass;
            lastPass = lastPass.getNextOper();
        }
        lastPass = prevPass;

        prevOper.setNextOper(firstPass);
        firstPass.setPrevOper(prevOper);

        lastPass.setNextOper(callOper);
        callOper.setPrevOper(lastPass);
        nextOper.setPrevOper(lastPass);
    }
}
