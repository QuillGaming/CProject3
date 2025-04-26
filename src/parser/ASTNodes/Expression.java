package parser.ASTNodes;

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
}
