package gui;
import java.io.File;
import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setSize(500, 500);
        setLayout(null);
        setVisible(true);

        for (int i=0; i<30; i++) {
            ResultAtomPanel aa = new ResultAtomPanel(new File("/Users/tmin002/workspace/lcsfind/lcsfind/README.md"));
            add(aa);
            aa.setBounds(0, 60*i, 500, 55);
        }
    }
}
