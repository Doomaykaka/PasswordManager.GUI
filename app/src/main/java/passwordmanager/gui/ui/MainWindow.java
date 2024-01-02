package passwordmanager.gui.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import org.checkerframework.checker.fenum.qual.SwingBoxOrientation;
import passwordmanager.manager.Logger;

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
		String group = null;

		group = "Group1";
		addPasswordGroupToListGUI(group);
		UIHelper.addGroup(group);
		group = "Group2";
		addPasswordGroupToListGUI(group);
		UIHelper.addGroup(group);
		group = "Group3";
		addPasswordGroupToListGUI(group);
		UIHelper.addGroup(group);
		group = "Group4";
		addPasswordGroupToListGUI(group);
		UIHelper.addGroup(group);
		group = "Group5";
		addPasswordGroupToListGUI(group);
		UIHelper.addGroup(group);
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
				addPasswordGroupToListGUI("Test");
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
				addPasswordGroupToListGUI("Test");
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
		field.setPreferredSize(new Dimension(200, 20));
		JButton searchButton = new JButton();
		searchButton.setText("find");
		searchButton.setPreferredSize(new Dimension(140, 20));

		//
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Temp
				filterPasswordGroups(field.getText());
			}
		});
		//

		panel.add(field);
		panel.add(searchButton);

		mainPanel.add(panel);
	}

	public void generatePasswordGroupsList() {
		groupPanel = new JPanel();
		// groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
		GridLayout gridLayout = new GridLayout();
		gridLayout.setColumns(1);
		gridLayout.setRows(0);
		gridLayout.setVgap(5);
		gridLayout.setHgap(0);
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

		JPanel panel = new JPanel();
		panel.setLayout(gridLayout);
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

		//
		openButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Temp
				String test = "TestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTestTest";
				addPasswordGroupToListGUI(test);
				UIHelper.addGroup(test);

				repaintListFromData();
			}
		});
		//

		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removePasswordGroupFromList(label.getText());
			}
		});

		panel.add(label);
		panel.add(openButton);
		panel.add(removeButton);

		groupPanel.add(panel);
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
