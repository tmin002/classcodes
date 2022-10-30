package lcsfind.gui;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {

    private JLabel fileNameLabel = new JLabel("File name to search: ");
    private JLabel fromPathLabel = new JLabel("Serach from: ");

    private JTextField fileNameText = new JTextField("", 50);
    private JTextField fromPathText = new JTextField("", 50);
    private JButton fromPathButton = new JButton("..");

    private JPanel resultPanel = new JPanel();
    private JScrollPane resultScrollPane = new JScrollPane(resultPanel);

    private JButton doSearchButton = new JButton("search");
    private JButton aboutButton = new JButton("about");

    private SpringLayout rootLayout = new SpringLayout();

    public MainWindow() {

        // basic
        Container root = getContentPane();
        root.setLayout(rootLayout);

        //// fileNameLabel 
        rootLayout.putConstraint(SpringLayout.WEST, fileNameLabel, 15, SpringLayout.WEST, root);
        rootLayout.putConstraint(SpringLayout.NORTH, fileNameLabel,10, SpringLayout.NORTH, root);
        root.add(fileNameLabel);

        //// fileNameText
        rootLayout.putConstraint(SpringLayout.WEST, fileNameText, 0, SpringLayout.EAST, fileNameLabel);
        rootLayout.putConstraint(SpringLayout.EAST, fileNameText, -15, SpringLayout.EAST, root);
        rootLayout.putConstraint(SpringLayout.NORTH, fileNameText, 5, SpringLayout.NORTH, root);
        root.add(fileNameText);

        //// fromPathLabel
        rootLayout.putConstraint(SpringLayout.EAST, fromPathLabel, 0, SpringLayout.EAST, fileNameLabel);
        rootLayout.putConstraint(SpringLayout.NORTH, fromPathLabel, 5, SpringLayout.SOUTH, fileNameText);
        root.add(fromPathLabel);

        //// fromPathText
        rootLayout.putConstraint(SpringLayout.WEST, fromPathText, 0, SpringLayout.EAST, fromPathLabel);
        rootLayout.putConstraint(SpringLayout.EAST, fromPathText, 0, SpringLayout.WEST, fromPathButton);
        rootLayout.putConstraint(SpringLayout.NORTH, fromPathText, 0, SpringLayout.SOUTH, fileNameText);
        root.add(fromPathText);

        //// fromPathButton
        rootLayout.putConstraint(SpringLayout.EAST, fromPathButton, -15, SpringLayout.EAST, root);
        rootLayout.putConstraint(SpringLayout.NORTH, fromPathButton, 0, SpringLayout.NORTH, fromPathText);
        fromPathButton.addActionListener(e -> selectFromPath());
        root.add(fromPathButton);

        //// resultPanel & resultScrollPane
        rootLayout.putConstraint(SpringLayout.NORTH, resultScrollPane, 5, SpringLayout.SOUTH, fromPathButton);
        rootLayout.putConstraint(SpringLayout.SOUTH, resultScrollPane, 0, SpringLayout.NORTH, doSearchButton);
        rootLayout.putConstraint(SpringLayout.WEST, resultScrollPane, 15, SpringLayout.WEST, root);
        rootLayout.putConstraint(SpringLayout.EAST, resultScrollPane, -15, SpringLayout.EAST, root);

        resultPanel.setLayout(null);
        resultPanel.setBackground(Color.white);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        root.add(resultScrollPane);

        //// doSearchButton
        rootLayout.putConstraint(SpringLayout.SOUTH, doSearchButton, -5, SpringLayout.SOUTH, root);
        rootLayout.putConstraint(SpringLayout.EAST, doSearchButton, -15, SpringLayout.EAST, root);
        doSearchButton.addActionListener(e -> doSearch());
        root.add(doSearchButton);

        //// aboutButton
        rootLayout.putConstraint(SpringLayout.SOUTH, aboutButton, -5, SpringLayout.SOUTH, root);
        rootLayout.putConstraint(SpringLayout.WEST, aboutButton, 15, SpringLayout.WEST, root);
        aboutButton.addActionListener(e -> {
            msgBox("Work in progress");
        });
        root.add(aboutButton);


        //// errorLabel

        // final
        setSize(500, 500);
        setTitle("lcsfind");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

    }

    // result panel repaint
    private void updateResultPanel() {
        resultScrollPane.revalidate();
        resultScrollPane.repaint();
    }

    // messagebox
    public void msgBox(String message) {
        JOptionPane.showMessageDialog(null, message, 
            "lcsfind", JOptionPane.ERROR_MESSAGE);
    }

    // search
    private void doSearch() {

        // validation
        String fromPath = fromPathText.getText();
        String fileName = fileNameText.getText();

        if (fileName == null || fileName.length() == 0) {
            msgBox("Enter search start directory.");
            return;
        }
        if (!(new File(fromPath).isDirectory())) {
            msgBox("Wrong search start path.");
            return;
        }

        // reset resultpanel
        resultPanel.removeAll();
        updateResultPanel();

        // search
        int i = 0;
        for (File f : lcsfind.LcsSearch.search(fromPath, fileName)) {
            ResultAtomPanel p = new ResultAtomPanel(f);
            p.setSize(500, 40);
            p.setBounds(0, 40*(i++), 4096, 40);
            resultPanel.add(p);
        } 

        // finish
        if (i == 0) {
            msgBox("No file/folder found.");
        }
        resultPanel.setPreferredSize(new Dimension(this.getSize().width, i*40));
        updateResultPanel();
    }

    // fromPathButton click
    public void selectFromPath() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Search from: ");
        fc.showSaveDialog(this);
        fromPathText.setText(fc.getCurrentDirectory().getAbsolutePath());
    }
}
