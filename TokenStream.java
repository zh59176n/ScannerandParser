import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TokenStream {

    private BufferedReader input;
    private int currentChar;   // last character read (as an int)

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
        // main loop: skip whitespace and comments
        skipWhitespace();

        // also skip comments if present (might need to loop)
        while (currentChar == '/') {
            advance();  // look at what comes after '/'
            if (currentChar == '/') {
                // line comment: skip until end of line or EOF
                while (currentChar != -1 && currentChar != '\n' && currentChar != '\r') {
                    advance();
                }
                skipWhitespace();
            } else if (currentChar == '*') {
                // block comment: skip until */
                advance(); // consume '*'
                boolean endFound = false;
                while (currentChar != -1) {
                    if (currentChar == '*') {
                        advance();
                        if (currentChar == '/') {
                            advance(); // consume '/'
                            endFound = true;
                            break;
                        }
                    } else {
                        advance();
                    }
                }
                // after block comment, skip whitespace again
                skipWhitespace();
            } else {
                // it was just a '/' operator
                return new Token("Operator", "/");
            }

            skipWhitespace();
        }

        // EOF check after skipping whitespace/comments
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

            // boolean literals in KAY
            if (lexeme.equals("True") || lexeme.equals("False")) {
                return new Token("Literal", lexeme);
            }

            // KAY keywords we know from the spec
            if (lexeme.equals("main") ||
                lexeme.equals("integer") ||
                lexeme.equals("bool") ||
                lexeme.equals("if") ||
                lexeme.equals("else") ||
                lexeme.equals("while")) {
                return new Token("Keyword", lexeme);
            }

            // otherwise identifier
            return new Token("Identifier", lexeme);
        }

        // ---------- Integer literals (and bad 3a-style tokens) ----------
        if (Character.isDigit(ch)) {
            StringBuilder sb = new StringBuilder();

            // grab the digits
            while (currentChar != -1 && Character.isDigit(currentChar)) {
                sb.append((char) currentChar);
                advance();
            }

            // if a letter or '_' immediately follows → lexical error like 3a
            if (currentChar != -1 && (Character.isLetter(currentChar) || currentChar == '_')) {
                while (currentChar != -1 &&
                       (Character.isLetterOrDigit(currentChar) || currentChar == '_')) {
                    sb.append((char) currentChar);
                    advance();
                }
                // 31 3a example → Type: Other - Value: 3a
                return new Token("Other", sb.toString());
            }

            // normal integer literal
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

        // ---------- Logical operators &&, ||, ! ----------
        if (ch == '&') {
            advance();
            if (currentChar == '&') {
                advance();
                return new Token("Operator", "&&");
            } else {
                return new Token("Other", "&");
            }
        }

        if (ch == '|') {
            advance();
            if (currentChar == '|') {
                advance();
                return new Token("Operator", "||");
            } else {
                return new Token("Other", "|");
            }
        }

        if (ch == '!') {
            advance();
            if (currentChar == '=') {
                advance();
                return new Token("Operator", "!=");
            } else {
                // logical NOT
                return new Token("Operator", "!");
            }
        }

        // ---------- Relational / equality operators ----------
        if (ch == '<' || ch == '>' || ch == '=') {
            char first = ch;
            advance();
            if (currentChar == '=') {
                char second = (char) currentChar;
                advance();
                return new Token("Operator", "" + first + second);  // <= >= ==
            } else {
                // single operators < > =
                return new Token("Operator", Character.toString(first));
            }
        }

        // ---------- Arithmetic operators (+, -, *, / that aren't comments) ----------
        if (ch == '+' || ch == '-' || ch == '*') {
            advance();
            return new Token("Operator", Character.toString(ch));
        }

        // note: '/' covered earlier (comment or Operator "/")

        // ---------- Separators ----------
        if ("(){};,".indexOf(ch) >= 0) {
            advance();
            return new Token("Separator", Character.toString(ch));
        }

        // ---------- Fallback: unknown character ----------
        advance();
        return new Token("Other", Character.toString(ch));
    }
}
