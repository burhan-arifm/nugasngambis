package uinsgd.mcampus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class ReminderService extends WakeReminderIntentService {

    public ReminderService() {
        super("ReminderService");
    }

    @Override
    void doReminderWork(Intent intent) {
        long idTugas = intent.getExtras().getLong(DBDataSource.allColumns_tugas[0]);

        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent (this, TugasEditActivity.class);
        notificationIntent.putExtra(DBDataSource.allColumns_tugas[0], idTugas);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Ayo kumpulkan tugasnya!");
        builder.setTicker("Alarm");
        builder.setSmallIcon(R.drawable.ic_mini_logo);
        builder.setContentText("Sudah waktunya dikumpulkan nih.");
        builder.setContentIntent(pendingIntent);

        notificationManager.notify(17, builder.getNotification());
    }
}
