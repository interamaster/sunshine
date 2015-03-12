package com.mio.jrdv.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {



    private final String LOG_TAG = MainActivity.class.getSimpleName();


    private String mLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //le damos el valor guardao en setting con la yuda de la utility class:

        mLocation = Utility.getPreferredLocation(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            //EN VEZ DE DEVILVER TRUE(NO HACE NADA QUEREMOS QUE ABRA EL SETTINGS ACTIVITY


            Intent SettingIntent=new Intent(this,SettingsActivity.class);
            startActivity(SettingIntent);//no hacemos startActivityForResult px no esperamos nada en el SettingsActivity
            return true;

        }

        //ahora añadimos el map

        if (id == R.id.action_map ){

            //aqui llamamos al emtodo que abrira el map

            openPreferedLocationInMap();



        }

        return super.onOptionsItemSelected(item);
    }

    private void openPreferedLocationInMap() {

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);

        //asi si no hay valor en el key coge le default!!:
        //http://developer.android.com/reference/android/content/SharedPreferences.html



        String LocationFromPrefs= pref.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));


        //a partir de obtenerla loaction de los sharedPrefs creamos un URI para poderlo pner en un mapa...?¿?

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", LocationFromPrefs)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + LocationFromPrefs + ", no receiving apps installed!");

            //y tambie  un toast


            Context context = this;
            String textForeCast = "chungo no hay ninguna app de mapas al que pasarle el intent";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, textForeCast, duration);
            toast.show();

        }

    }


}
