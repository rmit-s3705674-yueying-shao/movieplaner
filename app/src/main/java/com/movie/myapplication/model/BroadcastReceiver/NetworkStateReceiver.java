package com.movie.myapplication.model.BroadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.Service.FirstRemindIntentService;
import com.movie.myapplication.model.Service.RemindAgainIntentService;

public class NetworkStateReceiver extends BroadcastReceiver {
    private static PendingIntent pendingIntent=null,pendingIntent2=null;
    private static AlarmManager am=null,am2=null;


    @Override
    public void onReceive(Context context, Intent intent1) {

        // network change
        String action = intent1.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();

            // get the notification period
            DBHelper dbHelper = new DBHelper(context, "movieplan.db", null, 1);
            dbHelper.getWritableDatabase();
             int period = dbHelper.getPeriodTime();

             // network is connect
            if(info != null && info.isAvailable()) {

                Toast.makeText(context, "Network Connected", Toast.LENGTH_SHORT).show();
                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(context, FirstRemindIntentService.class);
                pendingIntent = PendingIntent.getService(context, 999, intent, 0);
                long interval = DateUtils.MINUTE_IN_MILLIS * period;//  notification period
                long firstWake = System.currentTimeMillis() + interval;
                am.setRepeating(AlarmManager.RTC, firstWake, interval, pendingIntent);
                am2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Intent intent2 = new Intent(context, RemindAgainIntentService.class);
                pendingIntent2 = PendingIntent.getService(context, 666, intent2, 0);
                long firstWake2 = System.currentTimeMillis() + interval;
                am2.setRepeating(AlarmManager.RTC, firstWake2, interval, pendingIntent2);

            } else {
                //net work is disconnected, close the AlarmManager
                if(am!=null)
                {
                    am.cancel(pendingIntent);
                }
                if(am2!=null)
                {
                    am2.cancel(pendingIntent2);
                }
                Toast.makeText(context, "Network Disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    }

}



