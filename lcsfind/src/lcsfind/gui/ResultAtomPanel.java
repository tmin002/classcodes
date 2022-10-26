package lcsfind.gui;
import java.io.File;
import javax.swing.*;

import java.awt.*;

public class ResultAtomPanel extends JPanel {
    private File file;

    public ResultAtomPanel(File file) {
        this.file = file; 
        setSize(500, 55);
        setLayout(null);

        JLabel thumbnail = new JLabel();
        String imgDir = file.isFile() ? "file" : "folder";
        thumbnail.setIcon(new ImageIcon(getClass().getResource("/resources/" + imgDir + ".png")));
        thumbnail.setBounds(0, 0, 50, 50);
        add(thumbnail);

        JLabel nameLabel = new JLabel(file.getName());
        JLabel pathLabel = new JLabel(file.getPath());
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        pathLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        nameLabel.setBounds(55, 0, 445, 25);
        pathLabel.setBounds(55, 25, 445, 24);
        add(nameLabel);
        add(pathLabel);

        setVisible(true);
    }    

}
