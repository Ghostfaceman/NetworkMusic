package com.example.networkmusic.player;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.networkmusic.model.Song;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * 该类使用了单例模式(只有一个实例)
 *
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {
    //单例模式
    private static MusicPlayer player = new MusicPlayer();
    //媒体播放器
    private MediaPlayer mMediaPlayer;
    //
    private Context mContext;
    //歌单
    private List<Song> mQueue;
    //歌单下标
    private int mQueueIndex;
    //队列列表的播放方式
    private PlayMode mPlayMode;

    /**
     * 歌曲播放的方式
     */
    private enum PlayMode {
        //循环、随机、重复
        LOOP, RANDOM, REPEAT
    }

    /**
     * 拿到音乐播放器
     * @return
     */
    public static MusicPlayer getPlayer() {
        return player;
    }

    public static void setPlayer(MusicPlayer player) {
        MusicPlayer.player = player;
    }

    public MusicPlayer() {

        mMediaPlayer = new ManagedMediaPlayer();

        mMediaPlayer.setOnCompletionListener(this);
        //初始化当前歌单
        mQueue = new ArrayList<>();
        mQueueIndex = 0;
        //设置播放方式为循环播放
        mPlayMode = PlayMode.LOOP;
    }

    /**
     * 用户点击歌曲
     * @param queue
     * @param index
     */
    public void setQueue(List<Song> queue, int index) {
        mQueue = queue;
        mQueueIndex = index;
        play(getNowPlaying());
    }

    /**
     * 播放音乐
     * @param song
     */
    public void play(Song song) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(song.getPath());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        mMediaPlayer.pause();
    }

    /**
     * 重新开始播放
     */
    public void resume() {
        mMediaPlayer.start();
    }

    /**
     * 播放下一首
     */
    public void next() {
        play(getNextSong());
    }

    public void previous() {
        play(getPreviousSong());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    private Song getNowPlaying() {
        if (mQueue.isEmpty()) {
            return null;
        }
        return mQueue.get(mQueueIndex);
    }

    private Song getNextSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getNextIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }

    private Song getPreviousSong() {
        if (mQueue.isEmpty()) {
            return null;
        }
        switch (mPlayMode) {
            case LOOP:
                return mQueue.get(getPreviousIndex());
            case RANDOM:
                return mQueue.get(getRandomIndex());
            case REPEAT:
                return mQueue.get(mQueueIndex);
        }
        return null;
    }


    public int getCurrentPosition() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public int getDuration() {
        if (getNowPlaying() != null) {
            return mMediaPlayer.getDuration();
        }
        return 0;
    }

    public PlayMode getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    /**
     * 队列的下一首下标
     * @return
     */
    private int getNextIndex() {
        mQueueIndex = (mQueueIndex + 1) % mQueue.size();
        return mQueueIndex;
    }

    /**
     * 队列的上一首下标
     * @return
     */
    private int getPreviousIndex() {
        mQueueIndex = (mQueueIndex - 1) % mQueue.size();
        return mQueueIndex;
    }

    /**
     * 随机队列中的一首歌
     * @return
     */
    private int getRandomIndex() {
        mQueueIndex = new Random().nextInt(mQueue.size()) % mQueue.size();
        return mQueueIndex;
    }

    /**
     * 释放播放资源
     */
    private void release() {
        mMediaPlayer.release();
        mMediaPlayer = null;
        mContext = null;
    }
}
