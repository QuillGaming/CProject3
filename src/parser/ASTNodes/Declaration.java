package parser.ASTNodes;

import scanner.TokenType;

public class Declaration {
    private final TokenType type;
    private final String ID;
    private final boolean array;
    private final int num;

    public Declaration(TokenType t, String i) {
        this(t, i, false, 0);
    }

    public Declaration(TokenType t, String i, boolean a, int n) {
        type = t;
        ID = i;
        array = a;
        num = n;
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

    public int getNum() {
        return num;
    }
}