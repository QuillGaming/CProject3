package parser;

import scanner.*;
import parser.ASTNodes.*;

public class CMinusParser implements Parser {
    private final Scanner scan;
    private Token currentToken;

    public CMinusParser(String file) {
        scan = new CMinusScanner(file);
        currentToken = null;
    }

    // This function is only partially complete; it does not print the contents of expressions
    public void printAST(Program parseTree) {
        parseTree.printAST();
    }

    public Program parse() {
        advanceToken();
        return parseProgram();
    }

    private Program parseProgram() {
        Program program = new Program();
        do {
            program.add(parseDecl());
        } while (currentToken.getType() != TokenType.ENDFILE);

        return program;
    }

    private Declaration parseDecl() {
        TokenType declType = null;
        switch(currentToken.getType()) {
            case VOID, INT:
                declType = currentToken.getType();
                advanceToken();
                break;
            default:
                //logParseError();
                return null;
        }

        if (currentToken.getType() == TokenType.ID) {
            String id = advanceToken().getTokenData();
            if (declType == TokenType.VOID) {
                return parseFunDeclP(declType, id);
            } else {
                return parseDeclP(id);
            }
        } else {
            //logParseError();
            return null;
        }
    }

    private FunDecl parseFunDeclP(TokenType declType, String id) {
        matchToken(TokenType.LPAREN);
        ParamList params = parseParams();
        matchToken(TokenType.RPAREN);
        CmpndStmt cmpndStmt = parseCmpndStmt();
        return new FunDecl(declType, id, params, cmpndStmt);
    }

    private ParamList parseParams() {
        if (currentToken.getType() == TokenType.VOID) {
            matchToken(TokenType.VOID);
            return new ParamList();
        }
        ParamList params = new ParamList();
        do {
            if (currentToken.getType() == TokenType.COMMA) {
                advanceToken();
            }
            matchToken(TokenType.INT);
            params.add(parseParam());
        } while (currentToken.getType() == TokenType.COMMA);
        return params;
    }

    private Param parseParam() {
        Token id = matchToken(TokenType.ID);
        if (currentToken.getType() == TokenType.LBRACK) {
            matchToken(TokenType.LBRACK);
            matchToken(TokenType.RBRACK);
            return new Param(TokenType.INT, id.getTokenData(), true);
        }
        return new Param(TokenType.INT, id.getTokenData());
    }

    private CmpndStmt parseCmpndStmt() {
        matchToken(TokenType.LBRACE);
        LocalDecls decls = parseLocalDecls();
        StmtList stmts = parseStmts();
        matchToken(TokenType.RBRACE);

        return new CmpndStmt(decls, stmts);
    }

    private LocalDecls parseLocalDecls() {
        LocalDecls localDecls = new LocalDecls();
        while (currentToken.getType() == TokenType.INT) {
            localDecls.add(parseVarDecl());
        }
        return localDecls;
    }

    private VarDecl parseVarDecl() {
        matchToken(TokenType.INT);
        Token id = matchToken(TokenType.ID);
        Token numToken = null;
        int num = 0;
        if (currentToken.getType() == TokenType.LBRACK) {
            matchToken(TokenType.LBRACK);
            numToken = matchToken(TokenType.NUM);
            num = Integer.parseInt(numToken.getTokenData());
            matchToken(TokenType.RBRACK);
        }
        matchToken(TokenType.SEMI);
        if (numToken == null) {
            return new VarDecl(TokenType.INT, id.getTokenData());
        }
        else {
            return new VarDecl(TokenType.INT, id.getTokenData(), true, num);
        }
    }

    private StmtList parseStmts() {
        StmtList stmts = new StmtList();
        while (isStatement(currentToken.getType())) {
            stmts.add(parseStmt());
        }
        return stmts;
    }

    private Statement parseStmt() {
        return switch (currentToken.getType()) {
            case LPAREN, NUM, ID -> {
                Statement returnStmt = new ExprStmt(parseExpression());
                matchToken(TokenType.SEMI);
                yield returnStmt;
            }
            case LBRACE -> parseCmpndStmt();
            case IF -> parseIfStmt();
            case WHILE -> parseWhileStmt();
            case RETURN -> parseReturnStmt();
            default -> null; // should never get here
        };
    }

    private Expression parseExpression() {
        Expression lhs;
        switch(currentToken.getType()) {
            case LPAREN:
                advanceToken();
                lhs = parseExpression();
                matchToken(TokenType.RPAREN);
                return parseSimpleExpr(lhs);
            case NUM:
                lhs = new NumExpr(Integer.parseInt(matchToken(TokenType.NUM).getTokenData()));
                return parseSimpleExpr(lhs);
            case ID:
                String id = matchToken(TokenType.ID).getTokenData();
                return parsePrimeExpr(id);
            default:
                return null; // should never get here
        }
    }

    private Expression parseSimpleExpr(Expression lhs) {
        lhs = parseAddExpr(lhs);

        if (isRelop(currentToken.getType())) {
            Token relop = advanceToken();
            Expression rhs = parseAddExpr(null);
            lhs = new BinopExpr(relop.getType(), lhs, rhs);
        }

        return lhs;
    }

    private Expression parseAddExpr(Expression lhs) {
        lhs = parseTerm(lhs);

        while(isAddop(currentToken.getType())) {
            Token addop = advanceToken();
            Expression rhs = parseTerm(null);
            lhs = new BinopExpr(addop.getType(), lhs, rhs);
        }

        return lhs;
    }

