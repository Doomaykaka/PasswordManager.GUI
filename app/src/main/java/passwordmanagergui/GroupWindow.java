package passwordmanagergui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.JOptionPane;

import passwordmanager.decoded.IRecord;
import passwordmanager.decoded.IStorage;
import passwordmanager.encoded.DefaultRawData;
import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Logger;
import passwordmanager.manager.Manager;

public class GroupWindow {
    private File groupFile;
    private IRawData encodedGroup;
    private IStorage decodedGroup;
    private JFrame groupFrame;
    private JPanel groupPanel;
    private JScrollPane passwordSPane;

    public GroupWindow(File groupFile) {
        this.groupFile = groupFile;
    }

    public void open() {
        String password = getPassword();

        if (password != null) {

            boolean isDecoded = decodeStruct(password);

            if (isDecoded) {
                startCreateWindow();

                generateComponents();

                finishCreateWindow();

                for (int i = 0; i < decodedGroup.size(); i++) {
                    addPasswordToListGUI(decodedGroup.getByIndex(i));
                }
            } else {
                getBadPasswordMessage();
            }
        }

    }

    public String getPassword() {
        return JOptionPane.showInputDialog("Enter password:");
    }

    public void getBadPasswordMessage() {
        JOptionPane.showMessageDialog(null, "Bad password");
    }

    public boolean decodeStruct(String password) {
        boolean isDecoded = false;

        if (groupFile != null) {
            if (groupFile.exists()) {
                try {
                    List<String> data = new ArrayList<String>();

                    Scanner scanner = new Scanner(groupFile);
                    while (scanner.hasNextLine()) {
                        data.add(scanner.nextLine());
                    }
                    scanner.close();

                    encodedGroup = new DefaultRawData();
                    encodedGroup.setName(UIHelper.getGroupNameByFile(groupFile));
                    encodedGroup.setData(data);

                    encodedGroup.save();
                } catch (IOException e) {
                    Logger.addLog("RawData", "decoding error");
                }
            }
        }

        if (encodedGroup != null) {
            decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, password);

            isDecoded = (decodedGroup != null);
        }

