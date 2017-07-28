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
import java.util.HashMap;

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

    public void update_balance(String c_id,String c_customer_id,String c_customer_balance) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.C_CUSTOMER_BALANCE, c_customer_balance);
        int i = database.update(DatabaseHelper.CUSTOMER_ACCOUNTS, contentValue, DatabaseHelper.C_ID + " = " + c_id, null);

    }


    public void insert_into_customer_accounts_hidden(String c_id,String c_customer_id,String c_customer_balance) {
        ContentValues contentValue = new ContentValues();

        contentValue.put(DatabaseHelper.CUS_CUSTOMER_ID, c_id);
        contentValue.put(DatabaseHelper.CUS_CUSTOMER_ID, c_customer_id);
        contentValue.put(DatabaseHelper.CUS_CUSTOMER_BALANCE, c_customer_balance);
        long result = database.insert(DatabaseHelper.CUSTOMER_ACCOUNTS_HIDDEN, null, contentValue);
    }

    public void update_balance_hidden(String c_id,String c_customer_id,String c_customer_balance) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.CUS_CUSTOMER_BALANCE, c_customer_balance);
        int i = database.update(DatabaseHelper.CUSTOMER_ACCOUNTS_HIDDEN, contentValue, DatabaseHelper.CUS_CUSTOMER_ID + " = " + c_customer_id, null);

    }
    public void update_balance_hidden_customerID(String c_customer_id,String c_customer_balance) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.CUS_CUSTOMER_BALANCE, c_customer_balance);
        int i = database.update(DatabaseHelper.CUSTOMER_ACCOUNTS_HIDDEN, contentValue, DatabaseHelper.CUS_CUSTOMER_ID + " = " + c_customer_id, null);

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
    public void insert_into_history_travel_live(String h_route_id,String h_trans_data,String h_user_id,String h_person_traveling,String h_date_added,String h_date_modified) {
        ContentValues contentValue = new ContentValues();

        // contentValue.put(DatabaseHelper.H_ID, h_id);
        contentValue.put(DatabaseHelper.H_live_ROUTE_ID, h_route_id);
        contentValue.put(DatabaseHelper.H_live_TRANS_ID,h_trans_data);
        contentValue.put(DatabaseHelper.H_live_USER_ID, h_user_id);
        contentValue.put(DatabaseHelper.H_live_PERSON_TRAVELING, h_person_traveling);
        contentValue.put(DatabaseHelper.H_live_DATE_ADDED, h_date_added);
        contentValue.put(DatabaseHelper.H_live_DATE_MODIFIED, h_date_modified);
        long result = database.insert(DatabaseHelper.HISTORY_TRAVEL_LIVE, null, contentValue);
    }

    public void insert_into_save_qr_table(String qr_string,String qr_date,String userid) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.Q_QR_STRING, qr_string);
        contentValue.put(DatabaseHelper.Q_QR_CUSTOMER_ID, userid);
        contentValue.put(DatabaseHelper.Q_QR_SAVE_DATE, qr_date);
        long result = database.insert(DatabaseHelper.QR_CODE_TABLE, null, contentValue);
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
    public String login_first_name(String id) {

        String[] args={id};
        // Cursor cursor=database.rawQuery("SELECT customer_id FROM SIGNUP WHERE email = "+userEmail+" and password ="+userPassword,null);
        Cursor cursor=database.rawQuery("SELECT first_name FROM SIGNUP WHERE customer_id = ?", args);
        // String[] daata = new String[cursor.getCount()];
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String firstName=null;
        if(cursor.moveToFirst()){
            do
            {
                firstName=cursor.getString(0);
//                stringArrayList.add(cursor.getString(0));
//                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return firstName;
        /*if(id==null)
            return "0";
        else
            return id;*/
    }
    public String login_last_name(String id) {

        String[] args={id};
        // Cursor cursor=database.rawQuery("SELECT customer_id FROM SIGNUP WHERE email = "+userEmail+" and password ="+userPassword,null);
        Cursor cursor=database.rawQuery("SELECT last_name FROM SIGNUP WHERE customer_id = ?", args);
        // String[] daata = new String[cursor.getCount()];
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String lastName=null;
        if(cursor.moveToFirst()){
            do
            {
                lastName=cursor.getString(0);
//                stringArrayList.add(cursor.getString(0));
//                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return lastName;
        /*if(id==null)
            return "0";
        else
            return id;*/
    }
    public String login_number(String id) {

        String[] args={id};
        // Cursor cursor=database.rawQuery("SELECT customer_id FROM SIGNUP WHERE email = "+userEmail+" and password ="+userPassword,null);
        Cursor cursor=database.rawQuery("SELECT phone_number FROM SIGNUP WHERE customer_id = ?", args);
        // String[] daata = new String[cursor.getCount()];
        ArrayList<String> stringArrayList=new ArrayList<String>();
        String lastName=null;
        if(cursor.moveToFirst()){
            do
            {
                lastName=cursor.getString(0);
//                stringArrayList.add(cursor.getString(0));
//                stringArrayList.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return lastName;
        /*if(id==null)
            return "0";
        else
            return id;*/
    }

    public ArrayList<String> fetch_route_table(String route_start) {
        String[] args={route_start};
        Cursor cursor=database.rawQuery("SELECT route_destination FROM ROUTES WHERE route_start = ?", args);
        ArrayList<String> stringArrayList=new ArrayList<String>();
        stringArrayList.add(0,"<Select>");
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
        stringArrayList.add(0,"<Select>");
        if(cursor.moveToFirst()){
            do
            {
                stringArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return stringArrayList;
    }
    public String fetch_route_id_for_history(String userid,String trans_id) {
        String[] args={userid,trans_id};
        Cursor cursor=database.rawQuery("SELECT route_id FROM HISTORY_TRAVEL WHERE user_id = ? and trans_id = ?", args);
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
    public String fetch_route_elapsed_time(String route_start,String route_destination) {
        String[] args={route_start,route_destination};
        Cursor cursor=database.rawQuery("SELECT time FROM ROUTES WHERE route_start = ? and route_destination = ?", args);
        String time = null;
        if(cursor.moveToFirst()){
            do
            {
                time=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return time;
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
    public String fetch_customer_balance_hidden(String customer_id) {
        String[] args={customer_id};
        Cursor cursor=database.rawQuery("SELECT customer_balance FROM CUSTOMER_ACCOUNTS_HIDDEN WHERE customer_id = "+customer_id,null);
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
    public String fetch_fare_type(String route_id) {
        String[] args={route_id};
        Cursor cursor=database.rawQuery("SELECT Fare_type FROM FARE WHERE Fare_route = "+route_id, null);
        String fare_type = null;
        if(cursor.moveToFirst()){
            do
            {
                fare_type=cursor.getString(0);

            } while (cursor.moveToNext());
        }
        return fare_type;
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
    public String fetch_history_table_date(String user_id,String transid) {
        String[] args={user_id,transid};
        Cursor cursor=database.rawQuery("SELECT date_added FROM HISTORY_TRAVEL WHERE user_id = ? and trans_id = ?", args);
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
        Cursor cursor = database.rawQuery("SELECT DISTINCT trans_id FROM HISTORY_TRAVEL WHERE user_id = ?",args);
        ArrayList<String> stringArrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                stringArrayList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return  stringArrayList;

    }
    public ArrayList<String> fetch_history_trans_id_for_live(){
        String[] columns = new String[] { DatabaseHelper.H_live_TRANS_ID};
        Cursor cursor = database.query(DatabaseHelper.HISTORY_TRAVEL_LIVE, columns, null, null, null, null, null);
        ArrayList<String> stringArrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                stringArrayList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return  stringArrayList;
    }

    public ArrayList<String> fetch_saved_qr(String customerid){
        String[] args = {customerid};
        Cursor cursor = database.rawQuery("SELECT qr_string FROM SAVE_QR_CODE WHERE customer_id = ?",args);
       // String[] columns = new String[] { DatabaseHelper.Q_QR_STRING};
        //Cursor cursor = database.query(DatabaseHelper.QR_CODE_TABLE, columns, null, null, null, null, null);
        ArrayList<String> stringArrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                stringArrayList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return  stringArrayList;
    }
    public ArrayList<String> fetch_saved_qr_date(String customerid){
        String[] args = {customerid};
        Cursor cursor = database.rawQuery("SELECT qr_save_date FROM SAVE_QR_CODE WHERE customer_id = ?",args);
        ArrayList<String> stringArrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                stringArrayList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return  stringArrayList;
    }
    public ArrayList<String> fetch_saved_qr_id(String customerid){
        String[] args = {customerid};
        Cursor cursor = database.rawQuery("SELECT q_id FROM SAVE_QR_CODE WHERE customer_id = ?",args);
        ArrayList<String> stringArrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                stringArrayList.add(cursor.getString(0));
            }
            while (cursor.moveToNext());
        }
        return  stringArrayList;
    }

    public HashMap<Integer,ArrayList<String>> fetch_trans_id_live() {
        Cursor cursor = database.query(DatabaseHelper.HISTORY_TRAVEL_LIVE, null, null, null, null, null, null);
        int i=0;
        HashMap<Integer,ArrayList<String>> data = new HashMap<>();

        if(cursor.moveToFirst()){
            do
            {
                ArrayList<String> stringArrayList=new ArrayList<String>();
                stringArrayList.add(cursor.getString(0));
                stringArrayList.add(cursor.getString(1));
                stringArrayList.add(cursor.getString(2));
                stringArrayList.add(cursor.getString(3));
                stringArrayList.add(cursor.getString(4));
                stringArrayList.add(cursor.getString(5));
                stringArrayList.add(cursor.getString(6));

                data.put(i,stringArrayList);
                i++;


            } while (cursor.moveToNext());
        }
        return data;
    }

    public String fetch_route_id_for_live(String trans_id) {

        String[] args={trans_id};
        Cursor cursor=database.rawQuery("SELECT route_id FROM HISTORY_TRAVEL_LIVE WHERE trans_id = ?", args);
        String id = null;
        if(cursor.moveToFirst()){
            do
            {
                id=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return id;
    }
    public String fetch_person_traveling_for_live(String trans_id) {

        String[] args={trans_id};
        Cursor cursor=database.rawQuery("SELECT person_travling FROM HISTORY_TRAVEL_LIVE WHERE trans_id = ?", args);
        String person_traveling = null;
        if(cursor.moveToFirst()){
            do
            {
                person_traveling=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return person_traveling;
    }
    public String fetch_date_added_for_live(String trans_id) {

        String[] args={trans_id};
        Cursor cursor=database.rawQuery("SELECT date_added FROM HISTORY_TRAVEL_LIVE WHERE trans_id = ?", args);
        String date_added = null;
        if(cursor.moveToFirst()){
            do
            {
                date_added=cursor.getString(0);
            } while (cursor.moveToNext());
        }
        return date_added;
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

    public int update_local_password(String customer_id,String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.PASSWORD, password);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + customer_id, null);
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

    public void deleteQR(String _id) {
        database.delete(DatabaseHelper.QR_CODE_TABLE, DatabaseHelper.Q_ID + "=" + _id, null);
    }
    public void delete_route_balance_fare_table() {
        database.delete(DatabaseHelper.ROUTES,null, null);
        database.delete(DatabaseHelper.CUSTOMER_ACCOUNTS,null, null);
        database.delete(DatabaseHelper.FARE,null, null);
    }
    public void delete_both_history_tables(){
        database.delete(DatabaseHelper.HISTORY_TRAVEL,null,null);
        database.delete(DatabaseHelper.HISTORY_TRAVEL_LIVE,null,null);
    }
    public void delete_history_tables(){
        database.delete(DatabaseHelper.HISTORY_TRAVEL,null,null);
    }

    public void delete_history_data_local(String trans_id){
        //database.execSQL("delete from HISTORY_TRAVEL where trans_id="+ trans_id);
      long result1= database.delete(DatabaseHelper.HISTORY_TRAVEL,DatabaseHelper.H_ID + "=" + trans_id, null);
       // long result= database.delete(DatabaseHelper.HISTORY_TRAVEL,"HISTORY_TRAVEL.trans_id = "+trans_id,null);
      //database.delete(DatabaseHelper.HISTORY_TRAVEL, DatabaseHelper.H_TRANS_ID + "=" + trans_id, null);
    }
    public void delete_history_data_live(String trans_id){
        //database.execSQL("delete from HISTORY_TRAVEL_LIVE where trans_id="+ trans_id);
       //long result= database.delete(DatabaseHelper.HISTORY_TRAVEL_LIVE, "HISTORY_TRAVEL_LIVE.trans_id = "+trans_id, null);
        database.delete(DatabaseHelper.HISTORY_TRAVEL_LIVE, DatabaseHelper.H_LIVE_ID + "=" + trans_id, null);

    }
    public void delete_saved_qr(String qr_id){
        //database.execSQL("delete from HISTORY_TRAVEL_LIVE where trans_id="+ trans_id);
        //long result= database.delete(DatabaseHelper.HISTORY_TRAVEL_LIVE, "HISTORY_TRAVEL_LIVE.trans_id = "+trans_id, null);
        database.delete(DatabaseHelper.QR_CODE_TABLE, DatabaseHelper.Q_ID + "=" + qr_id, null);

    }

    public void delete_route_table() {
        database.delete(DatabaseHelper.ROUTES,null, null);
    }

}