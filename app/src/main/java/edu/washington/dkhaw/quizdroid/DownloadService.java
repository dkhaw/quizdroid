package edu.washington.dkhaw.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class DownloadService extends IntentService {
    private DownloadManager dm;
    public static final int ALARM = 123;
    private static int interval= MainActivity.interval;
    private String url = MainActivity.url;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() { super.onCreate(); }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        Log.i("xxx", "onHandleIntent fired");
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(url));
        dm.enqueue(req);
    }

    public static void changeAlarm(Context context, boolean on) {
        Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        if (on) {
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        }
        else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}