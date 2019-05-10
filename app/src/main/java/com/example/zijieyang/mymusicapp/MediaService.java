package com.example.zijieyang.mymusicapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MediaService extends Service {
    private static final String TAG = "MediaService";
    private Timer mTimer;
    private TimerTask mTimerTask;
    private int currentPosition; //现在播放位置的时间
    private MyBinder mBinder = new MyBinder();
    //初始化MediaPlayer
    public static MediaPlayer mMediaPlayer = new MediaPlayer();

    //标记当前歌曲的序号
    private int i = 0;
    //歌曲路径
    /*private String[] musicPath = new String[]{
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sounds/test1.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sounds/test2.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sounds/test3.mp3",
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/Sounds/test4.kgm"  //kgm无法识别
    };*/
    public List<String> musicPath = new ArrayList<>();



    public MediaService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.print("进入到bind");
        musicPath = intent.getExtras().getStringArrayList("musicPath");
        iniMediaPlayerFile(i);
        return mBinder;
    }

    public class MyBinder extends Binder {

//        /**
//         *  获取MediaService.this（方便在ServiceConnection中）
//         *
//         * *//*
//        public MediaService getInstance() {
//            return MediaService.this;
//        }*/
        /**
         * 播放音乐
         */
        public void playMusic() {
            Toast.makeText(MediaService.this, "音乐开始播放", Toast.LENGTH_SHORT).show();
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.start();
            }
        }
        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mMediaPlayer.isPlaying()) {
                //如果还开始播放，就暂停
                mMediaPlayer.pause();
            }
        }

        /**
         * reset
         */
        public void resetMusic() {
            if (!mMediaPlayer.isPlaying()) {
                //如果还没开始播放，就开始
                mMediaPlayer.reset();
                iniMediaPlayerFile(i);
            }
        }

        /**
         * 关闭播放器
         */
        public void closeMedia() {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }

        /**
         * 下一首
         */
        public void nextMusic() {

            System.out.print("进入到下一首歌，此时i=" + i );
            //当i增加到歌曲数组的最后一首歌时
            if (i == musicPath.size()-1) {
                i = 0;
            } else {
                i++;
            }
            if (mMediaPlayer != null && i < musicPath.size() && i >= 0) {
                //切换歌曲reset()很重要很重要很重要，没有会报IllegalStateException
                mMediaPlayer.reset();
                iniMediaPlayerFile(i);
                playMusic();


            }
        }

        /**
         * 上一首
         */
        public void preciousMusic() {
            if (mMediaPlayer != null && i < 4 && i > 0) {
                mMediaPlayer.reset();
                //iniMediaPlayerFile(i - 1);
                if (i == 1) {

                } else {

                    i = i - 1;
                }
                playMusic();
            }
        }

        /**
         * 获取歌曲长度
         **/
        public int getProgress() {

            return mMediaPlayer.getDuration();
        }

        /**
         * 获取播放位置
         */
        public int getPlayPosition() {

            return mMediaPlayer.getCurrentPosition();
        }
        /**
         * 播放指定位置
         */
        public void seekToPositon(int msec) {

            mMediaPlayer.seekTo(msec);
        }
    }
    /**
     * 一开始进入界面播放，需要设置30s的时间
     */
    public void mainStartMusic(){
        mMediaPlayer.start();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                if (mMediaPlayer.isPlaying()) {
                    currentPosition = mMediaPlayer.getCurrentPosition();//当视频播放的时间点和指定结束点相同时暂停视频
                    Log.i(""," 当前播放时间： " + currentPosition);
                    if (currentPosition >= 10000) {
                        mMediaPlayer.pause();
                    }
                }
                else if (!mMediaPlayer.isPlaying() && currentPosition >= 10000){
                    mTimer.cancel();
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 10);//每10ms就run一次
    }
    /**
     * 添加file文件到MediaPlayer对象并且准备播放音频
     */
    public void iniMediaPlayerFile(int dex) {
        System.out.print("进入到iniMediaPlayerFile");
            //获取文件路径
            try {
                //此处的两个方法需要捕获IO异常
                //设置音频文件到MediaPlayer对象中
                mMediaPlayer.setDataSource(musicPath.get(dex));
                //注意下面的路径会失效 ， 失效会导致app无法打开
                //mMediaPlayer.setDataSource("https://fsshare.kugou.com/1904261207/wKFaP3vfHO-wcum5ce94XA/1556338035/G140/M08/0D/11/bJQEAFuy0VKAORvqADir7cUwAB4376.mp3");

                //让MediaPlayer对象准备
                mMediaPlayer.prepare();

                mainStartMusic();
            } catch (IOException e) {
                Log.d(TAG, "设置资源，准备阶段出错");
                e.printStackTrace();
            }

    }


}








