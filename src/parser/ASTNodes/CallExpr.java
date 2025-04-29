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
    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, int trash) {
        Operation callOper = new Operation(Operation.OperationType.CALL, currBlock);
        callOper.setDestOperand(0, new Operand(Operand.OperandType.STRING, ID));
        callOper.addAttribute(new Attribute("numParams", args.size() + ""));
        args.genLLCode(currBlock, firstItem);
        currBlock.appendOper(callOper);
    }
}
