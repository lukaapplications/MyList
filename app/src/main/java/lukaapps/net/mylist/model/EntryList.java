package lukaapps.net.mylist.model;

public class EntryList {

    public long id;
    public String title;

    public EntryList(String title) {
        this.title = title;
    }

    public EntryList(long id, String title) {
        this(title);
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("List (%1$s) [%2$s]", id, title);
    }
}
