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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mio.jrdv.sunshine.data.WeatherContract.LocationEntry;
import com.mio.jrdv.sunshine.data.WeatherContract.WeatherEntry;

/**
 * Manages a local database for weather data.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {







    //HELPER PARA EL LOG!!!

    public static final String LOG_TAG = WeatherDbHelper.class.getSimpleName();




    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "weather.db";

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //CON ESTO CREAMOS LA LOCATION TABLE!!!!

        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " +LocationEntry.TABLE_NAME+" (" +
                LocationEntry._ID + " INTEGER PRIMARY KEY ," +
                LocationEntry.COLUMN_LOCATION_SETTING + " TEXT UNIQUE NOT NULL," +//AL PONER TEXT UNIQUE NOT NULL ESTAMOS CREANDO EL
                                                                                    //TEXTO QUE NO PUEDE CAMBIAR YA QUE ESTE ES EL
                                                                                    //FOREIGN KEY DE NUESTRO WEATHER_DB!!
                LocationEntry.COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                LocationEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                LocationEntry.COLUMN_COORD_LONG + " REAL NOT NULL" +  ");";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);


                //ESTO CREA ESTE CHORIZO:



        Log.d(LOG_TAG, "LA LOCATION:TABLE SE CREA CON ESTE CHORIZO: "+ SQL_CREATE_LOCATION_TABLE);
          // LA LOCATION:TABLE SE CREA CON ESTE CHORIZO:
          // CREATE TABLE location (_idINTEGER PRIMARY KEY ,location_setting TEXT UNIQUE NOT NULL,city_name TEXT NOT NULL,
          // coord_lat REAL NOT NULL, coord_long REAL NOT NULL);




        //CON ESTO CREAMOS LA WEATHER TABLE!!!!


        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + WeatherEntry.TABLE_NAME + " (" +
                // Why AutoIncrement here, and not above?
                // Unique keys will be auto-generated in either case.  But for weather
                // forecasting, it's reasonable to assume the user will want information
                // for a certain date and all dates *following*, so the forecast data
                // should be sorted accordingly.
                WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the location entry associated with this weather data
                WeatherEntry.COLUMN_LOC_KEY + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                WeatherEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                WeatherEntry.COLUMN_WEATHER_ID + " INTEGER NOT NULL," +

                WeatherEntry.COLUMN_MIN_TEMP + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_MAX_TEMP + " REAL NOT NULL, " +

                WeatherEntry.COLUMN_HUMIDITY + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_PRESSURE + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_WIND_SPEED + " REAL NOT NULL, " +
                WeatherEntry.COLUMN_DEGREES + " REAL NOT NULL, " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + WeatherEntry.COLUMN_LOC_KEY + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + LocationEntry._ID + "), " +

                // To assure the application have just one weather entry per day
                // per location, it's created a UNIQUE constraint with REPLACE strategy
                " UNIQUE (" + WeatherEntry.COLUMN_DATE + ", " +
                WeatherEntry.COLUMN_LOC_KEY + ") ON CONFLICT REPLACE);";




        //ESTO CREA ESTE CHORIZO:



        Log.d(LOG_TAG, "LA WEATHER:TABLE SE CREA CON ESTE CHORIZO: "+ SQL_CREATE_WEATHER_TABLE);

                //LA WEATHER:TABLE SE CREA CON ESTE CHORIZO:
                // CREATE TABLE weather (_id INTEGER PRIMARY KEY AUTOINCREMENT,location_id INTEGER NOT NULL,
                // date INTEGER NOT NULL, short_desc TEXT NOT NULL, weather_id INTEGER NOT NULL,min REAL NOT NULL,
                // max REAL NOT NULL, humidity REAL NOT NULL, pressure REAL NOT NULL, wind REAL NOT NULL, degrees REAL NOT NULL,
                // FOREIGN KEY (location_id) REFERENCES location (_id),  UNIQUE (date, location_id) ON CONFLICT REPLACE);




        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
