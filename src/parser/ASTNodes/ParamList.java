package parser.ASTNodes;

import lowlevel.FuncParam;

import java.util.ArrayList;

import static lowlevel.Data.TYPE_INT;

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

    public void printAST() {
        for (Param param : params) {
            param.printAST();
        }
    }

    public FuncParam genLLCode() {
        FuncParam firstParam = null;
        FuncParam currParam;
        FuncParam prevParam = null;
        for (Param param : params) {
            currParam = param.genLLCode();

            if (firstParam == null) {
                firstParam = currParam;
            }

            if (prevParam != null) {
                prevParam.setNextParam(currParam);
            }
            prevParam = currParam;
        }
        return firstParam;
    }
}
