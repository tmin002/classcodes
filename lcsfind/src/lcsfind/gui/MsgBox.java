package lcsfind.gui;
import javax.swing.JOptionPane;

public class MsgBox {
   public static void showError(String message) {
    JOptionPane.showMessageDialog(null, message, 
        "lcsfind", JOptionPane.ERROR_MESSAGE);
   }

    public static void showInfo(String message) {
        JOptionPane.showMessageDialog(null, message,
                "lcsfind", JOptionPane.INFORMATION_MESSAGE);
    }
}
