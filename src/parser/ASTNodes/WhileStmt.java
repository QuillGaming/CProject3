package parser.ASTNodes;

import lowlevel.*;

public class WhileStmt extends Statement {
    Statement stmt;

    public WhileStmt(Expression e, Statement s) {
        super(e);
        stmt = s;
    }

    @Override
    public void printAST() {
        System.out.println("    While Statement");
        System.out.println("    Condition (Expression)");


        System.out.println("     Body (Statement)");
        stmt.printAST();
    }

    @Override
    public void genLLCode(Function currFunc, CodeItem firstItem) {
        BasicBlock bodyBlock = new BasicBlock(currFunc);
        BasicBlock postBlock = new BasicBlock(currFunc);

        expr.genLLCode(currFunc.getCurrBlock(), firstItem, false, 0);

        int conditionRegNum = (Integer) currFunc.getCurrBlock().getLastOper().getDestOperand(0).getValue();

        Operation branchOp = new Operation(Operation.OperationType.BNE, currFunc.getCurrBlock());
        branchOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionRegNum));
        branchOp.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
        branchOp.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, postBlock.getBlockNum()));
        currFunc.getCurrBlock().appendOper(branchOp);

        currFunc.getLastBlock().setNextBlock(bodyBlock);
        bodyBlock.setPrevBlock(currFunc.getLastBlock());
        currFunc.setLastBlock(bodyBlock);
        currFunc.setCurrBlock(bodyBlock);

        stmt.genLLCode(currFunc, firstItem);

        Operation jumpOp = new Operation(Operation.OperationType.JMP, bodyBlock);
        Operand target = new Operand(Operand.OperandType.BLOCK, bodyBlock.getBlockNum());
        jumpOp.setSrcOperand(0, target);
        bodyBlock.appendOper(jumpOp);

        /*
        Operation jumpToPost = new Operation(Operation.OperationType.JMP, currFunc.getCurrBlock());
        jumpToPost.setSrcOperand(0, new Operand(Operand.OperandType.BLOCK, Integer.valueOf(postBlock.getBlockNum())));
        currFunc.getCurrBlock().appendOper(jumpToPost);
        */

        currFunc.getLastBlock().setNextBlock(postBlock);
        postBlock.setPrevBlock(currFunc.getLastBlock());
        currFunc.setLastBlock(postBlock);
        currFunc.setCurrBlock(postBlock);
    }
}
