package lukaapps.net.mylist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.List;

import lukaapps.net.mylist.db.ListContract.ListEntry;
import lukaapps.net.mylist.model.Entry;


public final class EntryDAO implements IEntryDAO {

    private static DAOHelper mDBHelper;

    private static Entry insert(Entry entry, Context context) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDBHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ListEntry.COLUMN_NAME_TITLE, entry.title);

        try {
            // Insert the new row, returning the primary key value of the new row
            entry.id = db.insert(
                    ListEntry.TABLE_NAME,
                    null,
                    values);

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }

        return entry;
    }

    private static Entry update(Entry entry, Context context) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDBHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ListEntry.COLUMN_NAME_TITLE, entry.title);

        try {
            // Which row to update, based on the ID
            String selection = ListEntry._ID + " LIKE ?";
            String[] selectionArgs = {String.valueOf(entry.id)};

            int count = db.update(
                    ListEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }

        return entry;
    }

    public static DAOHelper getDBHelper(Context context) {
        return new DAOHelper(context);
    }

    @Override
    public List<Entry> getEntries(Context context) {
        return null;
    }

    @Override
    public Entry getEntry(long id, Context context) {
        return null;
    }

    @Override
    public Entry saveEntry(Entry entry, Context context) {
        if (entry.id == 0) {
            return insert(entry, context);
        } else {
            return update(entry, context);
        }
    }
}
