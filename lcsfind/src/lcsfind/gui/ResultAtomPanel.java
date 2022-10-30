package lcsfind.gui;
import java.io.File;
import javax.swing.*;
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
        // TODO: 파일 열기. 'explorer' 프로세스 시작하고 argument로 파일 경로 주면 알아서 열림. 명선담당
        MsgBox.show(filePath);
    }

}
