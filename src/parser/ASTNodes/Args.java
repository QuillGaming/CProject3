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

    public Operation genLLCode(BasicBlock currBlock) {
        Operation firstOper = null;
        Operation currOper;
        Operation prevOper = null;
        for (Expression expr : args) {
            Operation exprOper;
            //Operand operand = new Operand(Operand.OperandType.REGISTER, arg.getName());

            currOper = new Operation(Operation.OperationType.PASS, currBlock);
            //currOper.setSrcOperand(0, operand);

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
}
