package lcsfind;
import java.util.Scanner;
import lcsfind.gui.MainWindow;

public class App {
    public static void main(String[] args) throws Exception {
        for (java.io.File f : lcsfind.LcsSearch.search("/Users/tmin002/workspace", "App.tsx")) {
            System.out.println(f.getAbsolutePath());
        }
        /*new MainWindow();
        Scanner f = new Scanner(System.in);
        f.nextLine();
        f.close();*/
    }
}
