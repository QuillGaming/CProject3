package parser.ASTNodes;

public class Statement {
    private final Expression expr;

    public Statement(Expression e) {
        expr = e;
    }

    public Expression getExpr() {
        return expr;
    }
}
