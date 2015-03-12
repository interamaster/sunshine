package com.mio.jrdv.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mio.jrdv.sunshine.data.WeatherContract;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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



                //SI AHORA ELEGIMOS EL SETTINGS DENTRO DE ACTION BAR ABRIMOS LA SETTING ACTIVITY
                //LANZAMOS LA NEW ACTIVITY POR MEDIO DE UN INTENT


                Intent SettingIntent=new Intent(this,SettingsActivity.class);
                startActivity(SettingIntent);

            return true;





        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    //public static class DetailFragment extends Fragment {

    //PARA USAR UN LOADER IMPLEMENTAMOS TAMBIEN : implements LoaderManager.LoaderCallbacks<Cursor>:

   public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


        //helper things(log tag, un cadena a añadir y una ivar con el texto a pasar


        private static final String LOG_TAG = DetailFragment.class.getSimpleName();

        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

        private ShareActionProvider mShareActionProvider;


         private String ForeCastTextReciboDelIntent;

       // private String mForecastStr;

        private static final int DETAIL_LOADER = 0;

        /////////////////////////////////////////////////////////////////////////////
        /////////////////VAMOS A USAR PROJECTION PARA HACER EL ACCESO/////////////////////////////////////////
        /////////////////A LA DATABASE MAS EFICIENTE/////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////

        private static final String[] FORECAST_COLUMNS = {
                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
                WeatherContract.WeatherEntry.COLUMN_DATE,
                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        };

        // these constants correspond to the projection defined above, and must change if the
        // projection changes
        private static final int COL_WEATHER_ID = 0;
        private static final int COL_WEATHER_DATE = 1;
        private static final int COL_WEATHER_DESC = 2;
        private static final int COL_WEATHER_MAX_TEMP = 3;
        private static final int COL_WEATHER_MIN_TEMP = 4;


        public DetailFragment() {

            //esto es para que salga el menu!!si no no sale

            setHasOptionsMenu(true);
        }




        //UNA VEZ PUESTO A TRUE SE LLAMARAESTE METODO DENTRO DEL FRAGMENT




        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//            inflater.inflate(R.menu.detailfragment, menu);
//
//            // Retrieve the share menu item
//            MenuItem menuItem = menu.findItem(R.id.action_share);
//
//            // Get the provider and hold onto it to set/change the share intent.
//            ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
//
//
//
//            // Attach an intent to this ShareActionProvider.  You can update this at any time,
//            // like when the user selects a new piece of data they might like to share.
//            if (mShareActionProvider != null ) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
//            } else {
//                Log.d(LOG_TAG, "Share Action Provider is null?");
//            }



            ///////////////////////////PARA USAR UN LOADER ///////////////////////////



            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detailfragment, menu);

            // Retrieve the share menu item
            MenuItem menuItem = menu.findItem(R.id.action_share);

            // Get the provider and hold onto it to set/change the share intent.
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

            // If onLoadFinished happens before this, we can go ahead and set the share intent now.
            if (ForeCastTextReciboDelIntent != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }



        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            //AQUI RECOGEMOS LA INFO DEL INTENT

            Intent intent=getActivity().getIntent();

//
//            if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){
//
//                //String ForeCastTextReciboDelIntent =intent.getStringExtra(Intent.EXTRA_TEXT);
//
//                //le pasamos el valor a la ivar creada antes
//
//                 ForeCastTextReciboDelIntent =intent.getStringExtra(Intent.EXTRA_TEXT);
//
//
//                ((TextView) rootView.findViewById(R.id.detail_text)).setText(ForeCastTextReciboDelIntent);
//
//            }





            ////////////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////AHORA AQUI NOS LLEGA EL INTENT CON LA URI///////////////////////////////////////
            ////////////////////////// /////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////


            if (intent != null) {
                ForeCastTextReciboDelIntent = intent.getDataString();
            }

            if (null != ForeCastTextReciboDelIntent) {
                ((TextView) rootView.findViewById(R.id.detail_text))
                        .setText(ForeCastTextReciboDelIntent);
            }



            return rootView;
        }

        //dentro del fragment creamos un shareintent method




        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            // este  FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET es fundametal para que tras haber compartido
            //lo que sea vuelva anuestra app y no a la de compartir el intent

            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    ForeCastTextReciboDelIntent + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////NUEVOS DEL CURSOR LOADER////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////



        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
            super.onActivityCreated(savedInstanceState);
        }



        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            //return null;

            Log.v(LOG_TAG, "In onCreateLoader");
            Intent intent = getActivity().getIntent();
            if (intent == null) {
                return null;
            }

            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    intent.getData(),
                    FORECAST_COLUMNS,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor data) {

            Log.v(LOG_TAG, "In onLoadFinished");
            if (!data.moveToFirst()) { return; }

            String dateString = Utility.formatDate(
                    data.getLong(COL_WEATHER_DATE));

            String weatherDescription =
                    data.getString(COL_WEATHER_DESC);

            boolean isMetric = Utility.isMetric(getActivity());

            String high = Utility.formatTemperature(
                    data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);

            String low = Utility.formatTemperature(
                    data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

            ForeCastTextReciboDelIntent = String.format("%s - %s - %s/%s", dateString, weatherDescription, high, low);

            TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
            detailTextView.setText(ForeCastTextReciboDelIntent);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> cursorLoader) {

        }


        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////NUEVOS DEL CURSOR LOADER////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////////////////////



    }
}
