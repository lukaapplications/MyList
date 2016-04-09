package lukaapps.net.mylist.db;

import android.content.Context;

import java.util.List;

import lukaapps.net.mylist.model.Entry;

public interface IEntryDAO {
    List<Entry> getEntries(Context context);

    Entry getEntry(long id, Context context);

    Entry saveEntry(Entry entry, Context context);
}


