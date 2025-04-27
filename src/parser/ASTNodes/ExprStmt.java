package parser.ASTNodes;

import lowlevel.Function;

public class ExprStmt extends Statement {
    public ExprStmt(Expression e) {
        super(e);
    }

    public Expression getExpr() {
        return expr;
    }

    @Override
    public Object genLLCode(Function currItem) {
        return null;
    }
}
