package parser.ASTNodes;

import lowlevel.*;

public class AssignExpr extends Expression {
    String dest;
    Expression src;

    public AssignExpr(String d, Expression s) {
        dest = d;
        src = s;
    }

    @Override
    public void printAST() {

    }

    @Override
    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, int trash) {
        int regNum = IdentExpr.searchTable(currBlock.getFunc().getTable(), dest);
        boolean isGlobal = false;
        if (regNum == -1) {
            isGlobal = IdentExpr.searchData(firstItem, dest, regNum);
        }

        if (src instanceof BinopExpr) {
            Operation binop = new Operation(Operation.OperationType.UNKNOWN, currBlock);
            Operand binopValue = new Operand(Operand.OperandType.REGISTER, regNum);
            binop.setDestOperand(0, binopValue);
            currBlock.appendOper(binop);
            src.genLLCode(currBlock, firstItem, 0);
            if (isGlobal) {
                Operation storeOper = new Operation(Operation.OperationType.STORE_I, currBlock);
                storeOper.setSrcOperand(0, binopValue);
                storeOper.setSrcOperand(1, new Operand(Operand.OperandType.STRING, dest));
                currBlock.appendOper(storeOper);
            }
        }
        else if (src instanceof CallExpr) {
            src.genLLCode(currBlock, firstItem, 0);
            if (isGlobal) {
                Operation storeOper = new Operation(Operation.OperationType.STORE_I, currBlock);
                storeOper.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
                storeOper.setSrcOperand(1, new Operand(Operand.OperandType.MACRO,"RetReg"));
                currBlock.appendOper(storeOper);
            }
            else {
                Operation assignOper = new Operation(Operation.OperationType.ASSIGN, currBlock);
                assignOper.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
                assignOper.setSrcOperand(0, new Operand(Operand.OperandType.MACRO,"RetReg"));
                currBlock.appendOper(assignOper);
            }
        }
        else {
            if (isGlobal) {
                Operation storeOper = new Operation(Operation.OperationType.STORE_I, currBlock);
                storeOper.setSrcOperand(1, new Operand(Operand.OperandType.STRING, dest));
                currBlock.appendOper(storeOper);
            }
            else {
                Operation assignOper = new Operation(Operation.OperationType.ASSIGN, currBlock);
                assignOper.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
                currBlock.appendOper(assignOper);
            }
            src.genLLCode(currBlock, firstItem, 0); // src can either be an ID or a num here
        }
    }
}
