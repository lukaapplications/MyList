package lukaapps.net.mylist.model;

public class Entry {

    public long id;
    public String title;

    public Entry(String title) {
        this.title = title;
    }

    public Entry(long id, String title) {
        this(title);
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Entry (%1$s) [%2$s]", id, title);
    }
}
