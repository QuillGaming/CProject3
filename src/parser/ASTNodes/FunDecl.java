package parser.ASTNodes;

import lowlevel.*;
import scanner.TokenType;

import java.util.HashMap;

public class FunDecl extends Declaration {
    private final ParamList params;
    private final CmpndStmt cmpndStmt;

    public FunDecl(TokenType t, String i, ParamList p, CmpndStmt c) {
        super(t, i);
        params = p;
        cmpndStmt = c;
    }

    public ParamList getParams() {
        return params;
    }

    public CmpndStmt getCmpndStmt() {
        return cmpndStmt;
    }

    @Override
    public void printAST() {
        System.out.print(" Function: " + ID);
        System.out.println(" returns " + type.toString());

        System.out.println("  Parameters ");
        params.printAST();

        System.out.println("  Body (Compound Statement) ");
        cmpndStmt.printAST();
    }

    @Override
    public Function genLLCode(int declType, CodeItem firstItem) {
        Function currItem;
        if (params.isEmpty()) {
            currItem = new Function(declType, ID);
        }
        else {
            FuncParam firstParam = params.genLLCode();
            currItem = new Function(declType, ID, firstParam);

            HashMap symbolTable = currItem.getTable();
            FuncParam currParam = firstParam;
            while (currParam != null) {
                // Put the virtual register number and the variable name in the symbol table.
                // It may not be fully correct, but it's a start
                symbolTable.put(currParam.getName(), currItem.getNewRegNum());

                currParam = currParam.getNextParam();
            }
        }
        currItem.createBlock0();
        currItem.setCurrBlock(currItem.getFirstBlock());

        cmpndStmt.genLLCode(currItem, firstItem);

        currItem.appendBlock(currItem.getReturnBlock());

        BasicBlock currBlock = currItem.getFirstUnconnectedBlock();
        while (currBlock != null) {
            currItem.appendBlock(currBlock);
            currBlock = currBlock.getNextBlock();
        }

        return currItem;
    }
}
