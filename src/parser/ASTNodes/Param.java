package parser.ASTNodes;

import scanner.TokenType;

public class Param {
    private final TokenType type;
    private final String ID;
    private final boolean array;

    public Param(TokenType t, String i) {
        this(t, i, false);
    }

    public Param(TokenType t, String i, boolean a) {
        type = t;
        ID = i;
        array = a;
    }

    public TokenType getType() {
        return type;
    }

    public String getID() {
        return ID;
    }

    public boolean isArray() {
        return array;
    }
}
