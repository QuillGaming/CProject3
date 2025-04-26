package parser.ASTNodes;

public class CmpndStmt extends Statement {
    private final LocalDecls decls;
    private final StmtList stmts;

    public CmpndStmt(LocalDecls d, StmtList s) {
        super(null);
        decls = d;
        stmts = s;
    }

    public LocalDecls getDecls() {
        return decls;
    }

    public StmtList getStmts() {
        return stmts;
    }
}
