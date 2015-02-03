package com.mio.jrdv.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //creamos un array de datos para la listview
            String [] forecastArray={
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63",
                    "Today- Sunny -88/63"
            };

            //a partir de ese Array de String creasmos un List

            List<String> weekForecast=new ArrayList<String>(
                    Arrays.asList(forecastArray)
            );


            //Y a partir de ambos creamos el ArrayAdapter que cogera los
            //datos de la List(weekForeCast) recien creada con los datos del array(foresCastArray)

            ArrayAdapter<String> mForecastAdapter= new ArrayAdapter<String>(
                    //el context es este fragment parent activity
                    getActivity(),
                    //Id del de item layout
                    R.layout.list_item_forecast_textview,
                    //ID del text para rellenar dentro de ese layout
                    R.id.list_item_forecast_textview,
                    //los datos  a rellenar a partir de la List creada
                    weekForecast
            );

            //Find a reference to the ListView en el Layout del Fragment

            ListView listView= (ListView) rootView.findViewById(R.id.listView_forecast);

            //a esa listview le atach el adapter creado para que pong alos datos

            listView.setAdapter(mForecastAdapter);

            return rootView;
        }
    }
}
