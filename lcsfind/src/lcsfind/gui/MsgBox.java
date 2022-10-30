package lcsfind.gui;
import javax.swing.JOptionPane;

public class MsgBox {
   public static void show(String message) {
    JOptionPane.showMessageDialog(null, message, 
        "lcsfind", JOptionPane.ERROR_MESSAGE);
   } 
}
