package parser.ASTNodes;

import lowlevel.*;
import lowlevel.Operand.OperandType;

import java.util.HashMap;

public class IdentExpr extends Expression {
    String ID;
    Object arg;

    public IdentExpr(String i) {
        this(i, null);
    }

    public IdentExpr(String i, Object a) {
        ID = i;
        arg = a;
    }

    @Override
    public void printAST() {

    }

    @Override
    public Operand genLLCode(BasicBlock currBlock) {
        HashMap symbolTable = currBlock.getFunc().getTable();
        int regNum = -1;
        for (Object obj : symbolTable.keySet()) {
            int key = (int) obj;
            if (symbolTable.get(key) == ID) {
                regNum = key;
                break;
            }
        }

        if (regNum == -1) {
            regNum = currBlock.getFunc().getNewRegNum();
        }

        return new Operand(OperandType.REGISTER, regNum);
    }
}
