package parser.ASTNodes;

import lowlevel.*;

public class ReturnStmt extends Statement {
    public ReturnStmt(Expression e) {
        super(e);
    }

    @Override
    public void printAST() {
        System.out.println("    Return Statement");
        System.out.println("     Expression");

    }

    @Override
    public void genLLCode(Function currFunc, CodeItem firstItem) {
        BasicBlock returnBlock = currFunc.getReturnBlock();
        if (returnBlock == null) {
            returnBlock = currFunc.genReturnBlock();
        }
        BasicBlock currBlock = currFunc.getCurrBlock();
        
        // If it returns an expression, call genCode on the Expr
        if (expr != null) {
            expr.genLLCode(currBlock, firstItem, 0);
            Operation result = currBlock.getLastOper();

            Operation moveOper = new Operation(Operation.OperationType.ASSIGN, currBlock);
            moveOper.setDestOperand(0, returnBlock.getLastOper().getSrcOperand(0));
            moveOper.setSrcOperand(0, result.getDestOperand(0));
            currBlock.appendOper(moveOper);
        }
        
        // Add jump Operation to exit block
        Operation jumpOp = new Operation(Operation.OperationType.JMP, currBlock);
        Operand target = new Operand(Operand.OperandType.BLOCK, returnBlock.getBlockNum());
        jumpOp.setSrcOperand(0, target);
        currBlock.appendOper(jumpOp);
    }
}
