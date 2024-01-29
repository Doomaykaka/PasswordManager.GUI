package passwordmanagergui.group;

import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;

import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Logger;
import passwordmanagergui.UIHelper;
import passwordmanagergui.passwords.GroupWindow;

public class MainWindow {
    private JFrame mainWindow;
    private JPanel windowElementsPanel;
    private JPanel groupsPanel;
    private JScrollPane groupSPane;
    private int recordsCount = 0;

    public void create() {
        FlatDarkLaf.setup();

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            Logger.addLog("MainWindow", "failed to initialize LaF");
        }

        startCreateWindow();

        generateComponents();

        finishCreateWindow();

        UIHelper.readGroupsFromPath();

        repaintListFromData();

    }

    public void startCreateWindow() {
        mainWindow = new JFrame("Password manager by Doomayka");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        windowElementsPanel = new JPanel();
        windowElementsPanel.setLayout(null);
    }

    public void finishCreateWindow() {
        mainWindow.add(windowElementsPanel);

        mainWindow.setPreferredSize(new Dimension(400, 600));

        mainWindow.pack();
        mainWindow.setResizable(false);
        mainWindow.setVisible(true);
    }

    public void generateComponents() {

        generateMainTitle();

        generateAddButton();
        generateLoadButton();

        // Error
        generateFilterPanel();

        generatePasswordGroupsList();
    }

    public void generateMainTitle() {
        JLabel mainTitleLabel = new JLabel("Password groups");
        mainTitleLabel.setBounds(150, 40, 100, 20);
        windowElementsPanel.add(mainTitleLabel);
    }

    public void generateAddButton() {
        JButton addButton = new JButton();
        addButton.setText("+");
        addButton.setBounds(150, 80, 40, 20);
        addButton.setPreferredSize(new Dimension(100, 20));

        //
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Temp
                // addPasswordGroupToListGUI("Test");
                CreateGroupWindow createGroupWindow = new CreateGroupWindow();
                IRawData newPasswordGroup = createGroupWindow.create(mainWindow);
                if (newPasswordGroup != null) {
                    addPasswordGroupToListGUI(newPasswordGroup.getName());
                    UIHelper.addGroup(new File(newPasswordGroup.getName() + ".dat"));
                    repaintListFromData();
                }
            }
        });
        //

        windowElementsPanel.add(addButton);
    }

    public void generateLoadButton() {
        JButton loadButton = new JButton();
        loadButton.setText("▼");
        loadButton.setBounds(210, 80, 40, 20);
        loadButton.setPreferredSize(new Dimension(100, 20));

        //
        loadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Temp
                // addPasswordGroupToListGUI("Test");

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int questionDialogResult = fileChooser.showOpenDialog(mainWindow);
                if (questionDialogResult == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    // System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    String endOfSelectedFileName = ".dat";
                    int endOfSelectedFileNameIndex = selectedFile.getName().indexOf(endOfSelectedFileName);
                    addPasswordGroupToListGUI(selectedFile.getName().substring(0, endOfSelectedFileNameIndex));
                    UIHelper.addGroup(selectedFile);
                    repaintListFromData();
                }
            }
        });
        //

        windowElementsPanel.add(loadButton);
    }

    public void generateFilterPanel() {
        FlowLayout filterPanelLayout = new FlowLayout();

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(filterPanelLayout);
        filterPanel.setBounds(10, 120, 360, 40);

        JTextField searchField = new JTextField();
        searchField.setText("Search");
        searchField.setPreferredSize(new Dimension(350, 20));
//		JButton searchButton = new JButton();
//		searchButton.setText("find");
//		searchButton.setPreferredSize(new Dimension(140, 20));

        searchField.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                filterPasswordGroups(""); // reset filter

                filterPasswordGroups(searchField.getText());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                filterPasswordGroups(""); // reset filter

                filterPasswordGroups(searchField.getText());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                filterPasswordGroups(""); // reset filter

                filterPasswordGroups(searchField.getText());
            }
        });

        //
//		searchButton.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// Temp
//				filterPasswordGroups(""); // reset filter
//
//				filterPasswordGroups(field.getText());
//			}
//		});
        //

        filterPanel.add(searchField);
