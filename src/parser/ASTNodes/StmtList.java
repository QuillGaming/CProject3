package parser.ASTNodes;

import java.util.ArrayList;

public class StmtList {
    private final ArrayList<Statement> statements;

    public StmtList() {
        statements = new ArrayList<>();
    }

    public void add(Statement stmt) {
        statements.add(stmt);
    }

    public Statement get(int index) {
        return statements.get(index);
    }

    public void set(int index, Statement stmt) {
        statements.set(index, stmt);
    }

    public void remove(int index) {
        statements.remove(index);
    }

    public void clear() {
        statements.clear();
    }

    public int size() {
        return statements.size();
    }
}
