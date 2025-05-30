package parser.ASTNodes;

import lowlevel.*;
import scanner.TokenType;

public class Declaration {
    protected final TokenType type;
    protected final String ID;
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

    public void printAST() {
        System.out.print(" Global Variable:");
        System.out.println(type.toString() + " " + ID);
    }

    public CodeItem genLLCode(int declType, CodeItem firstItem) {
        Data currItem;
        if (array) {
            currItem = new Data(declType, ID, true, num);
        }
        else {
            currItem = new Data(declType, ID);
        }

        return currItem;
    }
}