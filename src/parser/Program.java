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
                if (((FunDecl) declaration).getParams().isEmpty()) {
                    currItem = new Function(declType, declaration.getID());
                }
                else {
                    FuncParam firstParam = getFuncParams((FunDecl) declaration);
                    currItem = new Function(declType, declaration.getID(), firstParam);
                }
            }
            else {
                if (declaration.isArray()) {
                    currItem = new Data(declType, declaration.getID(), true, declaration.getNum());
                }
                else {
                    currItem = new Data(declType, declaration.getID());
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

    private FuncParam getFuncParams(FunDecl declaration) {
        FuncParam firstParam = null;
        FuncParam currParam;
        FuncParam prevParam = null;
        for (Param param : declaration.getParams().getList()) {
            if (param.isArray()) {
                currParam = new FuncParam(TYPE_INT, param.getID(), true);
            }
            else {
                currParam = new FuncParam(TYPE_INT, param.getID());
            }

            if (firstParam == null) {
                firstParam = currParam;
            }

            if (prevParam != null) {
                prevParam.setNextParam(currParam);
            }
            prevParam = currParam;
        }
        return firstParam;
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
