package parser.ASTNodes;

import scanner.TokenType;

public class Param {
    private final TokenType type;
    private final Object ID;

    public Param(TokenType t, Object i) {
        type = t;
        ID = i;
    }

    public TokenType getType() {
        return type;
    }

    public Object getID() {
        return ID;
    }
}
