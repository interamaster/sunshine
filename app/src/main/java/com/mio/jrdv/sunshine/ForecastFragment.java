package com.mio.jrdv.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mio.jrdv.sunshine.data.WeatherContract;


//para sacarla de  la  mainActivity y crear una nueva class:ver evernote tambien)
// http://forums.udacity.com/questions/100207855/how-to-make-separate-forecastfragment-java-class

/**
 * A placeholder fragment containing a simple view.
 */
//public class ForecastFragment extends Fragment {


//PARA USAR UN LOADER IMPLEMENTAMOS TAMBIEN : implements LoaderManager.LoaderCallbacks<Cursor>

  public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


        private static final int FORECAST_LOADER = 0;

    /////////////////////////////////////////////////////////////////////////////
    /////////////////VAMOS A USAR PROJECTION PARA HACER EL ACCESO/////////////////////////////////////////
    /////////////////A LA DATABASE MAS EFICIENTE/////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////


    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.
    private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG

            //ESTO DA:[weather._id,date,short_desc,max,min,location_setting,weather_id,coord_lat,coord_long]
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;


    ////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    //  ArrayAdapter como ivar para poder acceder a el desde el asyntask
    //private ArrayAdapter<String> mForecastAdapter;

    //vamoa acambiarlo por el nuevo FroreCastAdapter!!:

//    1. Change mForecastAdapter's type
//
//    Change mForecastAdapter, to be an instance of ForecastAdapter.

    private ForecastAdapter mForecastAdapter;

    ////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

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
        inflater.inflate(R.menu.forecastfragment, menu);

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
    /*
            //return true;
            //ahora en vez de esto que haga el asyntask

            // new FetchWeatherTask().execute(null,null,null);
            //en curso lo hace asi...deb dar lo mismo

            FetchWeatherTask weatherTask = new FetchWeatherTask();
            //weatherTask.execute();
            //como ahora le podemos asar el parametro...

            //weatherTask.execute("seville");

            //aqui ahora en vez de meterlo a ano habria que cogerlo del Settings!!

            SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());

            //asi si no hay valor en el key coge le default!!:
            //http://developer.android.com/reference/android/content/SharedPreferences.html



            String LocationFromPrefs= pref.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));

            weatherTask.execute(LocationFromPrefs);
*/



            //AHORA LO DE ARRIBA LO PONGO ENUN METODO APARTE

            updateWeather();



            return true;


        }

        return super.onOptionsItemSelected(item);

    }


    private void updateWeather(){

        /*
        FetchWeatherTask weatherTask = new FetchWeatherTask();
        //weatherTask.execute();
        //como ahora le podemos asar el parametro...

        //weatherTask.execute("seville");

        //aqui ahora en vez de meterlo a ano habria que cogerlo del Settings!!

        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());

        //asi si no hay valor en el key coge le default!!:
        //http://developer.android.com/reference/android/content/SharedPreferences.html



        String LocationFromPrefs= pref.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));

        weatherTask.execute(LocationFromPrefs);

        */


        //CON NUEVA CLASS ASYNTASK SACADA FUERA DE ESTE FRAGMENT:


//        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        //asi si no hay valor en el key coge le default!!:
//        //http://developer.android.com/reference/android/content/SharedPreferences.html
//
//        String LocationFromPrefs = prefs.getString(getString(R.string.pref_location_key),
//                getString(R.string.pref_location_default));
//        weatherTask.execute(LocationFromPrefs);
//

        ////////////////////////////////////////////////////////////////////////////////////////////
        ////// ////////////////////////////////////////////////////////////////////////////////////////////
        //////NO SE USA
        //Inside of FetchWeatherTask, we’re going to remove the formatting code and anything for updating the adapter.
        //  SE HACE CON NUEVA UTILITY CLASS Y CAMBIO EL FECTWEATEHERTASK TAMBIEN:.
        //vamoa acambiarlo por el nuevo FroreCastAdapter!!:
        ////////////////////////////////////////////////////////////////////////////////////////////


        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);





    }




    @Override
    public void onStart() {
        super.onStart();

        //al arrancarque ya actualziae del tiron

        //updateWeather(); LO QUITAMOS POR LO DE ABAJO:


       // 5. Remove Excessive Weather Fetching
       // Now that we have a database, we don’t have to constantly talk to the network and fetch the weather.
       // But if you look at onStart from ForecastFragment, you'll see every time it's called, it downloads data from Open Weather Map.
       // This means every time you rotate the device, you'll be attempting to connect to Open Weather Map.
       // In Lesson 6 we’ll show you how to schedule updates in the background, but for now let’s save on network bandwidth and battery by deleting onStart.
       // You can use the “refresh” menu item to get new weather data.

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);



        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
        /*
        //creamos un array de datos para la listview
        String[] forecastArray = {
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

        List<String> weekForecast = new ArrayList<String>(
                Arrays.asList(forecastArray)
        );


        //Y a partir de ambos creamos el ArrayAdapter que cogera los
        //datos de la List(weekForeCast) recien creada con los datos del array(foresCastArray)



        //ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(
        //hemos definido antes este ArrayAdapter como ivar para poder acceder a el desde el asytask

         mForecastAdapter = new ArrayAdapter<String>(
                //el context es este fragment parent activity
                getActivity(),
                //Id del de item layout
                R.layout.list_item_forecast_textview,
                //ID del text para rellenar dentro de ese layout
                R.id.list_item_forecast_textview,
                //los datos  a rellenar a partir de la List creada
                weekForecast
        );

        */

        /////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////AHORA LO CREAMOS VACIO!!///////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////


