package com.example.anchary.personaldiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.MenuItem;
import android.widget.ListView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView lv;
    SQLiteDatabase sqdb;
    String[] stitle,sdate,sdesc;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Intent in=new Intent(MainActivity.this,Notes.class);
                startActivity(in);
                finish();



            }
        });


        lv=(ListView) findViewById(R.id.listView1);

        listDisplay();
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub

                onAlertBox(arg2);

                return false;
            }

        });

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                //	Intent i=new Intent(MainActivity.this, SecondActivity.class);

                Intent i=new Intent(MainActivity.this, Notes2.class);


                i.putExtra("title",stitle[arg2]);
                i.putExtra("date", sdate[arg2]);
                i.putExtra("desc",sdesc[arg2]);
                startActivity(i);
                finish();


            }
        });
    }
    private void onAlertBox(final int pos) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage("Delete this record?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onDeleteOption(pos);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Delete");
        alert.show();
    }

    protected void onDeleteOption(int arg2) {
        // TODO Auto-generated method stub
        sqdb=openOrCreateDatabase("privatediary1", MODE_PRIVATE, null);
        sqdb.execSQL("create table if not exists diary(date varchar, title varchar, desc varchar);");

        int r=sqdb.delete("diary", "title='" + stitle[arg2]+"' and date='"+sdate[arg2]+"' and desc='"+sdesc[arg2]+"'", null);
        if(r>0)
        {
            Toast.makeText(getApplicationContext(), "Deleted...", Toast.LENGTH_SHORT).show();
            listDisplay();
        }

    }
    private void listDisplay() {
        // TODO Auto-generated method stub

        ArrayList title=new ArrayList();
        ArrayList date=new ArrayList();
        ArrayList desc=new ArrayList();

        sqdb=openOrCreateDatabase("privatediary1", MODE_PRIVATE, null);

        sqdb.execSQL("create table if not exists diary(date varchar, title varchar, desc varchar);");

        Cursor c=sqdb.rawQuery("select * from diary;",null);

        if(c.moveToFirst())
        {
            do{

                date.add(c.getString(c.getColumnIndex("date")));
                title.add(c.getString(c.getColumnIndex("title")));
                desc.add(c.getString(c.getColumnIndex("desc")));


            }while (c.moveToNext());
        }


        stitle = new String[title.size()];
        stitle = (String[]) title.toArray(stitle);

        sdate = new String[date.size()];
        sdate = (String[]) date.toArray(sdate);

        sdesc = new String[desc.size()];
        sdesc = (String[]) desc.toArray(sdesc);

        // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
        CustomList cl=new CustomList(MainActivity.this,stitle,sdate);
        lv.setAdapter(cl);
        sqdb.close();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
