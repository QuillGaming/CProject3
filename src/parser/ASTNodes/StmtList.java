package parser.ASTNodes;

import lowlevel.Function;

import java.util.ArrayList;

public class StmtList {
    private final ArrayList<Statement> stmts;

    public StmtList() {
        stmts = new ArrayList<>();
    }

    public ArrayList<Statement> getList() {
        return stmts;
    }

    public void add(Statement stmt) {
        stmts.add(stmt);
    }

    public Statement get(int index) {
        return stmts.get(index);
    }

    public void set(int index, Statement stmt) {
        stmts.set(index, stmt);
    }

    public void remove(int index) {
        stmts.remove(index);
    }

    public void clear() {
        stmts.clear();
    }

    public int size() {
        return stmts.size();
    }

    public void printAST() {
        for (Statement stmt : stmts) {
            stmt.printAST();
        }
    }

    public void genLLCode(Function currItem) {
        for (Statement stmt : stmts) {
            stmt.genLLCode(currItem);
        }
    }
}
