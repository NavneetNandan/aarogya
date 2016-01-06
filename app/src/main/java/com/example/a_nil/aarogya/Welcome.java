package com.example.a_nil.aarogya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Welcome extends AppCompatActivity {

    String username=null;
    Context abc=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlcome);
        final Context c=this;
        abc=this;
        SharedPreferences sharedPref=c.getSharedPreferences("logininfo", MODE_PRIVATE);
        username=sharedPref.getString("username",null);
        int unsynced=sharedPref.getInt("unsynced",0);
        Log.e("Unsynced",""+unsynced);
        ConnectivityManager manager = (ConnectivityManager) c.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = manager.getActiveNetworkInfo();
        boolean isConnected = activenetwork != null && activenetwork.isConnectedOrConnecting();
        if (isConnected) {
            if (unsynced == 0) {
                renewDatabase renewDatabase = new renewDatabase();
                renewDatabase.execute(username);
            } else {
                HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
                SQLiteDatabase healthDb = mDbHelper.getWritableDatabase();
                String searchQuery = "SELECT  * FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
                Cursor cursor = healthDb.rawQuery(searchQuery, null);
                cursor.moveToLast();
                for (int i = 1; i < unsynced; i++) {
                    cursor.moveToPrevious();
                }
                uploadChanges(this, cursor);
            }
        }

        CardView cardViewRecorder=(CardView)findViewById(R.id.card_view0);
        cardViewRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent healthrecorder = new Intent(c, Healthrecorder.class);
                startActivity(healthrecorder);
            }
        });
        CardView cardViewBuzz=(CardView)findViewById(R.id.card_view1);

        cardViewBuzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsbuzz = new Intent(c, Healthbuzz.class);
                startActivity(newsbuzz);
            }
        });
        CardView cardViewCalorie=(CardView)findViewById(R.id.card_view2);
        cardViewCalorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calorie=new Intent(c,CalorieCalculator.class);
                startActivity(calorie);

            }
        });
    }

    private void uploadChanges(final Context c, final Cursor curse) {
        RequestQueue queue= Volley.newRequestQueue(c);
        String url ="http://aarogya.6te.net/aarogya/add_hr.php";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject json = new JSONObject(response);
                            int respons = json.getInt("status");
                            Log.e("status", response);
                            if (respons == 1){
                                if(!curse.isLast())
                                {
                                    curse.moveToNext();
                                    uploadChanges(c,curse);
                                }
                                else{

                                    renewDatabase renewDatabase = new renewDatabase();
                                    renewDatabase.execute(username);
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("doe",curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_DOE)));
                Log.e("volleyerror", error.toString());

            }
        })
        {protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                Log.e("Position",curse.getPosition()+"");
                //curse.moveToPrevious();
                //Log.e("Position2", curse.getPosition() + "");
                Log.e("abc", "fafa" + curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_DOE)));
                //params.put("JSON",jsonStr);

                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_DOE, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_DOE)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT,curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE,curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID)));
                params.put(HealthFormContract.HealthEntry.COLUMN_NAME_VISION, curse.getString(curse.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_VISION)));
                //params.put("id","2");
                return params;
            }};
        queue.add(stringRequest);
    }

    protected class renewDatabase extends AsyncTask<String,Void,Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;
            try {
                URL url = new URL("http://aarogya.6te.net/aarogya/fetch_hr.php?username="+strings[0]);
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream=urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(inputStream==null)
                {
                    jsonStr=null;
                }
                reader =new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
                Log.e("JSON",jsonStr);
            }
            catch (IOException e){
                Log.e("PlaceholderFragment", "Error ", e);
                return null;
            }
            finally {
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
            HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
            SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
            String deleteQuery = "DELETE FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
            healthDb.execSQL(deleteQuery);
            try {
                JSONObject data = new JSONObject(jsonStr);
                JSONArray dataarray=data.getJSONArray("detail");
                for (int i=0;i<dataarray.length();i++){
                    JSONObject dataJ=dataarray.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_DOE,dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_DOE));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT,dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT,dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_VISION,dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_VISION));
                    healthDb.insert(HealthFormContract.HealthEntry.TABLE_NAME, null, values);
                }

            } catch (JSONException e) {
                Log.e("ERROR",e.toString());
                e.printStackTrace();
            }

            healthDb.close();
            return 1;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wlcome, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_SignOut: {
                SharedPreferences sharedPref = this.getSharedPreferences("logininfo", MODE_PRIVATE);
                int unsynced=sharedPref.getInt("unsynced",0);
                if(unsynced==0){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear();
                    editor.commit();
                    HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
                    SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
                    String deleteQuery = "DELETE FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
                    healthDb.execSQL(deleteQuery);
                    healthDb.close();
                    startActivity(new Intent(this, MainActivity.class));
                    this.finish();
                }
                else {
                    SignoutWarning signoutWarning=new SignoutWarning(unsynced);
                    signoutWarning.show(getFragmentManager(),"Sign Out");
                }

                return true;
            }
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    public class SignoutWarning extends DialogFragment {
        int unsyncedNumber;
        SignoutWarning(int unsynced){
            unsyncedNumber=unsynced;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("There is/are "+unsyncedNumber+" unsynced heath record(s), you would lose them after logging out. To sync them connect to internet.\n\nAre you sure you want to logout?")
                    .setPositiveButton("No & Sync Now", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
                            SQLiteDatabase healthDb = mDbHelper.getWritableDatabase();
                            String searchQuery = "SELECT  * FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
                            Cursor cursor = healthDb.rawQuery(searchQuery, null);
                            cursor.moveToLast();
                            for (int i = 1; i < unsyncedNumber; i++) {
                                cursor.moveToPrevious();
                            }
                            uploadChanges(abc, cursor);
                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharedPreferences sharedPref = abc.getSharedPreferences("logininfo", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.clear();
                            editor.commit();
                            HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
                            SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
                            String deleteQuery = "DELETE FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
                            healthDb.execSQL(deleteQuery);
                            healthDb.close();
                            getActivity().finish();
                            startActivity(new Intent(abc, MainActivity.class));
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }

}
