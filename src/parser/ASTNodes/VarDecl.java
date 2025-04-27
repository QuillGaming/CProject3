package parser.ASTNodes;

import lowlevel.Function;
import scanner.TokenType;

import java.util.HashMap;

public class VarDecl {
    private final TokenType type;
    private final String ID;
    private final boolean array;
    private final int num;

    public VarDecl(TokenType t, String i) {
        this(t, i, false, 0);
    }

    public VarDecl(TokenType t, String i, boolean a, int n) {
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

    public Object getNum() {
        return num;
    }

    public void printAST() {
        System.out.print("    " + ID.toString() + " of type ");
        System.out.print(type.toString());
        if (array) {
            System.out.println(" array of size " + num);
        }
        else {
            System.out.println();
        }
    }

    public void genLLCode(Function currItem) {
        // Put the virtual register number and the variable name in the symbol table.
        // It may not be fully correct, but it's a start
        currItem.getTable().put(currItem.getNewRegNum(), ID);
    }
}
