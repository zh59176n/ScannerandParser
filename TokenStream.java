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

        // skip whitespace
        skipWhitespace();

        // ---------- skip comments ----------
        while (currentChar == '/') {
            advance();
            if (currentChar == '/') {
                // line comment
                while (currentChar != -1 && currentChar != '\n' && currentChar != '\r') {
                    advance();
                }
                skipWhitespace();
            } else if (currentChar == '*') {
                // block comment
                advance();
                boolean closed = false;
                while (currentChar != -1) {
                    if (currentChar == '*') {
                        advance();
                        if (currentChar == '/') {
                            advance();
                            closed = true;
                            break;
                        }
                    } else {
                        advance();
                    }
                }
                skipWhitespace();
            } else {
                // just '/'
                return new Token("Operator", "/");
            }
        }

        // end of file
        if (currentChar == -1) {
            return new Token("EOF", "EOF");
        }

        char ch = (char) currentChar;

        // ---------- Identifiers / Keywords / Bool literals ----------
        if (Character.isLetter(ch)) {
            StringBuilder sb = new StringBuilder();

            while (currentChar != -1 &&
                    (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                sb.append((char) currentChar);
                advance();
            }

            String lexeme = sb.toString();

            // boolean literals
            if (lexeme.equals("True") || lexeme.equals("False")) {
                return new Token("Literal", lexeme);
            }

            // keywords
            if (lexeme.equals("main") ||
                lexeme.equals("integer") ||
                lexeme.equals("bool") ||
                lexeme.equals("if") ||
                lexeme.equals("else") ||
                lexeme.equals("while")) {
                return new Token("Keyword", lexeme);
            }

            return new Token("Identifier", lexeme);
        }

        // ---------- Integer literals (and numeric lexical errors like 3a) ----------
        if (Character.isDigit(ch)) {
            StringBuilder sb = new StringBuilder();

            while (currentChar != -1 && Character.isDigit(currentChar)) {
                sb.append((char) currentChar);
                advance();
            }

            // if letters follow → lexical error
            if (currentChar != -1 &&
                (Character.isLetter(currentChar) || currentChar == '_')) {
                while (currentChar != -1 &&
                        (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                    sb.append((char) currentChar);
                    advance();
                }
                return new Token("Other", sb.toString());
            }

            return new Token("Literal", sb.toString());
        }

        // ---------- Assignment operator := ----------
        if (ch == ':') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", ":=");
            } else {
                return new Token("Other", ":");
            }
        }

        // ---------- Logical operators ----------
        // &&
        if (ch == '&') {
            advance();
            if (currentChar == '&') {
                advance();
                return new Token("Operator", "&&");
            }
            return new Token("Other", "&");
        }

        // ||
        if (ch == '|') {
            advance();
            if (currentChar == '|') {
                advance();
                return new Token("Operator", "||");
            }
            return new Token("Other", "|");
        }

        // ! or !=
        if (ch == '!') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", "!=");
            }
            return new Token("Operator", "!");
        }

        // ---------- Relational + equality operators ----------
        if (ch == '<' || ch == '>' || ch == '=') {
            char first = ch;
            advance();
            if (currentChar == '=') {
                char second = (char) currentChar;
                advance();
                return new Token("Operator", "" + first + second);
            } else {
                return new Token("Operator", Character.toString(first));
            }
        }

        // ---------- Arithmetic operators ----------
        if (ch == '+' || ch == '-' || ch == '*') {
            advance();
            return new Token("Operator", Character.toString(ch));
        }

        // modulo operator
        if (ch == '%') {
            advance();
            return new Token("Operator", "%");
        }

        // note: '/' handled earlier

        // ---------- Separators ----------
        if ("(){};,".indexOf(ch) >= 0) {
            advance();
            return new Token("Separator", Character.toString(ch));
        }

        // ---------- Unknown character ----------
        advance();
        return new Token("Other", Character.toString(ch));
    }
}
