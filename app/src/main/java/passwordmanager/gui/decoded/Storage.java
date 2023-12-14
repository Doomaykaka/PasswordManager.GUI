package passwordmanager.gui.decoded;

public interface Storage extends Cloneable {
    public void create(Record record);

    public Record read(int index);

    public void update(Record record);

    public void delete(int index);

    public Storage clone();

    public boolean isEmpty();

    public void clear();

    public int size();

    public String getName();

    public void setName(String newName);
}
