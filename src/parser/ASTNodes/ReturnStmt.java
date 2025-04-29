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
            if (expr instanceof BinopExpr) {
                Operation binop = new Operation(Operation.OperationType.UNKNOWN, currBlock);
                Operand binopValue = new Operand(Operand.OperandType.REGISTER, currFunc.getNewRegNum());
                binop.setDestOperand(0, binopValue);
                currBlock.appendOper(binop);
                expr.genLLCode(currBlock, firstItem, 0);

                Operation assignOper = new Operation(Operation.OperationType.ASSIGN, currBlock);
                assignOper.setSrcOperand(0, binopValue);
                assignOper.setDestOperand(0, new Operand(Operand.OperandType.MACRO,"RetReg"));
                currBlock.appendOper(assignOper);
            }
            else if (expr instanceof CallExpr) {
                expr.genLLCode(currBlock, firstItem, 0);
            }
            else {
                Operation assignOper = new Operation(Operation.OperationType.ASSIGN, currBlock);
                assignOper.setDestOperand(0, new Operand(Operand.OperandType.MACRO,"RetReg"));
                currBlock.appendOper(assignOper);
                expr.genLLCode(currBlock, firstItem, 0);
            }
        }
        
        // Add jump Operation to exit block
        Operation jumpOp = new Operation(Operation.OperationType.JMP, currBlock);
        Operand target = new Operand(Operand.OperandType.BLOCK, returnBlock.getBlockNum());
        jumpOp.setSrcOperand(0, target);
        currBlock.appendOper(jumpOp);
    }
}
