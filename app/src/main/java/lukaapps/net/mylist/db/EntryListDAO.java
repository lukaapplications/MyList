package lukaapps.net.mylist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import lukaapps.net.mylist.db.DAOHelper.ListEntryStructure;
import lukaapps.net.mylist.model.EntryList;


public final class EntryListDAO implements IEntryListDAO {

    private static DAOHelper mDBHelper;
    private Context mContext;

    public EntryListDAO(Context context) {
        mContext = context;
    }

    private static EntryList insert(EntryList entryList, Context context) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDBHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ListEntryStructure.COLUMN_NAME_TITLE, entryList.title);

        try {
            // Insert the new row, returning the primary key value of the new row
            entryList.id = db.insert(
                    ListEntryStructure.TABLE_NAME,
                    null,
                    values);

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }

        return entryList;
    }

    private static EntryList update(EntryList entryList, Context context) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDBHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ListEntryStructure.COLUMN_NAME_TITLE, entryList.title);

        try {
            // Which row to update, based on the ID
            String selection = ListEntryStructure._ID + " = ?";
            String[] selectionArgs = {String.valueOf(entryList.id)};

            int count = db.update(
                    ListEntryStructure.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }

        return entryList;
    }

    public static DAOHelper getDBHelper(Context context) {
        return new DAOHelper(context);
    }

    private EntryList mapCurrentCursorPositionToEntryList(Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow(ListEntryStructure.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndexOrThrow(ListEntryStructure._ID));

        EntryList entryList = new EntryList(id, title);

        return entryList;
    }

    @Override
    public List<EntryList> getLists() {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                ListEntryStructure._ID,
                ListEntryStructure.COLUMN_NAME_TITLE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ListEntryStructure.COLUMN_NAME_TITLE + " DESC";

        Cursor c;
        try {
            c = db.query(
                    ListEntryStructure.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                     // The columns for the WHERE clause
                    null,                                     // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            List<EntryList> entryLists = new ArrayList<EntryList>();

            while (c.moveToNext()) {
                entryLists.add(mapCurrentCursorPositionToEntryList(c));
            }

            return entryLists;

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }
    }

    @Override
    public boolean deleteList(long id) {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String selection = ListEntryStructure._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        try {
            int result = db.delete(ListEntryStructure.TABLE_NAME, selection, selectionArgs);
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
    public boolean deleteLists(List<EntryList> lists) {
        for (EntryList list : lists) {
            deleteList(list.id);
        }
        return true;
    }

    @Override
    public EntryList getList(long id) {

        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                ListEntryStructure._ID,
                ListEntryStructure.COLUMN_NAME_TITLE
        };
        String selection = ListEntryStructure._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor c;
        try {
            c = db.query(
                    ListEntryStructure.TABLE_NAME,            // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                      // The sort order
            );

            if (c.moveToNext()) {
                return mapCurrentCursorPositionToEntryList(c);
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
    public EntryList saveList(EntryList entryList) {
        if (entryList.id == 0) {
            return insert(entryList, mContext);
        } else {
            return update(entryList, mContext);
        }
    }
}
