package lukaapps.net.mylist.model;

import java.util.Date;

public class Item {
    public long id;
    public String title;
    public Date lastUsed;

    public Item(String title) {
        this.title = title;
        updateTimestamp();
    }

    public Item(long id, String title) {
        this(title);
        this.id = id;
    }

    public Item(long id, String title, Date lastUsed) {
        this(id, title);
        this.lastUsed = lastUsed;
    }

    public void updateTimestamp() {
        lastUsed = new Date();
    }
}
