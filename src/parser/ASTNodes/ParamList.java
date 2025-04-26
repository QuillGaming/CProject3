package parser.ASTNodes;

import java.util.ArrayList;

public class ParamList {
    private final ArrayList<Param> params;

    public ParamList() {
        params = new ArrayList<>();
    }

    public ArrayList<Param> getList() {
        return params;
    }

    public void add(Param param) {
        params.add(param);
    }

    public Param get(int index) {
        return params.get(index);
    }

    public void set(int index, Param param) {
        params.set(index, param);
    }

    public void remove(int index) {
        params.remove(index);
    }

    public void clear() {
        params.clear();
    }

    public int size() {
        return params.size();
    }

    public boolean isEmpty() {
        return params.isEmpty();
    }
}
