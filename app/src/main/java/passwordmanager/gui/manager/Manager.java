package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.ListStorage;
import passwordmanager.gui.decoded.MapStorage;
import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoder.DefaultEncoder;
import passwordmanager.gui.encoder.Encoder;
import passwordmanager.gui.encoder.ThreadEncoder;
import passwordmanager.gui.rawdata.CheckedRawData;
import passwordmanager.gui.rawdata.DefaultRawData;
import passwordmanager.gui.rawdata.RawData;

public class Manager {
    private static ManagerContext context;
    private static boolean logsUsing;

    private Manager() {
    }

    public static void initialize(boolean needLogs, boolean needRawDataChecked, boolean needMapStorage,
            boolean needThreadEncoder) {
        RawData rawData;
        Storage storage;
        Encoder encoder;

        if (needRawDataChecked) {
            rawData = new CheckedRawData();
        } else {
            rawData = new DefaultRawData();
        }

        if (needMapStorage) {
            storage = new MapStorage();
        } else {
            storage = new ListStorage();
        }

        if (needThreadEncoder) {
            encoder = new ThreadEncoder();
        } else {
            encoder = new DefaultEncoder();
        }

        logsUsing = needLogs;

        if (needLogs) {
            context = new ManagerContextWithLogs();
            context.setRawData(rawData);
            context.setStorage(storage);
            context.setEncoder(encoder);
        } else {
            context = new DefaultManagerContext();
            context.setRawData(rawData);
            context.setStorage(storage);
            context.setEncoder(encoder);
        }
    };

    public static ManagerContext getContext() {
        return Manager.context;
    }

    public static boolean logsIsUsing() {
        return logsUsing;
    }
}
