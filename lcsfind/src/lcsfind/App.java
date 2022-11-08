package lcsfind;
import lcsfind.gui.MainWindow;
import lcsfind.gui.MsgBox;

import java.io.File;

public class App {
    public static void main(String[] args) throws Exception {
        CheckOS.determineOS();
        new MainWindow();
    }
}
