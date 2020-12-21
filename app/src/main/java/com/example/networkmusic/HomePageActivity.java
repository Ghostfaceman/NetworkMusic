package com.example.networkmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.networkmusic.event.PlayEvent;
import com.example.networkmusic.model.Song;
import com.example.networkmusic.musicApiUtil.UrlParseJsonUtil;
import com.example.networkmusic.player.MusicPlayer;
import com.kelin.banner.BannerEntry;
import com.kelin.banner.transformer.GalleryTransformer;
import com.kelin.banner.view.BannerView;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private boolean loadFinish = false;
    private static BannerView bannerView;
    private List<MusicBean> musicBeans = new ArrayList<>();
    //创建一个MediaPlayer对象
    MediaPlayer mp3 = new MediaPlayer();
    private PlayEvent playEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        bannerView = findViewById(R.id.push_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bannerView.setShowLeftAndRightPage(30, true, new GalleryTransformer());
        } else {
            bannerView.setShowLeftAndRightPage(20);
        }

        bannerView.setOnPageClickListener(new BannerView.OnPageClickListener() {

            @Override
            public void onPageClick(BannerEntry entry, int index) {
                //这首音乐的下载地址
                String url = (String) entry.getValue();

                //播放
                Toast.makeText(HomePageActivity.this, "播放", Toast.LENGTH_SHORT).show();
                playEvent = new PlayEvent();
                //拿到播放器播放音乐
                MusicPlayer.getPlayer().play(getSong(url));
                //播放一次后加入到播放队列
                List<Song> queue = new ArrayList<>();
                queue.add(getSong(url));
                //播放事件设置PLAY
                playEvent.setAction(PlayEvent.Action.PLAY);
                //设置当前播放队列
                playEvent.setQueue(queue);
                //线程安全
                EventBus.getDefault().post(playEvent);
            }
        });

    }

    private Song getSong(String url) {
        Song song = new Song();
        song.setPath(url);
        return song;
    }



    @Override
    protected void onStart() {
        super.onStart();
        new Thread(){
            @Override
            public void run() {
                for(int i = 5;i>0;i--) {
                    String jsonString = UrlParseJsonUtil.getWebString("https://api.uomg.com/api/rand.music?sort=热歌榜&format=json");
                    MusicBean musicBean = UrlParseJsonUtil.paseJsonObject(jsonString);
                    musicBeans.add(musicBean);
                }
            loadFinish = true;
            }
        }.start();
        while (true){
            if(loadFinish){
                bannerView.setEntries(getImageBannerEntries(musicBeans));
                bannerView.selectCenterPage(2);
                break;
            }
        }
    }


    @NonNull
    private List<ImageBannerEntry> getImageBannerEntries(List<MusicBean> musicBeans) {
        List<ImageBannerEntry> items = new ArrayList<>();
        for(MusicBean m:musicBeans){
            items.add(new ImageBannerEntry(m.getPicurl(),m.getName(),m.getArtistsname(),m.getUrl()));
        }

        return items;
    }

}