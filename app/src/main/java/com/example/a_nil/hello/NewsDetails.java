package com.example.a_nil.hello;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class NewsDetails extends AppCompatActivity {
    ImageView img;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        Intent from=getIntent();
        String title=from.getStringExtra(listtry.TITLE);
        String date=from.getStringExtra(listtry.DATE);
        String image=from.getStringExtra(listtry.IMAGE);
        String detail=from.getStringExtra(listtry.DETAIL);
        String link=from.getStringExtra(listtry.LINK);
        TextView titleV=(TextView)findViewById(R.id.titleview);
        TextView dateV=(TextView)findViewById(R.id.dateView);
        TextView detailV=(TextView)findViewById(R.id.detailView);
        TextView linkV=(TextView)findViewById(R.id.textView7);
        img = (ImageView) findViewById(R.id.imageView);
        Loadimages loadimages = new Loadimages();
        loadimages.execute(image);
        titleV.setText(title);
        dateV.setText(date);
        detailV.setText(detail);
        linkV.setClickable(true);
        String text = "<a href='"+link+"'>"+"For More details go to this link\n"+link+"</a>";
        linkV.setText(Html.fromHtml(text));
        //img.setImageDrawable(CustomAdapter.LoadImageFromWebOperations(image));



    }

    public class Loadimages extends AsyncTask<String, Void, Drawable> {
        protected Drawable doInBackground(String... imglink) {
            Drawable a = LoadImageFromWebOperations(imglink[0]);
            return a;
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
            img.setImageDrawable(drawable);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
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
