package FitnessTracker_database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import kotlin.Pair;

public class fitnessDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="FitnessDB";//DATABASE_NAME is the filename for your SQLite database.
    private static final  int DATABASE_VERSION=1;//DATABASE_VERSION helps Android know if the database structure needs to be updated.

    // Table and column names
    private static final String TABLE_NAME= "weekly_data";
    private static final String COLUMN_DAY="day";
    private static final String COLUMN_STEPS="steps";
    private static final String COLUMN_HEART_POINTS="heart_points";

    public  fitnessDB(Context context)  // Call the constructor of the parent class SQLiteOpenHelper
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);// Pass the context (from your app), the name of the database,
        // null for the cursor factory (default), and the database version
        // In short:
        //This constructor sets up your custom database helper by telling Android:
        //“Hey! I want to create/open a database named FitnessDB with version 1.”
    }

    @Override
    public  void  onCreate(SQLiteDatabase db) // Create table when app runs for the first time
    {
        /*
        db.execSQL(...): Executes a raw SQL command.

"CREATE TABLE ": SQL command to create a table.

TABLE_NAME: Variable that holds the table name (you'll define it as a String like "FitnessData").

COLUMN_DAY: Column to store the weekday (e.g., "Mon", "Tue").

TEXT PRIMARY KEY: It’s a string (text), and also the primary key (each day appears only once).

COLUMN_STEPS: Integer column for storing step count.

COLUMN_HEART_POINTS: Integer column for storing heart points.
In short It tells SQLite to create a table with:

One column for the day (like "Mon", "Tue").

One column for steps.

One column for heart points.
         */
        db.execSQL(" CREATE TABLE " + TABLE_NAME + "(" + COLUMN_DAY + " TEXT PRIMARY KEY, " + COLUMN_STEPS + " INTEGER," + COLUMN_HEART_POINTS+" INTEGER )");
    }

    @Override
    // This runs when database version changes (not needed now)
    public  void onUpgrade(SQLiteDatabase db , int oldVersion , int newVersion)  // This method runs automatically if you increase DATABASE_VERSION
    {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);// First, delete the old table (if it exists)
        // "DROP TABLE IF EXISTS" ensures it doesn't crash if the table is already gone
        onCreate(db); // Then call onCreate() to recreate the table with the new structure
        // This is useful when you want to change table columns or structure
    }

    public void insertOrUpdateData(String day , long steps , int heart_points)// created a method to insert or update data
    {
        SQLiteDatabase db= this.getWritableDatabase();// get the database as writeable so that we can insert or update something

        ContentValues values =new ContentValues(); // created an object of contentvalues to store data
        values.put(COLUMN_DAY, day); // store the day like monday , tue etc in the  COLUMN_DAY column
        values.put(COLUMN_STEPS, steps); // store the steps  like 1000, 500 etc in the  COLUMN_STEPS column
        values.put(COLUMN_HEART_POINTS, heart_points); // store the heart points like 20 , 50  etc in the  COLUMN_HEART_POINTS column

        int rowsUpdate = db.update(TABLE_NAME, values, COLUMN_DAY +" = ?", new String[]{day});// update the rows inside db.update gave parameters like table name , values which have to store and COLUMN_DAY +"?", it means
        // COLUMN_DAY == something we will provide later basically "?" this sign indicates where clause means where COLUMN_DAY=day and here in new String[]{day}  we provided the data day means if COLUMN_DAY == day which is a parameter of this method if day is monday then update monday just like that
        if (rowsUpdate==0) // if it pass 0 it means no data has updated as no  entry has made
        {
            db.insert(TABLE_NAME, null, values); //then making entry first giving table name null is default parameter and values to be inserted or stored
        }
    }

    public Map<String, Pair<Integer, Integer>> getWeeklyStepsAndPointsAsMap() // method to get weekly data as map
    {
        //create a Hashmap where the data is stored as key value pair just like dictionary here key will be day and  value int[] which will store steps and heartpoints
        //example :  Monday : [steps:1200, heartpoint:25]
        Map<String, Pair<Integer, Integer>> datamap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase(); // get  the database as readable to read only
        Cursor cursor = db.rawQuery(" SELECT * FROM "+ TABLE_NAME, null); // cursor acts as a pointer to read the database and here we have written raw sql query to fetch everything the table have

        if (cursor!=null && cursor.moveToFirst()) // checking if the table is returning null or not if it is returning data then it will further execute
        {
            do {
                // getting the data from table and storing it in variable
                String day = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY));
                int steps = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STEPS));
                int heartpoint = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HEART_POINTS));

                datamap.put(day, new Pair<>(steps, heartpoint));

            } while (cursor.moveToNext());
            cursor.close(); // closing cursor will stop crashing always should close the cursor after using it

        }
        return datamap;
    }

    public String getTodayName() // creating a method to get today's date or day
    {
        Calendar calendar = Calendar.getInstance(); // created an object of calender
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.US); // created a object of  SimpleDateFormat where EEE ensures it retuens 3 letter day like mon , tue etc  and Locale.US ensure it formats the date using English language conventions.
        return sdf.format(calendar.getTime()); //format the sdf as string calendar.getTime() gives today's day like mon , tue etc
    }

    public  void clearDataIfNewWeek(int steps)
    {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_WEEK);
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int minute = calendar.get(Calendar.MINUTE);

        // clear all data when its monday 12 pm and again start counting
        if (day==Calendar.MONDAY  && steps>0)
        {
            SQLiteDatabase db =this.getWritableDatabase();
            db.execSQL(" DELETE FROM " +TABLE_NAME);
        }
    }

}
