package lukaapps.net.mylist.services;

import android.content.Context;

import java.util.Date;

import lukaapps.net.mylist.db.EntryDAO;
import lukaapps.net.mylist.db.ItemDAO;
import lukaapps.net.mylist.model.Entry;
import lukaapps.net.mylist.model.Item;

public class EntryManager {

    public static void saveEntry(Entry entry, Context context) {
        EntryDAO entryDAO = new EntryDAO(context);
        entryDAO.saveEntry(entry);

        ItemDAO itemDAO = new ItemDAO(context);
        Item item = itemDAO.getItem(entry.title);

        if (item == null) {
            item = new Item(0, entry.title, new Date());
        } else {
            item.updateTimestamp();
        }

        itemDAO.saveItem(item);
    }
}
