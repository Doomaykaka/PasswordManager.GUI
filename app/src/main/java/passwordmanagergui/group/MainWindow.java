package passwordmanagergui.group;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

		// Создаем панель группы с правильной разметкой
		JPanel groupPanel = new JPanel();
		GridBagLayout groupLayout = new GridBagLayout();
		groupPanel.setLayout(groupLayout);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(2, 2, 2, 2); // Отступы между компонентами
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Название группы - занимает основную ширину
		JLabel groupNameLabel = new JLabel(groupName);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0; // Распределяем доступное пространство
		gbc.anchor = GridBagConstraints.WEST;
		groupPanel.add(groupNameLabel, gbc);

		// Панель с кнопками
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		buttonPanel.setOpaque(false);

		JButton openGroupButton = new JButton("Open");
		openGroupButton.setPreferredSize(new Dimension(60, 25));

		JButton removeGroupButton = new JButton("X");
		removeGroupButton.setPreferredSize(new Dimension(45, 25));

		buttonPanel.add(openGroupButton);
		buttonPanel.add(removeGroupButton);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.0; // Кнопки не растягиваются
		gbc.anchor = GridBagConstraints.EAST;
		groupPanel.add(buttonPanel, gbc);

		// Настройка контейнера для группы
		GridBagConstraints panelGbc = new GridBagConstraints();
		panelGbc.fill = GridBagConstraints.HORIZONTAL;
		panelGbc.anchor = GridBagConstraints.NORTH;
		panelGbc.gridy = recordsCount;
		panelGbc.weightx = 1.0;
		panelGbc.insets = new Insets(1, 1, 1, 1);

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
				String confirmRemoveMessage = "You really want to remove " + groupNameCopy;
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

		groupsPanel.add(groupPanel, panelGbc);
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

		recordsCount = 0; // Сбрасываем счетчик
		for (String groupName : UIHelper.getGroups()) {
			addPasswordGroupToListGUI(groupName);
		}

		groupsPanel.revalidate();
		groupsPanel.repaint();
		groupsPanel.setVisible(true);
	}

	/**
	 * Method updating the list of groups from a given set (necessary for filtering
	 * to work correctly)
	 */
	public void repaintList() {
		List<String> groupNames = new ArrayList<String>();

		for (Component groupPanel : groupsPanel.getComponents()) {
			Component[] components = ((JPanel) groupPanel).getComponents();
			if (components.length > 0 && components[0] instanceof JLabel) {
				groupNames.add(((JLabel) components[0]).getText());
			}
		}

		groupsPanel.setVisible(false);
		groupsPanel.removeAll();

		recordsCount = 0; // Сбрасываем счетчик
		for (String groupName : groupNames) {
			addPasswordGroupToListGUI(groupName);
		}

		groupsPanel.revalidate();
		groupsPanel.repaint();
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
				Component[] components = ((JPanel) groupPanel).getComponents();
				if (components.length > 0 && components[0] instanceof JLabel) {
					JLabel label = (JLabel) components[0];
					if (!label.getText().contains(expression)) {
						groupsPanel.remove(groupPanel);
					}
				}
			}

			repaintList();
		} else {
			repaintListFromData();
		}

		if (expression.toLowerCase().equals("v01d")) {
			JOptionPane.showMessageDialog(null, "v01d nashel secret shalunishka");
		}

		if (expression.toLowerCase().equals("Doomayka")) {
			JOptionPane.showMessageDialog(null,
					"Doomayka is the crappiest programmer in the world in the crappiest programming language");
		}
	}
}
