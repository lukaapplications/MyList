package lukaapps.net.mylist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import lukaapps.net.mylist.db.DAOHelper.EntryStructure;
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
        values.put(EntryStructure.COLUMN_NAME_TITLE, entry.title);

        try {
            // Insert the new row, returning the primary key value of the new row
            entry.id = db.insert(
                    EntryStructure.TABLE_NAME,
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
        values.put(EntryStructure.COLUMN_NAME_TITLE, entry.title);

        try {
            // Which row to update, based on the ID
            String selection = EntryStructure._ID + " = ?";
            String[] selectionArgs = {String.valueOf(entry.id)};

            int count = db.update(
                    EntryStructure.TABLE_NAME,
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
        String title = c.getString(c.getColumnIndexOrThrow(EntryStructure.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndexOrThrow(EntryStructure._ID));

        Entry entry = new Entry(id, title);

        return entry;
    }

    @Override
    public List<Entry> getEntries() {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                EntryStructure._ID,
                EntryStructure.COLUMN_NAME_TITLE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                EntryStructure.COLUMN_NAME_TITLE + " DESC";

        Cursor c;
        try {
            c = db.query(
                    EntryStructure.TABLE_NAME,  // The table to query
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
                EntryStructure._ID,
                EntryStructure.COLUMN_NAME_TITLE
        };
        String selection = EntryStructure._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                EntryStructure.COLUMN_NAME_TITLE + " DESC";

        Cursor c;
        try {
            c = db.query(
                    EntryStructure.TABLE_NAME,                // The table to query
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
    public boolean deleteEntry(long id) {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String selection = EntryStructure._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        try {
            int result = db.delete(EntryStructure.TABLE_NAME, selection, selectionArgs);
            if (result > 0) {
                return true;
            }
        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }

        return false;
    }

    @Override
    public boolean deleteEntries(List<Entry> entries) {
        for (Entry entry : entries) {
            deleteEntry(entry.id);
        }
        return true;
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
