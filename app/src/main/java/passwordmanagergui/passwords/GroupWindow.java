package passwordmanagergui.passwords;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneLayout;
import passwordmanager.decoded.IRecord;
import passwordmanager.decoded.IStorage;
import passwordmanager.encoded.DefaultRawData;
import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Logger;
import passwordmanager.manager.Manager;
import passwordmanagergui.UIHelper;

/**
 * Window with group records
 *
 * @see UIHelper
 * @author Doomaykaka MIT License
 * @since 2024-01-31
 */
public class GroupWindow {
	/**
	 * File with an encrypted group of passwords
	 */
	private File groupFile;
	/**
	 * Encrypted password group
	 */
	private IRawData encodedGroup;
	/**
	 * Decrypted password group
	 */
	private IStorage decodedGroup;
	/**
	 * Group window object
	 */
	private JFrame groupWindow;
	/**
	 * Panel with children of the group window
	 */
	private JPanel windowPanel;
	/**
	 * Panel with a list of accounts
	 */
	private JPanel accountsPanel;
	/**
	 * Container with a slider for a list of accounts
	 */
	private JScrollPane passwordSPane;
	/**
	 * Group password
	 */
	private String groupPassword = "";
	/**
	 * Account counter
	 */
	private int recordsCount = 0;
	/**
	 * The maximum length of account information, from which the information will be
	 * shortened for convenient display
	 */
	private final int maxFieldLength = 25;
	/**
	 * Path to copy icon
	 */
	private final String copyIconPath = "images/copy.png";

	/**
	 * Class constructor initializing a group file with passwords
	 *
	 * @param groupFile
	 *            file with a group of passwords
	 */
	public GroupWindow(File groupFile) {
		this.groupFile = groupFile;
	}

	/**
	 * Method that creates a window with a list of accounts and additional controls
	 */
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

