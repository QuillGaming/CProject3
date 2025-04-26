package scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CMinusScanner implements Scanner {
    private BufferedReader inFile;
    private Token nextToken;
    private String currentLine;
    private int currentLinePos;
    private static final Map<String, TokenType> reservedWords;

    static {
        reservedWords = new HashMap<>();
        reservedWords.put("else", TokenType.ELSE);
        reservedWords.put("if", TokenType.IF);
        reservedWords.put("int", TokenType.INT);
        reservedWords.put("return", TokenType.RETURN);
        reservedWords.put("void", TokenType.VOID);
        reservedWords.put("while", TokenType.WHILE);
    }

    public CMinusScanner(String filename) {
        try {
            inFile = new BufferedReader(new FileReader(filename));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: could not find " + filename);
            return;
        }
        currentLine = "";
        currentLinePos = 0;
        nextToken = scanToken();
    }

    public Token getNextToken() {
        Token returnToken = nextToken;
        if (nextToken.getType() != TokenType.ENDFILE)
            nextToken = scanToken();
        return returnToken;
    }

    public Token viewNextToken() {
        return nextToken;
    }

    public Token scanToken() {
        StringBuilder tokenBuilder = new StringBuilder();
        State state = State.START;
        TokenType currentToken = null;

        while (state != State.DONE) {
            int c = getNextChar();
            boolean save = true;

            switch (state) {
                case DONE:
                    break;
                case START:
                    if (Character.isDigit(c)) {
                        state = State.INNUM;
                    }
                    else if (Character.isLetter(c)) {
                        state = State.INID;
                    }
                    else if (Character.isWhitespace(c)) {
                        save = false;
                    }
                    else if (c == '<') {
                        state = State.INLT;
                    }
                    else if (c == '>') {
                        state = State.INGT;
                    }
                    else if (c == '=') {
                        state = State.INEQ;
                    }
                    else if (c == '!') {
                        state = State.INNOT;
                    }
                    else if (c == '/') {
                        save = false;
                        state = State.INSLASH;
                    }
                    else {
                        state = State.DONE;
                        currentToken = switch (c) {
                            case -1 -> {
                                save = false;
                                yield TokenType.ENDFILE;
                            }
                            case '+' -> TokenType.PLUS;
                            case '-' -> TokenType.MINUS;
                            case '*' -> TokenType.TIMES;
                            case ';' -> TokenType.SEMI;
                            case ',' -> TokenType.COMMA;
                            case '(' -> TokenType.LPAREN;
                            case ')' -> TokenType.RPAREN;
                            case '[' -> TokenType.LBRACK;
                            case ']' -> TokenType.RBRACK;
                            case '{' -> TokenType.LBRACE;
                            case '}' -> TokenType.RBRACE;
                            default -> TokenType.ERROR;
                        };
                    }
                    break;
                case INSLASH:
                    if (c == '*') {
                        state = State.INCOMMENT;
                        save = false;
                    }
                    else {
                        state = State.DONE;
                        ungetNextChar();
                        save = true;
                        tokenBuilder.append('/');
                        currentToken = TokenType.OVER;
                    }
                    break;
                case INCOMMENT:
                    save = false;
                    if (c == -1) {
                        currentToken = TokenType.ERROR;
                        state = State.DONE;
                    }
                    else if (c == '*') {
                        state = State.INCOMMENTSTAR;
                    }
                    break;
                case INCOMMENTSTAR:
                    save = false;
                    if (c == -1) {
                        currentToken = TokenType.ERROR;
                        state = State.DONE;
                    }
                    else if (c == '/') {
                        state = State.START;
                    }
                    else if (c != '*') {
                        state = State.INCOMMENT;
                    }
                    break;
                case INLT:
                    state = State.DONE;
                    if (c == '=')
                        currentToken = TokenType.LTE;
                    else {
                        ungetNextChar();
                        currentToken = TokenType.LT;
                    }
                    break;
                case INGT:
                    state = State.DONE;
                    if (c == '=')
                        currentToken = TokenType.GTE;
                    else {
                        ungetNextChar();
                        currentToken = TokenType.GT;
                    }
                    break;
                case INEQ:
                    state = State.DONE;
                    if (c == '=')
                        currentToken = TokenType.EQ;
                    else {
                        ungetNextChar();
                        currentToken = TokenType.ASSIGN;
                    }
                    break;
                case INNOT:
                    state = State.DONE;
                    if (c == '=')
                        currentToken = TokenType.NEQ;
                    else {
                        ungetNextChar();
                        save = false;
                        currentToken = TokenType.ERROR;
                    }
                    break;
                case INNUM:
                    if (Character.isLetter(c)) {
                        //tokenString += (char) c;
                        currentToken = TokenType.ERROR;
                        state = State.DONE;
                    }
                    else if (!Character.isDigit(c)) {
                        ungetNextChar();
                        save = false;
                        state = State.DONE;
                        currentToken = TokenType.NUM;
                    }
                    break;
                case INID:
                    if (!Character.isLetterOrDigit(c)) {
                        ungetNextChar();
                        save = false;
                        state = State.DONE;
                        currentToken = TokenType.ID;
                    }
            }

            if (save)
                tokenBuilder.append((char) c);

            if (state == State.DONE && currentToken == TokenType.ID)
                currentToken = reservedWords.getOrDefault(tokenBuilder.toString(), TokenType.ID);
        }
        return new Token(currentToken, tokenBuilder.toString());
    }

    private int getNextChar() {
        int linePos;
        if (currentLinePos >= currentLine.length()) {
            try {
                String line = inFile.readLine();
                if (line != null) {
                    currentLine = line + '\n';
                    currentLinePos = 0;
                    linePos = currentLinePos;
                    currentLinePos++;
                    return currentLine.charAt(linePos);
                }
                else {
                    return -1;
                }
            }
            catch (IOException e) {
                System.out.println("Error: failed to read file.");
                return -1;
            }
        }
        linePos = currentLinePos;
        currentLinePos++;
        return currentLine.charAt(linePos);
    }

    private void ungetNextChar() {
        if (currentLinePos > 0) {
            currentLinePos--;
        }
    }
}
