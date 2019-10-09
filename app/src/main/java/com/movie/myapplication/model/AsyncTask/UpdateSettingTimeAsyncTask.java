package com.movie.myapplication.model.AsyncTask;

import android.app.Activity;
import android.os.AsyncTask;

import com.movie.myapplication.Database.DBHelper;
import com.movie.myapplication.model.SettingTimeParams;

public class UpdateSettingTimeAsyncTask extends AsyncTask<SettingTimeParams,Void,Void> {
    private Activity activity;




    public UpdateSettingTimeAsyncTask(Activity activity)
    {
        this.activity = activity;

    }
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected  Void doInBackground(SettingTimeParams... Param) {

        DBHelper dbHelper = new DBHelper(activity, "movieplan.db", null, 1);
        dbHelper.getWritableDatabase();
        dbHelper.updateRemindTime(Param[0].getRemind());
        dbHelper.updateThresholdTime(Param[0].getThreshold());
        dbHelper.updatePeriodTime(Param[0].getPeriod());
        return null;

    }




}
