package com.example.a_nil.hello;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

public class Welcome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wlcome);
        CardView cardViewRecorder=(CardView)findViewById(R.id.card_view0);
        final Context c=this;
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
                Intent newsbuzz = new Intent(c, listtry.class);
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
                SharedPreferences sharedPref = this.getSharedPreferences("logininfo", this.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", null);
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
                Cursor c = healthDb.rawQuery(deleteQuery, null);
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
                return true;
            }
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
