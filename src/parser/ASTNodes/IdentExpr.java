package parser.ASTNodes;

import lowlevel.*;
import lowlevel.Operand.OperandType;
import parser.CodeGenerationException;

import java.util.HashMap;

public class IdentExpr extends Expression {
    String ID;

    public IdentExpr(String i) {
        ID = i;
    }

    @Override
    public void printAST() {

    }

    public String getID() {
        return ID;
    }

    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, int currDestIdx) {
        int regNum = searchTable(currBlock.getFunc().getTable(), ID);
        boolean isGlobal = false;
        if (regNum == -1) {
            isGlobal = searchData(firstItem, ID, regNum);
        }

        Operation lastOper = currBlock.getLastOper();
        Operation prevOper = lastOper.getPrevOper();
        if (isGlobal) {
            regNum = currBlock.getFunc().getNewRegNum();
            Operation loadOper = new Operation(Operation.OperationType.LOAD_I, currBlock);
            loadOper.setSrcOperand(0, new Operand(OperandType.STRING, ID));
            loadOper.setDestOperand(0, new Operand(OperandType.REGISTER, regNum));

            if (prevOper != null) {
                prevOper.setNextOper(loadOper);
            }
            loadOper.setPrevOper(prevOper);
            loadOper.setNextOper(lastOper);
            lastOper.setPrevOper(loadOper);
        }

        lastOper.setSrcOperand(currDestIdx, new Operand(OperandType.REGISTER, regNum));
    }

    public static int searchTable(HashMap symbolTable, String ID) {
        int regNum = -1;
        for (Object obj : symbolTable.keySet()) {
            String key = (String) obj;
            if (key.equals(ID)) {
                regNum = (int) symbolTable.get(key);
                break;
            }
        }
        return regNum;
    }

    public static boolean searchData(CodeItem firstItem, String ID, int regNum) {
        boolean isGlobal = false;
        CodeItem currItem = firstItem;
        while (currItem != null) {
            if (currItem instanceof Data && ((Data) currItem).getName().equals(ID)) {
                isGlobal = true;
                break;
            }
            currItem = currItem.getNextItem();
        }
        return isGlobal;
    }
}
