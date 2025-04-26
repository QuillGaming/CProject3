package parser.ASTNodes;

import lowlevel.*;
import scanner.TokenType;

import java.util.HashMap;

public class FunDecl extends Declaration {
    private final ParamList params;
    private final CmpndStmt cmpndStmt;

    public FunDecl(TokenType t, String i, ParamList p, CmpndStmt c) {
        super(t, i);
        params = p;
        cmpndStmt = c;
    }

    public ParamList getParams() {
        return params;
    }

    public CmpndStmt getCmpndStmt() {
        return cmpndStmt;
    }

    @Override
    public CodeItem genLLCode(int declType) {
        Function currItem;
        if (params.isEmpty()) {
            currItem = new Function(declType, ID);
        }
        else {
            FuncParam firstParam = params.genLLCode();
            currItem = new Function(declType, ID, firstParam);
        }

        HashMap symbolTable = currItem.getTable();
        for (VarDecl varDecl : cmpndStmt.getDecls().getList()) {
            // Put the virtual register number and the variable name in the symbol table.
            // May not be fully correct, but it's a start
            symbolTable.put(currItem.getNewRegNum(), varDecl.getID());
        }

        for (Statement stmt : cmpndStmt.getStmts().getList()) {

        }

        return currItem;
    }
}
