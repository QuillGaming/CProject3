package parser.ASTNodes;

import lowlevel.*;
import parser.CodeGenerationException;

import java.util.HashMap;

public class AssignExpr extends Expression {
    String dest;
    Expression src; // Will never be a CallExpr

    public AssignExpr(String d, Expression s) {
        dest = d;
        src = s;
    }

    @Override
    public void printAST() {

    }

    @Override
    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, int trash) {
        HashMap symbolTable = currBlock.getFunc().getTable();
        int regNum = -1;
        for (Object obj : symbolTable.keySet()) {
            String key = (String) obj;
            if (key.equals(dest)) {
                regNum = (int) symbolTable.get(key);
                break;
            }
        }
        boolean isGlobal = false;
        if (regNum == -1) {
            CodeItem currItem = firstItem;
            while (currItem != null) {
                if (currItem instanceof Data && ((Data) currItem).getName().equals(dest)) {
                    regNum = currBlock.getFunc().getNewRegNum();
                    isGlobal = true;
                    break;
                }
                currItem = currItem.getNextItem();
            }

            if (regNum == -1) {
                throw new CodeGenerationException("Assign: Destination identifier not found");
            }
        }

        if (src instanceof BinopExpr) {
            Operation binop = new Operation(Operation.OperationType.UNKNOWN, currBlock);
            binop.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
            currBlock.appendOper(binop);
            src.genLLCode(currBlock, firstItem, 0);
            if (isGlobal) {
                Operation storeOper = new Operation(Operation.OperationType.STORE_I, currBlock);
                storeOper.setDestOperand(0, new Operand(Operand.OperandType.STRING, dest));
                currBlock.appendOper(storeOper);
            }
        }
        else {
            if (isGlobal) {
                Operation storeOper = new Operation(Operation.OperationType.STORE_I, currBlock);
                storeOper.setDestOperand(0, new Operand(Operand.OperandType.STRING, dest));
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
