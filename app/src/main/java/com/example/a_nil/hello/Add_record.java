package com.example.a_nil.hello;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Add_record extends AppCompatActivity {
    String username=null;
    String name=null;
    String bloodgroup=null;
    String DOE;
    int age;
    Values value=null;
    Context c=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        SharedPreferences sharedPref=this.getSharedPreferences("logininfo", this.MODE_PRIVATE);
        username=sharedPref.getString("username",null);
        name=sharedPref.getString("name",null);
        bloodgroup=sharedPref.getString("bloodgroup",null);
        String dob_s=sharedPref.getString("dob",null);
        c=this;
        SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
        Date dob=null;
        try {
            dob=simple.parse(dob_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date =new Date();
        DOE=simple.format(date);
        age=date.getYear()-dob.getYear();
        Button submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseOperation datasubmit = new DatabaseOperation();
                datasubmit.execute();
            }
        });
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
        String martstatusValue;

        public Values(){
            EditText height=(EditText)findViewById(R.id.editheight);
            EditText weight=(EditText)findViewById(R.id.editweight);
            EditText bloodpress=(EditText)findViewById(R.id.editbloodpressure);
            EditText vision=(EditText)findViewById(R.id.editvision);
            EditText haemo=(EditText)findViewById(R.id.edithaemoglobin);
            EditText bloodsugar=(EditText)findViewById(R.id.editbloodsugar);
            EditText throxine=(EditText)findViewById(R.id.edithyroid);

            RadioGroup martstatus=(RadioGroup) findViewById(R.id.radioGroup);
            RadioButton selected=(RadioButton)findViewById(martstatus.getCheckedRadioButtonId());
            nameValue=name;
            usernameValue=username;
            heightValue=height.getText().toString();
            weightValue=weight.getText().toString();
            bloodpressValue=bloodpress.getText().toString();
            visionValue=vision.getText().toString();
            haemoValue=haemo.getText().toString();
            bloodsugarValue=bloodsugar.getText().toString();
            thyroxineValue=throxine.getText().toString();
            martstatusValue=selected.getText().toString();
        }



    }

    public class DatabaseOperation extends AsyncTask<Void, Void, Void> {
        String jsonStr=null;
        protected Void doInBackground(Void... voids) {
            HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
            SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
            value=new Values();
            ContentValues values = new ContentValues();
            values.put(HealthFormContract.HealthEntry.COLUMN_NAME_NAME, value.nameValue);
            values.put(HealthFormContract.HealthEntry.COLUMN_NAME_DOE,DOE);
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
            healthDb.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            //PostJson postJson=new PostJson();
            RequestQueue queue= Volley.newRequestQueue(c);
            String url ="http://aarogya.6te.net/aarogya/dbpost.php";
            //String url="http://collegare.eu5.org/post.php";
            //Log.e("no","nasdof");
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.e("im", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volleyerror", error.toString());

                }
            })
            {protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    //params.put("JSON",jsonStr);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_NAME, value.nameValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_DOE,DOE);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_AGE,""+age);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME, value.usernameValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT, value.heightValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT, value.weightValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODGROUP, bloodgroup);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR, value.bloodsugarValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE, value.bloodpressValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN, value.haemoValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS, value.martstatusValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID, value.thyroxineValue);
                    params.put(HealthFormContract.HealthEntry.COLUMN_NAME_VISION, value.visionValue);
                    //params.put("id","2");
                    return params;
                }};
            queue.add(stringRequest);

// Add the request to the RequestQueue.
            //queue.add(stringRequest);
            // postJson.execute(jsonStr);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
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
}
