package com.example.networkmusic.player;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.networkmusic.event.PlayEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * 播放音乐服务
 */
public class PlayerService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //接收EventBus post过来的PlayEvent
    public void onEvent(PlayEvent playEvent) {
        switch (playEvent.getAction()) {
            case PLAY:
                MusicPlayer.getPlayer().setQueue(playEvent.getQueue(), 0);
                break;
            case NEXT:
                MusicPlayer.getPlayer().next();
                break;
        }
    }
}
