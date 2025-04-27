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

    @Override
    public Object genLLCode(Function currItem) {
        HashMap symbolTable = currItem.getTable();
        for (VarDecl varDecl : decls.getList()) {
            // Put the virtual register number and the variable name in the symbol table.
            // It may not be fully correct, but it's a start
            symbolTable.put(currItem.getNewRegNum(), varDecl.getID());
        }

        for (Statement stmt : stmts.getList()) {
            stmt.genLLCode(currItem); // returns an object
        }
        return null;
    }
}
