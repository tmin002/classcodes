package lcsfind.gui;
import lcsfind.CheckOS;
import java.io.File;
import javax.swing.*;
import java.io.IOException;
import java.awt.event.*;
import java.awt.*;

public class ResultAtomPanel extends JPanel {

    public ResultAtomPanel(File file) {

        // initialize
        setSize(4000, 40);
        setLayout(null);
        setBackground(Color.white);

        // define click mouseadapter
        MouseAdapter clickMa = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                clicked(file.getAbsolutePath());
            }
          };

        // thumbnail
        JLabel thumbnail = new JLabel();
        String imgDir = file.isFile() ? "file" : "folder";
        thumbnail.setIcon(new ImageIcon(getClass().getResource("/lcsfind/resources/" + imgDir + ".png")));
        thumbnail.setBounds(0, 0, 30, 30);
        thumbnail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        thumbnail.addMouseListener(clickMa);
        add(thumbnail);

        // file name, file full path
        JLabel nameLabel = new JLabel(file.getName());
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        nameLabel.setBounds(35, 0, 4000, 15);
        nameLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nameLabel.addMouseListener(clickMa);

        JLabel pathLabel = new JLabel(file.getPath());
        pathLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        pathLabel.setBounds(35, 17, 4000, 15);
        pathLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        pathLabel.addMouseListener(clickMa);

        add(nameLabel);
        add(pathLabel);

        // show
        setVisible(true);
    }    

    public void clicked(String filePath) {
        CheckOS.OS os = CheckOS.getOS();
        String[] cmd = null;

        try {
            if (os == CheckOS.OS.WINDOWS) {
                cmd = new String[] {"explorer", filePath};
            } else if (os == CheckOS.OS.LINUX || os == CheckOS.OS.SOLARIS) {
                cmd = new String[] {"xdg-open", filePath};
            } else if (os == CheckOS.OS.MAC) {
                cmd = new String[] {"open", filePath};
            } else {
                MsgBox.show("Opening files in this operating system is not supported yet.");
                return;
            }
            Runtime.getRuntime().exec(cmd);            
        } catch (IOException e) {
            MsgBox.show("error while opening file" + filePath + "\n" + e + ": " + e.getMessage());
        }

    }

}
