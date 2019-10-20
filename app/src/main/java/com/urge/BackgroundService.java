package com.urge;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import java.util.Objects;
import java.util.Random;

public class BackgroundService extends Service{

    @SuppressLint("CheckResult")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        try {
            ((AudioManager) Objects.requireNonNull(
                    getSystemService(Context.AUDIO_SERVICE))).setStreamMute(AudioManager.STREAM_SYSTEM, false);
        } catch (Exception e) {
            e.printStackTrace();
        }


        unmute();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    private void unmute() {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
       if (amanager != null) {
//            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
//            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
//            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
//            amanager.setStreamMute(AudioManager.STREAM_RING, false);
//            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
//            amanager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
            amanager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            //amanager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
            amanager.setStreamVolume(AudioManager.STREAM_RING,amanager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
            amanager.setStreamVolume(AudioManager.STREAM_MUSIC,amanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);

        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        PendingIntent service =
                PendingIntent.getService(getApplicationContext(), new Random().nextInt(),
                        new Intent(getApplicationContext(), BackgroundService.class), PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 1000, service);
        super.onTaskRemoved(rootIntent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

