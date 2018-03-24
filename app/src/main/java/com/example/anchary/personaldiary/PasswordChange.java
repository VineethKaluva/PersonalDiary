package com.example.anchary.personaldiary;


        import android.app.Activity;
        import android.content.ContentValues;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.*;

public class PasswordChange extends Activity{

    EditText oldpswd,newpswd;
    Button confirm,cancel;
    String op,np;
    SQLiteDatabase sqdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password);

        oldpswd=(EditText) findViewById(R.id.oldpswd);
        newpswd=(EditText) findViewById(R.id.newpswd);
        confirm=(Button) findViewById(R.id.confirm);
        cancel=(Button) findViewById(R.id.cancel);

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i=new Intent(PasswordChange.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        confirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String dpswd;
                op=oldpswd.getText().toString();
                np=newpswd.getText().toString();

                if(op.equals(np))
                {
                    Intent i=new Intent(PasswordChange.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else if(np.length()>=4 && np.length()<=32)
                {
                    sqdb=openOrCreateDatabase("pswddiary", MODE_PRIVATE, null);
                    //  sqdb.execSQL("create table if not exists pswdtable(id varchar,password varchar);");

                    Cursor c=sqdb.rawQuery("select password from pswdtable where id='123';",null);

                    dpswd="none";
                    //  Toast.makeText(getApplicationContext(),"pswd"+ dpswd , Toast.LENGTH_SHORT).show();
                    System.out.println("none = "+dpswd);
                    if(c.moveToFirst())
                    {  dpswd=c.getString(0);
                        System.out.println("pswd in table = "+dpswd);
                        //	Toast.makeText(getApplicationContext(),"pswd in table="+ dpswd , Toast.LENGTH_SHORT).show();

                    }

                    if(op.equals(dpswd))
                    {
                        System.out.println("before update");

                        ContentValues cv=new ContentValues();
                        cv.put("id","123");
                        cv.put("password", np);

                        System.out.println("after update");
                        int r=	sqdb.update("pswdtable", cv,"id='123'",null );

                        if (r>0) {
                            Toast.makeText(getApplicationContext(), "update successfull New Password = "+np, 2000).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "update unsuccessfull", 2000).show();
                        }


                        //  sqdb.execSQL("UPDATE pswdtable SET password='"+np+"' WHERE id='123' ;");
                        Intent i=new Intent(PasswordChange.this,LoginActivity.class);
                        startActivity(i);
                        finish();

                    }else
                    {
                        Toast.makeText(getApplicationContext(), "Enter correct password for changing", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    newpswd.setError("password length should be min 4 or max 32 characters");

                }

            }
        });


    }
}
