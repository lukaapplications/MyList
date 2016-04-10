package lukaapps.net.mylist.db;

import java.util.List;

import lukaapps.net.mylist.model.Entry;

public interface IEntryDAO {
    List<Entry> getEntries();

    List<Entry> getEntries(long listID);
    Entry getEntry(long id);
    Entry saveEntry(Entry entry);
    boolean deleteEntry(long id);
    boolean deleteEntries(List<Entry> entries);
}


