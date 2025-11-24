public class ScannerDemo {
    public static void main(String[] args) {

        // later we can make this use args[0]; for now hard-code the test file
        TokenStream ts = new TokenStream("prog1.kay");

        Token t = ts.nextToken();
        int i = 1;

        while (!t.type.equals("EOF")) {
            System.out.println("Token " + i + " - " + t);
            t = ts.nextToken();
            i++;
        }
    }
}
