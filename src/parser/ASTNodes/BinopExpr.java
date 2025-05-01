package parser.ASTNodes;

import lowlevel.*;
import lowlevel.Operation.OperationType;
import scanner.TokenType;

public class BinopExpr extends Expression {
    TokenType operator;

    // These Expressions are never AssignExprs
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
    public void genLLCode(BasicBlock currBlock, CodeItem firstItem, int trash) {
        OperationType type = switch (operator) {
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
        Operation currOper = currBlock.getLastOper();
        currOper.setType(type);
        lhs.genLLCode(currBlock, firstItem, 0);
        rhs.genLLCode(currBlock, firstItem, 1);
    }
}
