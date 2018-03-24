package com.example.anchary.personaldiary;


    import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteDatabase.CursorFactory;
        import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {

        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String str="create table remainder(content text,imp integer,rdate text,rtime text);";
        db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        String str="drop table remainder";
        db.execSQL(str);
        onCreate(db);
    }

}