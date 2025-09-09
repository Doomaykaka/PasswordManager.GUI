package passwordmanagergui.passwords;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
import java.util.Hashtable;
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
	private static final int WINDOW_WIDTH = 400;
	private static final int WINDOW_HEIGHT = 600;
	private static final int MAIN_TITLE_X = 150;
	private static final int MAIN_TITLE_Y = 40;
	private static final int MAIN_TITLE_WIDTH = 100;
	private static final int MAIN_TITLE_HEIGHT = 20;
	private static final int ADD_BUTTON_X = 150;
	private static final int ADD_BUTTON_Y = 80;
	private static final int ADD_BUTTON_WIDTH = 40;
	private static final int ADD_BUTTON_HEIGHT = 20;
	private static final int ADD_BUTTON_PREFERRED_WIDTH = 100;
	private static final int ADD_BUTTON_PREFERRED_HEIGHT = 20;
	private static final int FILTER_PANEL_X = 10;
	private static final int FILTER_PANEL_Y = 120;
	private static final int FILTER_PANEL_WIDTH = 360;
	private static final int FILTER_PANEL_HEIGHT = 40;
	private static final int SEARCH_FIELD_WIDTH = 350;
	private static final int SEARCH_FIELD_HEIGHT = 20;
	private static final int ACCOUNTS_PANEL_X = 0;
	private static final int ACCOUNTS_PANEL_Y = 160;
	private static final int ACCOUNTS_PANEL_WIDTH = 340;
	private static final int ACCOUNTS_PANEL_HEIGHT = 380;
	private static final int PASSWORD_SCROLL_PANE_X = 20;
	private static final int PASSWORD_SCROLL_PANE_Y = 160;
	private static final int PASSWORD_SCROLL_PANE_WIDTH = 340;
	private static final int PASSWORD_SCROLL_PANE_HEIGHT = 380;
	private static final int COPY_BUTTON_WIDTH = 50;
	private static final int COPY_BUTTON_HEIGHT = 35;
	private static final int QR_BUTTON_WIDTH = 50;
	private static final int QR_BUTTON_HEIGHT = 35;
	private static final int EDIT_BUTTON_WIDTH = 45;
	private static final int EDIT_BUTTON_HEIGHT = 30;
	private static final int REMOVE_BUTTON_WIDTH = 45;
	private static final int REMOVE_BUTTON_HEIGHT = 30;
	private static final int BUTTON_SPACING_SMALL = 2;
	private static final int BUTTON_SPACING_MEDIUM = 5;
	private static final int COMPONENT_INSETS = 3;
	private static final int PANEL_INSETS = 2;
	private static final String WRONG_PASSWORD_MESSAGE = "Wrong password";
	private static final String PASSWORD_DIALOG_TITLE = "Enter password:";
	private static final String DECODING_ERROR_LOG = "decoding error";
	private static final String RAW_DATA_LOG = "RawData";
	private static final String COPY_BUTTON_TEXT = "Copy";
	private static final String QR_BUTTON_TEXT = "QR";
	private static final String EDIT_BUTTON_TEXT = "\u270E";
	private static final String REMOVE_BUTTON_TEXT = "X";
	private static final String CONFIRM_REMOVE_MESSAGE = "You really want to remove record ";
	private static final String CONFIRM_EDIT_MESSAGE = "You really want to edit record ";
	private static final String WHITE_RECORD_FILTER = "Its white record";
	private static final String ADMIN_FILTER = "admin";
	private static final String QWERTY_FILTER = "qwerty";

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
	 * Path to show QR
	 */
	private final String showQRPath = "images/qr.png";

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

		JOptionPane.showConfirmDialog(null, pf, PASSWORD_DIALOG_TITLE, JOptionPane.OK_CANCEL_OPTION,
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
		JOptionPane.showMessageDialog(null, WRONG_PASSWORD_MESSAGE);
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
					Logger.addLog(RAW_DATA_LOG, DECODING_ERROR_LOG);
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
		groupWindow.add(windowPanel);

		groupWindow.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int xLocation = (int) center.getX() - (WINDOW_WIDTH / 2);
		int yLocation = (int) center.getY() - (WINDOW_HEIGHT / 2);
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
		mainTitleLabel.setBounds(MAIN_TITLE_X, MAIN_TITLE_Y, MAIN_TITLE_WIDTH, MAIN_TITLE_HEIGHT);
		windowPanel.add(mainTitleLabel);
	}

	/**
	 * Method that adds a button for creating a new account to a window
	 */
	public void generateAddButton() {
		JButton addButton = new JButton();
		addButton.setText("+");
		addButton.setBounds(ADD_BUTTON_X, ADD_BUTTON_Y, ADD_BUTTON_WIDTH, ADD_BUTTON_HEIGHT);
		addButton.setPreferredSize(new Dimension(ADD_BUTTON_PREFERRED_WIDTH, ADD_BUTTON_PREFERRED_HEIGHT));

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
		filterPanel.setBounds(FILTER_PANEL_X, FILTER_PANEL_Y, FILTER_PANEL_WIDTH, FILTER_PANEL_HEIGHT);

		JTextField searchField = new JTextField();
		searchField.setToolTipText("Search");
		searchField.setPreferredSize(new Dimension(SEARCH_FIELD_WIDTH, SEARCH_FIELD_HEIGHT));

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
		accountsPanel.setBounds(ACCOUNTS_PANEL_X, ACCOUNTS_PANEL_Y, ACCOUNTS_PANEL_WIDTH, ACCOUNTS_PANEL_HEIGHT);

		passwordSPane = new JScrollPane(accountsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		passwordSPane.setLayout(new ScrollPaneLayout());
		passwordSPane.setBounds(PASSWORD_SCROLL_PANE_X, PASSWORD_SCROLL_PANE_Y, PASSWORD_SCROLL_PANE_WIDTH,
				PASSWORD_SCROLL_PANE_HEIGHT);
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
		gbc.insets = new Insets(COMPONENT_INSETS, COMPONENT_INSETS, COMPONENT_INSETS, COMPONENT_INSETS);
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

		JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, BUTTON_SPACING_SMALL, 0));
		loginButtonPanel.setOpaque(false);

		JButton copyLoginButton = new JButton();
		try {
			copyLoginButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(copyIconPath)));
		} catch (Exception e) {
			copyLoginButton.setText(COPY_BUTTON_TEXT);
		}
		copyLoginButton.setPreferredSize(new Dimension(COPY_BUTTON_WIDTH, COPY_BUTTON_HEIGHT));
		loginButtonPanel.add(copyLoginButton);

		JButton qrLoginButton = new JButton();
		try {
			qrLoginButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(showQRPath)));
		} catch (Exception e) {
			qrLoginButton.setText(QR_BUTTON_TEXT);
		}
		qrLoginButton.setPreferredSize(new Dimension(QR_BUTTON_WIDTH, QR_BUTTON_HEIGHT));
		loginButtonPanel.add(qrLoginButton);

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

		JPanel passwordButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, BUTTON_SPACING_SMALL, 0));
		passwordButtonPanel.setOpaque(false);

		JButton copyPasswordButton = new JButton();
		try {
			copyPasswordButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(copyIconPath)));
		} catch (Exception e) {
			copyPasswordButton.setText(COPY_BUTTON_TEXT);
		}
		copyPasswordButton.setPreferredSize(new Dimension(COPY_BUTTON_WIDTH, COPY_BUTTON_HEIGHT));
		passwordButtonPanel.add(copyPasswordButton);

		JButton qrPasswordButton = new JButton();
		try {
			qrPasswordButton.setIcon(new ImageIcon(GroupWindow.class.getClassLoader().getResource(showQRPath)));
		} catch (Exception e) {
			qrPasswordButton.setText(QR_BUTTON_TEXT);
		}
		qrPasswordButton.setPreferredSize(new Dimension(QR_BUTTON_WIDTH, QR_BUTTON_HEIGHT));
		passwordButtonPanel.add(qrPasswordButton);

		gbc.gridx = 2;
		gbc.weightx = 0.0;
		accountPanel.add(passwordButtonPanel, gbc);

		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, BUTTON_SPACING_MEDIUM, 0));
		controlPanel.setOpaque(false);

		JButton editPasswordButton = new JButton(EDIT_BUTTON_TEXT);
		editPasswordButton.setPreferredSize(new Dimension(EDIT_BUTTON_WIDTH, EDIT_BUTTON_HEIGHT));
		controlPanel.add(editPasswordButton);

		JButton removePasswordButton = new JButton(REMOVE_BUTTON_TEXT);
		removePasswordButton.setPreferredSize(new Dimension(REMOVE_BUTTON_WIDTH, REMOVE_BUTTON_HEIGHT));
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
		panelConstraints.insets = new Insets(PANEL_INSETS, PANEL_INSETS, PANEL_INSETS, PANEL_INSETS);

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

		qrLoginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showQRCode(loginCopy, "Login QR Code");
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

		qrPasswordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showQRCode(passwordCopy, "Password QR Code");
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
				String confirmMessage = CONFIRM_REMOVE_MESSAGE + record.getLogin();
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
				String confirmMessage = CONFIRM_EDIT_MESSAGE + record.getLogin();
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

	private void showQRCode(String text, String title) {
		try {
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			Hashtable hints = new Hashtable();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300, hints);

			JFrame qrFrame = new JFrame(title);
			qrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			JLabel qrLabel = new JLabel(new ImageIcon(MatrixToImageWriter.toBufferedImage(bitMatrix)));
			JPanel panel = new JPanel();
			panel.add(qrLabel);
			qrFrame.add(panel);

			qrFrame.pack();
			qrFrame.setLocationRelativeTo(null);
			qrFrame.setVisible(true);
		} catch (WriterException e) {
			JOptionPane.showMessageDialog(groupWindow, "Error generating QR code");
		}
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
				if (!accountRecord.getLogin().contains(WHITE_RECORD_FILTER)
						&& !accountRecord.getLogin().contains(ADMIN_FILTER)
						&& !accountRecord.getPassword().contains(QWERTY_FILTER)) {
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
