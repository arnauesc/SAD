import java.io.*;

public class Terminal {
    
    public static void main(String[] args) {
        int col = 0, line = 0;
        try {
            col = Integer.parseInt(System.getenv("COLUMNS"));
            line = Integer.parseInt(System.getenv("LINES"));
        } catch (NumberFormatException ex) {
            System.out.println("Els valors de columnes i linies ha de ser enter");
            return;
        }

        System.out.println("Num columnes =  " + col);
        System.out.println("Num files =  " + line);
    }
}
