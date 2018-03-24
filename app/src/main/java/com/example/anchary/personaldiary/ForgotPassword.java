package com.example.anchary.personaldiary;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity{

    SQLiteDatabase sqdb;
    String pswd,email;
    EditText em;
    GMailSender sender;
    Button sendbtn;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fpassword);

        em=(EditText) findViewById(R.id.edt_fgtpswd);
        sendbtn=(Button) findViewById(R.id.fpswd_button);

        sender = new GMailSender("mailtheyogi1@gmail.com","yogi1234");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);



        sendbtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(isNetworkAvailable())
                {
                    if(sendEmail() )
                    {
                        try {

                            new MyAsyncClass().execute();

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                        }
                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Enter proper email ID", Toast.LENGTH_LONG).show();

                    }
                }else
                {
                    Toast.makeText(getApplicationContext(), "Please check to Internet Connection", Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    protected boolean sendEmail() {
        // TODO Auto-generated method stub
        sqdb=openOrCreateDatabase("pswddiary", MODE_PRIVATE, null);
        sqdb.execSQL("create table if not exists pswdtable(id varchar,password varchar,email varchar);");
        Cursor c=sqdb.rawQuery("select password,email from pswdtable where id='123';", null);
        if(c.getCount()==1)
        {
            if(c.moveToFirst())
            { pswd=c.getString(0);
                email=c.getString(1);

            }else
            {
                Toast.makeText(getApplicationContext(), "email not registered", Toast.LENGTH_LONG).show();
            }
        }

        if(em.getText().toString().equalsIgnoreCase(email))
        {
            return true;
        }

        return false;
    }
    class MyAsyncClass extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(ForgotPassword.this);
            pDialog.setMessage("Please wait...");
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... mApi) {
            try {


                //             Toast.makeText(getApplicationContext(),contactId+"   "+ displayName+"    "+address,Toast.LENGTH_LONG ).show();
                sender.sendMail("Personal Diary ","Hi,\nPassword used for the Personal Diary Application : "+pswd+"\nThanks for using Personal Diary Application","mailtheyogi1@gmail.com",email);
                // Toast.makeText(getApplicationContext(), "Email send asdfgh", Toast.LENGTH_LONG).show();


            }

            catch (Exception ex) {
                // Toast.makeText(getApplicationContext(), "Email send "+ex, Toast.LENGTH_LONG).show();

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pDialog.cancel();
            try {

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Email send in catch ", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(), "Email send", Toast.LENGTH_LONG).show();
        }
    }
}
