package com.example.alarmmanager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnStartNormalAlarm, btnStopNormalAlarm,
            btnStartRepetativeAlarm, btnStopRepetativeAlarm,
            btnStartTimepickerAlarm, btnStopTimepickerAlarm;
    TextInputLayout tilNormalAlarm, tilTimepickerAlarm;
    private AlarmManager alarmManager;
    private Intent intent;
    private PendingIntent oneTimeAlarmIntent, repeatAlarmIntent;
    private int seconds;
    public static final String ALARM_TYPE = "ALARM_TYPE";
    public static final String ALARM_TYPE_ONE_TIME = "ALARM_TYPE_ONE_TIME";
    public static final String ALARM_TYPE_REPEAT = "ALARM_TYPE_REPEAT";
    public static final String ALARM_DESCRIPTION = "ALARM_DESCRIPTION";
    TimePickerDialog timePickerDialog;
    private Calendar currentTime;

    @SuppressLint("ServiceCast") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartNormalAlarm = findViewById(R.id.btnStartNormalAlarm);
        btnStopNormalAlarm = findViewById(R.id.btnStopNormalAlarm);
        btnStartRepetativeAlarm = findViewById(R.id.btnStartRepetativeAlarm);
        btnStopRepetativeAlarm = findViewById(R.id.btnStopRepetativeAlarm);
        btnStartTimepickerAlarm = findViewById(R.id.btnStartTimepickerAlarm);
        btnStopTimepickerAlarm = findViewById(R.id.btnStopTimepickerAlarm);

        tilNormalAlarm = findViewById(R.id.tilNormalAlarm);
        tilTimepickerAlarm = findViewById(R.id.tilTimepickerAlarm);

        btnStartNormalAlarm.setOnClickListener(this);
        btnStopNormalAlarm.setOnClickListener(this);
        btnStartRepetativeAlarm.setOnClickListener(this);
        btnStopRepetativeAlarm.setOnClickListener(this);
        btnStartTimepickerAlarm.setOnClickListener(this);
        btnStopTimepickerAlarm.setOnClickListener(this);

        intent = new Intent(this, MyBroadcastReceiver.class);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        oneTimeAlarmIntent = PendingIntent.getBroadcast(this, 100, intent, 0);
        repeatAlarmIntent = PendingIntent.getBroadcast(this, 200, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        tilTimepickerAlarm.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Timepicker", Toast.LENGTH_SHORT).show();
                openTimepickerDialog();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M) @Override public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnStartNormalAlarm:
                seconds = Integer.parseInt(tilNormalAlarm.getEditText().getText().toString());

                intent.putExtra(ALARM_TYPE, ALARM_TYPE_ONE_TIME);
                intent.putExtra(ALARM_DESCRIPTION, "One time alarm");

                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + (seconds * 1000),
                        oneTimeAlarmIntent);
                Toast.makeText(this, "Alarm set.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnStopNormalAlarm:
                alarmManager.cancel(oneTimeAlarmIntent);
                Toast.makeText(this, "Alarm cancelled.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnStartRepetativeAlarm:
                intent.putExtra(ALARM_TYPE, ALARM_TYPE_REPEAT);
                intent.putExtra(ALARM_DESCRIPTION, "Repeat alarm");

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), 60000,
                        repeatAlarmIntent);
                Toast.makeText(this, "Alarm set.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnStopRepetativeAlarm:
                alarmManager.cancel(repeatAlarmIntent);
                Toast.makeText(this, "Alarm cancelled.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnStartTimepickerAlarm:
                intent.putExtra(ALARM_TYPE, ALARM_TYPE_ONE_TIME);
                intent.putExtra(ALARM_DESCRIPTION, "Timepicker alarm");

                Log.d("TAG", String.valueOf("Hour: " + currentTime.get(Calendar.HOUR_OF_DAY) +
                        "\n" +
                        "Minute: " + currentTime.get(Calendar.MINUTE) +
                        "\n"));
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC, currentTime.getTimeInMillis(),
                        oneTimeAlarmIntent);
                Toast.makeText(this, "Alarm set.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnStopTimepickerAlarm:
                alarmManager.cancel(oneTimeAlarmIntent);
                Toast.makeText(this, "Alarm cancelled.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void openTimepickerDialog() {
        currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(MainActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
                        currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentTime.set(Calendar.MINUTE, minuteOfHour);
                        currentTime.set(Calendar.SECOND, 0);
                        currentTime.set(Calendar.MILLISECOND, 0);

                        String hour = "" + hourOfDay;
                        String minute = "" + minuteOfHour;

                        if (hourOfDay < 10) {
                            hour = "0" + hour;
                        }
                        if (minuteOfHour < 10) {
                            minute = "0" + minute;
                        }
                        String time = hour + ":" + minute;
                        tilTimepickerAlarm.getEditText().setText(time);
                    }
                }, hour, minute, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}