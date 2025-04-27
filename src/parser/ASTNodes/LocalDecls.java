package parser.ASTNodes;

import java.util.ArrayList;

public class LocalDecls {
    private final ArrayList<VarDecl> varDecls;

    public LocalDecls() {
        varDecls = new ArrayList<>();
    }

    public ArrayList<VarDecl> getList() {
        return varDecls;
    }

    public void add(VarDecl varDecl) {
        varDecls.add(varDecl);
    }

    public VarDecl get(int index) {
        return varDecls.get(index);
    }

    public void set(int index, VarDecl varDecl) {
        varDecls.set(index, varDecl);
    }

    public void remove(int index) {
        varDecls.remove(index);
    }

    public void clear() {
        varDecls.clear();
    }

    public int size() {
        return varDecls.size();
    }

    public void printAST() {
        for (VarDecl varDecl : varDecls) {
            varDecl.printAST();
        }
    }
}
