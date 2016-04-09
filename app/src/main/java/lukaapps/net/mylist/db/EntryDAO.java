package lukaapps.net.mylist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import lukaapps.net.mylist.db.ListContract.ListEntry;
import lukaapps.net.mylist.model.Entry;


public final class EntryDAO implements IEntryDAO {

    private static DAOHelper mDBHelper;
    private Context mContext;

    public EntryDAO(Context context) {
        mContext = context;
    }

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
            String selection = ListEntry._ID + " = ?";
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

    private Entry mapCurrentCursorPositionToEntry(Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow(ListEntry.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndexOrThrow(ListEntry._ID));

        Entry entry = new Entry(id, title);

        return entry;
    }

    @Override
    public List<Entry> getEntries() {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                ListEntry._ID,
                ListEntry.COLUMN_NAME_TITLE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ListEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor c;
        try {
            c = db.query(
                    ListEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                     // The columns for the WHERE clause
                    null,                                     // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            List<Entry> entries = new ArrayList<Entry>();

            while (c.moveToNext()) {
                entries.add(mapCurrentCursorPositionToEntry(c));
            }

            return entries;

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }
    }

    @Override
    public Entry getEntry(long id) {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                ListEntry._ID,
                ListEntry.COLUMN_NAME_TITLE
        };
        String selection = ListEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ListEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor c;
        try {
            c = db.query(
                    ListEntry.TABLE_NAME,                     // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            if (c.moveToNext()) {
                return mapCurrentCursorPositionToEntry(c);
            } else {
                return null;
            }

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }
    }

    @Override
    public Entry saveEntry(Entry entry) {
        if (entry.id == 0) {
            return insert(entry, mContext);
        } else {
            return update(entry, mContext);
        }
    }
}
