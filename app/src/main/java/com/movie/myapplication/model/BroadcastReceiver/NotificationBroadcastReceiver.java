package com.movie.myapplication.model.BroadcastReceiver;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.view.CancelDialog;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {



        String action=intent.getStringExtra("action");
        if(action.contains("Dismiss")){
            action=action.replace("Dismiss","");
            performAction1(context,action);
        }
        else if(action.contains("Cancel")){

            action=action.replace("Cancel","");
            performAction2(context,action);

        }
        else if(action.contains("Reminder")){
            action=action.replace("Reminder","");
            performAction3(context,action);

        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void performAction1(Context context,String eventID){
        DBHelper dbHelper = new DBHelper(context, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();

        NotificationManager notificationManager = ( NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(eventID));

        dbHelper.removeOldRemind(eventID);
    }

    public void performAction2(final Context context, final String evenID){

        Intent trIntent = new Intent("android.intent.action.MAIN");
        trIntent.setClass(context, CancelDialog.class);
        trIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        trIntent.putExtra("evenID", evenID);
        context.startActivity(trIntent);
    }
    public void performAction3(Context context,String evenID){

        DBHelper dbHelper = new DBHelper(context, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.addRemind(evenID);
        Toast.makeText(context, "Remind again after "+ dbHelper.getRemindTime() + " minutes", Toast.LENGTH_SHORT).show();

        NotificationManager notificationManager = ( NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(evenID));


    }
}
