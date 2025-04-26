package parser.ASTNodes;

import scanner.TokenType;

public class VarDecl {
    private final TokenType type;
    private final Object ID;
    private final Object num;

    public VarDecl(TokenType t, Object i) {
        this(t, i, null);
    }

    public VarDecl(TokenType t, Object i, Object n) {
        type = t;
        ID = i;
        num = n;
    }

    public TokenType getType() {
        return type;
    }

    public Object getID() {
        return ID;
    }

    public Object getNum() {
        return num;
    }
}
