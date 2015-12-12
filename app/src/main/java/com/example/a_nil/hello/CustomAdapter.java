package com.example.a_nil.hello;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Navneet on 26-09-2015.
 */
public class CustomAdapter extends BaseAdapter implements ListAdapter{

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListData tempValues=null;
    int i=0;


    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;
        Log.e("sizea", "" + d.size());
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public TextView text1;
        public TextView source;
        public ImageView image;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;
        Log.e("sizev", "" + data.size());

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.listview, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text);
            holder.text1=(TextView)vi.findViewById(R.id.text1);
            //holder.source = (TextView) vi.findViewById(R.id.source);
            holder.image = (ImageView) vi.findViewById(R.id.image);
            holder.source = (TextView) vi.findViewById(R.id.src);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ListData ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.text.setText(tempValues.getTitle());
            holder.text1.setText(tempValues.getDate());
            holder.source.setText(tempValues.getSource());
            //holder.source.setText(tempValues.getSource());
            try {
                holder.image.setImageDrawable(tempValues.getImage());
            } catch (NullPointerException e) {
                //do nothing
            }
            //holder.image.setImageResource(res.getIdentifier(
            //                "com.aarogya.health.aarogya:drawable/"+tempValues.getImage()
            //                ,null,null));

            /******** Set Item Click Listner for LayoutInflater for each row *******/
        }



        return vi;
    }
}