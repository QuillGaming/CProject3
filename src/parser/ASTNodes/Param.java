package parser.ASTNodes;

import lowlevel.FuncParam;
import scanner.TokenType;

import static lowlevel.Data.TYPE_INT;

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

    public void printAST() {
        System.out.print("   " + ID + " of type ");
        System.out.println(type.toString());
    }

    public FuncParam genLLCode() {
        FuncParam currParam;
        if (array) {
            currParam = new FuncParam(TYPE_INT, ID, true);
        }
        else {
            currParam = new FuncParam(TYPE_INT, ID);
        }
        currParam.setNextParam(null);

        return currParam;
    }
}
