package passwordmanagergui.group;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
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

/**
 * Main window with a list of groups and elements for manipulating password
 * groups
 * 
 * @see UIHelper
 * @see GroupWindow
 * @author Doomaykaka MIT License
 * @since 2024-01-31
 */
public class MainWindow {
	/**
	 * Main window object
	 */
	private JFrame mainWindow;
	/**
	 * Panel with children of the main window
	 */
	private JPanel windowElementsPanel;
	/**
	 * Panel with a list of groups
	 */
	private JPanel groupsPanel;
	/**
	 * Container with a slider for a list of groups
	 */
	private JScrollPane groupSPane;
	/**
	 * Group counter
	 */
	private int recordsCount = 0;
	/**
	 * The maximum length of a group name, from which names will be shortened for
	 * convenient display
	 */
	private final int maxGroupNameLength = 36;

	/**
	 * Method that creates a window with a list of password groups and additional
	 * controls
	 */
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

	/**
	 * Method for starting to create a window
	 */
	public void startCreateWindow() {
		mainWindow = new JFrame("Password manager by Doomayka");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		windowElementsPanel = new JPanel();
		windowElementsPanel.setLayout(null);
	}

	/**
	 * A method that packs all static elements of a window and also completes the
	 * creation of the window
	 */
	public void finishCreateWindow() {
		int windowWidth = 400;
		int windowHeight = 600;

		mainWindow.add(windowElementsPanel);

		mainWindow.setPreferredSize(new Dimension(windowWidth, windowHeight));

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int xLocation = (int) center.getX() - (windowWidth / 2);
		int yLocation = (int) center.getY() - (windowHeight / 2);
		mainWindow.setLocation(xLocation, yLocation);

		mainWindow.pack();
		mainWindow.setResizable(false);
		mainWindow.setVisible(true);
	}

	/**
	 * Method that fills a window with elements
	 */
	public void generateComponents() {

		generateMainTitle();

		generateAddButton();
		generateLoadButton();

		generateFilterPanel();

		generatePasswordGroupsList();
	}

	/**
	 * Method for adding a title to a window
	 */
	public void generateMainTitle() {
		JLabel mainTitleLabel = new JLabel("Password groups");
		mainTitleLabel.setBounds(150, 40, 100, 20);
		windowElementsPanel.add(mainTitleLabel);
	}