	/**
	 * Method that generates a window for entering a password for a group to decode
	 * the group
	 *
	 * @return user-entered password for the group
	 */
	public String getPassword() {
		JPasswordField pf = new JPasswordField();

		JOptionPane.showConfirmDialog(null, pf, "Enter password:", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (pf.getPassword().length == 0) {
			return null;
		}

		return new String(pf.getPassword());
	}

	/**
	 * Method that generates a window with a message about the user entering an
	 * incorrect password for a group
	 */
	public void getBadPasswordMessage() {
		JOptionPane.showMessageDialog(null, "Wrong password");
	}

	/**
	 * Method for decrypting a group of passwords
	 *
	 * @param password
	 *            user entered password
	 * @return status of the correctness of the group password entered by the user
	 */
	public boolean decodeStruct(String password) {
		boolean isDecoded = false;

		if (groupFile != null) {
			if (groupFile.exists()) {
				try {
					List<String> records = new ArrayList<String>();

					Scanner scanner = new Scanner(groupFile);
					while (scanner.hasNextLine()) {
						records.add(scanner.nextLine());
					}
					scanner.close();

					encodedGroup = new DefaultRawData();
					encodedGroup.setName(UIHelper.getGroupNameByFile(groupFile));
					encodedGroup.setData(records);

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

	/**
	 * Method for starting to create a window
	 */
	public void startCreateWindow() {
		groupWindow = new JFrame("Password manager by Doomayka");
		groupWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		windowPanel = new JPanel();
		windowPanel.setLayout(null);
	}

	/**
	 * A method that packs all static elements of a window and also completes the
	 * creation of the window
	 */
	public void finishCreateWindow() {
		int windowWidth = 400;
		int windowHeight = 600;

		groupWindow.add(windowPanel);

		groupWindow.setPreferredSize(new Dimension(400, 600));

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int xLocation = (int) center.getX() - (windowWidth / 2);
		int yLocation = (int) center.getY() - (windowHeight / 2);
		groupWindow.setLocation(xLocation, yLocation);

		groupWindow.pack();
		groupWindow.setResizable(false);
		groupWindow.setVisible(true);
	}

	/**
	 * Method that fills a window with elements
	 */
	public void generateComponents() {

		generateMainTitle();

		generateAddButton();

		generateFilterPanel();

		generatePasswordsList();
	}

	/**
	 * Method for adding a title to a window
	 */
	public void generateMainTitle() {
		JLabel mainTitleLabel = new JLabel("Group " + encodedGroup.getName());
		mainTitleLabel.setBounds(150, 40, 100, 20);
		windowPanel.add(mainTitleLabel);
	}

	/**
	 * Method that adds a button for creating a new account to a window
	 */
	public void generateAddButton() {
		JButton addButton = new JButton();
		addButton.setText("+");
		addButton.setBounds(150, 80, 40, 20);
		addButton.setPreferredSize(new Dimension(100, 20));

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				PasswordWindow passwordWindow = new PasswordWindow();
				passwordWindow.create(groupWindow, groupPassword, encodedGroup);

				encodedGroup.load();

				decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, groupPassword);

				repaintListFromData();
			}
		});

		windowPanel.add(addButton);
	}

	/**
	 * Method that adds a panel to the window to filter the list of accounts
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
				filterPasswordsGroup(""); // reset filter

				filterPasswordsGroup(searchField.getText());
			}

			@Override
			public void keyReleased(KeyEvent e) {
				filterPasswordsGroup(""); // reset filter

				filterPasswordsGroup(searchField.getText());
			}

			@Override
			public void keyPressed(KeyEvent e) {
				filterPasswordsGroup(""); // reset filter

				filterPasswordsGroup(searchField.getText());
			}
		});

		filterPanel.add(searchField);
		windowPanel.add(filterPanel);
	}

	/**
	 * Method that adds a list to the window in which accounts will be located
	 */
	public void generatePasswordsList() {
		accountsPanel = new JPanel();
		accountsPanel.setLayout(new GridBagLayout());
		accountsPanel.setBounds(0, 160, 340, 380);

		passwordSPane = new JScrollPane(accountsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		passwordSPane.setLayout(new ScrollPaneLayout());
		passwordSPane.setBounds(20, 160, 340, 380);
		passwordSPane.setAutoscrolls(true);

		windowPanel.add(passwordSPane);
	}

	/**
	 * Method that dynamically adds a new account to the list
	 *
	 * @param record
	 *            account object to add to the list
	 */
	public void addPasswordToListGUI(IRecord record) {
		JPanel accountPanel = new JPanel(new GridBagLayout());
		accountPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(3, 3, 3, 3);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		JLabel infoHeader = new JLabel("Info:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		accountPanel.add(infoHeader, gbc);

		JTextArea infoLabel = new JTextArea(wrapText(record.getInfo(), maxFieldLength));
		infoLabel.setEditable(false);
		infoLabel.setLineWrap(true);
		infoLabel.setWrapStyleWord(true);
		infoLabel.setBackground(null);
		infoLabel.setBorder(null);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		accountPanel.add(infoLabel, gbc);

		JLabel loginHeader = new JLabel("Login:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		accountPanel.add(loginHeader, gbc);

		JTextArea loginLabel = new JTextArea(wrapText(record.getLogin(), maxFieldLength));
		loginLabel.setEditable(false);
		loginLabel.setLineWrap(true);
		loginLabel.setWrapStyleWord(true);
		loginLabel.setBackground(null);
		loginLabel.setBorder(null);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		accountPanel.add(loginLabel, gbc);

		JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		loginButtonPanel.setOpaque(false);

		JButton copyLoginButton = new JButton();
		try {
			copyLoginButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(copyIconPath)));
		} catch (Exception e) {
			copyLoginButton.setText("Copy");
		}
		copyLoginButton.setPreferredSize(new Dimension(60, 25));
		loginButtonPanel.add(copyLoginButton);

		gbc.gridx = 2;
		gbc.weightx = 0.0;
		accountPanel.add(loginButtonPanel, gbc);

		JLabel passwordHeader = new JLabel("Password:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.0;
		accountPanel.add(passwordHeader, gbc);

		String hiddenPassword = record.getPassword().replaceAll(".", "*");
		JTextArea passwordLabel = new JTextArea(wrapText(hiddenPassword, maxFieldLength));
		passwordLabel.setEditable(false);
		passwordLabel.setLineWrap(true);
		passwordLabel.setWrapStyleWord(true);
		passwordLabel.setBackground(null);
		passwordLabel.setBorder(null);
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		accountPanel.add(passwordLabel, gbc);

		JPanel passwordButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
		passwordButtonPanel.setOpaque(false);

		JButton copyPasswordButton = new JButton();
		try {
			copyPasswordButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(copyIconPath)));
		} catch (Exception e) {
			copyPasswordButton.setText("Copy");
		}
		copyPasswordButton.setPreferredSize(new Dimension(60, 25));
		passwordButtonPanel.add(copyPasswordButton);

		gbc.gridx = 2;
		gbc.weightx = 0.0;
		accountPanel.add(passwordButtonPanel, gbc);

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
		controlPanel.setOpaque(false);

		JButton editPasswordButton = new JButton("\u270E");
		editPasswordButton.setPreferredSize(new Dimension(45, 25));
		controlPanel.add(editPasswordButton);

		JButton removePasswordButton = new JButton("X");
		removePasswordButton.setPreferredSize(new Dimension(45, 25));
		controlPanel.add(removePasswordButton);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 0.0;
		gbc.anchor = GridBagConstraints.EAST;
		accountPanel.add(controlPanel, gbc);

		GridBagConstraints panelConstraints = new GridBagConstraints();
		panelConstraints.fill = GridBagConstraints.HORIZONTAL;
		panelConstraints.anchor = GridBagConstraints.NORTH;
		panelConstraints.gridy = recordsCount;
		panelConstraints.weightx = 1.0;
		panelConstraints.insets = new Insets(2, 2, 2, 2);

		String loginCopy = String.valueOf(record.getLogin());
		String passwordCopy = String.valueOf(record.getPassword());

		copyLoginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection selection = new StringSelection(loginCopy);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
		});

		copyPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection selection = new StringSelection(passwordCopy);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
			}
		});

		passwordLabel.addMouseListener(new MouseListener() {
			private boolean isHidden = true;

			@Override
			public void mouseClicked(MouseEvent e) {
				isHidden = !isHidden;
				if (isHidden) {
					passwordLabel.setText(wrapText(hiddenPassword, maxFieldLength));
				} else {
					passwordLabel.setText(wrapText(record.getPassword(), maxFieldLength));
				}
				passwordLabel.setCaretPosition(0);
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});

		removePasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String confirmMessage = "You really want to remove record " + record.getLogin();
				if (getConfirm(confirmMessage)) {
					PasswordWindow passwordWindow = new PasswordWindow();
					passwordWindow.removePasswordRecord(loginCopy, passwordCopy, groupPassword, encodedGroup);
					encodedGroup.load();
					decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, groupPassword);
					repaintListFromData();
				}
			}

			public boolean getConfirm(String message) {
				return JOptionPane.showConfirmDialog(groupWindow, message, null,
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			}
		});

		editPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String confirmMessage = "You really want to edit record " + record.getLogin();
				if (getConfirm(confirmMessage)) {
					PasswordWindow passwordWindow = new PasswordWindow();
					passwordWindow.update(groupWindow, record.getInfo(), record.getLogin(), record.getPassword(),
							groupPassword, encodedGroup);
					encodedGroup.load();
					decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, groupPassword);
					repaintListFromData();
				}
			}

			public boolean getConfirm(String message) {
				return JOptionPane.showConfirmDialog(groupWindow, message, null,
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
			}
		});

		accountsPanel.add(accountPanel, panelConstraints);
		recordsCount++;
	}

	/**
	 * Method that wraps text to fit within specified width
	 *
	 * @param text
	 *            text to wrap
	 * @param maxLength
	 *            maximum line length
	 * @return wrapped text
	 */
	private String wrapText(String text, int maxLength) {
		if (text == null || text.length() <= maxLength) {
			return text;
		}

		StringBuilder wrapped = new StringBuilder();
		String[] words = text.split(" ");
		StringBuilder line = new StringBuilder();

		for (String word : words) {
			if (line.length() + word.length() + 1 <= maxLength) {
				if (line.length() > 0) {
					line.append(" ");
				}
				line.append(word);
			} else {
				if (line.length() > 0) {
					wrapped.append(line.toString()).append("\n");
				}
				line = new StringBuilder(word);
			}
		}

		if (line.length() > 0) {
			wrapped.append(line.toString());
		}

		return wrapped.toString();
	}

	/**
	 * Method updating the list of accounts with all found accounts in the group
	 */
	public void repaintListFromData() {
		accountsPanel.setVisible(false);
		accountsPanel.removeAll();
		recordsCount = 0;

		if (decodedGroup != null) {
			for (int i = 0; i < decodedGroup.size(); i++) {
				IRecord accountRecord = decodedGroup.getByIndex(i);
				if (!accountRecord.getLogin().contains("Its white record")
						&& !accountRecord.getLogin().contains("admin")
						&& !accountRecord.getPassword().contains("qwerty")) {
					addPasswordToListGUI(accountRecord);
				}
			}
		}

		accountsPanel.revalidate();
		accountsPanel.repaint();
		accountsPanel.setVisible(true);
	}

	/**
	 * Method for updating the list of accounts from a given set (necessary for
	 * filtering to work correctly)
	 */
	public void repaintList() {
		accountsPanel.revalidate();
		accountsPanel.repaint();
	}

	/**
	 * Method that filters the list of accounts
	 *
	 * @param expression
	 *            expression expected in records that satisfy the filter
	 */
	public void filterPasswordsGroup(String expression) {
		if (!expression.equals("")) {
			Component[] accountPanels = accountsPanel.getComponents();
			for (Component accountPanel : accountPanels) {
				boolean shouldRemove = true;
				for (Component recordElement : ((JPanel) accountPanel).getComponents()) {
					if (recordElement instanceof JTextArea) {
						JTextArea recordFieldLabel = ((JTextArea) recordElement);
						String recordFieldText = recordFieldLabel.getText().replace("\n", "");
						if (recordFieldText.toLowerCase().contains(expression.toLowerCase())) {
							shouldRemove = false;
							break;
						}
					}
				}
				if (shouldRemove) {
					accountsPanel.remove(accountPanel);
				}
			}
			repaintList();
		} else {
			repaintListFromData();
		}
	}
}
