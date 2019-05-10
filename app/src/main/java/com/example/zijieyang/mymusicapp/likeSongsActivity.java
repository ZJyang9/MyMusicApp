package com.example.zijieyang.mymusicapp;
/**
 *  喜欢的歌
 */

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

public class likeSongsActivity extends AppCompatActivity {
    private Toolbar collect_toolbar;
    private ImageButton collectPlayBtn;
    private ImageView cover_phtoto;
    private RecyclerView recyclerView_collect;
    private SeekBar collectSeekBar;
    private TextView singer_name,song_name;
    public  collectAdapter collectAdapter;
    public  List<String> songNameList,singerNameList,dialogList;
    public  List<Integer> iconList;
    private ArrayList<String> musicPathList;
    private Handler mHandler = new Handler();
    public  collectService.MyBinder mMyBinder;
    public static likeSongsActivity instance = null;
    Intent collectServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_songs);
        instance = this;

        initView();
        initData();
        startService(musicPathList);

        setSupportActionBar(collect_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collect_toolbar.setNavigationIcon(R.mipmap.pick_back);
        collect_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collectPlayBtn.setOnClickListener(collectClick);

        collect_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectService.playTimer.cancel();
                finish();
            }
        });

    }


    private View.OnClickListener collectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.collectPlayBtn:
                    break;
            }
        }
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            collectSeekBar.setProgress(mMyBinder.getPlayPosition());
            collectSeekBar.setMax(mMyBinder.getProgress());
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    /**
     *  开启服务
     */
    public void startService (ArrayList<String> musicPath){
        Log.i("","进入startService");
        collectServiceIntent = new Intent(likeSongsActivity.this,collectService.class);
        Bundle collectServiceBundle = new Bundle();
        collectServiceBundle.putStringArrayList("musicPath",musicPath);
        collectServiceIntent.putExtras(collectServiceBundle);
        bindService(collectServiceIntent, serviceConnection, BIND_AUTO_CREATE);

    }
    /**
     这里是与服务连接，与音乐播放相关
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (collectService.MyBinder) service;
            collectSeekBar.setMax(mMyBinder.getProgress()); //设置最大的进度条
            //监听进度条的变化
            collectSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
                    if(fromUser){
                        mMyBinder.seekToPositon(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mHandler.post(mRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public void initData(){
        songNameList   = new ArrayList<>();
        songNameList.add("一路向北");
        songNameList.add("简单爱");
        singerNameList = new ArrayList<>();
        singerNameList.add("Jay Chou");
        singerNameList.add("Jay Chou");
        musicPathList  = new ArrayList<>();
        musicPathList.add("http://fs.android2.kugou.com/7faa828db38bd7b10a657aba3997aa9e/5cd3f9fe/G008/M06/0B/04/SA0DAFUOcVmAbRygABQaD6R2Yhk080.m4a");
        musicPathList.add("http://fs.android2.kugou.com/15b995e58720f89ace460100dd72c53b/5cd3f9fe/G141/M04/09/05/bZQEAFt2xNGAZ3gWABACoicJxAE618.m4a");

        dialogList = new ArrayList<>();
        dialogList.add("复制歌曲信息");
        dialogList.add("从记录中删除");

        iconList   = new ArrayList<>();
        iconList.add(R.mipmap.dislike);
        iconList.add(R.mipmap.like);


        recyclerView_collect.setLayoutManager(new LinearLayoutManager(this));
        collectAdapter = new collectAdapter(likeSongsActivity.this,songNameList,singerNameList,musicPathList);
        recyclerView_collect.setAdapter(collectAdapter);


    }

    public void initView(){
        collect_toolbar      = findViewById(R.id.collect_toolbar);
        collectPlayBtn       = findViewById(R.id.collectPlayBtn);
        recyclerView_collect = findViewById(R.id.recyclerView_collect);
        collectSeekBar       = findViewById(R.id.collectSeekBar);



    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("","likeSongsActivity destroy");
        mHandler.removeCallbacks(mRunnable);
        mMyBinder.closeMedia();
        unbindService(serviceConnection);
    }
}
