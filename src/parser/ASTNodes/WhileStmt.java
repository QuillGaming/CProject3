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
        // 1 make blocks
        BasicBlock bodyBlock = new BasicBlock(currFunc);
        BasicBlock postBlock = new BasicBlock(currFunc);

        // 2 gencode expr
        expr.genLLCode(currFunc.getCurrBlock(), firstItem, 0);

        // Get register number
        int conditionRegNum = (Integer) currFunc.getCurrBlock().getLastOper().getDestOperand(0).getValue();

        // 3 create branch
        Operation branchOp = new Operation(Operation.OperationType.BNE, currFunc.getCurrBlock());
        branchOp.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, conditionRegNum));
        branchOp.setSrcOperand(1, new Operand(Operand.OperandType.INTEGER, 0));
        branchOp.setSrcOperand(2, new Operand(Operand.OperandType.BLOCK, postBlock.getBlockNum()));
        currFunc.getCurrBlock().appendOper(branchOp);

        // 4 append body
        currFunc.appendToCurrentBlock(bodyBlock);

        // 5 cb = body
        currFunc.setCurrBlock(bodyBlock);

        // 6 codegen body
        stmt.genLLCode(currFunc, firstItem);

        // 7 append post
        currFunc.appendToCurrentBlock(postBlock);

        // 8 cb = post
        currFunc.setCurrBlock(postBlock);
    }
}
