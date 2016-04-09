package lukaapps.net.mylist.db;

import android.provider.BaseColumns;

public class ListContract {

    public ListContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}
