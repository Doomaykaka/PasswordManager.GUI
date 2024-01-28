package passwordmanagergui;

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
import passwordmanager.decoded.ListStorage;
import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Manager;

public class PasswordWindow {
    private final boolean isModal = true;
    private boolean windowIsClosed = false;
    private String recordInfo = "default";
    private String recordLogin = "default";
    private String recordPassword = "default";
    private JTextField recordInfoField = null;
    private JTextField recordLoginField = null;
    private JTextField recordPasswordField = null;
    private IRawData rawData = null;
    private String password = "";

    public void create(JFrame mainWindow, String password, IRawData rawData) {
        this.password = password;
        this.rawData = rawData;

        JDialog dialog = new JDialog(mainWindow, "Record creating", isModal);
        dialog.setSize(250, 160);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                windowIsClosed = true;
            }
        });

        FlowLayout layout = new FlowLayout();

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setBounds(10, 10, 230, 100);

        recordInfoField = new JTextField();
        recordInfoField.setText("Record info");
        recordInfoField.setPreferredSize(new Dimension(200, 20));
        recordLoginField = new JTextField();
        recordLoginField.setText("Record login");
        recordLoginField.setPreferredSize(new Dimension(200, 20));
        recordPasswordField = new JTextField();
        recordPasswordField.setText("Record password");
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
                recordLogin = recordInfoField.getText();
                recordPassword = recordPasswordField.getText();

                if (!recordInfo.equals("") && !recordLogin.equals("") && !recordPassword.equals("")) {
                    createPasswordRecord();
                    dialog.dispose();
                    windowIsClosed = true;
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                windowIsClosed = true;
            }
        });

        panel.add(recordInfoField);
        panel.add(recordLoginField);
        panel.add(recordPasswordField);
        panel.add(createButton);
        panel.add(cancelButton);
        dialog.add(panel);

        dialog.setVisible(true);

        waitWindowClose();
    }

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

    private void waitWindowClose() {
        while (true) {
            if (windowIsClosed) {
                break;
            }
        }
    }

    public void removePasswordRecord(String recordLogin, String recordPassword, String password, IRawData rawData) {
        IStorage passwordGroup = Manager.getContext().getEncoder().decodeStruct(rawData, password);
        for (int i = 0; i < passwordGroup.size(); i++) {
            IRecord passRecord = passwordGroup.getByIndex(i);

            if (passRecord.getLogin().equals(recordLogin) && passRecord.getPassword().equals(recordPassword)) {
                passwordGroup.delete(i);
            }
        }

        IRawData newPasswordGroupEncoded = Manager.getContext().getEncoder().encodeStruct(passwordGroup, password);

        newPasswordGroupEncoded.setName(rawData.getName());
        newPasswordGroupEncoded.save();
    }

    public void update(JFrame mainWindow, String oldRecordInfo, String oldRecordLogin, String oldRecordPassword,
            String password, IRawData rawData) {
        this.password = password;
        this.rawData = rawData;

        JDialog dialog = new JDialog(mainWindow, "Record editing", isModal);
        dialog.setSize(250, 200);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                e.getWindow().dispose();
                windowIsClosed = true;
            }
        });

        FlowLayout layout = new FlowLayout();

        JPanel panel = new JPanel();
        panel.setLayout(layout);
        panel.setBounds(10, 10, 230, 100);

        recordInfoField = new JTextField();
        recordInfoField.setText(oldRecordInfo);
        recordInfoField.setPreferredSize(new Dimension(200, 20));
        recordLoginField = new JTextField();
        recordLoginField.setText(oldRecordLogin);
        recordLoginField.setPreferredSize(new Dimension(200, 20));
        recordPasswordField = new JTextField();
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
                recordLogin = recordInfoField.getText();
                recordPassword = recordPasswordField.getText();

                if (!recordInfo.equals("") && !recordLogin.equals("") && !recordPassword.equals("")) {
                    editPasswordRecord(oldRecordLogin, oldRecordPassword);
                    dialog.dispose();
                    windowIsClosed = true;
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                windowIsClosed = true;
            }
        });

        panel.add(recordInfoField);
        panel.add(recordLoginField);
        panel.add(recordPasswordField);
        panel.add(updateButton);
        panel.add(cancelButton);
        dialog.add(panel);

        dialog.setVisible(true);

        waitWindowClose();
    }

    private void editPasswordRecord(String oldRecordLogin, String oldRecordPassword) {
        IRecord findedRecord = null;

        IStorage passwordGroup = Manager.getContext().getEncoder().decodeStruct(rawData, password);

        for (int i = 0; i < passwordGroup.size(); i++) {
            IRecord passRecord = passwordGroup.getByIndex(i);

            if (passRecord.getLogin().equals(oldRecordLogin) && passRecord.getPassword().equals(oldRecordPassword)) {
                findedRecord = passRecord;
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
