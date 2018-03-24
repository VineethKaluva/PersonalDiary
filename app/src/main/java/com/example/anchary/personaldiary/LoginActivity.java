package com.example.anchary.personaldiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class LoginActivity extends AppCompatActivity {

    EditText password;
    Button login;
    String uname,pswd;
    TextView tvhint,tvpswd,tvfgtpswd;
    String Apswd,Npswd;
    SQLiteDatabase sqdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        tvhint=(TextView) findViewById(R.id.tvhint);
        tvpswd=(TextView) findViewById(R.id.tvpswd);
        tvfgtpswd=(TextView) findViewById(R.id.tv_forgotpswd);

        password=(EditText) findViewById(R.id.password);
        login=(Button) findViewById(R.id.login);

        String htmlString="<u>Click here to change password</u>";
        tvpswd.setText(Html.fromHtml(htmlString));

        sqdb=openOrCreateDatabase("pswddiary", MODE_PRIVATE, null);
        sqdb.execSQL("create table if not exists pswdtable(id varchar,password varchar,email varchar);");
        Cursor c=sqdb.rawQuery("select password from pswdtable where id='123';", null);
        if(c.getCount()==1)
        {
            if(c.moveToFirst())
            { String s=c.getString(0);
                if(s.equals("0000"))
                {
                    tvhint.setText("hint: password->0000");
                }else
                {
                    tvhint.setText("");
                }


            }
        }

        tvpswd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent ip=new Intent(LoginActivity.this,PasswordChange.class);
                startActivity(ip);

            }
        });
        tvfgtpswd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent ip=new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(ip);

            }
        });
        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(),username.getText().toString()+password.getText().toString()+ uname+pswd, Toast.LENGTH_LONG).show();

                checkPassword();

            }
        });

    }
    public void checkPassword() {
        // TODO Auto-generated method stub

        Apswd="0000";
        sqdb=openOrCreateDatabase("pswddiary", MODE_PRIVATE, null);
        sqdb.execSQL("create table if not exists pswdtable(id varchar,password varchar,email varchar);");

        //sqdb.execSQL("insert into pswdtable values('123','0000');");

        Cursor c=sqdb.rawQuery("select password from pswdtable where id='123';", null);
        //  Toast.makeText(getApplicationContext(),"count = "+ c.getCount(), Toast.LENGTH_SHORT).show();

        if(c.getCount()==1)
        {
            c=sqdb.rawQuery("select password from pswdtable where id='123';", null);
            //		*/
            //  Npswd="12345";
            if(c.moveToFirst())
            {  Npswd=c.getString(0);
                // Npswd="12345";

                //	 Toast.makeText(getApplicationContext(),"pswd ="+ Npswd +"in else and ="+Apswd, Toast.LENGTH_SHORT).show();

            }

        }
        else{

            sqdb.execSQL("insert into pswdtable values('123','0000','abc@xyz.com');");
            Npswd=Apswd;
            //    Toast.makeText(getApplicationContext(),"pswd ="+ Npswd +"and ="+Apswd, Toast.LENGTH_SHORT).show();

        }
        //   Toast.makeText(getApplicationContext(),"pswd ="+ Npswd, Toast.LENGTH_SHORT).show();


        if( password.getText().toString().equals(Npswd))
        {
            Intent i=new Intent(LoginActivity.this,OptionsActivity.class);
            startActivity(i);
            finish();
        }
        else
        { //   Toast.makeText(getApplicationContext(),"pswd ="+ Npswd , Toast.LENGTH_SHORT).show();

            Toast.makeText(getApplicationContext(), "Enter correct details", Toast.LENGTH_LONG).show();
        }
        sqdb.close();


    }

}
