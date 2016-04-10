package lukaapps.net.mylist.db;

import java.util.List;

import lukaapps.net.mylist.model.Item;

public interface IItemDAO {
    List<Item> getItems(long topN);

    List<Item> getItems();

    Item getItem(long id);

    Item getItem(String title);

    Item saveItem(Item item);

    boolean deleteItem(long id);
}


