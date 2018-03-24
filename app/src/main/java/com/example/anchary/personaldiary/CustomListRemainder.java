package com.example.anchary.personaldiary;


import android.app.Activity;
        import android.graphics.Color;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

public class CustomListRemainder extends BaseAdapter{

    private final Activity mcontext;

    private final String[] strcontent,strdate,strtime,strimp;

    public CustomListRemainder(Activity mcontext,
                               String[] strcontent, String[] strimp, String[] strdate,
                               String[] strtime) {
        // TODO Auto-generated constructor stub
        this.mcontext=mcontext;
        this.strcontent=strcontent;
        this.strimp=strimp;
        this.strdate=strdate;
        this.strtime=strtime;


    }
    public int getCount() {
        // TODO Auto-generated method stub
        return strcontent.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    public View getView(int pos, View convertview, ViewGroup parent) {
        // TODO Auto-generated method stub

        View row=convertview;
        MyViewHolder2 holder;


        if(row==null)
        {
            LayoutInflater inflater=mcontext.getLayoutInflater();
            row=inflater.inflate(R.layout.customlistremainder,parent ,false);
            holder =new MyViewHolder2(row);
            row.setTag(holder);
            Log.d("message", "inflation inside if loop");
        }

        else
        { Log.e("message", "inflation outside if loop");
            holder=(MyViewHolder2) row.getTag();
        }


        holder.t1.setText(strcontent[pos]);
        holder.t2.setText(strdate[pos]+"  "+strtime[pos]);
        //	Toast.makeText(mcontext, strimp[pos]+"", 2000).show();
        if(strimp[pos].equals("1"))
        {
            //holder.img.setImageResource(R.drawable.ic_launcher);
            holder.img.setImageResource(android.R.drawable.btn_star_big_on);
            holder.t1.setTextColor(Color.RED);
        }else
        {
            holder.t1.setTextColor(Color.BLACK);
        }

        return row;
    }

}

class MyViewHolder2
{
    TextView t1;
    TextView t2;
    ImageView img;


    public MyViewHolder2(View v)
    {
        t1=(TextView) v.findViewById(R.id.textView_content);
        t2=(TextView) v.findViewById(R.id.textView_dt);
        img=(ImageView) v.findViewById(R.id.imageView_imp);
    }
}
