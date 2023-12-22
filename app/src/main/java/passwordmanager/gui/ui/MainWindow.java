package passwordmanager.gui.ui;

import com.formdev.flatlaf.FlatDarkLaf;

import passwordmanager.gui.manager.Logger;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;

public class MainWindow {
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel groupPanel;
    private JScrollPane groupSPane;

    public void create() {
        // FlatDarculaLaf.setup();
        FlatDarkLaf.setup();

        try {
            // UIManager.setLookAndFeel(new FlatDraculaIJTheme());
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            Logger.addLog("MainWindow", "failed to initialize LaF");
        }

        startCreateWindow();

        generateComponents();

        finishCreateWindow();

        // -------------------------
        addPasswordGroupToList("Group1");
        addPasswordGroupToList("Group2");
        addPasswordGroupToList("Group3");
        addPasswordGroupToList("Group4");
        addPasswordGroupToList("Group5");
    }

    public void startCreateWindow() {
        mainFrame = new JFrame("Password manager by Doomayka");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
    }

    public void finishCreateWindow() {
        mainFrame.add(mainPanel);

        mainFrame.setPreferredSize(new Dimension(400, 300));

        mainFrame.pack();
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);
    }

    public void generateComponents() {

        generateMainTitle();

        generateAddButton();
        generateLoadButton();
        
        //Error
        //generateFilterPanel();

        generatePasswordGroupsList();
    }

    public void generateMainTitle() {
        JLabel label = new JLabel("Password groups");
        label.setBounds(150, 40, 100, 20);
        mainPanel.add(label);
    }

    public void generateAddButton() {
        JButton button = new JButton();
        button.setText("+");
        button.setBounds(150, 80, 40, 20);
        //button.setPreferredSize(new Dimension(100, 20));

        mainPanel.add(button);
    }

    public void generateLoadButton() {
        JButton button = new JButton();
        button.setText("▼");
        button.setBounds(210, 80, 40, 20);
        //button.setPreferredSize(new Dimension(100, 20));

        mainPanel.add(button);
    }

    public void generateFilterPanel() {
        FlowLayout layout = new FlowLayout();

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setBounds(20, 120, 360, 80);

        JTextField field = new JTextField();
        field.setText("Search");
        JButton searchButton = new JButton();
        searchButton.setText("find");

        panel.add(field);
        panel.add(searchButton);

        mainPanel.add(panel);
    }

    public void generatePasswordGroupsList() {
        groupPanel = new JPanel();
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.setBounds(0, 160, 340, 80);

        groupSPane = new JScrollPane(groupPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        groupSPane.setLayout(new ScrollPaneLayout());
        
        groupSPane.setBounds(20, 160, 340, 80);
        groupSPane.setAutoscrolls(true);

        mainPanel.add(groupSPane);
    }

    public void addPasswordGroupToList(String groupName) {
        GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(2);
        gridLayout.setRows(0);
        gridLayout.setVgap(5);

        JPanel panel = new JPanel();
        panel.setLayout(gridLayout);
        panel.setBounds(20, 200, 360, 80);

        JButton button = new JButton();
        button.setText("Open");
        button.setBounds(0, 0, 100, 20);

        JLabel label = new JLabel(groupName);
        label.setBounds(0, 0, 100, 20);

        //
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addPasswordGroupToList("Test");
            }
        });
        //

        panel.add(button);
        panel.add(label);

        groupPanel.add(panel);
    }
}
