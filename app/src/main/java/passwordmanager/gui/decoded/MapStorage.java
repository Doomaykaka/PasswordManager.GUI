package passwordmanager.gui.decoded;

import java.util.HashMap;
import java.util.Map;

import passwordmanager.gui.manager.Logger;

public class MapStorage implements Storage {
    private Map<Integer, Record> storage;

    public MapStorage() {
        Logger.addLog("Storage", "creating");
        storage = new HashMap<Integer, Record>();
    }

    @Override
    public void create(Record record) {
        Logger.addLog("Storage", "record added");
        storage.put(record.hashCode(), record);
    }

    @Override
    public Record read(int index) {
        return (Record) storage.values().toArray()[index];
    }

    @Override
    public void update(Record record) {
        Logger.addLog("Storage", "record changed");
        Record findedRecord = null;
        for (Record rec : storage.values()) {
            if (rec.getInfo().equals(record.getInfo())) {
                findedRecord = rec;
            }
        }

        findedRecord.setInfo(record.getInfo());
        findedRecord.setLogin(record.getLogin());
        findedRecord.setPassword(record.getPassword());
    }

    @Override
    public void delete(Record record) {
        Logger.addLog("Storage", "record deleted");
        storage.remove(record.hashCode());
    }

    @Override
    public boolean isEmpty() {
        return storage.isEmpty();
    }

    @Override
    public void clear() {
        storage.clear();
    }
    
    @Override
    public int size() {
        return storage.size();
    }

    public MapStorage clone() {
        MapStorage clone = new MapStorage();
        clone.storage = storage;
        return clone;
    }

    public void put(int key, Record record) {
        storage.put(key, record);
    }

}
