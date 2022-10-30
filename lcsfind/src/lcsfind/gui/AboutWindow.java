package lcsfind.gui;

import javax.swing.*;

// TODO: JFrame "AboutWindow" 만들기. 명선담당
public class AboutWindow extends JFrame {
    public AboutWindow() {
        // 창 제목 설정
        setTitle("About lcsfind");

        // 크기 설정
        setSize(200, 100);

        // 크기조절 불가능하게
        setResizable(false);

        // JLabel
        JLabel aboutString = new JLabel("안녕하세요");
        add(aboutString);

        // Show
        setVisible(true);
    }
}
