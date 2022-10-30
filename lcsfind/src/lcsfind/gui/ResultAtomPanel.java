package lcsfind.gui;
import java.io.File;
import javax.swing.*;

import java.awt.*;

public class ResultAtomPanel extends JPanel {
    private File file;

    public ResultAtomPanel(File file) {
        this.file = file; 
        setSize(4000, 40);
        setLayout(null);
        setBackground(Color.white);

        JLabel thumbnail = new JLabel();
        String imgDir = file.isFile() ? "file" : "folder";
        thumbnail.setIcon(new ImageIcon(getClass().getResource("/lcsfind/resources/" + imgDir + ".png")));
        thumbnail.setBounds(0, 0, 30, 30);
        add(thumbnail);

        JLabel nameLabel = new JLabel(file.getName());
        JLabel pathLabel = new JLabel(file.getPath());
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        pathLabel.setFont(new Font("Dialog", Font.PLAIN, 11));
        nameLabel.setBounds(35, 0, 4000, 15);
        pathLabel.setBounds(35, 17, 4000, 15);
        add(nameLabel);
        add(pathLabel);

        setVisible(true);
    }    

}
