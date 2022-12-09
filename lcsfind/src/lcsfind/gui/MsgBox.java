package lcsfind.gui;
import javax.swing.JOptionPane;

public class MsgBox {
    // Both MsgBox functions are asynchronous,
    // since they must not cause trouble even when the thread that called the functions are killed
    // when msgBoxes are still alive.
   public static void showError(String message) {
       new Thread(() -> {
           JOptionPane.showMessageDialog(null, message,
                   "lcsfind", JOptionPane.ERROR_MESSAGE);
       }).start();
   }

    public static void showInfo(String message) {
        new Thread(() -> {
            JOptionPane.showMessageDialog(null, message,
                    "lcsfind", JOptionPane.INFORMATION_MESSAGE);
        }).start();
    }
}
