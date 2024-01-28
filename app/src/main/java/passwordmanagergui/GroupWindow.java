package passwordmanagergui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
    private JPanel mainPanel;
    private JPanel groupPanel;
    private JScrollPane passwordSPane;
    private String groupPassword = "";
    private int recordsCount = 0;

    public GroupWindow(File groupFile) {
        this.groupFile = groupFile;
    }

    public void open() {
        groupPassword = getPassword();

        if (groupPassword != null) {

            boolean isDecoded = decodeStruct(groupPassword);

            if (isDecoded) {
                startCreateWindow();

                generateComponents();

                finishCreateWindow();

                repaintListFromData();
            } else {
                getBadPasswordMessage();
            }
        }

    }

    public String getPassword() {
        return JOptionPane.showInputDialog("Enter password:");
    }

    public void getBadPasswordMessage() {
        JOptionPane.showMessageDialog(null, "Wrong password");
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

        mainPanel = new JPanel();
        mainPanel.setLayout(null);
    }

    public void finishCreateWindow() {
        groupFrame.add(mainPanel);

        groupFrame.setPreferredSize(new Dimension(400, 600));

        groupFrame.pack();
        groupFrame.setResizable(false);
        groupFrame.setVisible(true);
    }

    public void generateComponents() {

        generateMainTitle();

        generateAddButton();

        generateFilterPanel();

        generatePasswordsList();
    }

    // ------------

    public void generateMainTitle() {
        JLabel label = new JLabel("Group " + encodedGroup.getName());
        label.setBounds(150, 40, 100, 20);
        mainPanel.add(label);
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
                PasswordWindow passwordWindow = new PasswordWindow();
                passwordWindow.create(groupFrame, groupPassword, encodedGroup);

                encodedGroup.load();

                decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, groupPassword);

                repaintListFromData();
            }
        });
        //

        mainPanel.add(button);
    }

    public void generateFilterPanel() {
        FlowLayout layout = new FlowLayout();

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setBounds(10, 120, 360, 40);

        JTextField field = new JTextField();
        field.setText("Search");
        field.setPreferredSize(new Dimension(350, 20));
//        JButton searchButton = new JButton();
//        searchButton.setText("find");
//        searchButton.setPreferredSize(new Dimension(140, 20));

        field.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filterPasswordsGroup(""); // reset filter

                    filterPasswordsGroup(field.getText());
                }
            }
        });

        //
//        searchButton.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Temp
//                filterPasswordsGroup(""); // reset filter
//
//                filterPasswordsGroup(field.getText());
//            }
//        });
        //

        panel.add(field);
