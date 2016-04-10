package lukaapps.net.mylist.Utils;

import android.content.Context;

import lukaapps.net.mylist.db.EntryDAO;
import lukaapps.net.mylist.db.EntryListDAO;
import lukaapps.net.mylist.db.ItemDAO;
import lukaapps.net.mylist.model.Entry;
import lukaapps.net.mylist.model.EntryList;
import lukaapps.net.mylist.services.EntryManager;

public class SampleDataGenerator {

    private Context context;
    private EntryListDAO listDAO;
    private EntryDAO entryDAO;
    private ItemDAO itemDAO;

    private String[] shoppingList = new String[]{"Bread", "Milk", "Orange juice"};
    private String[] toDoList = new String[]{"Eat", "Sleep", "Watch TV"};

    public SampleDataGenerator(Context context) {
        this.context = context;
        listDAO = new EntryListDAO(context);
        entryDAO = new EntryDAO(context);
        itemDAO = new ItemDAO(context);
    }

    public void createSampleShoppingCartList() {
        EntryList list = new EntryList("My Shoppping list");
        listDAO.saveList(list);

        for (String s : shoppingList) {
            Entry entry = new Entry(0, s, list.id);
            EntryManager.saveEntry(entry, context);
        }
    }

    public void createSampleToDoList() {
        EntryList list = new EntryList("My TODO list");
        listDAO.saveList(list);

        for (String s : toDoList) {
            Entry entry = new Entry(0, s, list.id);
            EntryManager.saveEntry(entry, context);
        }
    }
}
