package parser.ASTNodes;

public class IdentExpr extends Expression {
    Object arg;

    public IdentExpr(Object id) {
        this(id, null);
    }

    public IdentExpr(Object id, Object a) {
        super(id, "Identifier");
        arg = a;
    }
}
