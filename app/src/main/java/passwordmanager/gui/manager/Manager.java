package passwordmanager.gui.manager;

import passwordmanager.gui.decoded.ListStorage;
import passwordmanager.gui.decoded.MapStorage;
import passwordmanager.gui.decoded.IStorage;
import passwordmanager.gui.encoded.CheckedRawData;
import passwordmanager.gui.encoded.DefaultRawData;
import passwordmanager.gui.encoded.IRawData;
import passwordmanager.gui.encoder.DefaultEncoder;
import passwordmanager.gui.encoder.IEncoder;
import passwordmanager.gui.encoder.ThreadEncoder;

/**
 * Manager for initializing the context and providing access to it
 * 
 * @see IContextManager
 * @see Logger
 * @see IRawData
 * @see CheckedRawData
 * @see DefaultRawData
 * @see IStorage
 * @see MapStorage
 * @see ListStorage
 * @see IEncoder
 * @see DefaultEncoder
 * @see ThreadEncoder
 * @author Doomaykaka MIT License
 * @since 2023-12-14
 */
public class Manager {
    /**
     * Link to context ({@link IContextManager})
     */
    private static IContextManager context;
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
     * Method initializing the {@link IContextManager} in accordance with the passed
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
        IRawData rawData;
        IStorage storage;
        IEncoder encoder;

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
            context = new ContextManagerLogged();
            context.setRawData(rawData);
            context.setStorage(storage);
            context.setEncoder(encoder);
        } else {
            context = new DefaultContextManager();
            context.setRawData(rawData);
            context.setStorage(storage);
            context.setEncoder(encoder);
        }
    };

    /**
     * Method for getting a link to the created {@link IContextManager}
     * 
     * @return manager context {@link IContextManager}
     */
    public static IContextManager getContext() {
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
