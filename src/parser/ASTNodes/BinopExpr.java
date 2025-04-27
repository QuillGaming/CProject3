package parser.ASTNodes;

import lowlevel.*;
import lowlevel.Operation.OperationType;
import lowlevel.Operand.OperandType;
import scanner.TokenType;

public class BinopExpr extends Expression {
    TokenType operator;
    Expression lhs;
    Expression rhs;

    public BinopExpr(TokenType o, Expression l, Expression r) {
        operator = o;
        lhs = l;
        rhs = r;
    }

    @Override
    public void printAST() {

    }

    @Override
    public Operation genLLCode(BasicBlock currBlock) {
        OperationType type = switch (operator) {
            case ASSIGN -> OperationType.ASSIGN;
            case PLUS -> OperationType.ADD_I;
            case MINUS -> OperationType.SUB_I;
            case TIMES -> OperationType.MUL_I;
            case OVER -> OperationType.DIV_I;
            case LT -> OperationType.LT;
            case LTE -> OperationType.LTE;
            case GT -> OperationType.GT;
            case GTE -> OperationType.GTE;
            case EQ -> OperationType.EQUAL;
            case NEQ -> OperationType.NOT_EQUAL;
            default -> OperationType.UNKNOWN;
        };
        Operation currOper = new Operation(type, currBlock);
        currOper.setNum(currBlock.getFunc().getNewOperNum());

        Operand left;
        if (!(lhs instanceof BinopExpr)) {
            left = (Operand) lhs.genLLCode(currBlock);
        }
        else {
            Operation temp = (Operation) lhs.genLLCode(currBlock);
            left = temp.getDestOperand(0);
        }

        Operand right;
        if (!(rhs instanceof BinopExpr)) {
            right = (Operand) rhs.genLLCode(currBlock);
        }
        else {
            Operation temp = (Operation) rhs.genLLCode(currBlock);
            right = temp.getDestOperand(0);
        }

        if (type == OperationType.ASSIGN) {
            currOper.setSrcOperand(0, right);
            currOper.setDestOperand(0, left);
        }
        else {
            currOper.setSrcOperand(0, right);
            currOper.setSrcOperand(1, left);

            Operand dest = new Operand(OperandType.REGISTER, currBlock.getFunc().getNewRegNum());
            currOper.setDestOperand(0, dest);
        }

        return currOper;
    }
}
