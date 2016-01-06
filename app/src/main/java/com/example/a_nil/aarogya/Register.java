package com.example.a_nil.aarogya;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Context abc=null;
    //TextView set=null;
    static TextView date=null;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);
        abc=this;
        date=(TextView)findViewById(R.id.editText);
        activity=this;
        Log.e("id",""+R.id.registerbtn);

    }
    public void register(final View view){
        EditText name=(EditText)findViewById(R.id.edname);
        final String name_str=name.getText().toString();
        EditText username=(EditText)findViewById(R.id.edusername);
        final String username_str=username.getText().toString();
        EditText password=(EditText)findViewById(R.id.edpassword);
        final String password_str=password.getText().toString();
        EditText date=(EditText)findViewById(R.id.editText);
        final String dob_str=date.getText().toString();
        EditText bd=(EditText)findViewById(R.id.edbd);
        final String bd_str=bd.getText().toString();
        RadioGroup gender=(RadioGroup)findViewById(R.id.radioGroup);
        RadioButton selected=(RadioButton)findViewById(gender.getCheckedRadioButtonId());
        final String gender_str=selected.getText().toString();
        EditText email=(EditText)findViewById(R.id.edemail);
        final String email_str=email.getText().toString();
        EditText phoneno=(EditText)findViewById(R.id.edphonenumber);
        final String phoneno_str=phoneno.getText().toString();
        ConnectivityManager manager = (ConnectivityManager) abc.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = manager.getActiveNetworkInfo();
        boolean isConnected = activenetwork != null && activenetwork.isConnectedOrConnecting();
            if (isPasswordValid(password_str)) {
                if(isUserValid(username_str)&&!TextUtils.isEmpty(dob_str)&&!TextUtils.isEmpty(bd_str)&&!TextUtils.isEmpty(gender_str)&&!TextUtils.isEmpty(email_str)&&!TextUtils.isEmpty(phoneno_str)) {
                    //continue the process
                    if (isConnected) {
                        RequestQueue queue = Volley.newRequestQueue(abc);
                        String url = "http://aarogya.6te.net/aarogya/registration.php";//url given
// Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.e("Response", response);
                                        if (response.equals("{\"status\":1}")) {
                                            //updates shared preferences with user information
                                            SharedPreferences sharedPref = abc.getSharedPreferences(getString(R.string.sharedPrefinfoName), MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putBoolean("loggedin", true);
                                            editor.putString("name", name_str);
                                            editor.putString("username", username_str);
                                            editor.putString("dob", dob_str);
                                            editor.putString("password", password_str);
                                            editor.putString("email", email_str);
                                            editor.putString("mob", phoneno_str);
                                            editor.putString("gender", gender_str);
                                            editor.putString("bloodgroup", bd_str);
                                            editor.apply();
                                            Toast.makeText(abc, "Registration successful", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(abc, Welcome.class));//start home activity
                                            activity.finish();
                                        } else {
                                            Snackbar.make(view, R.string.sameUsername, Snackbar.LENGTH_INDEFINITE).show();
                                        }
                                        //set.setText("Response is: "+ response);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar.make(view,R.string.ConnectivityIssue,Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        register(view);
                                    }
                                }).show();
                            }
                        }) {
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("name", name_str);
                                params.put("username", username_str);
                                params.put("dob", dob_str);
                                params.put("password", password_str);
                                params.put("email", email_str);
                                params.put("mob", phoneno_str);
                                params.put("gender", gender_str);
                                params.put("bloodgroup", bd_str);
                                return params;
                            }
                        };
                        queue.add(stringRequest);

                    }  else {
                        Snackbar.make(view,R.string.noInternetRegister,Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                register(view);
                            }
                        }).show();

                    }
                }
                else {
                    if (!isUserValid(username_str)) {
                        Snackbar.make(view, R.string.invalid_username,Snackbar.LENGTH_INDEFINITE).show();
                    } else {
                        Snackbar.make(view, R.string.fill_all_fields,Snackbar.LENGTH_INDEFINITE).show();
                    }
                }
        }
            else
            {
                Snackbar.make(view,R.string.invalid_password,Snackbar.LENGTH_INDEFINITE).show();
            }
    }


    public void datepick(View view){

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");

    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            DatePicker datePicker= view;
            SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal=Calendar.getInstance();
            cal.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
            date.setText(simple.format(cal));
        }
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        RadioButton male=(RadioButton)findViewById(R.id.male);
        RadioButton female=(RadioButton)findViewById(R.id.female);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male:
                if (checked)
                    female.setChecked(false);
                break;
            case R.id.female:
                if (checked)
                    male.setChecked(false);
                break;
        }
    }
    private boolean isUserValid(String username) {
        return username.length()>3;//username length is minimum 4
    }

    private boolean isPasswordValid(String password) {
        return password.length()>=6 ;//password length is minimum 6
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rigister, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
