package scanner;

public enum State {
    START, INLT, INGT, INEQ, INNOT, INSLASH,
    INCOMMENT, INCOMMENTSTAR, INNUM, INID, DONE
}
