package lcsfind;
import lcsfind.gui.MainWindow;

public class App {
    public static void main(String[] args) throws Exception {
        CheckOS.determineOS();
        new MainWindow();
    }
}
