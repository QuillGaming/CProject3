package parser.ASTNodes;

import lowlevel.*;
import lowlevel.Operand.OperandType;
import lowlevel.Operation.OperationType;

public class IfStmt extends Statement {
    Statement thenStmt;
    Statement elseStmt;

    public IfStmt(Expression express, Statement stmt1, Statement stmt2) {
        super(express);
        thenStmt = stmt1;
        elseStmt = stmt2;
    }

    public void printAST() {
        System.out.println("    If Statement");
        System.out.println("     Condition (Expression)");

        System.out.println("     Then Branch (Statement)");
        thenStmt.printAST();

        if (elseStmt != null) {
            System.out.println("     Else Branch (Statement)");
            elseStmt.printAST();
        }
    }

    @Override
    public void genLLCode(Function currFunc) {
        // 1 make 2/3 blocks
        BasicBlock thenBlock = new BasicBlock(currFunc);
        BasicBlock postBlock = new BasicBlock(currFunc);
        BasicBlock elseBlock = null;
        if (elseStmt != null) {
            elseBlock = new BasicBlock(currFunc);
        }

        // 2 gencode expr
        expr.genLLCode(currFunc.getCurrBlock(), false, 0);

        // Get register number
        int conditionRegNum = (Integer) currFunc.getCurrBlock().getLastOper().getDestOperand(0).getValue();

        // 3 create branch
        BasicBlock targetBlock = (elseStmt != null) ? elseBlock : postBlock;
        Operation branchOp = new Operation(OperationType.BEQ, currFunc.getCurrBlock());
        branchOp.setSrcOperand(0, new Operand(OperandType.REGISTER, Integer.valueOf(conditionRegNum)));
        branchOp.setSrcOperand(1, new Operand(OperandType.INTEGER, Integer.valueOf(0)));
        branchOp.setSrcOperand(2, new Operand(OperandType.BLOCK, Integer.valueOf(targetBlock.getBlockNum())));
        currFunc.getCurrBlock().appendOper(branchOp);

        // 4 append then
        currFunc.getLastBlock().setNextBlock(thenBlock);
        thenBlock.setPrevBlock(currFunc.getLastBlock());
        currFunc.setLastBlock(thenBlock);

        // 5 cb = then
        currFunc.setCurrBlock(thenBlock);

        // 6 codegen then
        thenStmt.genLLCode(currFunc);

        // 7 append post
        Operation jumpToPost = new Operation(OperationType.JMP, currFunc.getCurrBlock());
        jumpToPost.setSrcOperand(0, new Operand(OperandType.BLOCK, Integer.valueOf(postBlock.getBlockNum())));
        currFunc.getCurrBlock().appendOper(jumpToPost);

        // 8 - 11
        if (elseStmt != null) {
            currFunc.appendUnconnectedBlock(elseBlock);
            currFunc.setCurrBlock(elseBlock);
            elseStmt.genLLCode(currFunc);
            Operation jumpFromElse = new Operation(OperationType.JMP, currFunc.getCurrBlock());
            jumpFromElse.setSrcOperand(0, new Operand(OperandType.BLOCK, Integer.valueOf(postBlock.getBlockNum())));
            currFunc.getCurrBlock().appendOper(jumpFromElse);
        }

        // 12 cb = post
        currFunc.getLastBlock().setNextBlock(postBlock);
        postBlock.setPrevBlock(currFunc.getLastBlock());
        currFunc.setLastBlock(postBlock);
        currFunc.setCurrBlock(postBlock);
    }
}
