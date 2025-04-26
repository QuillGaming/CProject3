package scanner;

public class Token {
    private TokenType tokenType;
    private String tokenData;

    public Token(TokenType type) {
        this(type, null);
    }

    public Token(TokenType type, String data) {
        tokenType = type;
        tokenData = data;
    }

    public TokenType getType() { return tokenType; }
    public String getTokenData() { return tokenData; }
}
