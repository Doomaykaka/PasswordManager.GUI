package passwordmanagergui;

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

public class MainWindow {
	private JFrame mainFrame;
	private JPanel mainPanel;
	private JPanel groupPanel;
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
		mainFrame = new JFrame("Password manager by Doomayka");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
	}

	public void finishCreateWindow() {
		mainFrame.add(mainPanel);

		mainFrame.setPreferredSize(new Dimension(400, 600));

		mainFrame.pack();
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
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
		JLabel label = new JLabel("Password groups");
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
				CreateGroupWindow createGroupWindow = new CreateGroupWindow();
				IRawData newGroup = createGroupWindow.create(mainFrame);
				if (newGroup != null) {
					addPasswordGroupToListGUI(newGroup.getName());
					UIHelper.addGroup(new File(newGroup.getName() + ".dat"));
					repaintListFromData();
				}
			}
		});
		//

		mainPanel.add(button);
	}

	public void generateLoadButton() {
		JButton button = new JButton();
		button.setText("▼");
		button.setBounds(210, 80, 40, 20);
		button.setPreferredSize(new Dimension(100, 20));

		//
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Temp
				// addPasswordGroupToListGUI("Test");

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(mainFrame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					// System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					String endOfSelectedFileName = ".dat";
					int endIndex = selectedFile.getName().indexOf(endOfSelectedFileName);
					addPasswordGroupToListGUI(selectedFile.getName().substring(0, endIndex));
					UIHelper.addGroup(selectedFile);
					repaintListFromData();
				}
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
//		JButton searchButton = new JButton();
//		searchButton.setText("find");
//		searchButton.setPreferredSize(new Dimension(140, 20));
		
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
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    filterPasswordGroups(""); // reset filter

                    filterPasswordGroups(field.getText());
                }
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

		panel.add(field);
//		panel.add(searchButton);

		mainPanel.add(panel);
	}

	public void generatePasswordGroupsList() {
		groupPanel = new JPanel();
		//groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.setColumns(1);
//		gridLayout.setRows(0);
//		gridLayout.setVgap(5);
//		gridLayout.setHgap(0);
		GridBagLayout gridLayout = new GridBagLayout();
		groupPanel.setLayout(gridLayout);
		//
		groupPanel.setBounds(0, 160, 340, 380);

		groupSPane = new JScrollPane(groupPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		groupSPane.setLayout(new ScrollPaneLayout());

		groupSPane.setBounds(20, 160, 340, 380);
		groupSPane.setAutoscrolls(true);

		mainPanel.add(groupSPane);
	}

	public void addPasswordGroupToListGUI(String groupName) {
		if (groupName.length() > 36) {
			groupName = groupName.substring(0, 33) + "...";
		}

		GridBagLayout gridLayout = new GridBagLayout();
		gridLayout.columnWidths = new int[]{230, 40, 10};
		GridBagConstraints gridConstraint = new GridBagConstraints();  

		JPanel panel = new JPanel();
		panel.setLayout(gridLayout);
		panel.setSize(new Dimension(0, 0));
		//panel.setBackground(Color.WHITE);
		// wpanel.setBounds(20, 200, 360, 80);

		JButton openButton = new JButton();
		openButton.setText("Open");

		JButton removeButton = new JButton();
		removeButton.setText("X");
		// button.setBounds(0, 0, 30, 20);
		// button.setPreferredSize(new Dimension(30, 20));//

		JLabel label = new JLabel(groupName);
		// label.setHorizontalAlignment(SwingConstants.LEFT);
		// label.setPreferredSize(new Dimension(10, 20));//
		
		JLabel whitespace = new JLabel("         ");

		//
		String groupNameCopy = String.valueOf(groupName);
		openButton.addActionListener(new ActionListener() {

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

		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    String confirmMessage = "You realy want to remove " + groupNameCopy;
			    
			    if(getConfirm(confirmMessage)) {
			        removePasswordGroupFromList(label.getText());
			    }
			}
			
			public boolean getConfirm(String message) {
			    boolean isConfirmed = false;
			    
			    isConfirmed = JOptionPane.showConfirmDialog(mainFrame, message) == JOptionPane.YES_OPTION;
			    
		        return isConfirmed;
		    }
		});

		panel.add(label);
		panel.add(openButton);
		panel.add(removeButton);
		
		gridConstraint.gridx = 0;  
        gridConstraint.gridy = 1; 
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

		for (String groupName : UIHelper.getGroups()) {
			addPasswordGroupToListGUI(groupName);
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

		for (String groupName : groupNames) {
			addPasswordGroupToListGUI(groupName);
		}

		groupPanel.setVisible(true);
	}

	public void removePasswordGroupFromList(String groupName) {
	    recordsCount = 0;
		UIHelper.removeGroup(groupName);
		repaintListFromData();
	}

	public void filterPasswordGroups(String expression) {
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
