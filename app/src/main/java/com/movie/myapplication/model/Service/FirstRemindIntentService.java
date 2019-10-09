package com.movie.myapplication.model.Service;


import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.R;
import com.movie.myapplication.model.BroadcastReceiver.NotificationBroadcastReceiver;
import com.movie.myapplication.model.DistanceJson;
import com.movie.myapplication.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FirstRemindIntentService extends IntentService {
  private  ArrayList<Event> event;
  private int  Threshold;
  private  final  SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    public FirstRemindIntentService() {
        super("FirstRemindIntentService");
        event = new ArrayList<>();
        Threshold=60;
    }



    @Override
    protected void onHandleIntent(Intent intent) {




        // get event
        DBHelper dbHelper = new DBHelper(this, "movieplan.db", null, 1);
        dbHelper.getReadableDatabase();
        event=dbHelper.selectEvent();
        dbHelper.addDefaultTime();

        // get threshold
        Threshold=dbHelper.getThresholdTime();

       DistanceJson dj =new DistanceJson();


       for( int i= 0;i<event.size();i++) {

           // if the event has been click remind again, jump to next event
           if (dbHelper.checkEventRemindAgain(event.get(i).getId()))
           {
               continue;
           }

            // Calculate How long does the event start?
           Date sysD= new Date(System.currentTimeMillis());
           long diff = event.get(i).getSdate().getTime()-System.currentTimeMillis() ;
           if (event.get(i).getSdate().before(sysD))
           {
               continue;
           }
           diff = diff/60000;
           int  diffMin = (int) diff;

            // get the drive time by distance matrix
           int driveTime=dj.getRequireTime(event.get(i).getLocation(),this);

           // if is time to notify, post a notification
           if((driveTime+Threshold)>=diffMin){

               Intent intentDismiss = new Intent(this, NotificationBroadcastReceiver.class);
               Intent intentCancel = new Intent(this, NotificationBroadcastReceiver.class);
               Intent intentReminder = new Intent(this, NotificationBroadcastReceiver.class);


               intentDismiss.putExtra("action", "Dismiss"+event.get(i).getId());
               intentCancel.putExtra("action", "Cancel"+event.get(i).getId());
               intentReminder.putExtra("action", "Reminder"+event.get(i).getId());

               PendingIntent pIntentDismiss = PendingIntent.getBroadcast(this, Integer.parseInt(event.get(i).getId())+44, intentDismiss, PendingIntent.FLAG_ONE_SHOT);
               PendingIntent pIntentCancel = PendingIntent.getBroadcast(this, Integer.parseInt(event.get(i).getId())+99, intentCancel, PendingIntent.FLAG_ONE_SHOT);
               PendingIntent pIntentReminder = PendingIntent.getBroadcast(this, Integer.parseInt(event.get(i).getId())+188, intentReminder, PendingIntent.FLAG_ONE_SHOT);
               NotificationManager notificationManager = (NotificationManager) getSystemService
                       (NOTIFICATION_SERVICE);
               NotificationCompat.Builder drivingNotifBldr;
               drivingNotifBldr = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                       .setSmallIcon(R.drawable.ic_launcher_foreground)
                       .setContentTitle("Event Start Remind")
                       .setContentText(event.get(i).getTitle()+"\n" + "Start at:"+sdf.format(event.get(i).getSdate()))
                       .addAction(R.drawable.ic_launcher_foreground, "Dismiss", pIntentDismiss)
                       .addAction(R.drawable.ic_launcher_foreground, "Cancel", pIntentCancel)
                       .addAction(R.drawable.ic_launcher_foreground, "Reminder", pIntentReminder)
               ;

               notificationManager.notify(Integer.parseInt(event.get(i).getId()), drivingNotifBldr.build());

           }




       }







    }
}
