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
    public void genLLCode(BasicBlock currBlock, boolean isRhs, int currIdx) {
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
        currBlock.appendOper(currOper);

        if (type == OperationType.ASSIGN) {
            lhs.genLLCode(currBlock, false, 0);
        }
        else {
            lhs.genLLCode(currBlock, true, 0);
            currOper.setDestOperand(0, new Operand(OperandType.REGISTER, currBlock.getFunc().getNewRegNum()));
        }
        rhs.genLLCode(currBlock, true, 1);
    }
}
