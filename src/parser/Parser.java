package parser;

public interface Parser {
    Program parse();
    void printAST(Program parseTree);
}