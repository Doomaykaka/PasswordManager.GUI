package passwordmanagergui.passwords;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
	private final double maxNameLength = 18.0;
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

		GridBagLayout gridLayout = new GridBagLayout();
		accountsPanel.setLayout(gridLayout);
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
		GridBagLayout gridLayout = new GridBagLayout();
		GridBagConstraints gridConstraint = new GridBagConstraints();

		JPanel accountPanel = new JPanel();
		accountPanel.setLayout(gridLayout);
		accountPanel.setSize(new Dimension(0, 0));

		JButton copyLoginButton = new JButton();
		copyLoginButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(copyIconPath)));

		JButton copyPasswordButton = new JButton();
		copyPasswordButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(copyIconPath)));

		JButton removePasswordButton = new JButton("X");
		JButton editPasswordButton = new JButton("\u270E");

		String info = record.getInfo();
		String infoWithBreaks = "";
		JTextArea infoLabel = new JTextArea();

		infoWithBreaks = info;
		if (info.length() > maxNameLength) {
			infoWithBreaks = insertBreaksIntoString(info, maxNameLength);
		}
		infoLabel.setText(infoWithBreaks);
		infoLabel.setEditable(false);

		String login = record.getLogin();
		String loginWithBreaks = "";
		JTextArea loginLabel = new JTextArea();

		loginWithBreaks = login;
		if (login.length() > maxNameLength) {
			loginWithBreaks = insertBreaksIntoString(login, maxNameLength);
		}
		loginLabel.setText(loginWithBreaks);
		loginLabel.setEditable(false);

		String password = record.getPassword();
		String passwordWithBreaks = "";
		String hidedPassword = password.replaceAll(".", "*");
		String hidedPasswordWithBreaks = "";
		JTextArea passwordLabel = new JTextArea();

		passwordWithBreaks = password;
		hidedPasswordWithBreaks = hidedPassword;
		if (password.length() > maxNameLength) {
			passwordWithBreaks = insertBreaksIntoString(password, maxNameLength);
			hidedPasswordWithBreaks = insertBreaksIntoString(hidedPassword, maxNameLength);
		}
		passwordLabel.setText(hidedPasswordWithBreaks);
		passwordLabel.setEditable(false);

		String normalTextCopy = String.valueOf(passwordWithBreaks);
		String hidedTextCopy = String.valueOf(hidedPasswordWithBreaks);
		passwordLabel.addMouseListener(new MouseListener() {

			private String normalText = normalTextCopy;
			private String hidedText = hidedTextCopy;
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

				if (isHided) {
					passwordLabel.setText(normalText);
				} else {
					passwordLabel.setText(hidedText);
				}

				isHided = !isHided;

				passwordLabel.setEditable(false);
			}
		});

		JLabel whitespaceLabel = new JLabel("         ");

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

		removePasswordButton.addActionListener(new ActionListener() {
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

				isConfirmed = JOptionPane.showConfirmDialog(groupWindow, message, null,
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

				return isConfirmed;
			}
		});

		editPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String confirmMessage = "You realy want to edit record " + record.getLogin();

				if (getConfirm(confirmMessage)) {
					PasswordWindow passwordWindow = new PasswordWindow();
					passwordWindow.update(groupWindow, info, login, password, groupPassword, encodedGroup);

					encodedGroup.load();

					decodedGroup = Manager.getContext().getEncoder().decodeStruct(encodedGroup, groupPassword);

					repaintListFromData();
				}
			}

			public boolean getConfirm(String message) {
				boolean isConfirmed = false;

				isConfirmed = JOptionPane.showConfirmDialog(groupWindow, message, null,
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;

				return isConfirmed;
			}
		});

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 0, 0, true);
		accountPanel.add(infoLabel, gridConstraint);

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 0, 1, false);
		accountPanel.add(loginLabel, gridConstraint);

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 1, 1, true);
		accountPanel.add(copyLoginButton, gridConstraint);

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 0, 2, false);
		accountPanel.add(passwordLabel, gridConstraint);

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 1, 2, true);
		accountPanel.add(copyPasswordButton, gridConstraint);

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 0, 3, true);
		accountPanel.add(editPasswordButton, gridConstraint);

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 1, 3, true);
		accountPanel.add(removePasswordButton, gridConstraint);

		initializeConstraint(gridConstraint, GridBagConstraints.HORIZONTAL, 0, 4, true); // maybe 5
		accountPanel.add(whitespaceLabel, gridConstraint);

		GridBagConstraints constraints = new GridBagConstraints();

		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.gridy = recordsCount;

		accountsPanel.add(accountPanel, constraints);

		recordsCount++;
	}

	/**
	 * Method that splits a string into equal parts
	 * 
	 * @param text
	 *            the string that needs to be split
	 * @param partLength
	 *            maximum length of a text part
	 * @return a line with breaks separating parts of the text
	 */
	public String insertBreaksIntoString(String text, double partLength) {
		String resultText = "";
		StringBuilder resultTextBuilder = new StringBuilder();

		String[] loginTextParts = text.split("(?<=\\G.{" + (int) Math.ceil(text.length() / partLength) + "})");
		for (String loginTextPart : loginTextParts) {
			resultTextBuilder.append(loginTextPart);
			resultTextBuilder.append("\n");
		}
		resultText = resultTextBuilder.toString();
		resultText = resultText.substring(0, resultText.lastIndexOf('\n'));

		return resultText;
	}

	/**
	 * Method that determines the location of elements in an account record in the
	 * list of accounts
	 * 
	 * @param gridConstraint
	 *            object defining restrictions
	 * @param fillConstraint
	 *            container filling order rule
	 * @param cellXPosition
	 *            element x position
	 * @param cellYPosition
	 *            element y position
	 * @param isPageEnd
	 *            the need to go to a new line
	 */
	public void initializeConstraint(GridBagConstraints gridConstraint, int fillConstraint, int cellXPosition,
			int cellYPosition, boolean isPageEnd) {
		gridConstraint.fill = fillConstraint;
		gridConstraint.gridx = cellXPosition;
		gridConstraint.gridy = cellYPosition;
		if (isPageEnd) {
			gridConstraint.anchor = GridBagConstraints.PAGE_END;
		}
	}

	/**
	 * Method updating the list of accounts with all found accounts in the group
	 */
	public void repaintListFromData() {
		accountsPanel.setVisible(false);

		accountsPanel.removeAll();

		for (int i = 0; i < decodedGroup.size(); i++) {
			IRecord accountRecord = decodedGroup.getByIndex(i);
			if (!accountRecord.getLogin().contains("Its white record") && !accountRecord.getLogin().contains("admin")
					&& !accountRecord.getPassword().contains("qwerty")) {
				addPasswordToListGUI(accountRecord);
			}
		}

		accountsPanel.setVisible(true);
	}

	/**
	 * Method for updating the list of accounts from a given set (necessary for
	 * filtering to work correctly)
	 */
	public void repaintList() {
		accountsPanel.revalidate();
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
				for (Component recordElement : ((JPanel) accountPanel).getComponents()) {
					if (recordElement.getClass().equals(JTextArea.class)) {
						JTextArea recordFieldLabel = ((JTextArea) recordElement);
						String recordFieldText = recordFieldLabel.getText();

						if (!recordFieldText.contains(expression.replace("\n", ""))) {
							accountsPanel.remove(accountPanel);
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
