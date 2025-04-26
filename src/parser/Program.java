package parser;

import lowlevel.*;
import parser.ASTNodes.*;
import scanner.TokenType;

import java.util.ArrayList;

import static lowlevel.Data.*;

public class Program {
    private final ArrayList<Declaration> declarations;

    public Program() {
        declarations = new ArrayList<>();
    }

    public CodeItem genLLCode() {
        CodeItem firstItem = null;
        CodeItem currItem = null;
        CodeItem prevItem = null;
        for (Declaration declaration : declarations) {
            if (declaration instanceof FunDecl) {
                // Create a Function variable
            }
            else {
                int type;
                if (declaration.getType() == TokenType.VOID) {
                    type = TYPE_VOID;
                }
                else {
                    type = TYPE_INT;
                }

                if (!declaration.isArray()) {
                    currItem = new Data(type, declaration.getID());
                }
                else {
                    currItem = new Data(type, declaration.getID(), true, declaration.getNum());
                }
            }

            if (firstItem == null) {
                firstItem = currItem;
            }

            if (prevItem != null) {
                prevItem.setNextItem(currItem);
            }
            prevItem = currItem;
        }
        return firstItem;
    }

    public void add(Declaration decl) {
        declarations.add(decl);
    }

    public Declaration get(int index) {
        return declarations.get(index);
    }

    public void set(int index, Declaration decl) {
        declarations.set(index, decl);
    }

    public void remove(int index) {
        declarations.remove(index);
    }

    public void clear() {
        declarations.clear();
    }

    public int size() {
        return declarations.size();
    }
}
