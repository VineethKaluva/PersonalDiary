package com.example.anchary.personaldiary;


import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.Date;
        import java.util.Locale;


        import android.app.Activity;
        import android.app.AlertDialog;
        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.DatePickerDialog.OnDateSetListener;
        import android.content.ActivityNotFoundException;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.speech.RecognizerIntent;
        import android.support.design.widget.FloatingActionButton;
        import android.support.v7.app.AppCompatActivity;
        import android.text.InputType;
        import android.view.View;
        import android.view.Window;
        import android.view.View.OnClickListener;
        import android.widget.*;

public class Notes2 extends AppCompatActivity {
    EditText date,title,desc;

    SQLiteDatabase db;
    String dt;
    String tt ,des;
    Calendar c;
    ImageButton ImgBtnSpeak;
    String stitle,sdate,sdesc;
    String intitle,indate,indesc;
    FloatingActionButton fsave,fspeak;


    private DatePickerDialog DatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private final int REQ_CODE_SPEECH_INPUT = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);

        c = Calendar.getInstance();

        date = (EditText) findViewById(R.id.editText);
        title = (EditText) findViewById(R.id.editText2);
        desc = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        fsave = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fspeak = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        db = openOrCreateDatabase("privatediary1", MODE_PRIVATE, null);

        db.execSQL("create table if not exists diary(date varchar, title varchar, desc varchar);");


        Intent in = getIntent();
        intitle = in.getStringExtra("title");
        indate = in.getStringExtra("date");
        indesc = in.getStringExtra("desc");
        title.setText(intitle);
        date.setText(indate);
        desc.setText(indesc);

        //Toast.makeText(getApplicationContext(),"date="+ dt, Toast.LENGTH_LONG).show();
        ///////// setting date through date picker/////////////

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        date.setInputType(InputType.TYPE_NULL);
        date.requestFocus();
        date.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                DatePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog = new DatePickerDialog(this, new OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //////////////////////////////////////////////////////////////////////


        fsave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(title.getText().toString().equals("")){
                    title.setError("Please enter the title");
                }else{
                    addNotesToTable();
                }
            }
        });


        //******************voice to text conversion *****************************//
        fspeak.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                promptSpeechInput();
            }
        });
    }
    /**
     * Showing google speech input dialog
     * */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),getString(R.string.speech_not_supported),Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //desc.setText(result.get(0));
                    desc.append(result.get(0)+" ");
                }
                break;
            }
        }
    }
    // ***************** adding notes to database********************//
    public void addNotesToTable() {
        // TODO Auto-generated method stub
        String dpswd;
        stitle=title.getText().toString();
        sdesc=desc.getText().toString();
        sdate=date.getText().toString();

        int r=db.delete("diary", "date='"+sdate+"' and title='"+stitle+"' ", null);
        if(r>0)
        {
            db.execSQL("insert into diary values('"+sdate+"','"+stitle+"','"+sdesc+"');");

            Toast.makeText(getApplicationContext(), "saved..", Toast.LENGTH_SHORT).show();

        }else
        {
            db.execSQL("insert into diary values('"+sdate+"','"+stitle+"','"+sdesc+"');");

            Toast.makeText(getApplicationContext(), "saved..", Toast.LENGTH_SHORT).show();

        }
        db.close();
        //  sqdb.execSQL("UPDATE pswdtable SET password='"+np+"' WHERE id='123' ;");
        Intent i=new Intent(Notes2.this,MainActivity.class);
        startActivity(i);
        finish();


    }

    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        onAlertBoxSave();
    }
    private void onAlertBoxSave() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder123 = new AlertDialog.Builder(this);

        //Setting message manually and performing action on button click
        builder123.setMessage("Do you want to save the record ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Yes' Button

                        addNotesToTable();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        Intent i=new Intent(Notes2.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    }
                });

        //Creating dialog box
        AlertDialog alert = builder123.create();
        //Setting the title manually
        alert.setTitle("Exit???");

        alert.show();
    }


}
