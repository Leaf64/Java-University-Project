package printPackage;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Printer {

    private static PrintStream fileOut;

    static {
        try {
            fileOut = new PrintStream("./out.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void println(String text) throws FileNotFoundException {
        System.out.println(text);

        fileOut.println(text);
    }

    public static void println(int text) throws FileNotFoundException {
        System.out.println(text);

        fileOut.println(text);
    }

    public static void print(String text) throws FileNotFoundException {
        System.out.print(text);

        fileOut.print(text);
    }

    public static void print(int text) throws FileNotFoundException {
        System.out.print(text);

        fileOut.print(text);
    }

}
