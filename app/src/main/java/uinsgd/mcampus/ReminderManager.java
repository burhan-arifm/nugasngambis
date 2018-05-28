package uinsgd.mcampus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ReminderManager {
    private Context context;
    private AlarmManager alarmManager;

    public ReminderManager (Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setReminder(long taskId, Calendar when) {
        Intent i = new Intent(context, OnAlarmReceiver.class);
        i.putExtra(DBDataSource.allColumns_tugas[0], taskId);

        alarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), PendingIntent
                .getBroadcast(context, 0, i, PendingIntent.FLAG_ONE_SHOT));
    }
}
