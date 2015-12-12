package com.example.a_nil.hello;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnlogin;
    TextView etrigister;
    EditText eusername,epassward;
    TextView set=null;
    int respons;
    Context c=null;
    JSONObject json=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /// Intent healthrecord=new Intent(this,Healthrecorder.class);
        //startActivity(healthrecord);
        //this.finish();
        setContentView(R.layout.activity_main);

        c=this;
        SharedPreferences sharedPref=c.getSharedPreferences("logininfo",c.MODE_PRIVATE);
        boolean loggedin=sharedPref.getBoolean("loggedin",false);
        if(loggedin){
            Intent home=new Intent(this,Welcome.class);
            startActivity(home);
            this.finish();
        }

        //Intent intent=new Intent(this,Add_record.class);
        //startActivity(intent);
        set=(TextView)findViewById(R.id.textView5);
        eusername = (EditText) findViewById(R.id.edusername);
        epassward = (EditText) findViewById(R.id.edpassward);
        etrigister = (TextView) findViewById(R.id.register);
        btnlogin = (Button) findViewById(R.id.loginbtn);
        btnlogin.setOnClickListener(this);
        etrigister.setOnClickListener(this);
    }
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.loginbtn:
                {String username=eusername.getText().toString();
                    String password=epassward.getText().toString();

                    if (isPasswordValid(password)) {
                        if(!TextUtils.isEmpty(username)&&isUserValid(username)){
                            //continue the process
                            login(username,password);
                            break;
                        }
                        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)) {
                            set.setText("Username & password is Required");
                            Toast.makeText(this,"Username & password is Required",Toast.LENGTH_LONG);
                        } else if (!isUserValid(username)) {
                            set.setText("Invalid Username");
                            Toast.makeText(this, "Invalid Username", Toast.LENGTH_LONG);
                        }
                    }
                    else if(!TextUtils.isEmpty(password)&&!isPasswordValid(password))
                    {
                        set.setText("Invalid Password");
                        Toast.makeText(this, "Invalid Password", Toast.LENGTH_LONG);
                    }
                    this.finish();
                    break;

                    // Check for a valid email address.
                }
                case R.id.register:
                {
                    Intent intent=new Intent(this,Register.class);
                    startActivity(intent);
                    break;
                }
            }

        }
    private boolean isUserValid(String username) {
        //TODO: Replace this with your own logic
        return username.length()>3;//password length is minimum 4
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length()>=6 ;//password length is minimum 6
    }
    ProgressDialog dialog= null;
    void login(final String username, final String password){
        String url="http://aarogya.6te.net/aarogya/login.php";//url to post login request
        dialog=new ProgressDialog(c);
        dialog.setMessage("Verifying Credentials");
        dialog.setCancelable(false);
        dialog.show();
        RequestQueue queue= Volley.newRequestQueue(c);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            Log.e("response", response);
                            json=new JSONObject(response);
                            respons=json.getInt("status");
                            Log.e("status", "" + respons);
                            if (respons==1){
                                set.setText("Please wait, loading your data");
                                Toast.makeText(c, "Please wait, loading your data", Toast.LENGTH_LONG);
                                dialog.setMessage("Loading your data");
                                try {
                                    updatePreferences(username);
                                    Createdatabase createdatabase=new Createdatabase();
                                    createdatabase.execute(username);



                                } catch (JSONException e) {
                                    set.setText("Some problem occured. Try later");
                                    Toast.makeText(c, "Some problem occured. Try later", Toast.LENGTH_LONG);
                                    dialog.hide();
                                }
                            }
                            else set.setText("Invalid Credentials");
                            Toast.makeText(c, "Invalid Credentials", Toast.LENGTH_LONG);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.hide();
                            set.setText("Connectivity issue");
                            Toast.makeText(c, "Connectivity issue", Toast.LENGTH_LONG);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volleyerror",error.toString());
                dialog.hide();
                set.setText("Connectivity Error! Please retry and check your internet connection");
                Toast.makeText(c, "Connectivity Error! Please retry and check your internet connection", Toast.LENGTH_LONG);

            }
        })
        {protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password",password);
                return params;
            }};
        queue.add(stringRequest);
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
    void updatePreferences(String username) throws JSONException {
        SharedPreferences sharedPref=c.getSharedPreferences("logininfo",c.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString("username",username);//// TODO: add all entries
        editor.putBoolean("loggedin", true);
        JSONObject detail = json.getJSONObject("detail");
        String name=detail.getString("name");
        String dob=detail.getString("dob");
        String gender=detail.getString("gender");
        String bloodgroup=detail.getString("bloodgroup");
        String email=detail.getString("email");
        String mob=detail.getString("mob");
        editor.putString("name",name );
        editor.putString("dob",dob);
        editor.putString("gender",gender );
        editor.putString("bloodgroup",bloodgroup );
        editor.putString("email",email );
        editor.putString("mob",mob );
        editor.commit();
    }

    protected class Createdatabase extends AsyncTask<String,Void,Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            //// TODO: add all json related value& work;
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
            HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
            SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
            try {
                JSONObject data = new JSONObject(jsonStr);
                JSONArray dataarray=data.getJSONArray("detail");
                for (int i=0;i<dataarray.length();i++){
                    JSONObject dataJ=dataarray.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_DOE,dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_DOE));//TODO:do similar work
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

        @Override
        protected void onPostExecute(Integer integer) {

            super.onPostExecute(integer);
            dialog.hide();
            Intent intent=new Intent(c,Welcome.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
}
