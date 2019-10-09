package com.movie.myapplication.model.Service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.R;
import com.movie.myapplication.model.BroadcastReceiver.NotificationBroadcastReceiver;
import com.movie.myapplication.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class RemindAgainIntentService extends IntentService {
    private ArrayList<Event> event;
    private  final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    public RemindAgainIntentService() {
        super("RemindAgainIntentService");
        event = new ArrayList<>();
    }



    @Override
    protected void onHandleIntent( Intent intent) {

        DBHelper dbHelper = new DBHelper(this, "movieplan.db", null, 1);
        dbHelper.getReadableDatabase();
        event=dbHelper.selectEvent();
        for( int i= 0;i<event.size();i++) {




            if (dbHelper.checkEventRemindAgain(event.get(i).getId()) )
            {
                long diff = dbHelper.getRemindTime(event.get(i).getId()).getTime()-System.currentTimeMillis() ;
                diff=diff/60000;
                if (diff<0)
                    diff= -diff;
                if(diff>2)
                    continue;

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
                        .setContentText(event.get(i).getTitle())

                        .addAction(R.drawable.ic_launcher_foreground, "Dismiss", pIntentDismiss)
                        .addAction(R.drawable.ic_launcher_foreground, "Cancel", pIntentCancel)
                        .addAction(R.drawable.ic_launcher_foreground, "Reminder", pIntentReminder)              ;
                notificationManager.notify(10, drivingNotifBldr.build());
                dbHelper.removeOldRemind(event.get(i).getId());
            }
        }

    }
}
