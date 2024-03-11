package passwordmanagergui.passwords;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import passwordmanager.decoded.DefaultRecord;
import passwordmanager.decoded.IRecord;
import passwordmanager.decoded.IStorage;
import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Manager;

/**
 * Windows for creating, updating and deleting an account
 *
 * @author Doomaykaka MIT License
 * @since 2024-01-31
 */
public class PasswordWindow {
	/**
	 * Field responsible for whether the window is modal
	 */
	private final boolean windowIsModal = true;
	/**
	 * Window closed status
	 */
	private boolean windowIsClosed = false;
	/**
	 * Information of the created account
	 */
	private String recordInfo = "default";
	/**
	 * Login of the created account
	 */
	private String recordLogin = "default";
	/**
	 * Password of the created account
	 */
	private String recordPassword = "default";
	/**
	 * Field for entering account information
	 */
	private JTextField recordInfoField = null;
	/**
	 * Field for entering account login
	 */
	private JTextField recordLoginField = null;
	/**
	 * Field for entering account password
	 */
	private JPasswordField recordPasswordField = null;
	/**
	 * Changed group of passwords in encrypted form
	 */
	private IRawData rawData = null;
	/**
	 * Group password
	 */
	private String password = "";

	/**
	 * A method that creates a window for entering information about a new account
	 * with the ability to confirm or cancel the creation of a new account
	 *
	 * @param mainWindow
	 *            parent window
	 * @param password
	 *            group password
	 * @param rawData
	 *            password group to which you need to add a new account
	 */
	public void create(JFrame mainWindow, String password, IRawData rawData) {
		this.password = password;
		this.rawData = rawData;

		int windowWidth = 250;
		int windowHeight = 180;

		JDialog recordCreateDialog = new JDialog(mainWindow, "Record creating", windowIsModal);
		recordCreateDialog.setSize(windowWidth, windowHeight);

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int xLocation = (int) center.getX() - (windowWidth / 2);
		int yLocation = (int) center.getY() - (windowHeight / 2);
		recordCreateDialog.setLocation(xLocation, yLocation);

		recordCreateDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				windowIsClosed = true;
			}
		});

		FlowLayout panelLayout = new FlowLayout();

		JPanel windowElementsPanel = new JPanel();
		windowElementsPanel.setLayout(panelLayout);
		windowElementsPanel.setBounds(10, 10, 230, 100);

		recordInfoField = new JTextField();
		recordInfoField.setToolTipText("Info");
		recordInfoField.setPreferredSize(new Dimension(200, 20));
		recordLoginField = new JTextField();
		recordLoginField.setToolTipText("Login");
		recordLoginField.setPreferredSize(new Dimension(200, 20));
		recordPasswordField = new JPasswordField();
		recordPasswordField.setToolTipText("Password");
		recordPasswordField.setPreferredSize(new Dimension(200, 20));

		JButton createButton = new JButton();
		createButton.setText("Add");
		createButton.setPreferredSize(new Dimension(140, 20));
		JButton cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.setPreferredSize(new Dimension(140, 20));

		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				recordInfo = recordInfoField.getText();
				recordLogin = recordLoginField.getText();
				recordPassword = new String(recordPasswordField.getPassword());

				if (!recordInfo.equals("") && !recordLogin.equals("") && !recordPassword.equals("")) {
					createPasswordRecord();
					recordCreateDialog.dispose();
					windowIsClosed = true;
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				recordCreateDialog.dispose();
				windowIsClosed = true;
			}
		});

		windowElementsPanel.add(recordInfoField);
		windowElementsPanel.add(recordLoginField);
		windowElementsPanel.add(recordPasswordField);
		windowElementsPanel.add(createButton);
		windowElementsPanel.add(cancelButton);
		recordCreateDialog.add(windowElementsPanel);

		recordCreateDialog.setVisible(true);

		waitWindowClose();
	}

	/**
	 * Method that creates a new account, adds it to a group, and then saves the
	 * group
	 */
	private void createPasswordRecord() {
		IRecord newRecord = new DefaultRecord();
		newRecord.setInfo(recordInfo);
		newRecord.setLogin(recordLogin);
		newRecord.setPassword(recordPassword);

		IStorage passwordGroup = Manager.getContext().getEncoder().decodeStruct(rawData, password);
		passwordGroup.create(newRecord);

		IRawData newPasswordGroupEncoded = Manager.getContext().getEncoder().encodeStruct(passwordGroup, password);

		newPasswordGroupEncoded.setName(rawData.getName());
		newPasswordGroupEncoded.save();
	}

	/**
	 * Method that waits for the window to close and then returns the result
	 */
	private void waitWindowClose() {
		while (true) {
			if (windowIsClosed) {
				break;
			}
		}
	}

	/**
	 * A method that removes an account from a group and saves the group
	 *
	 * @param recordLogin
	 *            account login
	 * @param recordPassword
	 *            account password
	 * @param password
	 *            group password
	 * @param rawData
	 *            encoded group
	 */
	public void removePasswordRecord(String recordLogin, String recordPassword, String password, IRawData rawData) {
		IStorage passwordGroup = Manager.getContext().getEncoder().decodeStruct(rawData, password);
		for (int i = 0; i < passwordGroup.size(); i++) {
			IRecord passwordRecord = passwordGroup.getByIndex(i);

			if (passwordRecord.getLogin().equals(recordLogin) && passwordRecord.getPassword().equals(recordPassword)) {
				passwordGroup.delete(i);
			}
		}

		IRawData newPasswordGroupEncoded = Manager.getContext().getEncoder().encodeStruct(passwordGroup, password);

		newPasswordGroupEncoded.setName(rawData.getName());
		newPasswordGroupEncoded.save();
	}

	/**
	 * A method that creates a window for entering new information about an existing
	 * account with the ability to confirm or cancel the account change
	 *
	 * @param mainWindow
	 *            parent window
	 * @param oldRecordInfo
	 *            account information to find it
	 * @param oldRecordLogin
	 *            account login to find it
	 * @param oldRecordPassword
	 *            account password to find it
	 * @param password
	 *            group password
	 * @param rawData
	 *            password group to which you need to add a new account
	 */
	public void update(JFrame mainWindow, String oldRecordInfo, String oldRecordLogin, String oldRecordPassword,
			String password, IRawData rawData) {
		int windowWidth = 250;
		int windowHeight = 200;

		this.password = password;
		this.rawData = rawData;

		JDialog recordUpdateDialog = new JDialog(mainWindow, "Record editing", windowIsModal);
		recordUpdateDialog.setSize(windowWidth, windowHeight);

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int xLocation = (int) center.getX() - (windowWidth / 2);
		int yLocation = (int) center.getY() - (windowHeight / 2);
		recordUpdateDialog.setLocation(xLocation, yLocation);

		recordUpdateDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				windowIsClosed = true;
			}
		});

		FlowLayout panelLayout = new FlowLayout();

		JPanel windowElementsPanel = new JPanel();
		windowElementsPanel.setLayout(panelLayout);
		windowElementsPanel.setBounds(10, 10, 230, 100);

		recordInfoField = new JTextField();
		recordInfoField.setToolTipText("Info");
		recordInfoField.setText(oldRecordInfo);
		recordInfoField.setPreferredSize(new Dimension(200, 20));

		recordLoginField = new JTextField();
		recordInfoField.setToolTipText("Login");
		recordLoginField.setText(oldRecordLogin);
		recordLoginField.setPreferredSize(new Dimension(200, 20));

		recordPasswordField = new JPasswordField();
		recordInfoField.setToolTipText("Password");
		recordPasswordField.setText(oldRecordPassword);
		recordPasswordField.setPreferredSize(new Dimension(200, 20));

		JButton updateButton = new JButton();
		updateButton.setText("Update");
		updateButton.setPreferredSize(new Dimension(140, 20));
		JButton cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.setPreferredSize(new Dimension(140, 20));

		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				recordInfo = recordInfoField.getText();
				recordLogin = recordLoginField.getText();
				recordPassword = new String(recordPasswordField.getPassword());

				if (!recordInfo.equals("") && !recordLogin.equals("") && !recordPassword.equals("")) {
					editPasswordRecord(oldRecordLogin, oldRecordPassword);
					recordUpdateDialog.dispose();
					windowIsClosed = true;
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				recordUpdateDialog.dispose();
				windowIsClosed = true;
			}
		});

		windowElementsPanel.add(recordInfoField);
		windowElementsPanel.add(recordLoginField);
		windowElementsPanel.add(recordPasswordField);
		windowElementsPanel.add(updateButton);
		windowElementsPanel.add(cancelButton);
		recordUpdateDialog.add(windowElementsPanel);

		recordUpdateDialog.setVisible(true);

		waitWindowClose();
	}

	/**
	 * Method that updates information about an existing account and then saves the
	 * group
	 *
	 * @param oldRecordLogin
	 *            account login to find it
	 * @param oldRecordPassword
	 *            account password to find it
	 */
	private void editPasswordRecord(String oldRecordLogin, String oldRecordPassword) {
		IRecord findedRecord = null;

		IStorage passwordGroup = Manager.getContext().getEncoder().decodeStruct(rawData, password);

		for (int i = 0; i < passwordGroup.size(); i++) {
			IRecord passwordRecord = passwordGroup.getByIndex(i);

			if (passwordRecord.getLogin().equals(oldRecordLogin)
					&& passwordRecord.getPassword().equals(oldRecordPassword)) {
				findedRecord = passwordRecord;
			}
		}

		findedRecord.setLogin(recordLogin);
		findedRecord.setPassword(recordPassword);

		passwordGroup.update(findedRecord);

		findedRecord.setInfo(recordInfo);

		IRawData newPasswordGroupEncoded = Manager.getContext().getEncoder().encodeStruct(passwordGroup, password);

		newPasswordGroupEncoded.setName(rawData.getName());
		newPasswordGroupEncoded.save();
	}
}