//
//        mForecastAdapter = new ArrayAdapter<String>(
//                //el context es este fragment parent activity
//                getActivity(),
//                //Id del de item layout
//                R.layout.list_item_forecast_textview,
//                //ID del text para rellenar dentro de ese layout
//                R.id.list_item_forecast_textview,
//                //los datos  a rellenar a partir de la List creada
//                new ArrayList<String>()
//        );

        //COMO AHORA ES UN CURSOR ADAPTER LO LLENAMOS ASI:

//
//        2. Get Data from the Database
//
//        Let’s go to where we first need to populate the ForecastFragment with data and do
//        so by getting the data from the database. Go to onCreateView.
//        Use WeatherProvider to query the database the same way you are in FetchWeatherTask:



        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri,
                null, null, null, sortOrder);


//        3. Make a new ForecastAdapter
//
//        Still in onCreateView, we have a Cursor cur, so let’s use our new ForecastAdapter.
//        Create a new ForecastAdapter with the new cursor. The list will be empty the first time we run.

                mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);



        /////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////


        //Find a reference to the ListView en el Layout del Fragment

        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);

        //a esa listview le atach el adapter creado para que pong alos datos

        listView.setAdapter(mForecastAdapter);
        //ahora ponems un listener

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////AHORA DETECTAAMOS EL ONCLICK EN EL LISTVIEW ASI///////////////////////////////////////
            //////////////////////////EATA VEZ PASAMOS LA URI PARA EÑL DATA DE LA DETAILVIEW////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //y aqui de moemento poemo un toast con la weatherinfo
                /*
                Context context = getActivity();
                String textForeCast = mForecastAdapter.getItem(position);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, textForeCast, duration);
                toast.show();
                */
                ////////////////////////////////////////////////////////////////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////////////////

//                4. Delete OnItemClickListener
//
//                Because we changed the adapter, the OnItemClickListener in ForecastFragment for the ListView
//                won’t work. Specifically this line String forecast = mForecastAdapter.getItem(position);
//                is problematic because getItem with a CursorAdapter doesn’t return a string.
//
//                        Go ahead and remove or comment out this for now.
//
//                        We’ll talk more about this and correct this soon enough.
//                Until then, our code will compile and run but not have access to our DetailView.
//
//



                //vamos ahacerlo con el cursoradapter y este  NO devuleve una String:
//               // String textForeCast = mForecastAdapter.getItem(position);
//
//
//
//                //AHORA EN VEZ DE L TOAST LANZAMOS LA NEW ACTIVITY POR MEDIO DE UN INTENT
//
//                Intent intent=new Intent(getActivity(),DetailActivity.class).putExtra(Intent.EXTRA_TEXT,textForeCast);
//                startActivity(intent);

                ////////////////////////////////////////////////////////////////////////////////////////////
                ////////////////////////AHORA DETECTAAMOS EL ONCLICK EN EL LISTVIEW ASI///////////////////////////////////////
                //////////////////////////EATA VEZ PASAMOS LA URI PARA EÑL DATA DE LA DETAILVIEW////////////////////////////////
                /////////////////////////////////////////////////////////////////////////////////////////////




                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    Intent intent = new Intent(getActivity(), DetailActivity.class)
                            .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locationSetting, cursor.getLong(COL_WEATHER_DATE)
                            ));
                    startActivity(intent);
                }








            }
        });



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