	/**
	 * Method that adds a button for creating a new password group to a window
	 */
	public void generateAddButton() {
		JButton addButton = new JButton();
		addButton.setText("+");
		addButton.setBounds(150, 80, 40, 20);
		addButton.setPreferredSize(new Dimension(100, 20));

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CreateGroupWindow createGroupWindow = new CreateGroupWindow();
				IRawData newPasswordGroup = createGroupWindow.create(mainWindow);

				if (newPasswordGroup != null) {
					addPasswordGroupToListGUI(newPasswordGroup.getName());
					UIHelper.addGroup(new File(newPasswordGroup.getName() + UIHelper.getSuffix()));
					// Error
					repaintListFromData();
				}
			}
		});

		windowElementsPanel.add(addButton);
	}

	/**
	 * Method that adds a button to load a group of passwords to a window
	 */
	public void generateLoadButton() {
		JButton loadButton = new JButton();
		loadButton.setText("▼");
		loadButton.setBounds(210, 80, 40, 20);
		loadButton.setPreferredSize(new Dimension(100, 20));

		loadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int questionDialogResult = fileChooser.showOpenDialog(mainWindow);

				if (questionDialogResult == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					String endOfSelectedFileName = UIHelper.getSuffix();
					int endOfSelectedFileNameIndex = selectedFile.getName().indexOf(endOfSelectedFileName);

					addPasswordGroupToListGUI(selectedFile.getName().substring(0, endOfSelectedFileNameIndex));
					UIHelper.addGroup(selectedFile);
					repaintListFromData();
				}
			}
		});

		windowElementsPanel.add(loadButton);
	}

	/**
	 * Method that adds a panel to the window to filter the list of password groups
	 */
	public void generateFilterPanel() {
		FlowLayout filterPanelLayout = new FlowLayout();

		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(filterPanelLayout);
		filterPanel.setBounds(10, 120, 360, 40);

		JTextField searchField = new JTextField();
		searchField.setToolTipText("Search");
		searchField.setPreferredSize(new Dimension(350, 20));

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

		filterPanel.add(searchField);

		windowElementsPanel.add(filterPanel);
	}

	/**
	 * Method that adds a list to the window in which password groups will be
	 * located
	 */
	public void generatePasswordGroupsList() {
		groupsPanel = new JPanel();

		GridBagLayout gridLayout = new GridBagLayout();
		groupsPanel.setLayout(gridLayout);

		groupsPanel.setBounds(0, 160, 340, 380);

		groupSPane = new JScrollPane(groupsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		groupSPane.setLayout(new ScrollPaneLayout());
		groupSPane.setBounds(20, 160, 340, 380);
		groupSPane.setAutoscrolls(true);

		windowElementsPanel.add(groupSPane);
	}

	/**
	 * Method that dynamically adds a new group to the list
	 * 
	 * @param groupName
	 *            name of the group to add to the list
	 */
	public void addPasswordGroupToListGUI(String groupName) {
		if (groupName == null) {
			return;
		}

		if (groupName.length() > maxGroupNameLength) {
			String dotString = "...";
			groupName = groupName.substring(0, maxGroupNameLength - dotString.length()) + dotString;
		}

		GridBagLayout gridLayout = new GridBagLayout();
		gridLayout.columnWidths = new int[]{230, 40, 10};
		GridBagConstraints gridConstraint = new GridBagConstraints();

		JPanel groupPanel = new JPanel();
		groupPanel.setLayout(gridLayout);
		groupPanel.setSize(new Dimension(0, 0));

		JButton openGroupButton = new JButton();
		openGroupButton.setText("Open");

		JButton removeGroupButton = new JButton();
		removeGroupButton.setText("X");

		JLabel groupNameLabel = new JLabel(groupName);

		JLabel whitespaceLabel = new JLabel("         ");

		String groupNameCopy = String.valueOf(groupName);
		openGroupButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GroupWindow groupWindow = new GroupWindow(UIHelper.getGroupFileByName(groupNameCopy));
				groupWindow.open();

				repaintListFromData();
			}
		});

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

				isConfirmed = JOptionPane.showConfirmDialog(mainWindow, message, null,
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

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

	/**
	 * Method updating the list of groups with all found groups in the standard
	 * directory
	 */
	public void repaintListFromData() {
		groupsPanel.setVisible(false);

		groupsPanel.removeAll();

		UIHelper.clearData();

		UIHelper.readGroupsFromPath();

		for (String groupName : UIHelper.getGroups()) {
			addPasswordGroupToListGUI(groupName);
		}

		groupsPanel.setVisible(true);
	}

	/**
	 * Method updating the list of groups from a given set (necessary for filtering
	 * to work correctly)
	 */
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

	/**
	 * Method that removes a group from the list of displayed password groups
	 * (necessary for filtering to work correctly)
	 * 
	 * @param groupName
	 *            name of the group to be deleted from UI
	 */
	public void removePasswordGroupFromList(String groupName) {
		recordsCount = 0;
		UIHelper.removeGroup(groupName);
		repaintListFromData();
	}

	/**
	 * Method that filters the list of password groups
	 * 
	 * @param expression
	 *            expression expected in records that satisfy the filter
	 */
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

		if (expression.toLowerCase().equals("v01d")) {
			JOptionPane.showMessageDialog(null, "V01d nashel secret shalunishka");
		}

		if (expression.toLowerCase().equals("doomayka")) {
			JOptionPane.showMessageDialog(null,
					"Doomayka is the crappiest programmer in the world in the crappiest programming language");
		}
	}
}
