package com.movie.myapplication.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.movie.myapplication.R;
import com.movie.myapplication.controller.CalendarClickListener;
import com.movie.myapplication.controller.NoEventListener;
import com.movie.myapplication.model.AsyncTask.GetEventbyTitleAsyncTask;
import com.movie.myapplication.model.AsyncTask.HasEventAsyncTask;
import com.movie.myapplication.model.AsyncTask.RemoveEventbyTitleAsyncTask;
import com.movie.myapplication.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class EventCalendar extends AppCompatActivity {

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button next;
    private Button back;
    private TextView date;
    private Date firstDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        // set last sunday of current day as default day
        Calendar cal = Calendar.getInstance();
        int weekday = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DAY_OF_MONTH, -weekday + 1);
        firstDate = cal.getTime();


        //match widget
        date = findViewById(R.id.currentDate);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        next = findViewById(R.id.next);
        back = findViewById(R.id.back);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(firstDate);
                c.add(Calendar.DAY_OF_MONTH, 7);
                firstDate = c.getTime();
                setDay();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.setTime(firstDate);
                c.add(Calendar.DAY_OF_MONTH, -7);
                firstDate = c.getTime();
                setDay();
            }
        });

        //  setting seven days
        setDay();
    }

    //setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_menu, menu);
        MenuItem home = menu.add(0, 17, 0, "");
        MenuItem cal = menu.add(0, 18, 0, "");
        cal.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        cal.setIcon(R.drawable.calendar);
        home.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        home.setIcon(R.drawable.home);
        return super.onCreateOptionsMenu(menu);

    }

    // do something by id
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

    //init seven days and set color and listener
    private void setDay() {

        //default seven days as blue background and no events
        btn1.setBackgroundColor(Color.parseColor("#0492EB"));
        btn2.setBackgroundColor(Color.parseColor("#0492EB"));
        btn3.setBackgroundColor(Color.parseColor("#0492EB"));
        btn4.setBackgroundColor(Color.parseColor("#0492EB"));
        btn5.setBackgroundColor(Color.parseColor("#0492EB"));
        btn6.setBackgroundColor(Color.parseColor("#0492EB"));
        btn7.setBackgroundColor(Color.parseColor("#0492EB"));
        btn1.setOnClickListener(new NoEventListener(this));
        btn2.setOnClickListener(new NoEventListener(this));
        btn3.setOnClickListener(new NoEventListener(this));
        btn4.setOnClickListener(new NoEventListener(this));
        btn5.setOnClickListener(new NoEventListener(this));
        btn6.setOnClickListener(new NoEventListener(this));
        btn7.setOnClickListener(new NoEventListener(this));


        SimpleDateFormat getMonthYear = new SimpleDateFormat("MM-yyyy");
        SimpleDateFormat getYear = new SimpleDateFormat("yyyy");
        Calendar las = Calendar.getInstance();
        las.setTime(firstDate);
        las.add(Calendar.DAY_OF_MONTH, 6);
        Date lastDate = las.getTime();

        // if the week cross two years, display two years
        if (getYear.format(firstDate).compareTo(getYear.format(lastDate)) != 0) {
            date.setText(getMonthYear.format(firstDate) + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + getMonthYear.format(lastDate));
        } else
            //display the year and month
            date.setText(getMonthYear.format(firstDate));


        SimpleDateFormat getDay = new SimpleDateFormat("dd");
        final SimpleDateFormat getDate = new SimpleDateFormat("M-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(firstDate);

        // set the day and long press listener
        btn1.setText(getDay.format(firstDate));
        btn1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventCalendar.this, AddEvent.class);
                intent.putExtra("startDate", getDate.format(firstDate));
                EventCalendar.this.startActivity(intent);
                return true;
            }
        });
        c.add(Calendar.DAY_OF_MONTH, 1);
        final Date day2 = c.getTime();
        btn2.setText(getDay.format(day2));
        btn2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventCalendar.this, AddEvent.class);
                intent.putExtra("startDate", getDate.format(day2));
                EventCalendar.this.startActivity(intent);
                return true;
            }
        });
        c.add(Calendar.DAY_OF_MONTH, 1);
        final Date day3 = c.getTime();
        btn3.setText(getDay.format(day3));
        btn3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventCalendar.this, AddEvent.class);
                intent.putExtra("startDate", getDate.format(day3));
                EventCalendar.this.startActivity(intent);
                return true;
            }
        });
        c.add(Calendar.DAY_OF_MONTH, 1);
        final Date day4 = c.getTime();
        btn4.setText(getDay.format(day4));
        btn4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventCalendar.this, AddEvent.class);
                intent.putExtra("startDate", getDate.format(day4));
                EventCalendar.this.startActivity(intent);
                return true;
            }
        });

        c.add(Calendar.DAY_OF_MONTH, 1);
        final Date day5 = c.getTime();
        btn5.setText(getDay.format(day5));
        btn5.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventCalendar.this, AddEvent.class);
                intent.putExtra("startDate", getDate.format(day5));
                EventCalendar.this.startActivity(intent);
                return true;
            }
        });
        c.add(Calendar.DAY_OF_MONTH, 1);
        final Date day6 = c.getTime();
        btn6.setText(getDay.format(day6));
        btn6.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventCalendar.this, AddEvent.class);
                intent.putExtra("startDate", getDate.format(day6));
                EventCalendar.this.startActivity(intent);
                return true;
            }
        });
        c.add(Calendar.DAY_OF_MONTH, 1);
        final Date day7 = c.getTime();
        btn7.setText(getDay.format(day7));
        btn7.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent();
                intent.setClass(EventCalendar.this, AddEvent.class);
                intent.putExtra("startDate", getDate.format(day7));
                EventCalendar.this.startActivity(intent);
                return true;
            }
        });

        String fTitle="";
        try {
            fTitle = new HasEventAsyncTask(this).execute(firstDate).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //check the week has event or not. if have event then change the background color and listener
        if (fTitle.compareTo("") != 0) {
            btn1.setBackgroundColor(Color.parseColor("#FF5722"));
            btn1.setOnClickListener(new CalendarClickListener(fTitle, this));
            btn1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EventCalendar.this);
                    normalDialog.setTitle("Warning");
                    normalDialog.setMessage("Are you sure to remove this even?");
                    normalDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String fTitle="";
                                    Event clickedEvent=null;
                                    try {
                                        fTitle = new HasEventAsyncTask(EventCalendar.this).execute(firstDate).get();
                                        clickedEvent = new GetEventbyTitleAsyncTask(EventCalendar.this).execute(fTitle).get();
                                        new RemoveEventbyTitleAsyncTask(EventCalendar.this).execute(clickedEvent.getTitle()).get();

                                        Toast.makeText(EventCalendar.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(EventCalendar.this, EventCalendar.class);
                                        EventCalendar.this.startActivity(intent);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }






                                }
                            });
                    normalDialog.setNegativeButton("No", null);
                    normalDialog.show();
                    return true;
                }
            });
        }
        String Title2="";
        try {
            Title2 = new HasEventAsyncTask(this).execute(day2).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Title2.compareTo("") != 0) {
            btn2.setBackgroundColor(Color.parseColor("#FF5722"));
            btn2.setOnClickListener(new CalendarClickListener(Title2, this));
            btn2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EventCalendar.this);
                    normalDialog.setTitle("Warning");
                    normalDialog.setMessage("Are you sure to remove this even?");
                    normalDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String Title2="";
                                    Event clickedEvent=null;
                                    try {
                                        Title2 = new HasEventAsyncTask(EventCalendar.this).execute(day2).get();
                                        clickedEvent = new GetEventbyTitleAsyncTask(EventCalendar.this).execute(Title2).get();
                                        new RemoveEventbyTitleAsyncTask(EventCalendar.this).execute(clickedEvent.getTitle()).get();

                                        Toast.makeText(EventCalendar.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(EventCalendar.this, EventCalendar.class);
                                        EventCalendar.this.startActivity(intent);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    normalDialog.setNegativeButton("No", null);
                    normalDialog.show();
                    return true;
                }
            });
        }
        String Title3="";
        try {
            Title3 = new HasEventAsyncTask(this).execute(day3).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Title3.compareTo("") != 0) {
            btn3.setBackgroundColor(Color.parseColor("#FF5722"));
            btn3.setOnClickListener(new CalendarClickListener(Title3, this));
            btn3.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EventCalendar.this);
                    normalDialog.setTitle("Warning");
                    normalDialog.setMessage("Are you sure to remove this even?");
                    normalDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String Title3="";
                                    Event clickedEvent=null;
                                    try {
                                        Title3 = new HasEventAsyncTask(EventCalendar.this).execute(day3).get();
                                        clickedEvent = new GetEventbyTitleAsyncTask(EventCalendar.this).execute(Title3).get();
                                        new RemoveEventbyTitleAsyncTask(EventCalendar.this).execute(clickedEvent.getTitle()).get();

                                        Toast.makeText(EventCalendar.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(EventCalendar.this, EventCalendar.class);
                                        EventCalendar.this.startActivity(intent);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    normalDialog.setNegativeButton("No", null);
                    normalDialog.show();
                    return true;
                }
            });
        }
        String Title4="";
        try {
            Title4 = new HasEventAsyncTask(this).execute(day4).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Title4.compareTo("") != 0) {
            btn4.setBackgroundColor(Color.parseColor("#FF5722"));
            btn4.setOnClickListener(new CalendarClickListener(Title4, this));
            btn4.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EventCalendar.this);
                    normalDialog.setTitle("Warning");
                    normalDialog.setMessage("Are you sure to remove this even?");
                    normalDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String Title4="";
                                    Event clickedEvent=null;
                                    try {
                                        Title4 = new HasEventAsyncTask(EventCalendar.this).execute(day4).get();
                                        clickedEvent = new GetEventbyTitleAsyncTask(EventCalendar.this).execute(Title4).get();
                                        new RemoveEventbyTitleAsyncTask(EventCalendar.this).execute(clickedEvent.getTitle()).get();

                                        Toast.makeText(EventCalendar.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(EventCalendar.this, EventCalendar.class);
                                        EventCalendar.this.startActivity(intent);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    normalDialog.setNegativeButton("No", null);
                    normalDialog.show();
                    return true;
                }
            });
        }
        String Title5="";
        try {
            Title5 = new HasEventAsyncTask(this).execute(day5).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Title5.compareTo("") != 0) {
            btn5.setBackgroundColor(Color.parseColor("#FF5722"));
            btn5.setOnClickListener(new CalendarClickListener(Title5, this));
            btn5.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EventCalendar.this);
                    normalDialog.setTitle("Warning");
                    normalDialog.setMessage("Are you sure to remove this even?");
                    normalDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String Title5="";
                                    Event clickedEvent=null;
                                    try {
                                        Title5 = new HasEventAsyncTask(EventCalendar.this).execute(day5).get();
                                        clickedEvent = new GetEventbyTitleAsyncTask(EventCalendar.this).execute(Title5).get();
                                        new RemoveEventbyTitleAsyncTask(EventCalendar.this).execute(clickedEvent.getTitle()).get();

                                        Toast.makeText(EventCalendar.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(EventCalendar.this, EventCalendar.class);
                                        EventCalendar.this.startActivity(intent);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    normalDialog.setNegativeButton("No", null);
                    normalDialog.show();
                    return true;
                }
            });
        }
        String Title6="";
        try {
            Title6 = new HasEventAsyncTask(this).execute(day6).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Title6.compareTo("") != 0) {
            btn6.setBackgroundColor(Color.parseColor("#FF5722"));
            btn6.setOnClickListener(new CalendarClickListener(Title6, this));
            btn6.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EventCalendar.this);
                    normalDialog.setTitle("Warning");
                    normalDialog.setMessage("Are you sure to remove this even?");
                    normalDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String Title6="";
                                    Event clickedEvent=null;
                                    try {
                                        Title6 = new HasEventAsyncTask(EventCalendar.this).execute(day5).get();
                                        clickedEvent = new GetEventbyTitleAsyncTask(EventCalendar.this).execute(Title6).get();
                                        new RemoveEventbyTitleAsyncTask(EventCalendar.this).execute(clickedEvent.getTitle()).get();

                                        Toast.makeText(EventCalendar.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(EventCalendar.this, EventCalendar.class);
                                        EventCalendar.this.startActivity(intent);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    normalDialog.setNegativeButton("No", null);
                    normalDialog.show();
                    return true;
                }
            });
        }
        String Title7="";
        try {
            Title7 = new HasEventAsyncTask(this).execute(day7).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (Title7.compareTo("") != 0) {
            btn7.setBackgroundColor(Color.parseColor("#FF5722"));
            btn7.setOnClickListener(new CalendarClickListener(Title7, this));
            btn7.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder normalDialog =
                            new AlertDialog.Builder(EventCalendar.this);
                    normalDialog.setTitle("Warning");
                    normalDialog.setMessage("Are you sure to remove this even?");
                    normalDialog.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    String Title7="";
                                    Event clickedEvent=null;
                                    try {
                                        Title7 = new HasEventAsyncTask(EventCalendar.this).execute(day7).get();
                                        clickedEvent = new GetEventbyTitleAsyncTask(EventCalendar.this).execute(Title7).get();
                                        new RemoveEventbyTitleAsyncTask(EventCalendar.this).execute(clickedEvent.getTitle()).get();

                                        Toast.makeText(EventCalendar.this, "Event Remooved", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent();
                                        intent.setClass(EventCalendar.this, EventCalendar.class);
                                        EventCalendar.this.startActivity(intent);
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    normalDialog.setNegativeButton("No", null);
                    normalDialog.show();
                    return true;
                }
            });
        }
    }
}
