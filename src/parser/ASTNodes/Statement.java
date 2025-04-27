package parser.ASTNodes;

import lowlevel.Function;

public abstract class Statement {
    Expression expr;

    public Statement(Expression e) {
        expr = e;
    }

    public abstract Object genLLCode(Function currItem);
}
