import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

    private BufferedReader input;
    private int currentChar;   // last character read (as int)

    // ---------- constructor ----------
    public TokenStream(String filename) {
        try {
            input = new BufferedReader(new FileReader(filename));
            advance();   // read first character
        } catch (IOException e) {
            throw new RuntimeException("Cannot open file: " + filename);
        }
    }

    // move to the next character
    private void advance() {
        try {
            currentChar = input.read();   // -1 = EOF
        } catch (IOException e) {
            currentChar = -1;
        }
    }

    // skip spaces, tabs, newlines, etc.
    private void skipWhitespace() {
        while (currentChar != -1 && Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    // ---------- public token API ----------
    public Token nextToken() {
        // 1. ignore whitespace
        skipWhitespace();

        // 2. EOF
        if (currentChar == -1) {
            return new Token("EOF", "EOF");
        }

        char ch = (char) currentChar;

        // 3. Identifiers / keywords / bool literals
        if (Character.isLetter(ch)) {
            StringBuilder sb = new StringBuilder();

            while (currentChar != -1 &&
                   (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                sb.append((char) currentChar);
                advance();
            }

            String lexeme = sb.toString();

            // boolean literals in KAY
            if (lexeme.equals("True") || lexeme.equals("False")) {
                return new Token("Literal", lexeme);
            }

            // KAY keywords we know so far
            if (lexeme.equals("main") ||
                lexeme.equals("integer") ||
                lexeme.equals("bool")) {
                return new Token("Keyword", lexeme);
            }

            // otherwise identifier
            return new Token("Identifier", lexeme);
        }

        // 4. Integer literals
        if (Character.isDigit(ch)) {
            StringBuilder sb = new StringBuilder();

            while (currentChar != -1 && Character.isDigit(currentChar)) {
                sb.append((char) currentChar);
                advance();
            }

            return new Token("Literal", sb.toString());
        }

        // 5. Assignment operator :=
        if (ch == ':') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", ":=");
            } else {
                // ':' alone is not valid
                return new Token("Other", ":");
            }
        }

        // 6. Separators
        if ("(){};,".indexOf(ch) >= 0) {
            advance();
            return new Token("Separator", Character.toString(ch));
        }

        // 7. Fallback: unknown stuff (we’ll refine later)
        advance();
        return new Token("Other", Character.toString(ch));
    }
}
