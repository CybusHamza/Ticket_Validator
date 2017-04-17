package com.cybussolutions.ticketvalidator.Activities;

/**
 * Created by Rizwan Butt on 13-Apr-17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String id,String first_name, String last_name,String email,String phone_number) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._ID,id);
        contentValue.put(DatabaseHelper.FIRST_NAME, first_name);
        contentValue.put(DatabaseHelper.LAST_NAME, last_name);
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.PHONE_NUMBER, phone_number);
        long result = database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }

    public String[] fetch() {

        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.FIRST_NAME, DatabaseHelper.LAST_NAME,DatabaseHelper.EMAIL,DatabaseHelper.PHONE_NUMBER };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        String[] daata = new String[cursor.getCount()];

        if (cursor != null) {
            cursor.moveToFirst();

            daata[0]=cursor.getString(0);
        }
        return daata;
    }

    public int update(long _id, String first_name, String last_name,String email,String phone_number) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.FIRST_NAME, first_name);
        contentValues.put(DatabaseHelper.LAST_NAME, last_name);
        contentValues.put(DatabaseHelper.EMAIL, email);
        contentValues.put(DatabaseHelper.PHONE_NUMBER, phone_number);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}