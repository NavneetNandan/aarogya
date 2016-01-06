package com.example.a_nil.aarogya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Healthrecorder extends AppCompatActivity {
    String jsonStr=null;
    String username=null;
    Context cont =null;
    TextView msg=null;
    ImageView img=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthrecorder);
        cont=this;
        SharedPreferences sharedPref=this.getSharedPreferences("logininfo", this.MODE_PRIVATE);
        username=sharedPref.getString("username",null);
        HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
        SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
        final Cursor c = healthDb.rawQuery(searchQuery, null);
        c.moveToFirst();
        int count=0;
        while (c.isAfterLast() == false){
            count++;
            c.moveToNext();

        }
        Log.e("Count",""+count);
        //if(count==0){
        msg=(TextView)findViewById(R.id.textView9);
        img=(ImageView)findViewById(R.id.imageView4);
        Resources res = getResources();
        final Button retry=(Button)findViewById(R.id.retry);
        final Drawable loading=res.getDrawable(R.drawable.loading2);
        /*ConnectivityManager manager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = manager.getActiveNetworkInfo();
        boolean isConnected = activenetwork != null && activenetwork.isConnectedOrConnecting();
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager manager = (ConnectivityManager) cont.getSystemService(cont.CONNECTIVITY_SERVICE);
                NetworkInfo activenetwork = manager.getActiveNetworkInfo();
                boolean isConnected = activenetwork != null && activenetwork.isConnectedOrConnecting();
                if(isConnected){
                    retry.setVisibility(View.INVISIBLE);
                    msg.setText("Loading your old records");
                    img.setImageDrawable(loading);
                    FetchNetwork fetchNetwork=new FetchNetwork();
                    fetchNetwork.execute();
                }
            }
        });
        /*if(isConnected){
            retry.setVisibility(View.INVISIBLE);
            msg.setText("Loading your old records");
            img.setImageDrawable(loading);
           FetchNetwork fetchNetwork=new FetchNetwork();
            fetchNetwork.execute();}
        else
        {
            retry.setVisibility(View.VISIBLE);
            msg.setText("Can't connect to Internet\nYour old records can't be fetched.");
            img.setVisibility(View.VISIBLE);
            img.setImageDrawable(res.getDrawable(R.drawable.ic_error_black_18dp));
            Log.e("Yes","me");
        }
        //}
        else{*/
            int i=0;

        c.moveToFirst();
            String[] alldates=new String[count];
        final String[] dates=new String[count];
            while (c.isAfterLast() == false){
                dates[i]=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_DOE));
                alldates[i]="Health Record of "+dates[i++];
                Log.e("date",""+alldates[i-1]);
                c.moveToNext();
            }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(cont,R.layout.list,alldates);
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);
        final Intent intent = new Intent(cont,Recorder_View.class);
        //final JSONArray finalInfo = info;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//when a news is clicked in list
                intent.putExtra("doe", dates[i]);
                startActivity(intent);
            }
        });
            //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list,alldates);
            //listView.setAdapter(adapter);
        //}


        Button addNew=(Button)findViewById(R.id.button);
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addnew=new Intent(cont,Add_record.class);
                startActivity(addnew);
            }
        });
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addnew=new Intent(cont,Add_record.class);
                startActivity(addnew);

            }
        });



    }
    public class FetchNetwork extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... net) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            jsonStr = null;
            try {
                URL url = new URL("http://aarogya.6te.net/aarogya/fetch_hr.php?username="+username);
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
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                jsonStr = buffer.toString();
                Log.e("JSON",jsonStr);
                // System.out.println(jsonStr);
            }
            catch (IOException e){
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
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
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            String date=null;
            String[] alldates=null;
            JSONArray info=null;
            /*try {
                JSONObject db=new JSONObject(jsonStr);
                info=db.getJSONArray("detail");
                alldates=new String[info.length()];
                for(int i=0;i<info.length();i++){
                    JSONObject data=info.getJSONObject(i);
                    alldates[i]="Health Record of "+data.getString("doe");
                }*/
                img.setVisibility(View.INVISIBLE);
                msg.setVisibility(View.INVISIBLE);

                ArrayAdapter<String> adapter=new ArrayAdapter<String>(cont,R.layout.list,alldates);
                ListView listView=(ListView)findViewById(R.id.listView);
                listView.setAdapter(adapter);
                final Intent intent = new Intent(cont,Recorder_View.class);
                final JSONArray finalInfo = info;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//when a news is clicked in list
                        String data=null;
                        try {
                            JSONObject temp= finalInfo.getJSONObject(i);
                            data=temp.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra("json",data);
                        startActivity(intent);
                    }
                });

            /*} catch (JSONException e) {
                e.printStackTrace();
            }*/
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_healthrecorder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_SignOut: {
                SharedPreferences sharedPref = this.getSharedPreferences("logininfo", this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", null);//// TODO: add all entries
                editor.putBoolean("loggedin", false);
                editor.putString("name", null);
                editor.putString("dob", null);
                editor.putString("gender", null);
                editor.putString("bloodgroup", null);
                editor.putString("email", null);
                editor.putString("mob", null);
                editor.commit();
                HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
                SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
                String deleteQuery = "DELETE FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
                healthDb.execSQL(deleteQuery);
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
