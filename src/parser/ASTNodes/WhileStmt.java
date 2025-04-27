package parser.ASTNodes;

import lowlevel.Function;

public class WhileStmt extends Statement {
    Statement stmt;

    public WhileStmt(Expression e, Statement s) {
        super(e);
        stmt = s;
    }

    @Override
    public void genLLCode(Function currItem) {

    }
}
