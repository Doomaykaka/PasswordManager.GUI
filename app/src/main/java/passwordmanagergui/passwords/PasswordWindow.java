package passwordmanagergui.passwords;

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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import passwordmanager.decoded.DefaultRecord;
import passwordmanager.decoded.IRecord;
import passwordmanager.decoded.IStorage;
import passwordmanager.encoded.IRawData;
import passwordmanager.manager.Manager;

public class PasswordWindow {
    private final boolean windowIsModal = true;
    private boolean windowIsClosed = false;
    private String recordInfo = "default";
    private String recordLogin = "default";
    private String recordPassword = "default";
    private JTextField recordInfoField = null;
    private JTextField recordLoginField = null;
    private JPasswordField recordPasswordField = null;
    private IRawData rawData = null;
    private String password = "";

    public void create(JFrame mainWindow, String password, IRawData rawData) {
        this.password = password;
        this.rawData = rawData;

        JDialog recordCreateDialog = new JDialog(mainWindow, "Record creating", windowIsModal);
        recordCreateDialog.setSize(250, 180);
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
        recordInfoField.setText("Record info");
        recordInfoField.setPreferredSize(new Dimension(200, 20));
        recordLoginField = new JTextField();
        recordLoginField.setText("Record login");
        recordLoginField.setPreferredSize(new Dimension(200, 20));
        recordPasswordField = new JPasswordField();
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
            IRecord passwordRecord = passwordGroup.getByIndex(i);

            if (passwordRecord.getLogin().equals(recordLogin) && passwordRecord.getPassword().equals(recordPassword)) {
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

        JDialog recordUpdateDialog = new JDialog(mainWindow, "Record editing", windowIsModal);
        recordUpdateDialog.setSize(250, 200);
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
        recordInfoField.setText(oldRecordInfo);
        recordInfoField.setPreferredSize(new Dimension(200, 20));
        recordLoginField = new JTextField();
        recordLoginField.setText(oldRecordLogin);
        recordLoginField.setPreferredSize(new Dimension(200, 20));
        recordPasswordField = new JPasswordField();
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
