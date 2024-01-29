package passwordmanagergui.group;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import passwordmanager.decoded.DefaultRecord;
import passwordmanager.decoded.IRecord;
import passwordmanager.decoded.IStorage;
import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Manager;

public class CreateGroupWindow {
    private final boolean windowIsModal = true;
    private boolean windowIsClosed = false;
    private String groupName = "default";
    private String groupPassword = "default";
    private JTextField groupNameField = null;
    private JTextField groupPasswordField = null;
    private IRawData resultGroup = null;

    public IRawData create(JFrame mainWindow) {
        JDialog groupCreateDialog = new JDialog(mainWindow, "Group creating", windowIsModal);
        groupCreateDialog.setSize(250, 160);
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
        groupNameField.setText("Group name");
        groupNameField.setPreferredSize(new Dimension(200, 20));
        groupPasswordField = new JTextField();
        groupPasswordField.setText("Group password");
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

    public IRawData createPasswordGroup() {
        groupName = checkAndRemoveFieldWhitespaces(groupNameField.getText());

        groupPassword = checkAndRemoveFieldWhitespaces(groupPasswordField.getText());

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

    public String checkAndRemoveFieldWhitespaces(String fieldText) {
        if (!fieldText.replaceAll("[^A-Za-zА-Яа-я0-9]", "").equals("")) {
            fieldText = fieldText.replaceAll("[^A-Za-zА-Яа-я0-9]", "_");
        }

        return fieldText;
    }

    private void waitWindowClose() {
        while (true) {
            if (windowIsClosed) {
                break;
            }
        }
    }
}
