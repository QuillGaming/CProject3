package parser.ASTNodes;

import lowlevel.*;

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
    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, boolean isRhs, int currIdx) {
        Operation callOper = new Operation(Operation.OperationType.CALL, currBlock);
        callOper.addAttribute(new Attribute("numParams", args.size() + ""));
        Operation firstPass = args.genLLCode(currBlock, firstItem);
        currBlock.appendOper(callOper);
    }
}
