package parser.ASTNodes;

import scanner.TokenType;

public class BinopExpr extends Expression {
    TokenType operator;

    public BinopExpr(TokenType o, Expression l, Expression r) {
        super(l, r, "Binary Operator");
        operator = o;
    }
}
