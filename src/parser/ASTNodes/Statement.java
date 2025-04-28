package parser.ASTNodes;

import lowlevel.CodeItem;
import lowlevel.Function;

public abstract class Statement {
    Expression expr;

    public Statement(Expression e) {
        expr = e;
    }

    public abstract void printAST();

    public abstract void genLLCode(Function currItem, CodeItem firstItem);
}
