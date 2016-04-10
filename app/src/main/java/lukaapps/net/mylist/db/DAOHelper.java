package lukaapps.net.mylist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DAOHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "lukaapps.net.mylist.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EntryStructure.TABLE_NAME + " (" +
                    EntryStructure._ID + " INTEGER PRIMARY KEY," +
                    EntryStructure.COLUMN_NAME_TITLE + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_ENTRYLISTS =
            "CREATE TABLE " + ListEntryStructure.TABLE_NAME + " (" +
                    ListEntryStructure._ID + " INTEGER PRIMARY KEY," +
                    ListEntryStructure.COLUMN_NAME_TITLE + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EntryStructure.TABLE_NAME;
    private static final String SQL_DELETE_ENTRYLISTS =
            "DROP TABLE IF EXISTS " + ListEntryStructure.TABLE_NAME;

    public DAOHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRYLISTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_ENTRYLISTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static abstract class EntryStructure implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
    }

    public static abstract class ListEntryStructure implements BaseColumns {
        public static final String TABLE_NAME = "list";
        public static final String COLUMN_NAME_TITLE = "title";
    }
}