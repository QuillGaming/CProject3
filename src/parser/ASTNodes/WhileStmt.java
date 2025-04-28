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
    public void genLLCode(Function currItem) {
        BasicBlock savedBlock = currItem.getCurrBlock();
        
        BasicBlock loopCondBlock = new BasicBlock(currItem);
        savedBlock.appendOper(new Operation(Operation.OperationType.JMP, savedBlock));
        Operand target = new Operand(Operand.OperandType.BLOCK, loopCondBlock.getBlockNum());
        savedBlock.getLastOper().setSrcOperand(0, target);
        
        BasicBlock loopBodyBlock = new BasicBlock(currItem);
        BasicBlock postLoopBlock = new BasicBlock(currItem);
        
        currItem.setCurrBlock(loopCondBlock);
        
        Object exprResult = expr.genLLCode(currItem.getCurrBlock());
        int conditionRegNum;

        // Get register number from the result
        if (exprResult instanceof Operand operand) {
            conditionRegNum = (Integer) operand.getValue();
        } else {
            Operation op = (Operation) exprResult;
            conditionRegNum = ((Integer) op.getDestOperand(0).getValue()).intValue();
        }

        Operation branchOp = new Operation(Operation.OperationType.BNE, loopCondBlock);
        Operand conditionResult = new Operand(Operand.OperandType.REGISTER, conditionRegNum);
        branchOp.setSrcOperand(0, conditionResult);
        
        Operand trueTarget = new Operand(Operand.OperandType.BLOCK, loopBodyBlock.getBlockNum());
        branchOp.setSrcOperand(1, trueTarget);
        
        Operand falseTarget = new Operand(Operand.OperandType.BLOCK, postLoopBlock.getBlockNum());
        branchOp.setSrcOperand(2, falseTarget);
        
        loopCondBlock.appendOper(branchOp);
        
        currItem.setCurrBlock(loopBodyBlock);
        stmt.genLLCode(currItem);
        
        Operation jumpOp = new Operation(Operation.OperationType.JMP, loopBodyBlock);
        target = new Operand(Operand.OperandType.BLOCK, loopCondBlock.getBlockNum());
        jumpOp.setSrcOperand(0, target);
        loopBodyBlock.appendOper(jumpOp);
        
        currItem.setCurrBlock(postLoopBlock);
    }
}
