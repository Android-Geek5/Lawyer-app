package com.eweblog.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import com.eweblog.R;
import com.eweblog.SelectDateActivity;

public class MyAlarmService extends Service
{
      
   private NotificationManager mManager;
 
    @Override
    public IBinder onBind(Intent arg0)
    {
       // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
    }
 
   @SuppressWarnings("static-access")
   @Override
   public void onStart(Intent intent, int startId)
   {
       super.onStart(intent, startId);
      Notification.Builder mBuilder =
           new Notification.Builder(this.getApplicationContext())
                   .setSmallIcon(R.mipmap.elogo)
                   .setContentTitle("Reminder")
                   .setContentText("Please check the cases for tomorrow.")
              .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notify));


       Intent resultIntent = new Intent(this.getApplicationContext(), SelectDateActivity.class);
       resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
       PendingIntent resultPendingIntent = PendingIntent.getActivity(this.getApplicationContext(),0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
       mBuilder.setContentIntent(resultPendingIntent);
       NotificationManager mNotificationManager = (NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

       mNotificationManager.notify(1, mBuilder.build());

    }
 
    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
 
}