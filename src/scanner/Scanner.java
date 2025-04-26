package scanner;

public interface Scanner {
    Token getNextToken();
    Token viewNextToken();
    Token scanToken();
}
