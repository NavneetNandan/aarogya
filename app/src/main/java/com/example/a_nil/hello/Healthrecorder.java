package com.example.a_nil.hello;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Healthrecorder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthrecorder);
        String username=null;
        //String date=null;
        String JsonStr=null;
        String[] alldates=null;
        JSONArray info=null;
        try {
            JSONObject db=new JSONObject(JsonStr);
            JSONObject user=db.getJSONObject(username);
            info=user.getJSONArray("info");
            alldates=new String[info.length()];
            for(int i=0;i<info.length();i++){
                JSONObject data=info.getJSONObject(i);
                alldates[i]=data.getString("date");
            }
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.list,alldates);
            ListView listView=(ListView)findViewById(R.id.listView);
            listView.setAdapter(adapter);
            final Intent intent = new Intent(this,Recorder_View.class);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_healthrecorder, menu);
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
