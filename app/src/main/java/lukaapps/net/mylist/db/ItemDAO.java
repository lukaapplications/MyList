package lukaapps.net.mylist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lukaapps.net.mylist.db.DAOHelper.ItemStructure;
import lukaapps.net.mylist.model.Item;


public final class ItemDAO implements IItemDAO {

    private static DAOHelper mDBHelper;
    private Context mContext;

    public ItemDAO(Context context) {
        mContext = context;
    }

    private static Item insert(Item item, Context context) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDBHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemStructure.COLUMN_NAME_TITLE, item.title);
        values.put(ItemStructure.COLUMN_NAME_LASTUSED, item.lastUsed.getTime());

        try {
            // Insert the new row, returning the primary key value of the new row
            item.id = db.insert(
                    ItemStructure.TABLE_NAME,
                    null,
                    values);
        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }

        return item;
    }

    private static Item update(Item item, Context context) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getDBHelper(context).getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ItemStructure.COLUMN_NAME_TITLE, item.title);
        values.put(ItemStructure.COLUMN_NAME_LASTUSED, item.lastUsed.getTime());

        try {
            // Which row to update, based on the ID
            String selection = ItemStructure._ID + " = ?";
            String[] selectionArgs = {String.valueOf(item.id)};

            int count = db.update(
                    ItemStructure.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }

        return item;
    }

    public static DAOHelper getDBHelper(Context context) {
        return new DAOHelper(context);
    }

    private Item mapCurrentCursorPositionToItem(Cursor c) {
        String title = c.getString(c.getColumnIndexOrThrow(ItemStructure.COLUMN_NAME_TITLE));
        long id = c.getLong(c.getColumnIndexOrThrow(ItemStructure._ID));
        Date lastused = new Date(c.getLong(c.getColumnIndexOrThrow(ItemStructure.COLUMN_NAME_LASTUSED)));

        Item item = new Item(id, title, lastused);

        return item;
    }

    @Override
    public List<Item> getItems() {
        return getItems(0);
    }

    @Override
    public List<Item> getItems(long topN) {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                ItemStructure._ID,
                ItemStructure.COLUMN_NAME_TITLE,
                ItemStructure.COLUMN_NAME_LASTUSED
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                ItemStructure.COLUMN_NAME_LASTUSED + " DESC";

        Cursor c;
        try {
            c = db.query(
                    ItemStructure.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                     // The columns for the WHERE clause
                    null,                                     // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder,                                // The sort order
                    topN > 0 ? topN + "" : null               // Top N
            );

            List<Item> items = new ArrayList<Item>();

            while (c.moveToNext()) {
                items.add(mapCurrentCursorPositionToItem(c));
            }

            return items;

        } catch (SQLiteException e) {
            throw e;
        } finally {
            db.close();
        }
    }

    @Override
    public Item getItem(long id) {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                ItemStructure._ID,
                ItemStructure.COLUMN_NAME_TITLE,
                ItemStructure.COLUMN_NAME_LASTUSED
        };
        String selection = ItemStructure._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor c;
        try {
            c = db.query(
                    ItemStructure.TABLE_NAME,                // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (c.moveToNext()) {
                return mapCurrentCursorPositionToItem(c);
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
    public Item getItem(String title) {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String[] projection = {
                ItemStructure._ID,
                ItemStructure.COLUMN_NAME_TITLE,
                ItemStructure.COLUMN_NAME_LASTUSED
        };
        String selection = ItemStructure.COLUMN_NAME_TITLE + " like ?";
        String[] selectionArgs = {String.valueOf(title)};

        Cursor c;
        try {
            c = db.query(
                    ItemStructure.TABLE_NAME,                // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            if (c.moveToNext()) {
                return mapCurrentCursorPositionToItem(c);
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
    public boolean deleteItem(long id) {
        SQLiteDatabase db = getDBHelper(mContext).getReadableDatabase();

        String selection = ItemStructure._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        try {
            int result = db.delete(ItemStructure.TABLE_NAME, selection, selectionArgs);
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
    public Item saveItem(Item item) {
        if (item.id == 0) {
            return insert(item, mContext);
        } else {
            return update(item, mContext);
        }
    }
}
