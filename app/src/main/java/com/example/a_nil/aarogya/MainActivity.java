package com.example.a_nil.aarogya;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import static com.example.a_nil.aarogya.R.string.Retry;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnlogin;
    TextView etrigister;
    EditText eusername, epassword;
    //TextView set=null;
    int respons;
    Context c=null;
    JSONObject json=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c=this;
        //checking whether user is logged in or not
        SharedPreferences sharedPref=this.getSharedPreferences("logininfo", MODE_PRIVATE);
        boolean loggedin=sharedPref.getBoolean("loggedin",false);
        if(loggedin){
            Intent home=new Intent(this,Welcome.class);
            startActivity(home);
            this.finish();
        }
        eusername = (EditText) findViewById(R.id.edusername);
        epassword = (EditText) findViewById(R.id.edpassward);
        etrigister = (TextView) findViewById(R.id.register);
        btnlogin = (Button) findViewById(R.id.loginbtn);
        btnlogin.setOnClickListener(this);
        etrigister.setOnClickListener(this);
    }
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.loginbtn:
                {
                    final String username=eusername.getText().toString();
                    final String password= epassword.getText().toString();
                    attemptLogin(v, username, password);
                    break;
                }
                case R.id.register:
                {
                    Intent intent=new Intent(this,Register.class);
                    startActivity(intent);
                    break;
                }
            }
        }

    private void attemptLogin(View v, final String username, final String password) {
        if (isPasswordValid(password)) {//if password is valid
            if(isUserValid(username)){//if username is not empty and is valid
                ConnectivityManager manager = (ConnectivityManager) c.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo activenetwork = manager.getActiveNetworkInfo();
                boolean isConnected = activenetwork != null && activenetwork.isConnectedOrConnecting();
                if (isConnected) {
                login(username, password);
                }
                else{
                    Snackbar.make(v, R.string.NoInternetSignin, Snackbar.LENGTH_INDEFINITE).setAction(Retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            attemptLogin(v,username,password);
                        }
                    }).show();
                }
            }
            else if (!isUserValid(username)) {
                Snackbar.make(v, R.string.invalidusernamemsg, Snackbar.LENGTH_INDEFINITE).show();
            }
        }
        else if(!isPasswordValid(password))
        {
            Snackbar.make(v,R.string.invalidpasswordmsg,Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private boolean isUserValid(String username) {
        return username.length()>=4;//password length is minimum 4
    }

    private boolean isPasswordValid(String password) {
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
                        try {
                            Log.e("response", response);
                            json=new JSONObject(response);
                            respons=json.getInt("status");
                            Log.e("status", "" + respons);
                            if (respons==1){
                                dialog.setMessage(getString(R.string.Loading_data));
                                try {
                                    updatePreferences(username);
                                    createdatabase(username);
                                } catch (JSONException e) {
                                    Snackbar.make(etrigister, R.string.ProblemOccured, Snackbar.LENGTH_INDEFINITE).show();
                                    dialog.hide();
                                }
                            }
                            else {
                                Snackbar.make(etrigister, R.string.InvalidCredentials, Snackbar.LENGTH_INDEFINITE).show();
                                dialog.hide();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.hide();
                            Snackbar.make(etrigister,R.string.ConnectivityIssue,Snackbar.LENGTH_INDEFINITE).setAction(Retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    login(username,password);
                                }
                            }).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volleyerror",error.toString());
                dialog.hide();
                Snackbar.make(etrigister,R.string.ConnectivityIssue,Snackbar.LENGTH_INDEFINITE).show();
            }
        })
        {protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
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
        //int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    void updatePreferences(String username) throws JSONException {
        SharedPreferences sharedPref=c.getSharedPreferences(getString(R.string.sharedPrefinfoName),MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString("username",username);
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
        editor.putInt("unsynced",0);
        editor.apply();
    }

    void createdatabase(String username) {
        final String url = "http://aarogya.6te.net/aarogya/fetch_hr.php?username=" + username;
        RequestQueue queue = Volley.newRequestQueue(c);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonStr) {
                        HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
                        SQLiteDatabase healthDb = mDbHelper.getWritableDatabase();
                        try {
                            JSONObject data = new JSONObject(jsonStr);
                            JSONArray dataarray = data.getJSONArray("detail");
                            for (int i = 0; i < dataarray.length(); i++) {
                                JSONObject dataJ = dataarray.getJSONObject(i);
                                ContentValues values = new ContentValues();
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_DOE, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_DOE));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_USERNAME));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID));
                                values.put(HealthFormContract.HealthEntry.COLUMN_NAME_VISION, dataJ.getString(HealthFormContract.HealthEntry.COLUMN_NAME_VISION));
                                healthDb.insert(HealthFormContract.HealthEntry.TABLE_NAME, null, values);
                                dialog.hide();
                                Intent intent = new Intent(c, Welcome.class);
                                startActivity(intent);
                                MainActivity.this.finish();
                            }
                        } catch (JSONException e) {
                            Log.e("ERROR", e.toString());
                            e.printStackTrace();
                        }

                        healthDb.close();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volleyerror", error.toString());
                dialog.hide();
                Snackbar.make(etrigister, R.string.ConnectivityIssue, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
        queue.add(stringRequest);
    }
}
