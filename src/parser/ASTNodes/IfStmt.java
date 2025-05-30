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
    public void genLLCode(Function currFunc, CodeItem firstItem) {
        // 1 make 2/3 blocks
        BasicBlock thenBlock = new BasicBlock(currFunc);
        BasicBlock postBlock = new BasicBlock(currFunc);
        BasicBlock elseBlock = null;
        if (elseStmt != null) {
            elseBlock = new BasicBlock(currFunc);
        }

        // 2 gencode expr
        Operation binop = new Operation(OperationType.UNKNOWN, currFunc.getCurrBlock());
        binop.setDestOperand(0, new Operand(OperandType.REGISTER, currFunc.getNewRegNum()));
        currFunc.getCurrBlock().appendOper(binop);
        expr.genLLCode(currFunc.getCurrBlock(), firstItem, 0);

        // Get register number
        int conditionRegNum = (Integer) currFunc.getCurrBlock().getLastOper().getDestOperand(0).getValue();

        // 3 create branch
        BasicBlock targetBlock = (elseStmt != null) ? elseBlock : postBlock;
        Operation branchOp = new Operation(OperationType.BEQ, currFunc.getCurrBlock());
        branchOp.setSrcOperand(0, new Operand(OperandType.REGISTER, conditionRegNum));
        branchOp.setSrcOperand(1, new Operand(OperandType.INTEGER, 0));
        branchOp.setSrcOperand(2, new Operand(OperandType.BLOCK, targetBlock.getBlockNum()));
        currFunc.getCurrBlock().appendOper(branchOp);

        // 4 append then
        currFunc.appendToCurrentBlock(thenBlock);

        // 5 cb = then
        currFunc.setCurrBlock(thenBlock);

        // 6 codegen then
        thenStmt.genLLCode(currFunc, firstItem);

        // 7 append post
        currFunc.appendToCurrentBlock(postBlock);

        // 8 - 11
        if (elseBlock != null) {
            // 8 cb = else
            currFunc.setCurrBlock(elseBlock);

            // 9 codegen else
            elseStmt.genLLCode(currFunc, firstItem);

            // 10 append jmp to post
            Operation jumpToPost = new Operation(OperationType.JMP, currFunc.getCurrBlock());
            jumpToPost.setSrcOperand(0, new Operand(OperandType.BLOCK, Integer.valueOf(postBlock.getBlockNum())));
            thenBlock.appendOper(jumpToPost);

            // 11 append elseblock
            currFunc.appendUnconnectedBlock(elseBlock);

            thenBlock.setNextBlock(elseBlock);
            elseBlock.setPrevBlock(thenBlock);
            elseBlock.setNextBlock(postBlock);
            postBlock.setPrevBlock(elseBlock);
            //*/
        }

        // 12 cb = post
        currFunc.setCurrBlock(postBlock);
    }
}
