package com.example.mydraft2;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by dikki on 04/12/2016.
 */

public class DbUtility
{
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    static final String FIRSTNAME ="firstName";
    static final String LASTNAME   ="lastName";
    static final String GENDER ="gender";
    static final String MARRIEDSTATUS= "marriedStatus";
    static final String AGE="age" ;
    static final String OCCUPATION="occupation";
    static final String INTEREST="interest";
    static final String HOBBIES="hobbies"  ;
    static final String RELIGION = "religion";
    static final String COUNTRY="country" ;
    static final String HOWKNOWHIM="howKnowHim" ;
    static final String HOWCLOSE="howClose";
    static final String COMMFREQUNCY="commFrequency";
    static final String COMMTOPIC="commTopic";
    static final String USERNAME ="userName";
    static final String PASSWORD ="password";
    static final String PROFILENAME = "profileName";
    static final String RESULT = "result";
    static final String RESULTDATE = "resultDate";

    //Table Constant
    static final String USERTABLE ="user_details";
    static final String PROFILETABLE ="profiles";
    static final String RESULTSTABLE ="results";

    //Constant USed
    static final String dateFormat = "dd/MM/yy";

    public DbUtility()
    {

    }

    public void insertResultDetails(int Result, String profileName )
    {
        DateFormat df = new SimpleDateFormat(dateFormat);
        Date dateobj = new Date();

        ContentValues values = new ContentValues();
        values.put(RESULT,Result);
        values.put(PROFILENAME,profileName);
        values.put(RESULTDATE,df.format(dateobj));//Store current Date
        DeviceOpenHelper.getInstance(null).deviceInsert(RESULTSTABLE,null,values);
    }




    public void insertUserDetails(String firstName , String lastName , String occupation,String gender , String userName,
                                  String password )
    {

        ContentValues values = new ContentValues();
        values.put(FIRSTNAME,firstName);
        values.put(LASTNAME,lastName);
        values.put(OCCUPATION,occupation);
        values.put(GENDER,gender);
        values.put(USERNAME,userName);
        values.put(PASSWORD,password);
        DeviceOpenHelper.getInstance(null).deviceInsert(USERTABLE,null,values);
    }

    public void insertProfileDetails(String firstName , String lastName , String gender ,
                                     String marriedStatus  , Integer age  ,String occupation, String interest, String hobbies  ,
                                     String religion , String country , String howKnowHim , String howClose , String commFrequency ,
                                     String commTopic )
    {
        Log.e(TAG, "insertProfileDetails: Started" );
        ContentValues values = new ContentValues();
        values.put(FIRSTNAME,firstName);
        values.put(LASTNAME,lastName);
        values.put(GENDER,gender);
        values.put(MARRIEDSTATUS,marriedStatus);
        values.put(AGE,age);
        values.put(OCCUPATION,occupation);
        values.put(INTEREST,interest);
        values.put(HOBBIES,hobbies);
        values.put(RELIGION,religion);
        values.put(COUNTRY,country);
        values.put(HOWKNOWHIM,howKnowHim);

        values.put(HOWCLOSE,howClose);
        values.put(COMMFREQUNCY,commFrequency);
        values.put(COMMTOPIC,commTopic);
        DeviceOpenHelper.getInstance(null).deviceInsert(PROFILETABLE,null,values);
        Log.e(TAG, "insertProfileDetails: Ended " );
        //Toast.makeText()
    }

    public List<String> getProfileNames()
    {
        List<String> ProfileName = new ArrayList<String>();

        String Query = "SELECT " + FIRSTNAME + " FROM " + PROFILETABLE ;
        Cursor result = DeviceOpenHelper.getInstance(null).deviceRawQuery(Query,null);
        result.moveToFirst();
        for(int i = 0;i < result.getCount();i++)
        {
            ProfileName.add(result.getString(result.getColumnIndex(FIRSTNAME)));
            Log.e(TAG, "getProfileNames: Called for "+ (i +1) + "Value is "+ProfileName.get(i) + "Result ount is " + result.getCount()  ,null);
            result.moveToNext();
        }
        Log.e(TAG, "getProfileNames: Completed ",null );
            return  ProfileName;
    }

    public boolean isUserValid(String userName, String password, StringBuilder FirstNameDB)
    {

        String Query = "SELECT " +FIRSTNAME + " FROM "+ USERTABLE + " WHERE " + USERNAME + "= ? AND " + PASSWORD + " = ?";
        String [] Selection ={ userName , password};
        Cursor result = DeviceOpenHelper.getInstance(null).deviceRawQuery(Query,Selection);
       // Log.e("Query :",Query + "\n");
        //Log.e("Selection :",Selection[0] + " " + Selection[1] + "\n" );

        if (result.getCount() > 0)
        {
            result.moveToFirst();

            FirstNameDB.append(result.getString(0));
            Log.d(TAG, "isUserValid: FirstName "+ FirstNameDB);
            return true;
        }
        else
            return false;

    }

    public Cursor QueryDb(String Query, String [] Selection)
    {
        Cursor queryResult = DeviceOpenHelper.getInstance(null).deviceRawQuery(Query,Selection);
        return queryResult;
    }

}
