package com.example.a_nil.aarogya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Recorder_View extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder__view);
        Intent prev=getIntent();
        String doe=prev.getStringExtra("doe");
        SharedPreferences sharedPref=this.getSharedPreferences("logininfo", this.MODE_PRIVATE);
        String usernamevalue=sharedPref.getString("username", null);
        String namevalue=sharedPref.getString("name", null);
        String bloodgroupvalue=sharedPref.getString("bloodgroup", null);
        String dob_s=sharedPref.getString("dob", null);
        SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
        Date dob= null;
        try {
            dob = simple.parse(dob_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date =new Date();
        String DOE=simple.format(date);
        int age=date.getYear()-dob.getYear();
        HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
        SQLiteDatabase healthDb=mDbHelper.getWritableDatabase();
        String searchQuery = "SELECT  * FROM " + HealthFormContract.HealthEntry.TABLE_NAME+" WHERE "+HealthFormContract.HealthEntry.COLUMN_NAME_DOE+" = \"" +doe+"\"";
        final Cursor c = healthDb.rawQuery(searchQuery, null);
        c.moveToFirst();

        String heightValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT));
        String weightValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT));
        String bloodpressValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE));
        String visionValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_VISION));
        String haemoValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN));
        String bloodsugarValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR));
        String thyroxineValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID));
        String martstatusValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS));
        TextView height=(TextView)findViewById(R.id.editheight);
        height.setText(heightValue+" Kg");
        TextView weight=(TextView)findViewById(R.id.editweight);
        weight.setText(weightValue+" cm");
        TextView bloodgrp=(TextView)findViewById(R.id.editbloodgrp);
        bloodgrp.setText(bloodgroupvalue);
        TextView name=(TextView)findViewById(R.id.editname);
        name.setText(namevalue);
        TextView martialstatus=(TextView)findViewById(R.id.editmartialstatus);
        martialstatus.setText(martstatusValue);
        TextView haemoglobin=(TextView)findViewById(R.id.edithaemoglobin);
        haemoglobin.setText(haemoValue+" g/dl");
        TextView vision=(TextView)findViewById(R.id.editvision);
        vision.setText(visionValue);
        TextView bldpressure=(TextView)findViewById(R.id.editblood_pressure);
        bldpressure.setText(bloodpressValue);
        TextView bloodsuger=(TextView)findViewById(R.id.editbloodsugar);
        bloodsuger.setText(bloodsugarValue+" g/dl");
        TextView thyroid=(TextView)findViewById(R.id.editThyroid);
        thyroid.setText(thyroxineValue);
        TextView ageView=(TextView)findViewById(R.id.editage);
        ageView.setText(age+" years");
        /*try {
            JSONObject jsonObject=new JSONObject(jsonStr);
            String heightValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_HEIGHT));
            String weightValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_WEIGHT));
            String bloodpressValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODPRESSURE))));
            String visionValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_VISION));
            String haemoValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_HAEMOGLOBIN));
            String bloodsugarValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_BLOODSUGAR));
            String thyroxineValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_THYROID));
            String martstatusValue=c.getString(c.getColumnIndex(HealthFormContract.HealthEntry.COLUMN_NAME_MARTIALSTATUS));
            TextView height=(TextView)findViewById(R.id.editheight);
            height.setText(heightValue);
            TextView weight=(TextView)findViewById(R.id.editweight);
            weight.setText(weightValue);
            TextView bloodgrp=(TextView)findViewById(R.id.editbloodgrp);
            bloodgrp.setText(bloodgroupvalue);
            TextView name=(TextView)findViewById(R.id.editname);
            name.setText(namevalue);
            TextView martialstatus=(TextView)findViewById(R.id.editmartialstatus);
            martialstatus.setText(martstatusValue);
            TextView haemoglobin=(TextView)findViewById(R.id.edithaemoglobin);
            haemoglobin.setText(haemoValue);
            TextView vision=(TextView)findViewById(R.id.editvision);
            vision.setText(visionValue);
            TextView bldpressure=(TextView)findViewById(R.id.editblood_pressure);
            bldpressure.setText(bloodpressValue);
            TextView bloodsuger=(TextView)findViewById(R.id.editbloodsugar);
            bloodsuger.setText(bloodsugarValue);
            TextView thyroid=(TextView)findViewById(R.id.editThyroid);
            thyroid.setText(thyroxineValue);
            TextView ageView=(TextView)findViewById(R.id.editage);
            ageView.setText(""+age);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        /*HealthFormDbHelper mDbHelper = new HealthFormDbHelper(getApplicationContext());
        SQLiteDatabase healthDb=mDbHelper.getReadableDatabase();
        String searchQuery = "SELECT  * FROM " + HealthFormContract.HealthEntry.TABLE_NAME;
        Cursor cont = healthDb.rawQuery(searchQuery, null);
        cont.moveToFirst();

        String json=prev.getStringExtra("json");
        try {
            JSONObject data=new JSONObject(json);
            String date=data.getString("date");


        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recorder__view, menu);
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