//		panel.add(searchButton);

        windowElementsPanel.add(filterPanel);
    }

    public void generatePasswordGroupsList() {
        groupsPanel = new JPanel();
        // groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.setColumns(1);
//		gridLayout.setRows(0);
//		gridLayout.setVgap(5);
//		gridLayout.setHgap(0);
        GridBagLayout gridLayout = new GridBagLayout();
        groupsPanel.setLayout(gridLayout);
        //
        groupsPanel.setBounds(0, 160, 340, 380);

        groupSPane = new JScrollPane(groupsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        groupSPane.setLayout(new ScrollPaneLayout());

        groupSPane.setBounds(20, 160, 340, 380);
        groupSPane.setAutoscrolls(true);

        windowElementsPanel.add(groupSPane);
    }

    public void addPasswordGroupToListGUI(String groupName) {
        if (groupName.length() > 36) {
            groupName = groupName.substring(0, 33) + "...";
        }

        GridBagLayout gridLayout = new GridBagLayout();
        gridLayout.columnWidths = new int[] { 230, 40, 10 };
        GridBagConstraints gridConstraint = new GridBagConstraints();

        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(gridLayout);
        groupPanel.setSize(new Dimension(0, 0));
        // panel.setBackground(Color.WHITE);
        // wpanel.setBounds(20, 200, 360, 80);

        JButton openGroupButton = new JButton();
        openGroupButton.setText("Open");

        JButton removeGroupButton = new JButton();
        removeGroupButton.setText("X");
        // button.setBounds(0, 0, 30, 20);
        // button.setPreferredSize(new Dimension(30, 20));//

        JLabel groupNameLabel = new JLabel(groupName);
        // label.setHorizontalAlignment(SwingConstants.LEFT);
        // label.setPreferredSize(new Dimension(10, 20));//

        JLabel whitespaceLabel = new JLabel("         ");

        //
        String groupNameCopy = String.valueOf(groupName);
        openGroupButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Temp
                // String test =
                // "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest";
                // addPasswordGroupToListGUI(test);
                // UIHelper.addGroup(test);

                GroupWindow groupWindow = new GroupWindow(UIHelper.getGroupFileByName(groupNameCopy));
                groupWindow.open();

                repaintListFromData();
            }
        });
        //

        removeGroupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String confirmRemoveMessage = "You realy want to remove " + groupNameCopy;

                if (getConfirm(confirmRemoveMessage)) {
                    removePasswordGroupFromList(groupNameLabel.getText());
                }
            }

            public boolean getConfirm(String message) {
                boolean isConfirmed = false;

                isConfirmed = JOptionPane.showConfirmDialog(mainWindow, message) == JOptionPane.YES_OPTION;

                return isConfirmed;
            }
        });

        groupPanel.add(groupNameLabel);
        groupPanel.add(openGroupButton);
        groupPanel.add(removeGroupButton);

        gridConstraint.gridx = 0;
        gridConstraint.gridy = 1;
        groupPanel.add(whitespaceLabel, gridConstraint);

        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridConstraints.anchor = GridBagConstraints.NORTH;
        gridConstraints.gridy = recordsCount;

        groupsPanel.add(groupPanel, gridConstraints);

        recordsCount++;
    }

    public void repaintListFromData() {
        groupsPanel.setVisible(false);

        groupsPanel.removeAll();

        for (String groupName : UIHelper.getGroups()) {
            addPasswordGroupToListGUI(groupName);
        }

        groupsPanel.setVisible(true);
    }

    public void repaintList() {
        List<String> groupNames = new ArrayList<String>();

        for (Component groupPanel : groupsPanel.getComponents()) {
            Component groupNameLabel = ((JPanel) groupPanel).getComponents()[0];
            groupNames.add(((JLabel) groupNameLabel).getText());
        }

        groupsPanel.setVisible(false);

        groupsPanel.removeAll();

        for (String groupName : groupNames) {
            addPasswordGroupToListGUI(groupName);
        }

        groupsPanel.setVisible(true);
    }

    public void removePasswordGroupFromList(String groupName) {
        recordsCount = 0;
        UIHelper.removeGroup(groupName);
        repaintListFromData();
    }

    public void filterPasswordGroups(String expression) {
        if (!expression.equals("")) {
            for (Component groupPanel : groupsPanel.getComponents()) {
                Component groupNameLabel = ((JPanel) groupPanel).getComponents()[0];
                if (!((JLabel) groupNameLabel).getText().contains(expression)) {
                    groupsPanel.remove(groupPanel);
                }
            }

            repaintList();
        } else {
            repaintListFromData();
        }
    }
}
