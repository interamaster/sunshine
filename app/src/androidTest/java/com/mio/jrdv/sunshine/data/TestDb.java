/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mio.jrdv.sunshine.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

public class TestDb extends AndroidTestCase {



    //HELPER PARA EL LOG!!!

    public static final String LOG_TAG = TestDb.class.getSimpleName();


    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }


    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(WeatherContract.LocationEntry.TABLE_NAME);
        tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + WeatherContract.LocationEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(WeatherContract.LocationEntry._ID);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.  Return
        the rowId of the inserted location.
    */
    public long testLocationTable() {
        // First step: Get reference to writable database

        WeatherDbHelper dbHelprmio=new WeatherDbHelper(mContext);
        SQLiteDatabase testLocationTableWithValues=dbHelprmio.getWritableDatabase();


        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)

        ContentValues testValues=TestUtilities.createNorthPoleLocationValues();

        // esto nos devolvera los valores creados de test en la Class TestUtilities y en su metodo createNorthPoleLocationValues:

        Log.d(LOG_TAG, "EL testValues creado como contentValue es: " + testValues);



        // Insert ContentValues into database and get a row ID back

        long locationRowId;
        locationRowId=testLocationTableWithValues.insert(WeatherContract.LocationEntry.TABLE_NAME,null,testValues);




        // Query the database and receive a Cursor back

        assertTrue(locationRowId != -1);




        Cursor cursor=testLocationTableWithValues.query(WeatherContract.LocationEntry.TABLE_NAME,//table to query
                null,//al colums
                null, //colums for the where clause
                null, //values for the where clause
                null,//colums to group by
                null , //coluns to filter by row groups
                null //sort by order
                         );



        // Move the cursor to a valid database row and check if we got any records back from the query


        assertTrue("Error: No Records returned from location query", cursor.moveToFirst());




        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)


        TestUtilities.validateCurrentRecord("Error:Locarion Query Validation Failed",cursor,testValues);





        //MOVE The cursor next to demostarte that there is only one record in the databse
        //osea si solo hay uno fallara y dara ese error!!!

        assertFalse("Error:More than one record returned from location query",cursor.moveToNext());

        // Finally, close the cursor and database

        cursor.close();
        testLocationTableWithValues.close();


        // Return the rowId of the inserted location, or "-1" on failure.
        //return -1L;

        return  locationRowId;

    }


    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
    public void testWeatherTable() {
        // First insert the location, and then use the locationRowId to insert
        // the weather. Make sure to cover as many failure cases as you can.


        long LocationRowId=testLocationTable();


        //nos aseguramos que lo obtenemos

        //We return the rowId of the inserted location in testLocationTable, so
        // you should just call that function rather than rewriting it


        assertFalse("Error: Location not inserted ok en el metodo de testLocationTable",LocationRowId == -1L);


        // First step: Get reference to writable database


        WeatherDbHelper dbHelpr=new WeatherDbHelper(mContext);
        SQLiteDatabase dbWeatherTest=dbHelpr.getWritableDatabase();


        // Create ContentValues of what you want to insert
        // (you can use the createWeatherValues TestUtilities function if you wish)


        ContentValues WeathertestValues=TestUtilities.createWeatherValues(LocationRowId);


        // esto nos devolvera los valores creados de test en la Class TestUtilities y en su metodo createNorthPoleLocationValues:

        Log.d(LOG_TAG, "EL copntentValues de la Weather DB  creado como contentValue es: " + WeathertestValues);



        // Insert ContentValues into database and get a row ID back


        long weatherRowId=dbWeatherTest.insert(WeatherContract.WeatherEntry.TABLE_NAME,null,WeathertestValues);





        // Query the database and receive a Cursor back

        assertTrue(weatherRowId != -1);


        // Query the database and receive a Cursor back


        Cursor WeatheeCursor=dbWeatherTest.query(WeatherContract.WeatherEntry.TABLE_NAME,//table to query
                null,//al colums
                null, //colums for the where clause
                null, //values for the where clause
                null,//colums to group by
                null , //coluns to filter by row groups
                null //sort by order
        );



        // Move the cursor to a valid database row


        assertTrue("Error: No Records returned from location query", WeatheeCursor.moveToFirst());



        // Validate data in resulting Cursor with the original ContentValues



        TestUtilities.validateCurrentRecord("testInsertReadDB weatherEntry Validation Failed",WeatheeCursor,WeathertestValues);


        //MOVE The cursor next to demostarte that there is only one record in the databse
        //osea si solo hay uno fallara y dara ese error!!!

        assertFalse("Error:More than one record returned from Weather query",WeatheeCursor.moveToNext());


        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

        // Finally, close the cursor and database

        WeatheeCursor.close();
        dbWeatherTest.close();
    }


//////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //////
//
//    ///nuevos copiados de Verison 1
//
//    static final String TEST_LOCATION = "99705";
//    static final String TEST_DATE = "20141205";
//
//    public static final String COLUMN_DATETEXT = "date";
//
//    static ContentValues createNorthPoleLocationValues() {
//        // Create a new map of values, where column names are the keys
//        ContentValues testValues = new ContentValues();
//        testValues.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, TEST_LOCATION);
//        testValues.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME, "North Pole");
//        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT, 64.7488);
//        testValues.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG, -147.353);
//
//        return testValues;
//    }
//
//    static void validateCursor(Cursor valueCursor, ContentValues expectedValues) {
//
//        assertTrue(valueCursor.moveToFirst());
//
//        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
//        for (Map.Entry<String, Object> entry : valueSet) {
//            String columnName = entry.getKey();
//            int idx = valueCursor.getColumnIndex(columnName);
//            assertFalse(idx == -1);
//            String expectedValue = entry.getValue().toString();
//            assertEquals(expectedValue, valueCursor.getString(idx));
//        }
//        valueCursor.close();
//    }
//
//
//    static ContentValues createWeatherValues(long locationRowId) {
//        ContentValues weatherValues = new ContentValues();
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_LOC_KEY, locationRowId);
//        weatherValues.put( COLUMN_DATETEXT, TEST_DATE);
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, 1.1);
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, 1.2);
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, 1.3);
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, 75);
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, 65);
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC, "Asteroids");
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, 5.5);
//        weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, 321);
//
//        return weatherValues;
//    }
}
