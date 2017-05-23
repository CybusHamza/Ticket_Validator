package com.cybussolutions.ticketvalidator.Activities;

/**
 * Created by Rizwan Butt on 13-Apr-17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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

    public void insert(String id,String first_name, String last_name,String password,String phone_number,String gender,String email,String cardType) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper._ID,id);
        contentValue.put(DatabaseHelper.FIRST_NAME, first_name);
        contentValue.put(DatabaseHelper.LAST_NAME, last_name);
        contentValue.put(DatabaseHelper.PASSWORD,password);
        contentValue.put(DatabaseHelper.PHONE_NUMBER, phone_number);
        contentValue.put(DatabaseHelper.GENDER,gender);
        contentValue.put(DatabaseHelper.EMAIL, email);
        contentValue.put(DatabaseHelper.CARDTYPE,cardType);
        long result = database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);

    }
    public void insert_into_routes(String id,String route_code, String route_name,String route_start,String route_destination,String route_added_date,String time,String route_added_by,String route_updated_date,String route_updated_by) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.ID, id);
        contentValue.put(DatabaseHelper.ROUTE_CODE, route_code);
        contentValue.put(DatabaseHelper.ROUTE_NAME, route_name);
        contentValue.put(DatabaseHelper.ROUTE_START, route_start);
        contentValue.put(DatabaseHelper.ROUTE_DESTINATION, route_destination);
        contentValue.put(DatabaseHelper.ROUTE_ADDED_DATE, route_added_date);
        contentValue.put(DatabaseHelper.TIME, time);
        contentValue.put(DatabaseHelper.ROUTE_ADDED_BY, route_added_by);
        contentValue.put(DatabaseHelper.ROUTE_UPDATED_DATE, route_updated_date);
        contentValue.put(DatabaseHelper.ROUTE_UPDATED_BY, route_updated_by);
        long result = database.insert(DatabaseHelper.ROUTES, null, contentValue);

    }

    public void insert_into_fare(String fare_id,String fare_route,String fare_price, String fare_type,String added_by,String updated_by,String date_added,String date_updated) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.FARE_ID, fare_id);
        contentValue.put(DatabaseHelper.FARE_ROUTE, fare_route);
        contentValue.put(DatabaseHelper.FARE_PRICE, fare_price);
        contentValue.put(DatabaseHelper.FARE_TYPE, fare_type);
        contentValue.put(DatabaseHelper.ADDED_BY, added_by);
        contentValue.put(DatabaseHelper.UPDATED_BY, updated_by);
        contentValue.put(DatabaseHelper.DATE_ADDED, date_added);
        contentValue.put(DatabaseHelper.DATE_UPDATED, date_updated);
        long result = database.insert(DatabaseHelper.FARE, null, contentValue);
    }

    public void insert_into_customer_accounts(String c_id,String c_customer_id,String c_customer_balance) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.C_ID, c_id);
        contentValue.put(DatabaseHelper.C_CUSTOMER_ID, c_customer_id);
        contentValue.put(DatabaseHelper.C_CUSTOMER_BALANCE, c_customer_balance);
        long result = database.insert(DatabaseHelper.CUSTOMER_ACCOUNTS, null, contentValue);
    }
    public void insert_into_history_travel(String h_route_id,String h_trans_data,String h_user_id,String h_person_traveling,String h_date_added,String h_date_modified) {
        ContentValues contentValue = new ContentValues();

       // contentValue.put(DatabaseHelper.H_ID, h_id);
        contentValue.put(DatabaseHelper.H_ROUTE_ID, h_route_id);
        contentValue.put(DatabaseHelper.H_TRANS_ID,h_trans_data);
        contentValue.put(DatabaseHelper.H_USER_ID, h_user_id);
        contentValue.put(DatabaseHelper.H_PERSON_TRAVELING, h_person_traveling);
        contentValue.put(DatabaseHelper.H_DATE_ADDED, h_date_added);
        contentValue.put(DatabaseHelper.H_DATE_MODIFIED, h_date_modified);
        long result = database.insert(DatabaseHelper.HISTORY_TRAVEL, null, contentValue);
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
    public String login(String userEmail,String userPassword) {

        String[] args={userEmail,userPassword};
       // Cursor cursor=database.rawQuery("SELECT customer_id FROM SIGNUP WHERE email = "+userEmail+" and password ="+userPassword,null);
        Cursor cursor=database.rawQuery("SELECT customer_id FROM SIGNUP WHERE email = ? and password = ?", args);
       // String[] daata = new String[cursor.getCount()];
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String id=null;
        if(cursor.moveToFirst()){
            do
            {
                id=cursor.getString(0);
//                stringArrayList.add(cursor.getString(0));
//                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if(id==null)
            return "0";
        else
        return id;
    }
    public String login_first_name(String userEmail,String userPassword) {

        String[] args={userEmail,userPassword};
        // Cursor cursor=database.rawQuery("SELECT customer_id FROM SIGNUP WHERE email = "+userEmail+" and password ="+userPassword,null);
        Cursor cursor=database.rawQuery("SELECT customer_id FROM SIGNUP WHERE email = ? and password = ?", args);
        // String[] daata = new String[cursor.getCount()];
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String id=null;
        if(cursor.moveToFirst()){
            do
            {
                id=cursor.getString(0);
//                stringArrayList.add(cursor.getString(0));
//                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        if(id==null)
            return "0";
        else
            return id;
    }

    public ArrayList<String> fetch_route_table(String route_start) {
        String[] args={route_start};
        Cursor cursor=database.rawQuery("SELECT route_destination FROM ROUTES WHERE route_start = ?", args);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public String h_fetch_route_table_start(String id) {
        String[] args={id};
        Cursor cursor=database.rawQuery("SELECT route_start FROM ROUTES WHERE id = ?", args);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String route_start=null;
        if(cursor.moveToFirst()){
            do
            {
                route_start=cursor.getString(0);
             //   stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return route_start;
        //return stringArrayList;
    }
    public String h_fetch_route_table_dest(String id) {
        String[] args={id};
        Cursor cursor=database.rawQuery("SELECT route_destination FROM ROUTES WHERE id = ?", args);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String route_dest=null;
        if(cursor.moveToFirst()){
            do
            {
                route_dest=cursor.getString(0);
                //stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return route_dest;
        //return stringArrayList;
    }
    public String h_fetch_route_fare_price(String id) {
        String[] args={id};
        Cursor cursor=database.rawQuery("SELECT Fare_price FROM FARE WHERE Fare_route = ?", args);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String fare_price=null;
        if(cursor.moveToFirst()){
            do
            {
                fare_price=cursor.getString(0);
                //stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return fare_price;
        //return stringArrayList;
    }

    public ArrayList<String> fetch_route_start() {
        String[] columns = new String[] { DatabaseHelper.ROUTE_START};
        String select = "SELECT DISTINCT " + DatabaseHelper.ROUTE_START + " FROM " + DatabaseHelper.ROUTES;
       // Cursor cursor = database.query(DatabaseHelper.ROUTES, columns, null, null, null, null, null);
        Cursor cursor=database.rawQuery(select,null);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public String fetch_route_id_for_history(String userid) {
        String[] args={userid};
        Cursor cursor=database.rawQuery("SELECT route_id FROM HISTORY_TRAVEL WHERE user_id = ? ", args);
        String id = null;
        if(cursor.moveToFirst()){
            do
            {
                id=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return id;
    }
    public String fetch_route_id(String route_start,String route_destination) {
        String[] args={route_start,route_destination};
        Cursor cursor=database.rawQuery("SELECT ID FROM ROUTES WHERE route_start = ? and route_destination = ?", args);
        String id = null;
        if(cursor.moveToFirst()){
            do
            {
                id=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return id;
    }
    public String fetch_customer_balance(String customer_id) {
        String[] args={customer_id};
        Cursor cursor=database.rawQuery("SELECT customer_balance FROM CUSTOMER_ACCOUNTS WHERE customer_id = "+customer_id,null);
        String balance = null;
        if(cursor.moveToFirst()){
            do
            {
                balance=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return balance;
    }

    public String fetch_fare(String fare_id) {
        String[] args={fare_id};
        Cursor cursor=database.rawQuery("SELECT Fare_price FROM FARE WHERE Fare_route = "+fare_id, null);
        String price = null;
        if(cursor.moveToFirst()){
            do
            {
                price=cursor.getString(0);

            } while (cursor.moveToNext());
        }
        return price;
    }

    public String fetch_history_table(String user_id,String trans_id) {
        String[] args={user_id,trans_id};
        Cursor cursor=database.rawQuery("SELECT person_travling FROM HISTORY_TRAVEL WHERE user_id = ? and trans_id = ?", args);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String personTraveling=null;
        if(cursor.moveToFirst()){
            do
            {
                personTraveling=cursor.getString(0);
               // stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return personTraveling;
        //return stringArrayList;
    }
    public String fetch_history_table_date(String user_id) {
        String[] args={user_id};
        Cursor cursor=database.rawQuery("SELECT date_added FROM HISTORY_TRAVEL WHERE user_id = ?", args);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String date=null;
        if(cursor.moveToFirst()){
            do
            {
                date=cursor.getString(0);
                //stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return date;
        //return stringArrayList;
    }

    public ArrayList<String> fetch_history_trans_id(String user_id){
        String[] args = {user_id};
        Cursor cursor = database.rawQuery("SELECT trans_id FROM HISTORY_TRAVEL WHERE user_id = ?",args);
        ArrayList<String> stringArrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                stringArrayList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return  stringArrayList;


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
    public int update_customer_balance(String customer_id,String customer_remaining_balance) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.C_CUSTOMER_BALANCE, customer_remaining_balance);
        int i = database.update(DatabaseHelper.CUSTOMER_ACCOUNTS, contentValues, DatabaseHelper.C_CUSTOMER_ID + " = " + customer_id, null);
        return i;
    }
    public int update_history_travel(String route_id, String user_id,String person_traveling,String trans_id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.H_ROUTE_ID, route_id);
        contentValues.put(DatabaseHelper.H_PERSON_TRAVELING, person_traveling);
        contentValues.put(DatabaseHelper.H_TRANS_ID,trans_id);
        int i = database.update(DatabaseHelper.HISTORY_TRAVEL, contentValues, DatabaseHelper.H_USER_ID + " = " + user_id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}