//        panel.add(searchButton);

        mainPanel.add(panel);
    }

    public void generatePasswordsList() {
        groupPanel = new JPanel();
        // groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
//        GridLayout gridLayout = new GridLayout();
//        gridLayout.setColumns(1);
//        gridLayout.setRows(0);
//        gridLayout.setVgap(5);
//        gridLayout.setHgap(0);
        GridBagLayout gridLayout = new GridBagLayout();
        groupPanel.setLayout(gridLayout);
        //
        groupPanel.setBounds(0, 160, 340, 380);

        passwordSPane = new JScrollPane(groupPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        passwordSPane.setLayout(new ScrollPaneLayout());

        passwordSPane.setBounds(20, 160, 340, 380);
        passwordSPane.setAutoscrolls(true);

        mainPanel.add(passwordSPane);
    }

    // Error
    public void addPasswordToListGUI(IRecord record) {
        GridBagLayout gridLayout = new GridBagLayout();
        GridBagConstraints gridConstraint = new GridBagConstraints();

        JPanel panel = new JPanel();
        panel.setLayout(gridLayout);
        panel.setSize(new Dimension(0, 0));
        // panel.setBackground(Color.WHITE);
        // panel.setBounds(20, 160, 340, 80);
        // panel.setSize(340, 80);

        JButton copyLoginButton = new JButton();
        // copyLoginButton.setLayout(new GridLayout(0, 1));
        // copyLoginButton.add(new JLabel("Copy"));
        // copyLoginButton.add(new JLabel("login"));
        copyLoginButton.setIcon(new ImageIcon("src/main/resources/images/copy.png"));

        JButton copyPasswordButton = new JButton();
        // copyPasswordButton.setLayout(new GridLayout(0, 1));
        // copyPasswordButton.add(new JLabel("Copy"));
        // copyPasswordButton.add(new JLabel("password"));
        copyPasswordButton.setIcon(new ImageIcon("src/main/resources/images/copy.png"));

        JButton removeButton = new JButton("X");
        JButton editButton = new JButton("\u270E");
        // button.setBounds(0, 0, 30, 20);
        // button.setPreferredSize(new Dimension(30, 20));//

        String info = record.getInfo();
        // String info = "aaaaaa"; //6
        JTextArea infoLabel = new JTextArea();
        if (info.length() > 18) {
            for (int i = 0; i < info.length(); i += 18) {
                if (i + 6 < info.length() - 1) {
                    infoLabel.append(info.substring(i, i + 18) + "\n");
                } else {
                    infoLabel.append(info.substring(i, info.length() - 1) + "\n");
                }
            }
        } else {
            infoLabel.setText(info);
        }
        infoLabel.setEditable(false);
        // JLabel infoLabel = new JLabel(record.getInfo());
        // JLabel loginLabel = new JLabel(record.getLogin());
        String login = record.getLogin();
        // String login = "aaaaaa"; //6
        JTextArea loginLabel = new JTextArea();
        if (login.length() > 18) {
            for (int i = 0; i < login.length(); i += 18) {
                if (i + 18 < login.length() - 1) {
                    loginLabel.append(login.substring(i, i + 18) + "\n");
                } else {
                    loginLabel.append(login.substring(i, login.length() - 1) + "\n");
                }
            }
        } else {
            loginLabel.setText(login);
        }
        loginLabel.setEditable(false);

        String password = record.getPassword();
        String hidedPassword = password.replaceAll(".", "*");
        
        String tempPassword = "";       
        // String password = "aaaaaaaaaaaaaaaaaaaaaaaa"; //6
        JTextArea passwordLabel = new JTextArea();
        if (password.length() > 18) {
            for (int i = 0; i < password.length(); i += 18) {
                if (i + 18 < password.length() - 1) {
                    tempPassword += password.substring(i, i + 18) + "\n";
                    passwordLabel.append(hidedPassword.substring(i, i + 18) + "\n");
                } else {
                    tempPassword += password.substring(i, password.length() - 1) + "\n";
                    passwordLabel.append(hidedPassword.substring(i, hidedPassword.length() - 1) + "\n");
                }
            }
        } else {
            tempPassword = password;
            passwordLabel.setText(hidedPassword);
        }
        passwordLabel.setEditable(false);
        
        String normalTextCopy = String.valueOf(tempPassword);
        passwordLabel.addMouseListener(new MouseListener() {
            
            private String normalText =  String.valueOf(normalTextCopy);
            private String hidedText = String.copyValueOf(hidedPassword.toCharArray());
            private boolean isHided = true;
            
            @Override
            public void mouseReleased(MouseEvent e) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                // TODO Auto-generated method stub
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordLabel.setEditable(true);
                
                if(isHided) {
                    passwordLabel.setText(normalText);   
                } else {
                    passwordLabel.setText(hidedText);
                }
                
                isHided = !isHided;
                
                passwordLabel.setEditable(false);
            }
        });
        
        // JLabel passwordLabel = new JLabel(record.getPassword());
        // label.setHorizontalAlignment(SwingConstants.LEFT);
        // label.setPreferredSize(new Dimension(10, 20));//

        JLabel whitespace = new JLabel("         ");

        //
        String loginCopy = String.valueOf(record.getLogin());
        copyLoginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(loginCopy);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        });

        String passwordCopy = String.valueOf(record.getPassword());
        copyPasswordButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(passwordCopy);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
            }
        });
        //

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String confirmMessage = "You realy want to remove record " + record.getLogin();

                if (getConfirm(confirmMessage)) {
                    PasswordWindow passwordWindow = new PasswordWindow();
                    passwordWindow.removePasswordRecord(loginCopy, passwordCopy, groupPassword, encodedGroup);

                    encodedGroup.load();

                    decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, groupPassword);

                    repaintListFromData();
                }
            }

            public boolean getConfirm(String message) {
                boolean isConfirmed = false;

                isConfirmed = JOptionPane.showConfirmDialog(groupFrame, message) == JOptionPane.YES_OPTION;

                return isConfirmed;
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String confirmMessage = "You realy want to edit record " + record.getLogin();

                if (getConfirm(confirmMessage)) {
                    PasswordWindow passwordWindow = new PasswordWindow();
                    passwordWindow.update(groupFrame, info, login, password, groupPassword, encodedGroup);

                    encodedGroup.load();

                    decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, groupPassword);

                    repaintListFromData();
                }
            }

            public boolean getConfirm(String message) {
                boolean isConfirmed = false;

                isConfirmed = JOptionPane.showConfirmDialog(groupFrame, message) == JOptionPane.YES_OPTION;

                return isConfirmed;
            }
        });

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 0;
        gridConstraint.gridy = 0;
        gridConstraint.anchor = GridBagConstraints.PAGE_END;
        panel.add(infoLabel, gridConstraint);

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 0;
        gridConstraint.gridy = 1;
        panel.add(loginLabel, gridConstraint);

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 1;
        gridConstraint.gridy = 1;
        gridConstraint.anchor = GridBagConstraints.PAGE_END;
        panel.add(copyLoginButton, gridConstraint);

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 0;
        gridConstraint.gridy = 2;
        panel.add(passwordLabel, gridConstraint);

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 1;
        gridConstraint.gridy = 2;
        gridConstraint.anchor = GridBagConstraints.PAGE_END;
        panel.add(copyPasswordButton, gridConstraint);

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 0;
        gridConstraint.gridy = 3;
        gridConstraint.anchor = GridBagConstraints.PAGE_END;
        panel.add(editButton, gridConstraint);

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 0;
        gridConstraint.gridy = 4;
        gridConstraint.anchor = GridBagConstraints.PAGE_END;
        panel.add(removeButton, gridConstraint);

        gridConstraint.fill = GridBagConstraints.HORIZONTAL;
        gridConstraint.gridx = 0;
        gridConstraint.gridy = 5;
        gridConstraint.anchor = GridBagConstraints.PAGE_END;
        panel.add(whitespace, gridConstraint);

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridy = recordsCount;

        groupPanel.add(panel, constraints);

        recordsCount++;
    }

    public void repaintListFromData() {
        groupPanel.setVisible(false);

        groupPanel.removeAll();

        for (int i = 0; i < decodedGroup.size(); i++) {
            IRecord passRecord = decodedGroup.getByIndex(i);
            if (!passRecord.getLogin().contains("Its white record") && !passRecord.getLogin().contains("admin")
                    && !passRecord.getPassword().contains("qwerty")) {
                addPasswordToListGUI(passRecord);
            }
        }

        groupPanel.setVisible(true);
    }

    public void repaintList() {
        groupPanel.revalidate();
    }

    public void filterPasswordsGroup(String expression) {
        if (!expression.equals("")) {
            Component[] components = groupPanel.getComponents();
            for (Component panel : components) {
                for (Component recordElement : ((JPanel) panel).getComponents()) {
                    if (recordElement.getClass().equals(JTextArea.class)) {
                        JTextArea label = ((JTextArea) recordElement);
                        String text = label.getText();

                        if (!text.contains(expression.replace("\n", ""))) {
                            groupPanel.remove(panel);
                        } else {
                            break;
                        }
                    }
                }
            }

            repaintList();
        } else {
            repaintListFromData();
        }
    }
}
