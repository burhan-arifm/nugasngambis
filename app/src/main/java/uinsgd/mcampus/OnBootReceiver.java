package uinsgd.mcampus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ReminderManager reminderManager = new ReminderManager(context);
        DBDataSource dbDataSource = new DBDataSource(context);
        dbDataSource.open();
        Cursor cursor = dbDataSource.fetchAllTugas();
        if (cursor != null) {
            cursor.moveToFirst();

            int rowIdColumnIndex = cursor.getColumnIndex(DBDataSource.allColumns_tugas[0]);
            int dateTimeColumnIndex = cursor.getColumnIndex(DBDataSource.allColumns_tugas[4]);

            while (cursor.isAfterLast() == false) {
                long rowId = cursor.getLong(rowIdColumnIndex);
                String dateTime = cursor.getString(dateTimeColumnIndex);

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm",
                        Locale.getDefault());
                try {
                    Date date = simpleDateFormat.parse(dateTime);
                    calendar.setTime(date);

                    reminderManager.setReminder(rowId, calendar);
                } catch (ParseException e) {
                    Log.e("OnBootReceiver", e.getMessage(), e);
                }

                cursor.moveToNext();

                Log.d("OnBootReceiver", "Adding alarm from boot.");
                Log.d("OnBootReceiver", "Row Id Column Index - " + rowIdColumnIndex);
            }

            cursor.close();
        }

        dbDataSource.close();
    }
}