    private Expression parsePrimeExpr(String id) {
        Expression lhs;
        switch(currentToken.getType()) {
            case ASSIGN:
                lhs = new IdentExpr(id);
                advanceToken();
                return new AssignExpr(((IdentExpr) lhs).getID(), parseExpression());
            case LBRACK:
                advanceToken();
                Expression index = parseExpression();
                matchToken(TokenType.RBRACK);
                lhs = new IdentExpr(id);
                return parseDPrimeExpr(lhs);
            case LPAREN:
                advanceToken();
                Args args;
                if (currentToken.getType() == TokenType.RPAREN) {
                    args = new Args();
                }
                else {
                    args = parseArgs();
                }
                matchToken(TokenType.RPAREN);
                lhs = new CallExpr(id, args);
                return parseSimpleExpr(lhs);
            case PLUS, MINUS, TIMES, OVER:
                lhs = new IdentExpr(id);
                Token op = advanceToken();
                return new BinopExpr(op.getType(), lhs, parseSimpleExpr(null));
            default:
                return new IdentExpr(id);
        }
    }

    private Expression parseDPrimeExpr(Expression lhs) {
        if (currentToken.getType() == TokenType.ASSIGN) {
            advanceToken();
            return new AssignExpr(((IdentExpr) lhs).getID(), parseExpression());
        }
        return parseSimpleExpr(lhs);
    }

    private Args parseArgs() {
        if (currentToken.getType() == TokenType.RPAREN) {
            return null;
        }
        Args args = new Args();
        do {
            if (currentToken.getType() == TokenType.COMMA) {
                advanceToken();
            }
            args.add(parseExpression());
        } while (currentToken.getType() == TokenType.COMMA);
        return args;
    }

    private Expression parseTerm(Expression lhs) {
        lhs = parseFactor(lhs);

        while (isMulop(currentToken.getType())) {
            Token mulop = advanceToken();
            Expression rhs = parseFactor(null);
            lhs = new BinopExpr(mulop.getType(), lhs, rhs);
        }

        return lhs;
    }

    private Expression parseFactor(Expression lhs) {
        if (lhs != null) {
            return lhs;
        }
        switch(currentToken.getType()) {
            case LPAREN:
                advanceToken();
                Expression expr = parseExpression();
                matchToken(TokenType.RPAREN);
                return expr;
            case ID:
                return parseVarcall();
            case NUM:
                return new NumExpr(Integer.parseInt(advanceToken().getTokenData()));
            default:
                //logParseError();
                return null;
        }
    }

    // Fundamentally changed, not sure if it makes the parser incorrect
    private Expression parseVarcall() {
        String idName = matchToken(TokenType.ID).getTokenData();
        if (currentToken.getType() == TokenType.LPAREN) {
            advanceToken();
            Expression id = new CallExpr(idName, parseArgs());
            matchToken(TokenType.RPAREN);
            return id;
        }
        return new IdentExpr(idName);
    }

    private Statement parseIfStmt() {
        matchToken(TokenType.IF);
        matchToken(TokenType.LPAREN);
        Expression ifExpr = parseSimpleExpr(null);
        matchToken(TokenType.RPAREN);
        Statement thenStmt = parseStmt();
        Statement elseStmt = null;

        if (currentToken.getType() == TokenType.ELSE) {
            advanceToken();
            elseStmt = parseStmt();
        }

        return new IfStmt(ifExpr, thenStmt, elseStmt);
    }

    private Statement parseWhileStmt() {
        matchToken(TokenType.WHILE);
        matchToken(TokenType.LPAREN);
        Expression whileExpr = parseSimpleExpr(null);
        matchToken(TokenType.RPAREN);
        Statement stmt = parseStmt();
        return new WhileStmt(whileExpr, stmt);
    }

    private Statement parseReturnStmt() {
        matchToken(TokenType.RETURN);
        Expression expr = null;
        if(currentToken.getType() != TokenType.SEMI) {
            expr = parseExpression();
        }
        matchToken(TokenType.SEMI);
        return new ReturnStmt(expr);
    }

    private Declaration parseDeclP(String id) {
        switch(currentToken.getType()) {
            case SEMI:
                advanceToken();
                return new Declaration(TokenType.INT, id);
            case LBRACK:
                advanceToken();
                Token numToken = matchToken(TokenType.NUM);
                int num = Integer.parseInt(numToken.getTokenData());
                matchToken(TokenType.SEMI);
                return new Declaration(TokenType.INT, id, true, num);
            case LPAREN:
                return parseFunDeclP(TokenType.INT, id);
            default:
                //logParseError()
                return null;
        }
    }

    private Token advanceToken() {
        Token oldToken = currentToken;
        currentToken = scan.getNextToken();
        return oldToken;
    }

    private Token matchToken(TokenType expected) {
        if (currentToken.getType() == expected) {
            return advanceToken();
        }

        System.out.println("Error: Expected " + expected + ", found " + currentToken.getType());
        return null;
    }

    private boolean isAddop(TokenType type) {
        return type == TokenType.PLUS || type == TokenType.MINUS;
    }

    private boolean isMulop(TokenType type) {
        return type == TokenType.TIMES || type == TokenType.OVER;
    }

    private boolean isStatement(TokenType type) {
        return type == TokenType.LPAREN || type == TokenType.NUM
               || type == TokenType.ID || type == TokenType.LBRACE
               || type == TokenType.IF || type == TokenType.WHILE
               || type == TokenType.RETURN;
    }

    private boolean isRelop(TokenType type) {
        return type == TokenType.LT || type == TokenType.GT
               || type == TokenType.LTE || type == TokenType.GTE
               || type == TokenType.EQ || type == TokenType.NEQ;
    }
}