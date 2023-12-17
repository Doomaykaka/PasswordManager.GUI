package passwordmanager.gui.ui;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarkLaf;

public class MainWindow {
    public void create() {
        // FlatDarculaLaf.setup();
        FlatDarkLaf.setup();

        try {
            // UIManager.setLookAndFeel(new FlatDraculaIJTheme());
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        JFrame frame = new JFrame("Test frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel label = new JLabel("Test label");
        label.setBounds(150, 80, 100, 20);
        panel.add(label);

        JButton button = new JButton();
        button.setText("Click me");
        button.setBounds(150, 120, 100, 20);
        panel.add(button);

        JCheckBox checkBox = new JCheckBox();
        checkBox.setText("Check me");
        checkBox.setBounds(150, 40, 100, 20);
        panel.add(checkBox);

        JTextField field = new JTextField();
        field.setText("Input me");
        field.setBounds(150, 160, 100, 20);
        panel.add(field);

        frame.add(panel);

        frame.setPreferredSize(new Dimension(400, 400));

        frame.pack();
        frame.setVisible(true);
    }
}
