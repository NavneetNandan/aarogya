package com.example.a_nil.hello;

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
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnlogin;
    Button etrigister;
    EditText eusername,epassward;
    TextView set=null;
    int respons;
    Context c=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,Add_record.class);
        startActivity(intent);
        c=this;
        set=(TextView)findViewById(R.id.textView5);
        eusername = (EditText) findViewById(R.id.edusername);
        epassward = (EditText) findViewById(R.id.edpassward);
        etrigister = (Button) findViewById(R.id.etrigister);
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
                            if(loginsuccess(username,password))
                            {   set.setText("Please wait, loading your data");
                                updatePreferences(username);
                                try {
                                    Createdatabase db=new Createdatabase();
                                    db.execute(username);
                                } catch (JSONException e) {
                                    set.setText("Json Error");
                                    e.printStackTrace();
                                }
                                Intent intent=new Intent(this,wlcome.class);
                                startActivity(intent);}
                            break;
                        }
                        if (TextUtils.isEmpty(username)) {
                            set.setText("Username is Required");
                        } else if (!isUserValid(username)) {
                            set.setText("Invalid Username");
                        }
                    }
                    else if(!TextUtils.isEmpty(password)&&!isPasswordValid(password))
                    {
                        set.setText("Invalid Password");
                    }

                    // Check for a valid email address.
                }
                case R.id.etrigister:
                {
                    Intent intent=new Intent(this,Rigister.class);
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
    boolean loginsuccess(final String username, final String password){
        String url="";//url to post login request
        RequestQueue queue= Volley.newRequestQueue(c);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        if(response=="1"){
                            respons=1;
                        }else {
                            set.setText("Login failed");
                            respons=0;
                        }

                        Log.e("im", response);
                                            }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volleyerror",error.toString());
                set.setText("Connectivity Error! Please retry and check your internet connection");
                respons=0;
            }
        })
        {protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password",password);
                return params;
            }};
        queue.add(stringRequest);
        if(respons==1)
        return true;
        else
            return false;
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
    void updatePreferences(String username){
        SharedPreferences sharedPref=c.getSharedPreferences("logininfo",c.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString("username",username);//// TODO: add all entries
    }
    public class Values{
        String nameValue;
        String usernameValue;
        String heightValue;
        String weightValue;
        String bloodpressValue;
        String visionValue;
        String haemoValue;
        String bloodsugarValue;
        String thyroxineValue;
        String martstatusValue;}
    protected class Createdatabase extends AsyncTask<String,Void,Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            String data=null;//// TODO: add all json related value& work;
            JSONArray dataarray= null;
            HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
            SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
            try {
                dataarray = new JSONArray(data);
                for (int i=0;i<dataarray.length();i++){
                    JSONObject dataJ=dataarray.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_NAME, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_NAME));
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_DOE,DOE);//TODO:do similar work
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_AGE,""+age);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME, value.usernameValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT, value.heightValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT, value.weightValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODGROUP, bloodgroup);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR, value.bloodsugarValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE, value.bloodpressValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN, value.haemoValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS, value.martstatusValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID, value.thyroxineValue);
                    values.put(HealthFormContract.HealthEntry.COLUMN_NAME_VISION, value.visionValue);
                    healthDb.insert(HealthFormContract.HealthEntry.TABLE_NAME, null, values);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent back=new Intent(c,Healthrecorder.class);
            startActivity(back);
            healthDb.close();
            return 1;
        }


    }
}
