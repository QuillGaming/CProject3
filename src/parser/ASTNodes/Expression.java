package parser.ASTNodes;

import lowlevel.Function;

public class Expression {
    Object lhs;
    Expression rhs;
    String type;

    public Expression(Object l, String t) {
        this(l, null, t);
    }

    public Expression(Object l, Expression r, String t) {
        lhs = l;
        rhs = r;
        type = t;
    }

    public void genLLCode(Function currItem) {

    }
}
