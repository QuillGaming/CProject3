package parser.ASTNodes;

import lowlevel.Function;

import java.util.HashMap;

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

    public void printAST() {
        if (decls.size() > 0) {
            System.out.println("   Local Declarations");
            decls.printAST();
        }

        if (stmts.size() > 0) {
            System.out.println("   Statements");
            stmts.printAST();
        }
    }

    @Override
    public void genLLCode(Function currItem) {
        decls.genLLCode(currItem);
        stmts.genLLCode(currItem);
    }
}
