package com.example.a_nil.hello;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    Context abc=null;
    TextView set=null;
    static TextView date=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigister);
        abc=this;
        set=(TextView)findViewById(R.id.error);
        date=(TextView)findViewById(R.id.editText);
        Button register=(Button)findViewById(R.id.registerbtn);

        Log.e("id",""+R.id.registerbtn);


    }
    public void register(View view){
        EditText name=(EditText)findViewById(R.id.edname);
        final String name_str=name.getText().toString();
        EditText username=(EditText)findViewById(R.id.edname);
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

            if (isPasswordValid(password_str)) {
                if(!TextUtils.isEmpty(username_str)&&isUserValid(username_str)&&!TextUtils.isEmpty(dob_str)&&!TextUtils.isEmpty(bd_str)&&!TextUtils.isEmpty(gender_str)&&!TextUtils.isEmpty(email_str)&&!TextUtils.isEmpty(phoneno_str)){
                    //continue the process
                    RequestQueue queue= Volley.newRequestQueue(abc);
                    String url ="http://aarogya.6te.net/aarogya/dbpost.php";//url given
// Request a string response from the provided URL.
                    Log.e("click","ok");
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // if(response.equals(1))
                                    startActivity(new Intent(abc,wlcome.class));
                                    Toast.makeText(abc,"Registration successful",Toast.LENGTH_LONG).show();
                                    Log.e("response","made");

                                    //set.setText("Response is: "+ response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("response",error.toString());
                            Toast.makeText(abc,"Some connectivity problem, please retry.",Toast.LENGTH_LONG).show();
                            //set.setText("That didn't work!");
                        }
                    })
                    {protected Map<String,String> getParams(){
                            Log.e("taking","params");
                            Map<String,String> params = new HashMap<String, String>();
                            params.put("name",name_str);
                        params.put("username",username_str);//"name",name_string
                        params.put("dob",dob_str);
                        params.put("password",password_str);
                        params.put("email",email_str);
                        params.put("mob",phoneno_str);
                        params.put("gender",gender_str);
                        params.put("bloodgroup",bd_str);
                            return params;
                        }};
                    queue.add(stringRequest);

                }
                if (TextUtils.isEmpty(username_str)) {
                    set.setText("Username is Required");
                } else if (!isUserValid(username_str)) {
                    set.setText("Invalid Username");
                }
                else {
                    set.setText("Please fill all fields");
                }
            }
            else if(!TextUtils.isEmpty(password_str)&&!isPasswordValid(password_str))
            {
                set.setText("Invalid Password");
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
            // Do something with the date chosen by the user
            DatePicker datePicker= view;
            Date ate=new Date(datePicker.getYear()-1900,datePicker.getMonth(),datePicker.getDayOfMonth());
            SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
            date.setText(simple.format(ate));
            //String dat=new String();
            //DateFormat.format(dat,ate);
            Log.e("ok", "" + "");
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
        //TODO: Replace this with your own logic
        return username.length()>3;//username length is minimum 4
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
