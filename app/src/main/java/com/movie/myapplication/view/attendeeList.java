package com.movie.myapplication.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.movie.myapplication.R;
import com.movie.myapplication.adapter.attendeesAdapter;
import com.movie.myapplication.model.AddAttendeeParams;
import com.movie.myapplication.model.AsyncTask.AddAttendeeAsyncTask;
import com.movie.myapplication.model.AsyncTask.GetEventbyIDAsyncTask;
import com.movie.myapplication.model.AsyncTask.cleanAttendeeAsyncTask;
import com.movie.myapplication.model.Attendees;
import com.movie.myapplication.model.Event;
import com.movie.myapplication.model.GetAttendeesbyEvent;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class attendeeList extends AppCompatActivity {
    private ArrayList<Attendees> attendeesList = new ArrayList<>();
    private Event thisEvent = null;
    private TextView title;
    private Attendees[] attendees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_list);


        // get current event
        Intent intent = getIntent();

        try {
            thisEvent= new GetEventbyIDAsyncTask(this).execute(intent.getStringExtra("eventID")).get();
            attendeesList = new GetAttendeesbyEvent(this).execute(thisEvent).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        title = findViewById(R.id.title3);
        title.setText(thisEvent.getTitle() + " Attendees");
        attendees = new Attendees[attendeesList.size()];
        for (int i = 0; i < attendees.length; i++) {
            attendees[i] = new Attendees(attendeesList.get(i).getName(), attendeesList.get(i).getPhoneNum());
        }
        attendeesAdapter attendeeAdapter = new attendeesAdapter(this, attendees);
        ListView attendeeListView = findViewById(R.id.attendeesList);
        attendeeListView.setAdapter(attendeeAdapter);

        // long press attendees to delete the attendees
        attendeeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int position, long arg3) {
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(attendeeList.this);
                normalDialog.setTitle("Warning");
                normalDialog.setMessage("Are you sure to remove this attendee?");
                normalDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new cleanAttendeeAsyncTask(attendeeList.this).execute(attendees[position].getName());

                                Intent attendIntent = new Intent();
                                attendIntent.setClass(attendeeList.this, attendeeList.class);
                                attendIntent.putExtra("eventID", thisEvent.getId());
                                attendeeList.this.startActivity(attendIntent);

                            }
                        });
                normalDialog.setNegativeButton("No",null);
                normalDialog.show();

                return false;
            }
        });

    }

    //setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        menu.add(0, 1, 0, "Add Attendees");
        MenuItem home = menu.add(0, 17, 0, "");
        MenuItem cal = menu.add(0, 18, 0, "");

        cal.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cal.setIcon(R.drawable.calendar);
        home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        home.setIcon(R.drawable.home);
        return super.onCreateOptionsMenu(menu);

    }

    //According to id do something
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //open system address book
            case 1:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
                break;
            case 2:
                Intent i = new Intent();
                i.setClass(this, MainActivity.class);
                this.startActivity(i);
                break;
            case 17:
                Intent back = new Intent();
                back.setClass(this, MainActivity.class);
                this.startActivity(back);
                break;
            case 18:
                Intent cal = new Intent();
                cal.setClass(this, EventCalendar.class);
                this.startActivity(cal);
                break;


        }
        return true;
    }

    // return selected attendees and add to the event
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                Cursor cursor = getContentResolver()
                        .query(uri,
                                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                                null, null, null);
                while (cursor.moveToNext()) {
                    String number = cursor.getString(0);
                    String name = cursor.getString(1);


                    //add to event
                    Attendees newAttendee = new Attendees(name, number);
                    AddAttendeeParams params = new AddAttendeeParams(newAttendee, thisEvent);
                   new AddAttendeeAsyncTask(this).execute(params);


                    //go to attendee list
                    Intent attendIntent = new Intent();
                    attendIntent.setClass(this, attendeeList.class);
                    attendIntent.putExtra("eventID", thisEvent.getId());
                    this.startActivity(attendIntent);
                    break;


                }

            }
        }

    }
}
