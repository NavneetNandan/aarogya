package com.example.a_nil.hello;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class listtry extends AppCompatActivity {
    //ArrayAdapter listadapter;
    Context c=null;
    public static Drawable LoadImageFromWebOperations(String url) {//function for image download and return a drawable object
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");

            return d;
        } catch (Exception e) {
            Log.e("loading imag", e.toString());
            return null;
        }
    }
    ProgressDialog progressDialog=null;

    String[] image = null;
    public ArrayList<ListData> lData = new ArrayList<ListData>();//arraylist of listdata class type
    CustomAdapter adapter = null;//declaring customadapter for global use
    public final static String TITLE = "com.aarogya.health.aarogya.listtry.titlemessage";//putting name for extras
    public final static String DATE ="com.aarogya.health.aarogya.listtry.date";
    public final static String IMAGE ="com.aarogya.health.aarogya.listtry.image";
    public final static String DETAIL="com.aarogya.health.aarogya.listtry.detail";
    public final static String LINK="com.aarogya.health.aarogya.listtry.link";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listtry);
        c=this;
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activenetwork = manager.getActiveNetworkInfo();
        boolean isConnected = activenetwork != null && activenetwork.isConnectedOrConnecting();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Health News.\nPlease Wait...");
        progressDialog.setCancelable(false);
        if (isConnected) {
            FetchNetwork fetch = new FetchNetwork();
            progressDialog.show();
            fetch.execute();//starting download of data
        }
        else {
            Connectivityissue issuedialog=new Connectivityissue();
            issuedialog.show(getFragmentManager(),"issue");
        }
            ListView list = (ListView) findViewById(R.id.listView);
            listtry tryo = this;//getting context
            Resources res = getResources();
            //Log.e("id", "" + res.getIdentifier("com.aarogya.health.aarogya:layout/activity_home", null, null));
            adapter = new CustomAdapter(this, lData, res);

            final Intent intent = new Intent(this, NewsDetails.class);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {//when a news is clicked in list
                    intent.putExtra(TITLE, lData.get(i).getTitle());
                    intent.putExtra(DATE, lData.get(i).getDate());
                    intent.putExtra(IMAGE, image[i]);
                    intent.putExtra(DETAIL, lData.get(i).getDescription());
                    intent.putExtra(LINK, lData.get(i).getLink());
                    startActivity(intent);
                }
            });
            //Log.e("set", "list");
            list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listtry, menu);
        return true;
    }
    public class Connectivityissue extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Active internet connection required!")
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ConnectivityManager manager = (ConnectivityManager) c.getSystemService(c.CONNECTIVITY_SERVICE);
                            NetworkInfo activenetwork = manager.getActiveNetworkInfo();
                            boolean isConnected = activenetwork != null && activenetwork.isConnectedOrConnecting();
                            if (isConnected) {
                                FetchNetwork fetch = new FetchNetwork();
                                progressDialog.show();
                                fetch.execute();//starting download of data
                            }
                            else {
                                Connectivityissue issuedialog=new Connectivityissue();
                                issuedialog.show(getFragmentManager(),"issue");
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            Intent back= new Intent(c,Welcome.class);
                            startActivity(back);
                            listtry.this.finish();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            FetchNetwork fetch =new FetchNetwork();
            fetch.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchNetwork extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... net) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;
            try {
                URL url = new URL("http://www.aarogya.6te.net/aarogya/get.data.php?get_json");
                urlConnection =(HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                //urlConnection.setRequestMethod();
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
            try {
                parseTitles(jsonStr);
                return 1;
            }
            catch (JSONException e)
            {
                Log.e("ERROR IN PARSING"," JSON",e);
            }
            return null;
        }

        protected void onPostExecute(Integer x) {
            Log.e("i m", "here");
            adapter.notifyDataSetChanged();
            Log.e("notify", "adapter");
            progressDialog.hide();
            Loadimages loadimg = new Loadimages();
            loadimg.execute();

        }
    }

    public class Loadimages extends AsyncTask<Void, Void, Integer> {
        protected Integer doInBackground(Void... net) {
            ListData temp = new ListData();
            Log.e("Image", "loading");
            if (image.length != 0)
                for (int i = 0; i < image.length; i++) {
                    temp = lData.get(i);
                    temp.setImage(LoadImageFromWebOperations(image[i]));
                    lData.set(i, temp);
                    Log.e("Loaded", "" + i);
                }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            adapter.notifyDataSetChanged();

        }
    }
    public void parseTitles(String json_Str) throws JSONException
    {
        Log.e("Json", json_Str);
        /*JSONObject page=new JSONObject(json_Str);
        JSONObject result=page.getJSONObject("r");
        JSONArray docs=result.getJSONArray("docs");
*/
        JSONObject page = new JSONObject(json_Str);
        JSONArray result = page.getJSONArray("item");
        Log.e("Json", "Starting");
        String[] title = new String[result.length()];
        String[] date = new String[result.length()];
        String[] source = new String[result.length()];
        image = new String[result.length()];
        String[] description = new String[result.length()];
        String[] link = new String[result.length()];
        for (int i = 0; i < result.length(); i++)
        {
            JSONObject article = result.getJSONObject(i);
            JSONObject tit = article.getJSONObject("title");//Tue, 03 Nov 2015 01:11:39 GMT
            title[i] = tit.getString("0");
            JSONObject dte = article.getJSONObject("date");
            String dateStr = dte.getString("0");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd MMM yyyy h:m:s z");
            ParsePosition parsePosition = new ParsePosition(0);
            Date dat = simpleDateFormat.parse(dateStr, parsePosition);
            SimpleDateFormat simple = new SimpleDateFormat("hh:mm dd MMM yy");
            date[i] = simple.format(dat);
            source[i] = "Source:" + article.optString("source");
            //source[i]=sorc.optString("0");
            Log.e("i", "" + i);
            JSONObject img = article.optJSONObject("image");
            if (img != null) image[i] = img.optString("0");
            JSONObject dscr = article.optJSONObject("description");
            if (dscr != null) description[i] = dscr.getString("0");
            JSONObject lnk = article.getJSONObject("link");
            link[i] = lnk.getString("0");


        }
        ListData[] temp = new ListData[result.length()];

        for (int i = 0; i < result.length(); i++) {
            temp[i] = new ListData();
            temp[i].setTitle(title[i]);
            temp[i].setTitle(title[i]);
            temp[i].setDate(date[i]);
            temp[i].setSource(source[i]);
            //temp[i].setImage(image[i]);
            temp[i].setDescription(description[i]);
            temp[i].setLink(link[i]);
            lData.add(temp[i]);
            Log.e("" + i, lData.get(i).getTitle());
        }

    }
}
