📘 README — KAY Scanner & Parser Project
👥 Authors

Zara Hameedi  

Erjon Ahmetaj


🧩 Project Overview

This project implements the scanner (lexer) and parser for the KAY programming language, as described in the assignment specification.

Your implementation includes:

A fully functional lexical analyzer (TokenStream.java)

A symbol-rich Token model (Token.java)

A simple scanner driver (ScannerDemo.java)

Later: a complete parser (ConcreteSyntax.java)

Later: a parse tree driver (ParserDemo.java)

The scanner and parser will be tested against visible and hidden .kay programs.

📁 Current Files
Token.java
TokenStream.java
ScannerDemo.java
prog1.kay


Token.java — stores token type and value.

TokenStream.java — implements the lexical rules of KAY.

ScannerDemo.java — prints tokens from a .kay file.

prog1.kay — sample test program for your scanner.

🔧 How to Compile and Run

From the project folder:

Compile
javac *.java

Run Scanner
java ScannerDemo


This will scan prog1.kay and print each token:

Token 1 - Type: Keyword - Value: main
Token 2 - Type: Separator - Value: {
...
Token N - Type: EOF - Value: EOF

🧠 KAY Language Highlights (per specification)

Program structure:

main {
   ...
}


Types:

integer
bool


Assignment operator:

:=


Boolean literals:

True
False


KAY supports:

identifiers

integer literals

comments (// and /* ... */)

operators (+ - * / %, && || !, < <= > >= == !=, etc.)

🧪 Testing

Use .kay files to test scanner functionality.
Example:

prog1.kay
main{
  integer i;
  i := 7;
}


Run:

java ScannerDemo


Expected output involves correct keywords, separators, identifiers, operators, literals, and EOF.

🗂 Commit Roadmap

The project is being developed in small, clean commits to reduce bugs and improve traceability:

Commit Group 1 — Scanner
Commit	Description
1	Project initialization, scaffolding, sample .kay file
2	Basic tokenization (identifiers, literals, keywords, separators, :=)
3	Add operators (+ - * / % < <= > >= == != &&
4	Lexical errors (e.g., 3a → Other)
5	Add comments (//, /* ... */)
6	Final cleanup + hidden test prep
Commit Group 2 — Parser
Commit	Description
7	Parser scaffolding, match(), error format
8	Program-level grammar (main{…})
9	Declarations
10	Statements (assignment, blocks, if/else, while)
11	Expressions (recursive descent)
12	Final formatting and polish
🙌 Contribution Style

Frequent small commits

Teamwork shown through alternating commit authorship

No large “all-in-one” pushes

Follow KAY formatting EXACTLY (professor’s output tester is strict)

📌 Notes

Hidden test programs passed in by the professor may include tricky edge cases.

The scanner should gracefully handle whitespace, comments, invalid tokens, and operator sequences.

The parser should reproduce exact output formatting in the assignment instructions
