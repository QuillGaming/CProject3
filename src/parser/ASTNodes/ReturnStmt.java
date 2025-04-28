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
            expr.genLLCode(currBlock, firstItem, false, 0);
            Operation result = currBlock.getLastOper();

            // Add Operation to move expression result into return register
            Operation returnOp = new Operation(Operation.OperationType.ASSIGN, currBlock);
            Operand src = result.getDestOperand(0);
            returnOp.setSrcOperand(0, src);
            Operand dest = new Operand(Operand.OperandType.MACRO, "RetReg");
            returnOp.setDestOperand(0, dest);
            currBlock.appendOper(returnOp);
        }
        
        // Add jump Operation to exit block
        Operation jumpOp = new Operation(Operation.OperationType.JMP, currBlock);
        Operand target = new Operand(Operand.OperandType.BLOCK, returnBlock.getBlockNum());
        jumpOp.setSrcOperand(0, target);
        currBlock.appendOper(jumpOp);
    }
}