        return isDecoded;
    }

    public void startCreateWindow() {
        groupFrame = new JFrame("Password manager by Doomayka");
        groupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        groupPanel = new JPanel();
        groupPanel.setLayout(null);
    }

    public void finishCreateWindow() {
        groupFrame.add(groupPanel);

        groupFrame.setPreferredSize(new Dimension(400, 600));

        groupFrame.pack();
        groupFrame.setResizable(false);
        groupFrame.setVisible(true);
    }

    public void generateComponents() {

        generateMainTitle();

        generateAddButton();

        // Error
        generateFilterPanel();

        generatePasswordsList();
    }

    // ------------

    public void generateMainTitle() {
        JLabel label = new JLabel("Group " + encodedGroup.getName());
        label.setBounds(150, 40, 100, 20);
        groupFrame.add(label);
    }

    public void generateAddButton() {
        JButton button = new JButton();
        button.setText("+");
        button.setBounds(150, 80, 40, 20);
        button.setPreferredSize(new Dimension(100, 20));

        //
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Temp
                // addPasswordGroupToListGUI("Test");
//                CreateGroupWindow createGroupWindow = new CreateGroupWindow();
//                IRawData newGroup = createGroupWindow.create(mainFrame);
//                if (newGroup != null) {
//                    addPasswordGroupToListGUI(newGroup.getName());
//                    UIHelper.addGroup(new File(newGroup.getName() + ".dat"));
//                    repaintListFromData();
//                }
            }
        });
        //

        groupFrame.add(button);
    }

    public void generateFilterPanel() {
        FlowLayout layout = new FlowLayout();

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setBounds(10, 120, 360, 40);

        JTextField field = new JTextField();
        field.setText("Search");
        field.setPreferredSize(new Dimension(200, 20));
        JButton searchButton = new JButton();
        searchButton.setText("find");
        searchButton.setPreferredSize(new Dimension(140, 20));

        //
        searchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Temp
                filterPasswordsGroup(""); // reset filter

                filterPasswordsGroup(field.getText());
            }
        });
        //

        panel.add(field);
        panel.add(searchButton);

        groupFrame.add(panel);
    }

    public void generatePasswordsList() {
        groupPanel = new JPanel();
        // groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        GridLayout gridLayout = new GridLayout();
        gridLayout.setColumns(1);
        gridLayout.setRows(0);
        gridLayout.setVgap(5);
        gridLayout.setHgap(0);
        groupPanel.setLayout(gridLayout);
        //
        groupPanel.setBounds(20, 160, 340, 380);

        passwordSPane = new JScrollPane(groupPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        passwordSPane.setLayout(new ScrollPaneLayout());

        passwordSPane.setBounds(20, 160, 340, 380);
        passwordSPane.setAutoscrolls(true);

        groupFrame.add(passwordSPane);
    }

    public void addPasswordToListGUI(IRecord record) {
        GridBagLayout gridLayout = new GridBagLayout();
        gridLayout.columnWidths = new int[] { 200 };

        JPanel panel = new JPanel();
        // panel.setLayout(gridLayout);
        // panel.setLayout(gridLayout);
        // wpanel.setBounds(20, 200, 360, 80);

        JButton copyLoginButton = new JButton();
        copyLoginButton.setText("CL");

        JButton copyPasswordButton = new JButton();
        copyPasswordButton.setText("CP");

        JButton removeButton = new JButton();
        removeButton.setText("X");
        // button.setBounds(0, 0, 30, 20);
        // button.setPreferredSize(new Dimension(30, 20));//

        // JLabel infoLabel = new JLabel(record.getInfo());
        JLabel infoLabel = new JLabel("inf");
        // JLabel loginLabel = new JLabel(record.getLogin());
        JLabel loginLabel = new JLabel("ln");
        JLabel passwordLabel = new JLabel("ps");
        // JLabel passwordLabel = new JLabel(record.getPassword());
        // label.setHorizontalAlignment(SwingConstants.LEFT);
        // label.setPreferredSize(new Dimension(10, 20));//

        //
        String loginCopy = String.valueOf(record.getLogin());
        copyLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        String passwordCopy = String.valueOf(record.getPassword());
        copyPasswordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        //

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String confirmMessage = "You realy want to remove record " + record.getLogin();

                if (getConfirm(confirmMessage)) {
                    // removePasswordGroupFromList(label.getText());
                }
            }

            public boolean getConfirm(String message) {
                boolean isConfirmed = false;

                isConfirmed = JOptionPane.showConfirmDialog(groupFrame, message) == JOptionPane.YES_OPTION;

                return isConfirmed;
            }
        });

        panel.add(infoLabel);
        panel.add(loginLabel);
        panel.add(passwordLabel);
        panel.add(copyLoginButton);
        panel.add(copyPasswordButton);
        panel.add(removeButton);

        groupPanel.add(panel);
    }

    public void repaintListFromData() {
        groupPanel.setVisible(false);

        groupPanel.removeAll();

        for (int i = 0; i < decodedGroup.size(); i++) {
            addPasswordToListGUI(decodedGroup.getByIndex(i));
        }

        groupPanel.setVisible(true);
    }

    public void repaintList() {
        List<String> groupNames = new ArrayList<String>();

        for (Component panel : groupPanel.getComponents()) {
            Component label = ((JPanel) panel).getComponents()[0];
            groupNames.add(((JLabel) label).getText());
        }

        groupPanel.setVisible(false);

        groupPanel.removeAll();

        for (int i = 0; i < decodedGroup.size(); i++) {
            addPasswordToListGUI(decodedGroup.getByIndex(i));
        }

        groupPanel.setVisible(true);
    }

    public void filterPasswordsGroup(String expression) {
        if (!expression.equals("")) {
            for (Component panel : groupPanel.getComponents()) {
                Component label = ((JPanel) panel).getComponents()[0];
                if (!((JLabel) label).getText().contains(expression)) {
                    groupPanel.remove(panel);
                }
            }

            repaintList();
        } else {
            repaintListFromData();
        }
    }
}
