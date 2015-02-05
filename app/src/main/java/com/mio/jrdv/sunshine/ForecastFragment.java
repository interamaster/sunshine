package com.mio.jrdv.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



//para sacarla de  la  mainActivity y crear una nueva class:8ver evernote tambien)
// http://forums.udacity.com/questions/100207855/how-to-make-separate-forecastfragment-java-class

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //se neceista añadir este metodo y esta linea para que el fragment pueda recibir los menu events
        // del action Bar?¿?¿

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //AÑADIOS ESTE METODO PARA EL MENU Y INFLAMOS AQUI EL ELEMENT MENU CREADO NEW(forecastfragment.xml)
        inflater.inflate(R.menu.forecastfragment,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //aqui recbimos los click del action bar item
        //The action bar wil automatically handle clicks on the home/up button so long
        //as you specify a parent activity in AndroidManifest.xml
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {

            //return true;
            //ahora en vez de esto que haga el asyntask

           // new FetchWeatherTask().execute(null,null,null);
            //en curso lo hace asi...deb dar lo mismo

            FetchWeatherTask weatherTask= new FetchWeatherTask();
            //weatherTask.execute();
            //como ahora le podemos asar el parametro...

            weatherTask.execute("94043");
            return true;



        }

        return super.onOptionsItemSelected(item);

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



/*
        //////////////////////////LO QUITAMSO Y LO PASAMOS A UN ASYNTASK!!!!!//////// ///// //////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////// aqui pegamos del github para conseguir el JSON httprequest////// //// ////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////

// These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

// Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJsonStr = null;
            }
            forecastJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            forecastJsonStr = null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
*/



        return rootView;
    }




    //CREAMOS NA NUEVA ASYNTASK CLASS:

    //se crea asi:

    //public class FetchWeatherTask extends AsyncTask <Params,Progress,Result>{
    /*

    The three types used by an asynchronous task are the following:

Params, the type of the parameters sent to the task upon execution.
Progress, the type of the progress units published during the background computation.
Result, the type of the result of the background computation.
Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void:

 private class MyTask extends AsyncTask<Void, Void, Void> { ... }


     */

  //  public class FetchWeatherTask extends AsyncTask <Void,Void,Void>{
    //lo cambio para recibir postal code

     public class FetchWeatherTask extends AsyncTask <String,Void,Void>{

        //para el TAG del Logging:

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();


        @Override
        //protected Void doInBackground(Void... params) {
        //ahora recibimos un String del PostalCode:
         protected Void doInBackground(String... params) {



            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////// aqui pegamos del github para conseguir el JSON httprequest////// //// ////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////////

// These two need to be declared outside the try/catch
// so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

// Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format="json";
            String units="metric";
            int numDays=7;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
               // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");

                        //////////////////////////////////////
                        //AQUI VAMOS A HACER UN URI PARA CONSTRUIR ESA URL PERO LE PASAREMOS NOSOTROS UN PARAMETRO DESDE FUERA
                            final String FORECAST_BASE_URL =  "http://api.openweathermap.org/data/2.5/forecast/daily?";
                            final String  QUERY_PARAM = "q";
                            final String FORMAT_PARAM = "mode";
                            final String  UNITS_PARAM = "units";
                            final String DAYS_PARAM = "cnt";

                         //Y AHORA CREAMO EL URI

                                    Uri biultUri=Uri.parse(FORECAST_BASE_URL).buildUpon()
                                    .appendQueryParameter(QUERY_PARAM,params[0])
                                    .appendQueryParameter(FORMAT_PARAM,format)
                                    .appendQueryParameter(UNITS_PARAM,units)
                                    .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays))
                                    .build();

                           //  Y A PARTIR DEL URI CREAMOS EL MISMO URL DE ANTES!!!!

                            URL url=new URL(biultUri.toString());
                            //y lo chequeamos en Log
                            Log.v(LOG_TAG,"URI Creada="+biultUri.toString());
                            //esto deberia dar lo miso de arriba
                            //  URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
                             // Y EFECTIVAMENTE DA ESO:!!!
                            //02-05 20:39:00.298: V/FetchWeatherTask(1670): URI Creada=http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7



                /////////////////////////////////////






                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    //forecastJsonStr = null; en vez de esto devolvemos null
                    return null;

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    //forecastJsonStr = null; en vez de esto devolvemos null
                    return null;
                }
                forecastJsonStr = buffer.toString();

                //vamos aponer en el LOG el resulatdo recibido

                Log.v(LOG_TAG,"Forecast JSON String RECIBIDO:"+forecastJsonStr);

            } catch (IOException e) {
               // Log.e("PlaceholderFragment", "Error ", e); con el nuevo LOG_TAG:
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                //forecastJsonStr = null; en vez de esto devolvemos null
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                       // Log.e("PlaceholderFragment", "Error closing stream", e);//con new LOG_TaG
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////////////////////////////////



            return null;
        }
    }
}
