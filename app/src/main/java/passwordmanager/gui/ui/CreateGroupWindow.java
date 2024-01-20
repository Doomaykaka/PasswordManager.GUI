package passwordmanager.gui.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import passwordmanager.decoded.DefaultRecord;
import passwordmanager.decoded.IRecord;
import passwordmanager.decoded.DefaultRecord;
import passwordmanager.decoded.IStorage;
import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Manager;

public class CreateGroupWindow {
    private final boolean isModal = true;
    private boolean windowIsClosed = false;
    private String groupName = "default";
    private String groupPassword = "default";
    private JTextField groupField = null;
    private JTextField groupPasswordField = null;
    private IRawData resultGroup = null;

    public IRawData create(JFrame mainWindow) {
        JDialog dialog = new JDialog(mainWindow, "Group creating", isModal);
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setSize(250, 160);

        FlowLayout layout = new FlowLayout();

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setBounds(10, 10, 230, 100);

        groupField = new JTextField();
        groupField.setText("Group name");
        groupField.setPreferredSize(new Dimension(200, 20));
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
                dialog.dispose();
                windowIsClosed = true;
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                windowIsClosed = true;
            }
        });

        panel.add(groupField);
        panel.add(groupPasswordField);
        panel.add(createButton);
        panel.add(cancelButton);
        dialog.add(panel);

        dialog.setVisible(true);

        waitWindowClose();

        return resultGroup;
    }

    private IRawData createPasswordGroup() {
        if (!groupField.getText().replaceAll("[^A-Za-zА-Яа-я0-9]", "").equals("")) {
            groupName = groupField.getText().replaceAll("[^A-Za-zА-Яа-я0-9]", "_");
        }

        if (!groupPasswordField.getText().replaceAll("[^A-Za-zА-Яа-я0-9]", "").equals("")) {
            groupPassword = groupPasswordField.getText().replaceAll("[^A-Za-zА-Яа-я0-9]", "_");
        }
        
        IRecord whiteRecord = new DefaultRecord();

        IStorage newPasswordGroup = Manager.getContext().getStorage();
        newPasswordGroup.clear();
        newPasswordGroup.create(whiteRecord);
        
        IRawData newPasswordGroupEncoded = Manager.getContext().getEncoder().encodeStruct(newPasswordGroup,
                groupPassword);

        newPasswordGroupEncoded.setName(groupName);
        newPasswordGroupEncoded.save();
        
        return newPasswordGroupEncoded;
    }

    private void waitWindowClose() {
        while (true) {
            if (windowIsClosed) {
                break;
            }
        }
    }
}
