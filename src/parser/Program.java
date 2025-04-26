package parser;

import lowlevel.*;
import parser.ASTNodes.*;
import scanner.TokenType;

import java.util.ArrayList;
import java.util.HashMap;

import static lowlevel.Data.*;

public class Program {
    private final ArrayList<Declaration> declarations;

    public Program() {
        declarations = new ArrayList<>();
    }

    public CodeItem genLLCode() {
        CodeItem firstItem = null;
        CodeItem currItem;
        CodeItem prevItem = null;
        for (Declaration declaration : declarations) {
            int declType;
            if (declaration.getType() == TokenType.VOID) {
                declType = TYPE_VOID;
            }
            else {
                declType = TYPE_INT;
            }

            if (declaration instanceof FunDecl) {
                currItem = ((FunDecl) declaration).genFunLLCode(declType);
            }
            else { // declaration instanceof Declaration
                currItem = declaration.genLLCode(declType);
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
