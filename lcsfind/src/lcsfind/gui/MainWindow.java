package lcsfind.gui;

import java.io.*;
import javax.swing.*;

import lcsfind.LcsSearch;

import java.awt.*;
import java.util.Objects;

public class MainWindow extends JFrame implements SearchProgressListener {

    private JLabel fileNameLabel = new JLabel("File name to search: ");
    private JLabel fromPathLabel = new JLabel("Search from: ");
    private JLabel depthLabel = new JLabel("Search depth: ");
    private JLabel totalLabel = new JLabel("");


    private JTextField fileNameText = new JTextField("", 50);
    private JTextField fromPathText = new JTextField("", 50);
    private JSlider depthSlider = new JSlider(JSlider.HORIZONTAL, 0, 21, 5);
    private JLabel depthSliderLabel = new JLabel(String.valueOf(depthSlider.getValue()));
    private JButton fromPathButton = new JButton("..");

    private JPanel resultPanel = new JPanel();
    private JScrollPane resultScrollPane = new JScrollPane(resultPanel);

    private JButton doSearchButton = new JButton("search");
    private SpringLayout rootLayout = new SpringLayout();

    // Search Instance
    private LcsSearch search;

    public MainWindow() {

        // basic
        Container root = getContentPane();
        root.setLayout(rootLayout);

        //// fileNameLabel 
        rootLayout.putConstraint(SpringLayout.WEST, fileNameLabel, 15, SpringLayout.WEST, root);
        rootLayout.putConstraint(SpringLayout.NORTH, fileNameLabel,2, SpringLayout.NORTH, fileNameText);
        root.add(fileNameLabel);

        //// fileNameText
        rootLayout.putConstraint(SpringLayout.WEST, fileNameText, 5, SpringLayout.EAST, fileNameLabel);
        rootLayout.putConstraint(SpringLayout.EAST, fileNameText, -15, SpringLayout.EAST, root);
        rootLayout.putConstraint(SpringLayout.NORTH, fileNameText, 5, SpringLayout.NORTH, root);
        root.add(fileNameText);

        //// fromPathLabel
        rootLayout.putConstraint(SpringLayout.EAST, fromPathLabel, 0, SpringLayout.EAST, fileNameLabel);
        rootLayout.putConstraint(SpringLayout.NORTH, fromPathLabel, 2, SpringLayout.NORTH, fromPathText);
        root.add(fromPathLabel);

        //// fromPathText
        rootLayout.putConstraint(SpringLayout.WEST, fromPathText, 5, SpringLayout.EAST, fromPathLabel);
        rootLayout.putConstraint(SpringLayout.EAST, fromPathText, 0, SpringLayout.WEST, fromPathButton);
        rootLayout.putConstraint(SpringLayout.NORTH, fromPathText, 10, SpringLayout.SOUTH, fileNameText);
        root.add(fromPathText);

        //// depthLabel
        rootLayout.putConstraint(SpringLayout.EAST, depthLabel, 0, SpringLayout.EAST, fileNameLabel);
        rootLayout.putConstraint(SpringLayout.NORTH, depthLabel, 0, SpringLayout.NORTH, depthSlider);
        root.add(depthLabel);

        //// depthSlider
        rootLayout.putConstraint(SpringLayout.WEST, depthSlider, 0, SpringLayout.EAST, depthLabel);
        rootLayout.putConstraint(SpringLayout.EAST, depthSlider, -15, SpringLayout.WEST, depthSliderLabel);
        rootLayout.putConstraint(SpringLayout.NORTH, depthSlider, 10, SpringLayout.SOUTH, fromPathText);
        depthSlider.addChangeListener(e -> {
            if (depthSlider.getValue() == depthSlider.getMaximum()) {
                depthSliderLabel.setText("infinite");
            } else {
                depthSliderLabel.setText(String.valueOf(depthSlider.getValue()));
            }
        });
        root.add(depthSlider);


        //// depthSliderLabel
        rootLayout.putConstraint(SpringLayout.WEST, depthSliderLabel, 0, SpringLayout.WEST, fromPathButton);
        rootLayout.putConstraint(SpringLayout.NORTH, depthSliderLabel, 0, SpringLayout.NORTH, depthLabel);
        root.add(depthSliderLabel);

        //// fromPathButton
        rootLayout.putConstraint(SpringLayout.EAST, fromPathButton, -15, SpringLayout.EAST, root);
        rootLayout.putConstraint(SpringLayout.NORTH, fromPathButton, 0, SpringLayout.NORTH, fromPathText);
        rootLayout.putConstraint(SpringLayout.SOUTH, fromPathButton, 0, SpringLayout.SOUTH, fromPathText);
        fromPathButton.addActionListener(e -> selectFromPath());
        root.add(fromPathButton);

        //// resultPanel & resultScrollPane
        rootLayout.putConstraint(SpringLayout.NORTH, resultScrollPane, 5, SpringLayout.SOUTH, depthSliderLabel);
        rootLayout.putConstraint(SpringLayout.SOUTH, resultScrollPane, -5, SpringLayout.NORTH, doSearchButton);
        rootLayout.putConstraint(SpringLayout.WEST, resultScrollPane, 15, SpringLayout.WEST, root);
        rootLayout.putConstraint(SpringLayout.EAST, resultScrollPane, -15, SpringLayout.EAST, root);

        resultPanel.setLayout(null);
        resultPanel.setBackground(Color.white);
        resultScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        root.add(resultScrollPane);

        //// doSearchButton
        rootLayout.putConstraint(SpringLayout.SOUTH, doSearchButton, -5, SpringLayout.SOUTH, root);
        rootLayout.putConstraint(SpringLayout.EAST, doSearchButton, -15, SpringLayout.EAST, root);
        doSearchButton.addActionListener(e -> {
            if (doSearchButton.getText().equals("search")) {
                // search not started
                if (doSearch()) {
                    doSearchButton.setText("cancel");
                }
            } else {
                // search started, so cancel
                cancelSearch();
                doSearchButton.setText("search");
            }
        });
        root.add(doSearchButton);

        //// totalLabel
        rootLayout.putConstraint(SpringLayout.SOUTH, totalLabel, -10, SpringLayout.SOUTH, root);
        rootLayout.putConstraint(SpringLayout.WEST, totalLabel, 15, SpringLayout.WEST, root);
        rootLayout.putConstraint(SpringLayout.EAST, totalLabel, -100, SpringLayout.EAST, doSearchButton);
        root.add(totalLabel);

        // final
        setIconImage(new ImageIcon(
                Objects.requireNonNull(
                        getClass().getResource("/lcsfind/resources/lcsfind_icon.png"))).getImage());
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

    // search
    private boolean doSearch() {

        // validation
        String fromPath = fromPathText.getText();
        String fileName = fileNameText.getText();

        if (fileName == null || fileName.length() == 0) {
            MsgBox.showError("Enter search start directory.");
            return false;
        }
        if (!(new File(fromPath).isDirectory())) {
            MsgBox.showError("Wrong search start path.");
            return false;
        }

        // reset resultPanel
        resultPanel.removeAll();
        updateResultPanel();

        // calc depth
        int i = 0;
        int depth = depthSlider.getValue();
        if (depth == depthSlider.getMaximum()) {
            depth = LcsSearch.INFINITE;
        }

        // reset search count
        this.searchCount = 0;
        this.foundCount = 0;

        // start search thread
        search = new LcsSearch(fromPath, fileName, depth, this);
        search.startSearchAsync();

        return true;
    }

    // cancel search
    private void cancelSearch() {
        search.stopSearch();
        updateStatusText("canceled");
    }

    // fromPathButton click
    private void selectFromPath() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Search from: ");
        fc.showSaveDialog(this);
        fromPathText.setText(fc.getCurrentDirectory().getAbsolutePath());
    }


    private int searchCount = 0, foundCount = 0;
    private void updateStatusText(String currentSearching) {
        totalLabel.setText("searched " + searchCount + ", found " + foundCount +
                (currentSearching.length() == 0 ? "" : ", " + currentSearching));
    }
    @Override
    public void onFileFound(File f, int foundCount) {
        this.foundCount = foundCount;
        ResultAtomPanel p = new ResultAtomPanel(f);
        p.setSize(500, 40);
        p.setBounds(0, 40*(foundCount-1), 4096, 40);
        resultPanel.add(p);
        resultPanel.setPreferredSize(new Dimension(this.getSize().width, foundCount*40));
        updateStatusText(f.getAbsolutePath());
    }
    @Override
    public void onFileSearching(File f, int searchCount) {
        this.searchCount = searchCount;
        updateStatusText(f.getAbsolutePath());
    }
    @Override
    public void onSearchFinished(boolean killed) {
        updateStatusText("done");
        if (!killed) {
            MsgBox.showInfo("search done!\nfound " + foundCount + " files from " +searchCount + " files");
        }
        doSearchButton.setText("search");
    }
}
