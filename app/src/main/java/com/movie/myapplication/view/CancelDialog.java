package com.movie.myapplication.view;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.movie.myapplication.model.AsyncTask.GetEventbyIDAsyncTask;
import com.movie.myapplication.model.AsyncTask.RemoveEventbyTitleAsyncTask;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.GetAttendeesbyEvent;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CancelDialog extends AppCompatActivity {
        private  Event event=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_cancel_dialg);
        Intent intent = getIntent();
        final String evenID=intent.getStringExtra("evenID");
        int attendNum=0;
        try {
            event =new GetEventbyIDAsyncTask(this).execute(evenID).get();
            attendNum=new GetAttendeesbyEvent(this).execute(event).get().size();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy h:mm:ss aa", Locale.ENGLISH);

        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(this);
        normalDialog.setTitle("Warning");
        normalDialog.setMessage("Are you sure to remove this event? "+"\n"+"Even Title:\t"+event.getTitle()+"\n"+"Start Time:\t "+sdf.format(event.getSdate())+"\n"+"End Time:\t "+sdf.format(event.getEdate())+"\n"+"Venue:\t "+event.getVenue()+"\n"+"Number of attendees:\t "+attendNum);
        normalDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new RemoveEventbyTitleAsyncTask(CancelDialog.this).execute(event.getTitle());
                        Intent intent = new Intent();
                        intent.setClass(CancelDialog.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        NotificationManager notificationManager = ( NotificationManager)CancelDialog.this.getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(10);
                        CancelDialog.this.startActivity(intent);
                    }
                });
        normalDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationManager notificationManager = ( NotificationManager)CancelDialog.this.getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(Integer.parseInt(evenID));
                        finish();

                    }
                });
        normalDialog.show();
    }
}
