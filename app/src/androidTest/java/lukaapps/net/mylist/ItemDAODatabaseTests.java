package lukaapps.net.mylist;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

import lukaapps.net.mylist.db.DAOHelper;
import lukaapps.net.mylist.db.IItemDAO;
import lukaapps.net.mylist.db.ItemDAO;
import lukaapps.net.mylist.model.Item;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ItemDAODatabaseTests {

    private IItemDAO db;
    private String testString = "test12345";

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(DAOHelper.DATABASE_NAME);
        db = new ItemDAO(getTargetContext());
    }

    @Test
    public void getItemReturnsItem() throws Exception {
        Item item = db.saveItem(new Item(testString));
        Item newItem = db.getItem(item.id);

        assertEquals(newItem.title, testString);
    }

    @Test
    public void getItem_NegativeID_ReturnsNull() throws Exception {
        db.saveItem(new Item(testString));
        Item newItem = db.getItem(-9);

        assertEquals(newItem, null);
    }

    @Test
    public void getItems_EmptyDB_ReturnsEmptyList() throws Exception {
        List<Item> items = db.getItems();
        assertThat(items.size(), is(0));
    }

    @Test
    public void getItemsReturnsItems() throws Exception {
        Item item = new Item(testString);
        db.saveItem(item);
        db.saveItem(item);
        db.saveItem(new Item(testString));
        db.saveItem(new Item(testString));

        List<Item> items = db.getItems();
        assertThat(items.size(), is(3));
    }

    @Test
    public void deleteItemDeletesEntry() throws Exception {
        Item item = db.saveItem(new Item(testString));

        assertTrue(db.getItems(0).size() > 0);

        db.deleteItem(item.id);

        assertTrue(db.getItems().size() == 0);
    }

    @Test
    public void deleteNotExistingItemReturnsFalse() throws Exception {
        Item item = db.saveItem(new Item(testString));

        boolean result = db.deleteItem(item.id + 1);

        assertFalse(result);
    }

    @Test
    public void getTopNItemsReturnsNItems() throws Exception {
        db.saveItem(new Item(testString));
        db.saveItem(new Item(testString));
        db.saveItem(new Item(testString));
        db.saveItem(new Item(testString));

        assertTrue(db.getItems().size() == 4);
        assertTrue(db.getItems(2).size() == 2);
    }

    @Test
    public void saveItem() throws Exception {
        Item item = db.saveItem(new Item(0, testString, new Date(100)));

        long id = item.id;
        Item testItem = db.getItem(id);

        assertEquals(testItem.title, testString);
        assertEquals(testItem.id, id);
        assertEquals(testItem.lastUsed, new Date(100));
    }

    @Test
    public void updateItem() throws Exception {
        Item item = db.saveItem(new Item(0, testString, new Date(100)));

        long id = item.id;
        item.title = "NewTitle";
        item.lastUsed = new Date(99);

        db.saveItem(item);

        Item testItem = db.getItem(id);

        assertEquals(testItem.title, "NewTitle");
        assertEquals(testItem.id, id);
        assertEquals(testItem.lastUsed, new Date(99));
    }
}