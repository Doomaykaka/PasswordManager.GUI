package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.ListStorage;
import passwordmanager.gui.decoded.MapStorage;
import passwordmanager.gui.decoded.Storage;
import passwordmanager.gui.encoded.CheckedRawData;
import passwordmanager.gui.encoded.DefaultRawData;
import passwordmanager.gui.encoded.RawData;
import passwordmanager.gui.encoder.DefaultEncoder;
import passwordmanager.gui.encoder.Encoder;
import passwordmanager.gui.encoder.ThreadEncoder;

/**
 * Manager for initializing the context and providing access to it
 * 
 * @see ManagerContext
 * @see Logger
 * @see RawData
 * @see CheckedRawData
 * @see DefaultRawData
 * @see Storage
 * @see MapStorage
 * @see ListStorage
 * @see Encoder
 * @see DefaultEncoder
 * @see ThreadEncoder
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class Manager {
    /**
     * Link to context ({@link ManagerContext})
     */
    private static ManagerContext context;
    /**
     * Field indicating the need to use logs
     */
    private static boolean logsUsing;

    /**
     * Hidden constructor - prevents the creation of a class object
     */
    private Manager() {
    }

    /**
     * Method initializing the {@link ManagerContext} in accordance with the passed
     * values
     * 
     * @param needLogs           talks about the need to use logs
     * @param needRawDataChecked indicates the need to check the encoded data
     * @param needMapStorage     talks about the need to store decoded data in a
     *                           key-value structure
     * @param needThreadEncoder  speaks about the need to use multithreading when
     *                           encrypting/decrypting
     */
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

        Logger.inicialize(logsUsing);

        if (logsUsing) {
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

    /**
     * Method for getting a link to the created {@link ManagerContext}
     * 
     * @return manager context {@link ManagerContext}
     */
    public static ManagerContext getContext() {
        return Manager.context;
    }

    /**
     * Method for obtaining information about log usage
     * 
     * @return information about log usage
     */
    public static boolean logsIsUsing() {
        return logsUsing;
    }
}
