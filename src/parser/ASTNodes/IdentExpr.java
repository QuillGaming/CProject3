package parser.ASTNodes;

import lowlevel.*;
import lowlevel.Operand.OperandType;

import java.util.HashMap;

public class IdentExpr extends Expression {
    String ID;

    public IdentExpr(String i) {
        ID = i;
    }

    @Override
    public void printAST() {

    }

    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, boolean isRhs, int currIdx) {
        HashMap symbolTable = currBlock.getFunc().getTable();
        int regNum = -1;
        for (Object obj : symbolTable.keySet()) {
            String key = (String) obj;
            if (key == ID) {
                regNum = (int) symbolTable.get(key);
                break;
            }
        }

        if (regNum == -1) {
            regNum = currBlock.getFunc().getNewRegNum();
        }

        CodeItem currItem = firstItem;
        while (currItem != null) {
            if (currItem instanceof Data && ((Data) currItem).getName().equals(ID)) {
                Operation loadOper = new Operation(Operation.OperationType.LOAD_I, currBlock);
                loadOper.setSrcOperand(0, new Operand(OperandType.STRING, ID));
                loadOper.setDestOperand(0, new Operand(OperandType.REGISTER, regNum));

                Operation nextOper = currBlock.getLastOper();
                loadOper.setNextOper(nextOper);

                Operation prevOper = nextOper.getPrevOper();
                if (prevOper != null) {
                    prevOper.setNextOper(loadOper);
                }
                nextOper.setPrevOper(loadOper);
                loadOper.setPrevOper(prevOper);
                break;
            }
            currItem = currItem.getNextItem();
        }

        Operation lastOper = currBlock.getLastOper();
        Operand operand = new Operand(OperandType.REGISTER, regNum);
        if (isRhs) {
            lastOper.setSrcOperand(currIdx, operand);
        }
        else {
            lastOper.setDestOperand(currIdx, operand);
        }
    }
}
