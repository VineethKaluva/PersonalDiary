package com.example.anchary.personaldiary;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Locale;

        import android.app.AlarmManager;
        import android.app.AlertDialog;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.PendingIntent;
        import android.app.DatePickerDialog.OnDateSetListener;
        import android.content.ContentValues;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.text.InputType;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.Window;
        import android.view.View.OnClickListener;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemLongClickListener;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageButton;
        import android.widget.ListView;
        import android.widget.TimePicker;
        import android.widget.Toast;

public class ReminderActivity extends AppCompatActivity {
    ImageButton add_reminder;
    ListView lv;

    static SQLiteDatabase db=null;

    String[] strcontent,strdate,strtime;
    String[] strimp;
    final static int RQS_1 = 1;


    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.remainder);

        DBHelper dbh=new DBHelper(this, "personaldiary.db", null, 1);
        db=dbh.getWritableDatabase();

        // listDisplay();

        add_reminder=(ImageButton) findViewById(R.id.floatingActionButton3);
        lv=(ListView) findViewById(R.id.reminder_listView);
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


        add_reminder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                onCustomDialogBox();

            }
        });

    }

    private void onAlertBox(final int pos) {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder.setMessage("Delete this remainder?")
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

    protected void onDeleteOption(int pos) {
        // TODO Auto-generated method stub

        int r=db.delete("remainder", "content='" + strcontent[pos]+"' and rdate='"+strdate[pos]+"' and rtime='"+strtime[pos]+"'", null);

        if(r>0)
        {
            Toast.makeText(getApplicationContext(), "Deleted...", Toast.LENGTH_SHORT).show();
            listDisplay();
        }

    }

    private void onCustomDialogBox() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        final Dialog dialog = new Dialog(ReminderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_remainder);

        final EditText dialog_content = (EditText)dialog.findViewById(R.id.etcontent);
        final EditText dialog_date = (EditText)dialog.findViewById(R.id.date_in_dialog);

        final TimePicker pickerTime=(TimePicker) dialog.findViewById(R.id.timePicker_dialog);

//pickerTime.setIs24HourView(true);
        final CheckBox chbox=(CheckBox) dialog.findViewById(R.id.checkBox_Imp);
        Button button = (Button)dialog.findViewById(R.id.remainder_submit);

////////////////////////getting date from date picker///////////////////////////////////////////////////////

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dialog_date.setInputType(InputType.TYPE_NULL);
        dialog_date.requestFocus();
        dialog_date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                fromDatePickerDialog.show();
            }
        });

        final Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dialog_date.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        ///////////////////////////////////////////////////////

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!dialog_content.getText().equals("")) {
                    Calendar current = Calendar.getInstance();
                    addRemainderToDatabase(dialog_content, chbox, dialog_date, pickerTime);
                    Calendar cal = Calendar.getInstance();
                    int imp = 0;
                    cal.set(fromDatePickerDialog.getDatePicker().getYear(),
                            fromDatePickerDialog.getDatePicker().getMonth(),
                            fromDatePickerDialog.getDatePicker().getDayOfMonth(),
                            pickerTime.getCurrentHour(),
                            pickerTime.getCurrentMinute(),
                            00);

                    if (cal.compareTo(current) <= 0) {
                        //The set Date/Time already passed
                        Toast.makeText(getApplicationContext(), "Invalid Date/Time", Toast.LENGTH_LONG).show();
                    } else {
                        if (chbox.isChecked()) {
                            imp = 1;
                        }
                        setAlarm(cal, dialog_content, imp);
                    }

                    dialog.dismiss();
                }else{
                    dialog_content.setError("Title should not be empty");
                }

            }
        });

        dialog.show();

    }
    ///////////////////////setting notification////////////////////////////////////
    private void setAlarm(Calendar targetCal, EditText dialog_content,int imp){

        Toast.makeText(getApplicationContext(),"Alarm is set@ " + targetCal.getTime() , Toast.LENGTH_LONG).show();

        Intent intent1 = new Intent(getBaseContext(), AlarmReceiver.class);
        intent1.putExtra("row_id", 1);
        intent1.putExtra("text",dialog_content.getText().toString());
        intent1.putExtra("imp",imp+"");
        final int alarmId = (int) System.currentTimeMillis();

        PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(), alarmId, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(), sender);

    }
/////////////////////////////////////////////////////////////////////////////////

    protected void addRemainderToDatabase(EditText dialog_content, CheckBox chbox, EditText dialog_date, TimePicker tp) {
        // TODO Auto-generated method stub

        int imp=0;
        // TODO Auto-generated method stub

        if(chbox.isChecked())
        {	imp=1;
        }

        //Toast.makeText(getApplicationContext(), "imp=="+imp, 2000).show();

        String rcontent=dialog_content.getText().toString();

        String rdate=dialog_date.getText().toString();


        int hour = tp.getCurrentHour();
        int min = tp.getCurrentMinute();

        //Toast.makeText(getApplicationContext(), "time=="+hour+":"+min, 2000).show();


        String rtime=""+hour+":"+min;



        ContentValues cv=new ContentValues();
        cv.put("content",rcontent);
        cv.put("imp", imp);
        cv.put("rdate", rdate);
        cv.put("rtime", rtime);
        long r=db.insert("remainder", null , cv);
        if (r==-1) {
            Toast.makeText(getApplicationContext(), "unsuccessfull",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Added successfull", Toast.LENGTH_LONG).show();
            listDisplay();
        }

    }

    private void show() {
        // TODO Auto-generated method stub
        Cursor res=db.rawQuery("select * from remainder",null);

        if (res.moveToFirst())
        {
            do{
                String scontent=res.getString(0);
                String simp=res.getInt(1)+"";
                String sdate=res.getString(2);
                String stime=res.getString(3);
            }while(res.moveToNext());
        }
    }

    ////////////////////list display////////////////////////
    private void listDisplay() {
        // TODO Auto-generated method stub

        ArrayList Acontent=new ArrayList();
        ArrayList Adate=new ArrayList();
        ArrayList Atime=new ArrayList();
        ArrayList Aimp=new ArrayList();




        Cursor res=db.rawQuery("select * from remainder",null);

        if (res.moveToFirst())
        {
            do{
                String scontent=res.getString(0);
                String simp=res.getInt(1)+"";
                String sdate=res.getString(2);
                String stime=res.getString(3);

                Acontent.add(scontent);
                Aimp.add(simp);
                Adate.add(sdate);
                Atime.add(stime);
            }while(res.moveToNext());
        }

        strcontent = new String[Acontent.size()];
        strcontent = (String[]) Acontent.toArray(strcontent);

        strimp = new String[Aimp.size()];
        strimp = (String[]) Aimp.toArray(strimp);

        strdate = new String[Adate.size()];
        strdate = (String[]) Adate.toArray(strdate);

        strtime = new String[Atime.size()];
        strtime = (String[]) Atime.toArray(strtime);

        // Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
        CustomListRemainder cl=new CustomListRemainder(ReminderActivity.this,strcontent,strimp,strdate,strtime);
        lv.setAdapter(cl);


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

