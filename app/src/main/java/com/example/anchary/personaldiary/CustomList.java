package com.example.anchary.personaldiary;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends BaseAdapter{

    private final Activity mcontext;

    private final String[] title,date;
    //	private final int img[];
    public CustomList(Activity mcontext,String[] title,String[] date)
    {
        this.mcontext=mcontext;
        this.title=title;
        this.date=date;
        //	this.img=img;

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int pos, View convertview, ViewGroup parent) {
        // TODO Auto-generated method stub

        View row=convertview;
        MyViewHolder holder;
		/*if (row==null)
		{
		LayoutInflater inflater=mcontext.getLayoutInflater();
	    row=inflater.inflate(R.layout.single_row,parent ,false);
		Log.d("message", "inflation inside if loop");
		}
		Log.e("message", "inflation outside if loop");
		*/


        if(row==null)
        {
            LayoutInflater inflater=mcontext.getLayoutInflater();
            row=inflater.inflate(R.layout.customlist,parent ,false);
            holder =new MyViewHolder(row);
            row.setTag(holder);
            Log.d("message", "inflation inside if loop");
        }

        else
        { Log.e("message", "inflation outside if loop");
            holder=(MyViewHolder) row.getTag();
        }

	/*	TextView t1=(TextView) row.findViewById(R.id.textView1);
		TextView t2=(TextView) row.findViewById(R.id.textView2);
		ImageView i=(ImageView) row.findViewById(R.id.imageView1);

		t1.setText(title[pos]);
		t2.setText(desc[pos]);
		i.setImageResource(img[pos]);
		*/

        holder.t1.setText(title[pos]);
        holder.t2.setText(date[pos]);
        //holder.i.setImageResource(img[pos]);
        return row;
    }

}
class MyViewHolder
{
    TextView t1;
    TextView t2;


    public MyViewHolder	(View v)
    {
        t1=(TextView) v.findViewById(R.id.textView1);
        t2=(TextView) v.findViewById(R.id.textView2);
//i=(ImageView) v.findViewById(R.id.imageView1);
    }
}

