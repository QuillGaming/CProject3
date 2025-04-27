package parser.ASTNodes;

import lowlevel.Function;

public class ReturnStmt extends Statement {
    public ReturnStmt(Expression e) {
        super(e);
    }

    @Override
    public void printAST() {
        System.out.println("    Return Statement");
        System.out.println("     Expression");

    }

    @Override
    public void genLLCode(Function currItem) {

    }
}
