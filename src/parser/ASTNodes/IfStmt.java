package parser.ASTNodes;

public class IfStmt extends Statement {
    Statement thenStmt;
    Statement elseStmt;

    public IfStmt(Expression express, Statement stmt1, Statement stmt2) {
        super(express);
        thenStmt = stmt1;
        elseStmt = stmt2;
    }
}
