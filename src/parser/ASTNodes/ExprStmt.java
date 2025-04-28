package parser.ASTNodes;

import lowlevel.CodeItem;
import lowlevel.Function;

public class ExprStmt extends Statement {
    public ExprStmt(Expression e) {
        super(e);
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public void printAST() {
        System.out.println("    Expression");

    }

    @Override
    public void genLLCode(Function currItem, CodeItem firstItem) {
        expr.genLLCode(currItem.getCurrBlock(), firstItem, false, 0);
    }
}
