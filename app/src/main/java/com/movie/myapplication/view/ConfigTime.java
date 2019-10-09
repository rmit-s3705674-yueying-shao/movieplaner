package com.movie.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.movie.myapplication.R;
import com.movie.myapplication.controller.SetTimeListener;
import com.movie.myapplication.model.AsyncTask.GetPeriodTimeAsyncTask;
import com.movie.myapplication.model.AsyncTask.GetRemindTimeAsyncTask;
import com.movie.myapplication.model.AsyncTask.GetThresholdTimeAsyncTask;
import com.movie.myapplication.model.AsyncTask.UpdateSettingTimeAsyncTask;
import com.movie.myapplication.model.SettingTimeParams;

import java.util.concurrent.ExecutionException;

public class ConfigTime extends AppCompatActivity {
       private Button btnOK;
       private EditText edTime;
       private EditText npTime;
       private EditText ntTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_time);

       int eTime = 0,pTime=0,tTime=0;
        try {
            eTime = new GetRemindTimeAsyncTask(this).execute().get();
            pTime= new GetPeriodTimeAsyncTask(this).execute().get();
            tTime= new GetThresholdTimeAsyncTask(this).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        btnOK=findViewById(R.id.button);
       edTime=findViewById(R.id.eTime);
       npTime=findViewById(R.id.npTime);
       ntTime=findViewById(R.id.ntTime);
       npTime.setText(pTime+"");
       ntTime.setText(tTime+"");
       edTime.setText(eTime+"");
       btnOK.setOnClickListener( new SetTimeListener(this));



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem home = menu.add(0, 17, 0, "");
        MenuItem cal = menu.add(0, 18, 0, "");

        cal.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cal.setIcon(R.drawable.calendar);
        home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        home.setIcon(R.drawable.home);
        return super.onCreateOptionsMenu(menu);

    }

    //do something by id
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    public void UpDateTime() {

        SettingTimeParams params = new SettingTimeParams(npTime.getText().toString(),ntTime.getText().toString(),edTime.getText().toString());

        new UpdateSettingTimeAsyncTask(this).execute(params);

        Toast.makeText(this, "Time Change", Toast.LENGTH_SHORT).show();

        // go back to main activity
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        this.startActivity(intent);
    }
}
