package com.example.networkmusic.event;

import com.example.networkmusic.model.Song;

import java.util.List;


public class PlayEvent {
    //用户行为获取
    public enum Action {
        PLAY, STOP, RESUME, NEXT, PREVIOES, SEEK
    }

    private Action mAction;
    private Song mSong;
    private List<Song> mQueue;
    private int seekTo;

    public Song getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    public List<Song> getQueue() {
        return mQueue;
    }

    public void setQueue(List<Song> queue) {
        mQueue = queue;
    }

    public int getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(int seekTo) {
        this.seekTo = seekTo;
    }
}
