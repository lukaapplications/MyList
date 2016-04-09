package lukaapps.net.mylist;

import org.junit.Test;

import lukaapps.net.mylist.model.Entry;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void createEntry_setsAttributes() {
        Entry entry = new Entry(1, "title");

        assertEquals(entry.toString(), "Entry (1) [title]");
    }

    @Test
    public void createEntry_setsNullAttributes() {
        Entry entry = new Entry(0, null);

        assertEquals(entry.toString(), "Entry (0) [null]");
    }
}