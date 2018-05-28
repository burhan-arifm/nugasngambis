package uinsgd.mcampus;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        long idTugas = intent.getExtras().getLong(DBDataSource.allColumns_tugas[0]);
        WakeReminderIntentService.acquireStaticLock(context);

        Intent i = new Intent(context, ReminderService.class);
        i.putExtra(DBDataSource.allColumns_tugas[0], idTugas);
        context.startService(i);
    }
}
