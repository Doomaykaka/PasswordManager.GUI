package passwordmanager.gui.decoded;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import passwordmanager.gui.manager.Logger;

public class ListStorage implements Storage {
    private List<Record> storage;

    public ListStorage() {
        Logger.addLog("Storage", "creating");
        storage = new LinkedList<Record>();
    }

    @Override
    public void create(Record record) {
        Logger.addLog("Storage", "record added");
        storage.add(record);
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
        for (Record rec : storage) {
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

    public ListStorage clone() {
        ListStorage clone = new ListStorage();
        clone.storage.addAll(storage);
        return clone;
    }

    public Iterator<Record> iterator() {
        return storage.iterator();
    }
}
