package passwordmanager.gui.decoded;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import passwordmanager.gui.manager.Logger;

public class MapStorage implements Storage {
    private Map<Integer, Record> storage;
    private String name;

    public MapStorage() {
        Logger.addLog("Storage", "creating");
        storage = new HashMap<Integer, Record>();
        name = "default";
    }

    @Override
    public void create(Record record) {
        Logger.addLog("Storage", "record added");
        int maxInd = -1;
        maxInd = Collections.max(storage.keySet());
        storage.put(maxInd + 1, record);
    }

    @Override
    public Record read(int index) {
        Logger.addLog("Storage", "record readed");
        return storage.get(index);
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
    public void delete(int index) {
        Logger.addLog("Storage", "record deleted");
        storage.remove(index);
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String newName) {
        this.name = newName;
    }

    public MapStorage clone() {
        MapStorage clone = new MapStorage();
        clone.storage.putAll(storage);
        return clone;
    }

    public void put(int index, Record record) {
        storage.put(index, record);
    }

}
