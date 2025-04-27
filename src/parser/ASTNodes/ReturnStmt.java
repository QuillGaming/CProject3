package parser.ASTNodes;

import lowlevel.Function;

public class ReturnStmt extends Statement {
    public ReturnStmt(Expression e) {
        super(e);
    }

    @Override
    public Object genLLCode(Function currItem) {
        return null;
    }
}
