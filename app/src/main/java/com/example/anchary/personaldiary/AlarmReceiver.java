package com.example.anchary.personaldiary;


        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v4.app.NotificationCompat;
        import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private static final int MY_NOTIFICATION_ID=1;
    NotificationManager notificationManager;
    Notification myNotification;
    private final String myBlog = "4th module completed";

    @SuppressWarnings("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm received!", Toast.LENGTH_LONG).show();

        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(myBlog));
        String str=intent.getStringExtra("text");
        int imp=Integer.parseInt(intent.getStringExtra("imp"));
      //  PendingIntent pendingIntent = PendingIntent.getActivity(context,0,myIntent,Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        if(imp==1) {
            myNotification = new NotificationCompat.Builder(context)
                    .setContentTitle("Personal Diary")
                    .setContentText(str)
                    .setTicker("Notification!")
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(android.R.drawable.btn_star_big_on)
                    .build();
        }else{
            myNotification = new NotificationCompat.Builder(context)
                    .setContentTitle("Personal Diary")
                    .setContentText(str)
                    .setTicker("Notification!")
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .build();
        }
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID, myNotification);
    }

}