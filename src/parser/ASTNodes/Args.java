package parser.ASTNodes;

import lowlevel.*;

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

    public Operation genLLCode(BasicBlock currBlock, CodeItem firstItem) {
        Operation firstOper = null;
        Operation currOper;
        Operation prevOper = null;
        for (Expression expr : args) {
            expr.genLLCode(currBlock, firstItem, true, -1);
            currOper = currBlock.getLastOper();
            currOper.setType(Operation.OperationType.PASS);
            String nameType = currBlock.getLastOper().getSrcOperand(0).getType().toString();
            String type = getString(nameType);
            currOper.addAttribute(new Attribute("PARAM_NUM", type));

            if (firstOper == null) {
                firstOper = currOper;
            }

            if (prevOper != null) {
                prevOper.setNextOper(currOper);
                currOper.setPrevOper(prevOper);
            }
            prevOper = currOper;
        }

        return firstOper;
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
