package com.example.chiakiiwamoto.countme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by chiakiiwamoto on 1/02/16.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 50;
    private static final String DATABASE_NAME = "CoffeeInfo";

    private static final String COFFEE_TABLE_NAME = "coffee";
    private static final String COFFEE_COLUMN_COFFEE_ID = "coffee_id";
    private static final String COFFEE_COLUMN_COFFEE_TYPE = "type";
    private static final String COFFEE_COLUMN_CAFFEINE = "caffeine";
    private static final String COFFEE_COLUMN_IMG = "img";

    private static final String CHOSENCOFFEE_TABLE_NAME = "chosenCoffee";
    private static final String CHOSENCOFFEE_COLUMN_CHOSENCOFFEE_ID = "chosenCoffee_id";
    private static final String CHOSENCOFFEE_COLUMN_COFFEE_ID = "coffee_id";
    private static final String CHOSENCOFFEE_COLUMN_SUGAR = "sugar";
    private static final String CHOSENCOFFEE_COLUMN_DATE = "date";

    private static final String COFFEECONFIG_TABLE_NAME = "coffeeConfig";
    private static final String COFFEECONFIG_COLUMN_CONFIGCOFFEE_ID = "coffeeConfig_id";
    private static final String COFFEECONFIG_COLUMN_CHOSENCOFFEE_ID = "coffee_id";
    private static final String COFFEECONFIG_COLUMN_CHOSENCOFFEE_SUGAR = "sugar";



    public static final String tb1= "create table coffee " +
            "(coffee_id integer primary key, type text, caffeine integer, img text)";

    public static final String tb2="create table chosenCoffee " +
            "(chosenCoffee_id integer primary key, coffee_id integer, sugar integer, insertDate text)";

    public static final String tb3="create table coffeeConfig " +
            "(coffeeConfig_id integer primary key,coffee_id integer, sugar integer)";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        CoffeeModel tmpCofee = numberOfRows();
        if( tmpCofee.get_coffeeTablenumRows() == 0) {
            insertCoffee("Flat White", 80, "type_fw");
            insertCoffee("Cappuccino", 80, "type_cap");
            insertCoffee("Latte", 80, "type_l");
            insertCoffee("Mocha", 80, "type_m");
            insertCoffee("Long Black", 120, "type_lb");
            insertCoffee("Short Black", 80, "type_sb");
            insertCoffee("Piccolo", 80, "type_p");
            insertCoffee("Macchiato", 80, "type_mac");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tb1);
        //insert ref data here
        db.execSQL(tb2);
        db.execSQL(tb3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS coffee");
        db.execSQL("DROP TABLE IF EXISTS chosenCoffee");
        db.execSQL("DROP TABLE IF EXISTS coffeeConfig");

        onCreate(db);
    }

    //Insert  coffee
    public boolean insertCoffee(String type, int caffeine, String img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues coffeeValues = new ContentValues();

        coffeeValues.put("type", type);
        coffeeValues.put("caffeine", caffeine);
        coffeeValues.put("img", img);
        db.insert("coffee", null, coffeeValues);
        return true;
    }

    //Insert chosen coffee
    public boolean insertChosenCoffee(int coffee_id, int sugar, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues chosenCoffeeValues = new ContentValues();

        //chosenCoffeeValues.put("chosenCoffee_id",chosenCoffee_id);
        chosenCoffeeValues.put("coffee_id",coffee_id);
        chosenCoffeeValues.put("sugar", sugar);
        chosenCoffeeValues.put("insertDate", date);
        db.insert("chosenCoffee", null, chosenCoffeeValues);
        return true;
    }

    //Insert favourite coffee to config table
    public boolean insertFavouriteCoffee(int coffee_id, int sugar){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues favouriteCoffeeValues = new ContentValues();

        //chosenCoffeeValues.put("chosenCoffee_id",chosenCoffee_id);
        favouriteCoffeeValues.put("coffee_id", coffee_id);
        favouriteCoffeeValues.put("sugar", sugar);

        CoffeeModel tmpCoffee = numberOfRows();
        if( tmpCoffee.get_configTablenumRows()== 0) {
            db.insert("coffeeConfig", null, favouriteCoffeeValues);
        }else{
            db.update("coffeeConfig",favouriteCoffeeValues,"coffeeConfig_id = 1",null);
        }
        return true;
    }

    //Get coffeeId by coffee name
    public CoffeeModel getCoffeeIdByName(String cname){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from coffee where type='" + cname + "'", null);
        res.moveToFirst();
        CoffeeModel tmpCoffee = new CoffeeModel();
        tmpCoffee.set_coffeeId(res.getInt(res.getColumnIndex(COFFEE_COLUMN_COFFEE_ID)));

        //coffee.add(tmpCoffee);

        return tmpCoffee;
    }

    //Sum of total sugar on chosen table
    public CoffeeModel sum(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select sum(cc.sugar),sum(c.caffeine) from chosenCoffee cc, coffee c where cc.coffee_id=c.coffee_id",null);
        res.moveToFirst();
        CoffeeModel tmpCoffee = new CoffeeModel();
        tmpCoffee.set_sugarSum(res.getInt(0));
        tmpCoffee.set_caffeineSum(res.getInt(1));

        return tmpCoffee;
    }

    //Sum of total sugar on chosen table
    public CoffeeModel dailySum(){
        SQLiteDatabase db = this.getReadableDatabase();

        //get current date
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        Cursor res = db.rawQuery("select sum(cc.sugar),sum(c.caffeine) from chosenCoffee cc, coffee c where cc.coffee_id=c.coffee_id and insertDate='"+formattedDate+"'",null);
        res.moveToFirst();
        CoffeeModel tmpCoffee = new CoffeeModel();
        tmpCoffee.set_sugarDailySum(res.getInt(0));
        tmpCoffee.set_caffeineDailySum(res.getInt(1));

        return tmpCoffee;
    }


    //Get coffee from config
    public CoffeeModel getFavouriteCoffee(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from coffeeConfig", null);
        res.moveToFirst();
        CoffeeModel tmpCoffee = new CoffeeModel();
        tmpCoffee.set_coffeeId(res.getInt(res.getColumnIndex(COFFEECONFIG_COLUMN_CHOSENCOFFEE_ID)));
        tmpCoffee.set_sugars(res.getInt(res.getColumnIndex(COFFEECONFIG_COLUMN_CHOSENCOFFEE_SUGAR)));
        //using id we just got, look up coffee in coffee table
        //res = db.rawQ -get coffee by id
        //res.moveToFirst();
        //add values to tmpCoffee from new result set

        res = db.rawQuery("select * from coffee where coffee_id = "+tmpCoffee.get_coffeeId(), null);
        res.moveToFirst();
        tmpCoffee.set_coffeeName(res.getString(res.getColumnIndex(COFFEE_COLUMN_COFFEE_TYPE)));
        tmpCoffee.set_coffeeImage(res.getString(res.getColumnIndex(COFFEE_COLUMN_IMG)));


        return tmpCoffee;
    }

    public CoffeeModel numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();

        CoffeeModel tmpCoffee = new CoffeeModel();
        //int numRows = (int) DatabaseUtils.queryNumEntries(db, COFFEE_TABLE_NAME);
        tmpCoffee.set_coffeeTablenumRows((int) DatabaseUtils.queryNumEntries(db, COFFEE_TABLE_NAME));
        tmpCoffee.set_configTablenumRows((int) DatabaseUtils.queryNumEntries(db, COFFEECONFIG_TABLE_NAME));
        tmpCoffee.set_chosenTablenumRows((int) DatabaseUtils.queryNumEntries(db, CHOSENCOFFEE_TABLE_NAME));

        //get current date
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

       // Cursor res = db.rawQuery("select count() from chosenCoffee where insertDate=date('now')", null);
        Cursor res = db.rawQuery("select count() from chosenCoffee where insertDate='"+formattedDate+"'", null);
        res.moveToFirst();
        tmpCoffee.set_chosenDailyTablenumRows(res.getInt(0));

        return tmpCoffee;
    }

    //update chosen coffee
    public boolean updateChosenCoffee(Integer id, String coffeeType, int caffeine, int sugar){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("coffeeType", coffeeType);
        values.put("caffeine", caffeine);
        values.put("sugar", sugar);
        db.update("chosenCoffee", values, "id = ?", new String[]{Integer.toString(id)});
        return true;
    }

    //delete all rows on chosenCoffee and coffeeConfig table to reset data
    public void deleteRows(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("delete from chosenCoffee", null);
        res.moveToFirst();
        res = db.rawQuery("delete from coffeeConfig", null);
        res.moveToFirst();

    }

    //delete newest input row on chosenCoffee
    public void deleteOneRow(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("delete FROM chosenCoffee WHERE chosenCoffee_id = (select MAX(chosenCoffee_id) from chosenCoffee)", null);
        res.moveToFirst();

    }

    //get all coffee name
    public ArrayList<CoffeeModel> getAllCoffee()
    {
        ArrayList<CoffeeModel> coffees = new ArrayList<CoffeeModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from coffee", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            CoffeeModel tmpCoffee = new CoffeeModel();

            tmpCoffee.set_coffeeName(res.getString(res.getColumnIndex(COFFEE_COLUMN_COFFEE_TYPE)));
            tmpCoffee.set_coffeeImage(res.getString(res.getColumnIndex(COFFEE_COLUMN_IMG)));

            coffees.add(tmpCoffee);
            res.moveToNext();
        }

        return coffees;
    }

}
