package com.example.anchary.personaldiary;


        import android.content.Intent;
        import android.database.sqlite.SQLiteDatabase;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText pswd,cpswd,email;
    Button spbutton;
    SQLiteDatabase sqdb;
    String email_pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean emailValid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        pswd=(EditText) findViewById(R.id.et_password);
        cpswd=(EditText) findViewById(R.id.et_cpassword);
        email=(EditText) findViewById(R.id.et_email);
        spbutton=(Button) findViewById(R.id.sp_button);

        spbutton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String p1=pswd.getText().toString();
                String p2=cpswd.getText().toString();
                String em=email.getText().toString();
                if(!p1.equals(p2))
                {
                    cpswd.setError("Password doesn't match");
                }
                if(p1.length()<4 || p1.equals("") || p2.equals("") || p1.length()>32)
                {
                    pswd.setError("Password should be min 4 and max 32 characters");
                }
                email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!(email.getText().toString().matches(email_pattern))) {
                            email.setError("Invalid EmailID");
                        }else{
                           emailValid=true;
                        }
                    }
                });

                if((p1.length()>3 && p1.length()<33) && p1.equals(p2)&& emailValid )
                {
                    addDataToDatabase(p1,em);
                    Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        });



    }
    protected void addDataToDatabase(String pswd, String em) {
        // TODO Auto-generated method stub
        sqdb=openOrCreateDatabase("pswddiary", MODE_PRIVATE, null);
        sqdb.execSQL("create table if not exists pswdtable(id varchar,password varchar,email varchar);");

		/*
		   sqdb.execSQL("insert into pswdtable values('123','0000','abc@xyz.com');");

		   Cursor c=sqdb.rawQuery("select password from pswdtable where id='123';", null);
		*/
        sqdb.execSQL("insert into pswdtable values('123','"+pswd+"','"+em+"');");

        sqdb.close();

    }
}