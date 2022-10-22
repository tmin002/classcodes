import java.util.Scanner;
import gui.MainWindow;

public class App {
    public static void main(String[] args) throws Exception {
        new MainWindow();
        Scanner f = new Scanner(System.in);
        f.nextLine();
        f.close();
    }
}
