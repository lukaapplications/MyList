package lukaapps.net.mylist;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import lukaapps.net.mylist.db.DAOHelper;
import lukaapps.net.mylist.db.EntryDAO;
import lukaapps.net.mylist.db.IEntryDAO;
import lukaapps.net.mylist.model.Entry;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class EntryDAODatabaseTests {

    private IEntryDAO db;
    private String testString = "test12345";

    @Before
    public void setUp() throws Exception {
        getTargetContext().deleteDatabase(DAOHelper.DATABASE_NAME);
        db = new EntryDAO(getTargetContext());
    }

    @Test
    public void getEntryReturnsEntry() throws Exception {
        Entry entry = db.saveEntry(new Entry(testString));
        Entry newEntry = db.getEntry(entry.id);

        assertEquals(newEntry.title, testString);
    }

    @Test
    public void getEntry_NegativeID_ReturnsNull() throws Exception {
        Entry entry = db.saveEntry(new Entry(testString));
        Entry testEntry = db.getEntry(-9);

        assertEquals(testEntry, null);
    }

    @Test
    public void entryUpdated() throws Exception {
        Entry entry = db.saveEntry(new Entry(testString));
        long id = entry.id;
        entry.title = "NewTitle";
        db.saveEntry(entry);
        Entry testEntry = db.getEntry(id);

        assertEquals(testEntry.title, "NewTitle");
    }

    @Test
    public void getEntries_EmptyDB_ReturnsEmptyList() throws Exception {
        List<Entry> entries = db.getEntries();
        assertThat(entries.size(), is(0));
    }

    @Test
    public void getEntriesReturnsEntries() throws Exception {
        Entry entry = new Entry(testString);
        db.saveEntry(entry);
        db.saveEntry(entry);
        db.saveEntry(new Entry(testString));
        db.saveEntry(new Entry(testString));

        List<Entry> entries = db.getEntries();
        assertThat(entries.size(), is(3));
    }
}