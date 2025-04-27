package parser.ASTNodes;

import lowlevel.Function;

public class IfStmt extends Statement {
    Statement thenStmt;
    Statement elseStmt;

    public IfStmt(Expression express, Statement stmt1, Statement stmt2) {
        super(express);
        thenStmt = stmt1;
        elseStmt = stmt2;
    }

    public void printAST() {
        System.out.println("    If Statement");
        System.out.println("     Condition (Expression)");


        System.out.println("     Then Branch (Statement)");
        thenStmt.printAST();

        if (elseStmt != null) {
            System.out.println("     Else Branch (Statement)");
            elseStmt.printAST();
        }
    }

    @Override
    public void genLLCode(Function currItem) {

    }
}