/////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////PARA ARREGLAR SETTINGS////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////


    // since we read the location when we create the loader, all we need to do is restart things
    void onLocationChanged( ) {
        updateWeather();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }




    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////NUEVOS DEL CURSOR LOADER////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////





    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //return null;

        String locationSetting = Utility.getPreferredLocation(getActivity());

        // Sort order:  Ascending, by date.
        String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

//        return new CursorLoader(getActivity(),
//                weatherForLocationUri,
//                null,
//                null,
//                null,
//                sortOrder);

        /////////////////////////////////////////////////////////////////////////////
        /////////////////VAMOS A USAR PROJECTION PARA HACER EL ACCESO/////////////////////////////////////////
        /////////////////A LA DATABASE MAS EFICIENTE/////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////

        return new CursorLoader(getActivity(),
                weatherForLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mForecastAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mForecastAdapter.swapCursor(null);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////AL SER EN UN FRAGMENT SE INICILIZA EL CURSOR LOADER EN ONACTIVITYCREATED///////////////////


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);

    }

        //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


//
///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////CON NUEVO ASYNTASK SACADO DE EESTE FRAGMNET SOLO TENEMOS QUE MODIFICAR ///////
    //////////////////////EL UPDATEWEATHER DE ESTA CLASS!!!!!!!!!!!!!////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //////








