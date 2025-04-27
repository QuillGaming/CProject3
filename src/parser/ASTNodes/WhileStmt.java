package parser.ASTNodes;

import lowlevel.Function;

public class WhileStmt extends Statement {
    Statement stmt;

    public WhileStmt(Expression e, Statement s) {
        super(e);
        stmt = s;
    }

    @Override
    public void printAST() {
        System.out.println("    While Statement");
        System.out.println("     Condition (Expression)");


        System.out.println("     Body (Statement)");
        stmt.printAST();
    }

    @Override
    public void genLLCode(Function currItem) {

    }
}
