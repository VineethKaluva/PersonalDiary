package com.example.anchary.personaldiary;


        import android.os.Bundle;
        import android.content.Intent;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.AppCompatActivity;
        import android.view.animation.AlphaAnimation;
        import android.view.animation.Animation;
        import android.view.animation.Animation.AnimationListener;
        import android.view.animation.BounceInterpolator;
        import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {
    //ImageView welcomescreen;
    TextView tv;
    SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        //  welcomescreen=(ImageView) findViewById(R.id.imageView1);
        tv=(TextView) findViewById(R.id.tvws);

        Animation fadeout=new AlphaAnimation(1, 1);
        fadeout.setInterpolator(new BounceInterpolator());
        fadeout.setStartTime(1000);
        fadeout.setDuration(2500);
        fadeout.setFillEnabled(true);
        fadeout.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                sqdb=openOrCreateDatabase("pswddiary", MODE_PRIVATE, null);
                sqdb.execSQL("create table if not exists pswdtable(id varchar,password varchar,email varchar);");

                Cursor c=sqdb.rawQuery("select * from pswdtable where id='123';",null);

                if(c.getCount()==0)
                {
                    Intent i=new Intent(WelcomeScreen.this,RegisterActivity.class);
                    startActivity(i);
                    finish();
                }else
                {
                    Intent i=new Intent(WelcomeScreen.this,LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });
        //   welcomescreen.setAnimation(fadeout);
        tv.setAnimation(fadeout);
        fadeout.start();


    }
}