//    //CREAMOS NA NUEVA ASYNTASK CLASS:
//
//    //se crea asi:
//
//    //public class FetchWeatherTask extends AsyncTask <Params,Progress,Result>{
//    /*
//
//    The three types used by an asynchronous task are the following:
//
//Params, the type of the parameters sent to the task upon execution.
//Progress, the type of the progress units published during the background computation.
//Result, the type of the result of the background computation.
//Not all types are always used by an asynchronous task. To mark a type as unused, simply use the type Void:
//
// private class MyTask extends AsyncTask<Void, Void, Void> { ... }
//
//
//     */
//
//    //  public class FetchWeatherTask extends AsyncTask <Void,Void,Void>{
//    //lo cambio para recibir postal code
//
//    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
//
//        //para el TAG del Logging:
//
//        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();
//
//
//        @Override
//        //protected Void doInBackground(Void... params) {
//        //ahora recibimos un String del PostalCode:
//        //protected Void doInBackground(String... params) {
//        //Y AHORA QUEREMOS QUE NOS DEVUELVA UN ARRAY DE STRINS
//            protected String[] doInBackground(String... params) {
//
//
//
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////
//            /////////// aqui pegamos del github para conseguir el JSON httprequest////// //// ////////////////
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//// These two need to be declared outside the try/catch
//// so that they can be closed in the finally block.
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//
//// Will contain the raw JSON response as a string.
//            String forecastJsonStr = null;
//
//            String format = "json";
//            String units = "metric";
//            int numDays = 7;
//
//
//            try {
//                // Construct the URL for the OpenWeatherMap query
//                // Possible parameters are avaiable at OWM's forecast API page, at
//                // http://openweathermap.org/API#forecast
//                // URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
//
//                //////////////////////////////////////
//                //AQUI VAMOS A HACER UN URI PARA CONSTRUIR ESA URL PERO LE PASAREMOS NOSOTROS UN PARAMETRO DESDE FUERA
//                final String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
//                final String QUERY_PARAM = "q";
//                final String FORMAT_PARAM = "mode";
//                final String UNITS_PARAM = "units";
//                final String DAYS_PARAM = "cnt";
//
//                //Y AHORA CREAMO EL URI
//
//                Uri biultUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//                        .appendQueryParameter(QUERY_PARAM, params[0])
//                        .appendQueryParameter(FORMAT_PARAM, format)
//                        .appendQueryParameter(UNITS_PARAM, units)
//                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
//                        .build();
//
//                //  Y A PARTIR DEL URI CREAMOS EL MISMO URL DE ANTES!!!!
//
//                URL url = new URL(biultUri.toString());
//                //y lo chequeamos en Log
//                Log.v(LOG_TAG, "URI Creada=" + biultUri.toString());
//                //esto deberia dar lo miso de arriba
//                //  URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
//                // Y EFECTIVAMENTE DA ESO:!!!
//                //02-05 20:39:00.298: V/FetchWeatherTask(1670): URI Creada=http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
//
//
//                /////////////////////////////////////
//
//
//                // Create the request to OpenWeatherMap, and open the connection
//                urlConnection = (HttpURLConnection) url.openConnection();
//
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    //forecastJsonStr = null; en vez de esto devolvemos null
//                    return null;
//
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//                    //forecastJsonStr = null; en vez de esto devolvemos null
//                    return null;
//                }
//                forecastJsonStr = buffer.toString();
//
//                //vamos aponer en el LOG el resulatdo recibido
//
//                Log.v(LOG_TAG, "Forecast JSON String RECIBIDO:" + forecastJsonStr);
//
//
//
//
//
//            } catch (IOException e) {
//                // Log.e("PlaceholderFragment", "Error ", e); con el nuevo LOG_TAG:
//                Log.e(LOG_TAG, "Error ", e);
//                // If the code didn't successfully get the weather data, there's no point in attemping
//                // to parse it.
//                //forecastJsonStr = null; en vez de esto devolvemos null
//                return null;
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        // Log.e("PlaceholderFragment", "Error closing stream", e);//con new LOG_TaG
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//            }
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////
//                //AHORA CON LOS HELPERS METODOS DEVOLVEMOS EL ARRAY DE STRINGS YA OK:
//
//                                try {
//                                    return getWeatherDataFromJson(forecastJsonStr,numDays);
//
//
//                                }
//                                catch (JSONException e){
//                                    Log.e(LOG_TAG,e.getMessage(),e);
//                                    e.printStackTrace();
//                                }
//
//
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////
//            //////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String[] result) {
//            super.onPostExecute(result);
//            //CUANDO HA TERMIADO TENEMOS AQUI EL ARRAY DE STRINGS[] LLAMADO "strings"
//            //Y COMO HEMOS HECHO IVAR EL ARRAYADAPTER "mFrorecastAdapter" podemos actualizarlo
//
//            /*
//            adapter.clear();
//                for(int i = 0;i<categoriesArray.length;i++){
//                  adapter.add(categoriesArray[i]);
//                    }
//             */
//            if (result !=null){
//
//                mForecastAdapter.clear();
//
//                for (String dayForeCastStr : result){
//                    mForecastAdapter.add(dayForeCastStr);
//                }
//
//                //listo actualizado
//
//            }
//
//        }
//
//
//        //////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////JSON HELPER METHODS!!!//////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//        /* The date/time conversion code is going to be moved outside the asynctask later,
//         * so for convenience we're breaking it out into its own method now.
//         */
//        private String getReadableDateString(long time) {
//            // Because the API returns a unix timestamp (measured in seconds),
//            // it must be converted to milliseconds in order to be converted to valid date.
//            Date date = new Date(time * 1000);
//            SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
//            return format.format(date).toString();
//        }
//
//
//        /*
//        //Prepare the weather high/lows for presentation.
//
//        private String formatHighLows(double high, double low) {
//            // For presentation, assume the user doesn't care about tenths of a degree.
//            long roundedHigh = Math.round(high);
//            long roundedLow = Math.round(low);
//
//            String highLowStr = roundedHigh + "/" + roundedLow;
//            return highLowStr;
//        }
//*/
//
//        //new metodo para cambiar de celsius a farenheit
//
//
//        /**
//         * Prepare the weather high/lows for presentation.
//         */
//        private String formatHighLows(double high, double low) {
//            // Data is fetched in Celsius by default.
//            // If user prefers to see in Fahrenheit, convert the values here.
//            // We do this rather than fetching in Fahrenheit so that the user can
//            // change this option without us having to re-fetch the data once
//            // we start storing the values in a database.
//            SharedPreferences sharedPrefs =
//                    PreferenceManager.getDefaultSharedPreferences(getActivity());
//            String unitType = sharedPrefs.getString(
//                    getString(R.string.pref_temperature_key),
//                    getString(R.string.pref_units_metric));
//
//
//
//            if (unitType.equals(getString(R.string.pref_units_imperial))) {
//                high = (high * 1.8) + 32;
//                low = (low * 1.8) + 32;
//            } else if (!unitType.equals(getString(R.string.pref_units_metric))) {
//                Log.d(LOG_TAG, "Unit type not found: " + unitType);
//            }
//
//            // For presentation, assume the user doesn't care about tenths of a degree.
//            long roundedHigh = Math.round(high);
//            long roundedLow = Math.round(low);
//
//            String highLowStr = roundedHigh + "/" + roundedLow;
//            return highLowStr;
//        }
//
//        /**
//         * Take the String representing the complete forecast in JSON Format and
//         * pull out the data we need to construct the Strings needed for the wireframes.
//         * <p/>
//         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
//         * into an Object hierarchy for us.
//         */
//        private String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
//                throws JSONException {
//
//            // These are the names of the JSON objects that need to be extracted.
//            final String OWM_LIST = "list";
//            final String OWM_WEATHER = "weather";
//            final String OWM_TEMPERATURE = "temp";
//            final String OWM_MAX = "max";
//            final String OWM_MIN = "min";
//            final String OWM_DATETIME = "dt";
//            final String OWM_DESCRIPTION = "main";
//
//            JSONObject forecastJson = new JSONObject(forecastJsonStr);
//            JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);
//
//            String[] resultStrs = new String[numDays];
//            for (int i = 0; i < weatherArray.length(); i++) {
//                // For now, using the format "Day, description, hi/low"
//                String day;
//                String description;
//                String highAndLow;
//
//                // Get the JSON object representing the day
//                JSONObject dayForecast = weatherArray.getJSONObject(i);
//
//                // The date/time is returned as a long.  We need to convert that
//                // into something human-readable, since most people won't read "1400356800" as
//                // "this saturday".
//                long dateTime = dayForecast.getLong(OWM_DATETIME);
//                day = getReadableDateString(dateTime);
//
//                // description is in a child array called "weather", which is 1 element long.
//                JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
//                description = weatherObject.getString(OWM_DESCRIPTION);
//
//                // Temperatures are in a child object called "temp".  Try not to name variables
//                // "temp" when working with temperature.  It confuses everybody.
//                JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
//                double high = temperatureObject.getDouble(OWM_MAX);
//                double low = temperatureObject.getDouble(OWM_MIN);
//
//               highAndLow = formatHighLows(high, low);
//                resultStrs[i] = day + " - " + description + " - " + highAndLow;
//            }
//
//
//
//            //AÑADIMOS UN LOGGNG
//
//
//            for (String s :resultStrs){
//
//                Log.v(LOG_TAG,"ForeCast Entry= "+s);
//                    /*
//                      ESTO DA:estso elemento del array:
//                                               /com.mio.jrdv.sunshine V/FetchWeatherTask﹕ ForeCast Entry= Sun, Feb 8 - Rain - 17/12
//                02-08 19:56:19.585    1557-1580/com.mio.jrdv.sunshine V/FetchWeatherTask﹕ ForeCast Entry= Mon, Feb 9 - Rain - 14/5
//                02-08 19:56:19.585    1557-1580/com.mio.jrdv.sunshine V/FetchWeatherTask﹕ ForeCast Entry= Tue, Feb 10 - Clear - 14/3
//                02-08 19:56:19.585    1557-1580/com.mio.jrdv.sunshine V/FetchWeatherTask﹕ ForeCast Entry= Wed, Feb 11 - Clear - 17/1
//                02-08 19:56:19.589    1557-1580/com.mio.jrdv.sunshine V/FetchWeatherTask﹕ ForeCast Entry= Thu, Feb 12 - Rain - 19/9
//                02-08 19:56:19.589    1557-1580/com.mio.jrdv.sunshine V/FetchWeatherTask﹕ ForeCast Entry= Fri, Feb 13 - Rain - 17/10
//                02-08 19:56:19.589    1557-1580/com.mio.jrdv.sunshine V/FetchWeatherTask﹕ ForeCast Entry= Sat, Feb 14 - Clear - 19/8
//                */
//
//
//            }
//            return resultStrs;
//        }
//
//
//    }
}






