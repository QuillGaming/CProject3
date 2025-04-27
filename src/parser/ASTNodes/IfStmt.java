package parser.ASTNodes;

import lowlevel.Function;

public class IfStmt extends Statement {
    Statement thenStmt;
    Statement elseStmt;

    public IfStmt(Expression express, Statement stmt1, Statement stmt2) {
        super(express);
        thenStmt = stmt1;
        elseStmt = stmt2;
    }

    @Override
    public void genLLCode(Function currItem) {

    }
}
