package lukaapps.net.mylist;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import lukaapps.net.mylist.db.DAOHelper;
import lukaapps.net.mylist.db.EntryListDAO;
import lukaapps.net.mylist.db.IEntryListDAO;
import lukaapps.net.mylist.model.EntryList;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class EntryListDAODatabaseTests {

    private IEntryListDAO db;
    private String testString = "test12345";

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(DAOHelper.DATABASE_NAME);
        db = new EntryListDAO(getTargetContext());
    }

    @Test
    public void getEntryListReturnsEntryList() throws Exception {
        EntryList entryList = db.saveList(new EntryList(testString));
        EntryList newEntryList = db.getList(entryList.id);

        assertEquals(newEntryList.title, testString);
    }

    @Test
    public void getEntryList_NegativeID_ReturnsNull() throws Exception {
        db.saveList(new EntryList(testString));
        EntryList testList = db.getList(-9);

        assertEquals(testList, null);
    }

    @Test
    public void entryListUpdated() throws Exception {
        EntryList list = db.saveList(new EntryList(testString));
        long id = list.id;
        list.title = "NewTitle";
        db.saveList(list);
        EntryList testEntry = db.getList(id);

        assertEquals(testEntry.title, "NewTitle");
    }

    @Test
    public void getEntryLists_EmptyDB_ReturnsEmptyList() throws Exception {
        List<EntryList> lists = db.getLists();
        assertThat(lists.size(), is(0));
    }

    @Test
    public void getEntryListsReturnsEntryLists() throws Exception {
        EntryList list = new EntryList(testString);
        db.saveList(list);
        db.saveList(list);
        db.saveList(new EntryList(testString));
        db.saveList(new EntryList(testString));

        List<EntryList> lists = db.getLists();
        assertThat(lists.size(), is(3));
    }

    @Test
    public void deleteEntryListDeletesEntryList() throws Exception {
        EntryList list = db.saveList(new EntryList(testString));

        assertTrue(db.getLists().size() > 0);

        db.deleteList(list.id);

        assertTrue(db.getLists().size() == 0);
    }

    @Test
    public void deleteNotExistingEntryListReturnsFalse() throws Exception {
        EntryList entry = db.saveList(new EntryList(testString));

        boolean result = db.deleteList(entry.id + 1);

        assertFalse(result);
    }

    @Test
    public void deleteEntries() throws Exception {
        List<EntryList> list = new ArrayList<EntryList>();
        list.add(db.saveList(new EntryList(testString)));
        list.add(db.saveList(new EntryList(testString)));

        assertTrue(db.getLists().size() == 2);

        db.deleteLists(list);

        assertTrue(db.getLists().size() == 0);
    }
}