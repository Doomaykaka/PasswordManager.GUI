package passwordmanagergui.group;

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
 * Window for creating a new password group
 * 
 * @author Doomaykaka MIT License
 * @since 2024-01-31
 */
public class CreateGroupWindow {
	/**
	 * Field responsible for whether the window is modal
	 */
	private final boolean windowIsModal = true;
	/**
	 * Window closed status
	 */
	private boolean windowIsClosed = false;
	/**
	 * Name of the group being created
	 */
	private String groupName = "default";
	/**
	 * Password of the created group
	 */
	private String groupPassword = "default";
	/**
	 * Group name entry field
	 */
	private JTextField groupNameField = null;
	/**
	 * Group password entry field
	 */
	private JPasswordField groupPasswordField = null;
	/**
	 * Created group of passwords in encrypted form
	 */
	private IRawData resultGroup = null;
	/**
	 * Expression to remove whitespace characters from a password group name
	 */
	private final String allNotWhitespaceSymbolsRegexp = "[^A-Za-zА-Яа-я0-9]";

	/**
	 * A method that creates a window for entering information about a new group of
	 * passwords with the ability to confirm or cancel the creation of a new group
	 * of passwords
	 * 
	 * @param mainWindow
	 *            parent window
	 * @return reference to an encoded password group object
	 */
	public IRawData create(JFrame mainWindow) {
		int windowWidth = 250;
		int windowHeight = 160;

		JDialog groupCreateDialog = new JDialog(mainWindow, "Group creating", windowIsModal);
		groupCreateDialog.setSize(windowWidth, windowHeight);

		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		int xLocation = (int) center.getX() - (windowWidth / 2);
		int yLocation = (int) center.getY() - (windowHeight / 2);
		groupCreateDialog.setLocation(xLocation, yLocation);

		groupCreateDialog.addWindowListener(new WindowAdapter() {
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

		groupNameField = new JTextField();
		groupNameField.setToolTipText("Group name");
		groupNameField.setPreferredSize(new Dimension(200, 20));
		groupPasswordField = new JPasswordField();
		groupPasswordField.setToolTipText("Group password");
		groupPasswordField.setPreferredSize(new Dimension(200, 20));

		JButton createButton = new JButton();
		createButton.setText("Create");
		createButton.setPreferredSize(new Dimension(140, 20));
		JButton cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.setPreferredSize(new Dimension(140, 20));

		createButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				resultGroup = createPasswordGroup();
				groupCreateDialog.dispose();
				windowIsClosed = true;
			}
		});

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				groupCreateDialog.dispose();
				windowIsClosed = true;
			}
		});

		windowElementsPanel.add(groupNameField);
		windowElementsPanel.add(groupPasswordField);
		windowElementsPanel.add(createButton);
		windowElementsPanel.add(cancelButton);
		groupCreateDialog.add(windowElementsPanel);

		groupCreateDialog.setVisible(true);

		waitWindowClose();

		return resultGroup;
	}

	/**
	 * Method that creates an encrypted group of passwords
	 * 
	 * @return reference to an encoded password group object
	 */
	public IRawData createPasswordGroup() {
		groupName = checkAndRemoveFieldWhitespaces(groupNameField.getText());

		groupPassword = checkAndRemoveFieldWhitespaces(new String(groupPasswordField.getPassword()));

		IRecord whiteRecord = new DefaultRecord();
		whiteRecord.setInfo("Its white record");
		whiteRecord.setLogin("admin");
		whiteRecord.setPassword("qwerty");

		IStorage newPasswordGroup = Manager.getContext().getStorage();
		newPasswordGroup.clear();
		newPasswordGroup.create(whiteRecord);

		IRawData newPasswordGroupEncoded = Manager.getContext().getEncoder().encodeStruct(newPasswordGroup,
				groupPassword);

		newPasswordGroupEncoded.setName(groupName);
		newPasswordGroupEncoded.save();

		return newPasswordGroupEncoded;
	}

	/**
	 * Method that removes all whitespace characters from a string
	 * 
	 * @param fieldText
	 * @return text without whitespace characters
	 */
	public String checkAndRemoveFieldWhitespaces(String fieldText) {
		if (!fieldText.replaceAll(allNotWhitespaceSymbolsRegexp, "").equals("")) {
			fieldText = fieldText.replaceAll(allNotWhitespaceSymbolsRegexp, "_");
		}

		return fieldText;
	}

	/**
	 * Method that waits for the window to close and then returns the created group
	 */
	private void waitWindowClose() {
		while (true) {
			if (windowIsClosed) {
				break;
			}
		}
	}
}
