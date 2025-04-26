package parser.ASTNodes;

import scanner.TokenType;

public class FunDecl extends Declaration {
    private final ParamList params;
    private final CmpndStmt cmpndStmt;

    public FunDecl(TokenType t, Object i, ParamList p, CmpndStmt c) {
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
}
