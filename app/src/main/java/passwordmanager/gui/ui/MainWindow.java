package passwordmanager.gui.ui;

import com.formdev.flatlaf.FlatDarkLaf;

import passwordmanager.gui.manager.Logger;

import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MainWindow {
    private JFrame mainFrame;
    private JPanel mainPanel;

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

    }

    public void startCreateWindow() {
        mainFrame = new JFrame("Password manager by Doomayka");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
    }

    public void finishCreateWindow() {
        mainFrame.add(mainPanel);

        mainFrame.setPreferredSize(new Dimension(400, 400));

        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public void generateComponents() {
        JLabel label = new JLabel("Test label");
        label.setBounds(150, 80, 100, 20);
        mainPanel.add(label);

        JButton button = new JButton();
        button.setText("Click me");
        button.setBounds(150, 120, 100, 20);
        mainPanel.add(button);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setText("Check me");
        checkBox.setBounds(150, 40, 100, 20);
        mainPanel.add(checkBox);

        JTextField field = new JTextField();
        field.setText("Input me");
        field.setBounds(150, 160, 100, 20);
        mainPanel.add(field);
    }
}
