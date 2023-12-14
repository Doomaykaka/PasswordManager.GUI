/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package passwordmanager.gui;

import passwordmanager.gui.decoded.DefaultRecord;
import passwordmanager.gui.decoded.Record;
import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.RawData;
import passwordmanager.gui.encoder.Encoder;
import passwordmanager.gui.encoder.Encoder.EncoderAlgorithm;
import passwordmanager.gui.manager.Logger;
import passwordmanager.gui.manager.Manager;

public class App {
    public static void main(String[] args) {
        boolean needsLogs = false;
        boolean needRawDataChecked = true;
        boolean needMapStorage = false;
        boolean needThreadEncoder = true;
        Manager.initialize(needsLogs, needRawDataChecked, needMapStorage, needThreadEncoder);

        System.out.println("=========Light=========");
        System.out.println("Hello");
        Manager.getContext().getEncoder().changeAlgorithm(EncoderAlgorithm.SHA256);

        String coded = Manager.getContext().getEncoder().encodeData("Hello", "123");

        System.out.println(coded);
        System.out.println("Bad key: " + Manager.getContext().getEncoder().decodeData(coded, "12"));
        System.out.println("Right key: " + Manager.getContext().getEncoder().decodeData(coded, "123"));

        System.out.println("=========Hard=========");
        Record newRecord = new DefaultRecord();
        newRecord.setLogin("admin");
        newRecord.setPassword("123");
        newRecord.setInfo("info");
        Record newRecord2 = new DefaultRecord();
        newRecord2.setLogin("admin2");
        newRecord2.setPassword("123");
        newRecord2.setInfo("info");
        Manager.getContext().getStorage().create(newRecord);
        Manager.getContext().getStorage().create(newRecord2);

        System.out
                .println("Record: " + newRecord.getLogin() + "," + newRecord.getPassword() + "," + newRecord.getInfo());
        System.out.println(
                "Record2: " + newRecord2.getLogin() + "," + newRecord2.getPassword() + "," + newRecord2.getInfo());
        RawData newRaw = Manager.getContext().getEncoder().encodeStruct(Manager.getContext().getStorage(), "123");
        for (String chunk : newRaw.getData()) {
            System.out.println(chunk);
        }

        Storage decodeStorage = Manager.getContext().getEncoder().decodeStruct(newRaw, "1"); // is null
        System.out.println("Bad key: " + decodeStorage);
        Encoder encoder = Manager.getContext().getEncoder();
        Storage storage = encoder.decodeStruct(newRaw, "123");
        Record decodeRecord = storage.read(0);
        System.out.print("Right key: " + decodeRecord.getLogin());
        System.out.print("," + decodeRecord.getPassword());
        System.out.println("," + decodeRecord.getInfo());

        Record decodeRecord2 = storage.read(1);
        System.out.print("Right key: " + decodeRecord2.getLogin());
        System.out.print("," + decodeRecord2.getPassword());
        System.out.println("," + decodeRecord2.getInfo());

        System.out.println("=========Log=========");

        for (String log : Logger.getLogs()) {
            System.out.println(log);
        }
    }
}
