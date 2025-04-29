package parser.ASTNodes;

import lowlevel.*;
import parser.CodeGenerationException;

import java.util.ArrayList;

public class Args {
    private final ArrayList<Expression> args;

    public Args() {
        args = new ArrayList<>();
    }

    public void add(Expression expr) {
        args.add(expr);
    }

    public Expression get(int index) {
        return args.get(index);
    }

    public void set(int index, Expression expr) {
        args.set(index, expr);
    }

    public void remove(int index) {
        args.remove(index);
    }

    public void clear() {
        args.clear();
    }

    public int size() {
        return args.size();
    }

    public void genLLCode(BasicBlock currBlock, CodeItem firstItem) {
        for (Expression expr : args) {
            Operation passOper = new Operation(Operation.OperationType.PASS, currBlock);
            if (expr instanceof IdentExpr) {
                passOper.addAttribute(new Attribute("PARAM_NUM", "2"));
                String ID = ((IdentExpr) expr).getID();
                int regNum = IdentExpr.searchTable(currBlock.getFunc().getTable(), ID);
                boolean isGlobal = false;
                if (regNum == -1) {
                    isGlobal = IdentExpr.searchData(firstItem, ID, regNum);
                }
                if (isGlobal) {
                    regNum = currBlock.getFunc().getNewRegNum();

                    Operation loadOper = new Operation(Operation.OperationType.LOAD_I, currBlock);
                    loadOper.setSrcOperand(0, new Operand(Operand.OperandType.STRING, ID));
                    loadOper.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
                    currBlock.appendOper(loadOper);
                }
                passOper.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
            }
            else if (expr instanceof NumExpr) {
                passOper.addAttribute(new Attribute("PARAM_NUM", "1"));
                passOper.setSrcOperand(0, new Operand(Operand.OperandType.INTEGER, ((NumExpr) expr).getNum()));
            }
            else if (expr instanceof BinopExpr){
                passOper.addAttribute(new Attribute("PARAM_NUM", "2"));
                Operation binop = new Operation(Operation.OperationType.UNKNOWN, currBlock);
                int regNum = currBlock.getFunc().getNewRegNum();
                binop.setDestOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
                currBlock.appendOper(binop);
                expr.genLLCode(currBlock, firstItem, 0);
                passOper.setSrcOperand(0, new Operand(Operand.OperandType.REGISTER, regNum));
            }
            else {
                throw new CodeGenerationException("Arg: CallExpr or Unexpected Expression found");
            }
            currBlock.appendOper(passOper);
        }
    }

    private String getString(String nameType) {
        String type = switch (nameType) {
            case "INTEGER" -> "1";
            case "REGISTER" -> "2";
            case "MACRO" -> "3";
            case "BLOCK" -> "4";
            case "STRING" -> "5";
            default -> "6";
        };
        return type;
    }
}
