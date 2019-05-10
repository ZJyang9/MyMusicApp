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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MediaService extends Service {
    private static final String TAG = "MediaService";
    public static Timer mTimer; //30s高潮的Timer
    public Timer playTimer = new Timer();
    public static TimerTask mTimerTask,playTimerTask;//完整播放的Timer
    private int currentPosition; //现在播放位置的时间
    private MyBinder mBinder = new MyBinder();
    //初始化MediaPlayer
    public static MediaPlayer mMediaPlayer;
    //标记当前歌曲的序号
    private int i = 0;
    public static List<String> musicPath = new ArrayList<>();
    public List<String> startTimeList = new ArrayList<>();


    public MediaService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.print("进入到bind");
        musicPath = intent.getExtras().getStringArrayList("musicPath");
        startTimeList = intent.getExtras().getStringArrayList("startTimeList");
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
         * 正常播放音乐,有模式判断
         */
        public void playMusic() {
            mTimer.cancel();
            mTimer = null;
            Log.d(TAG, "时间监控取消");
            Log.i("","mTimer还在不在：" + mTimer);
            /*
             * 判断播放模式，加上判断是为了，当在完整版播放结束后能够循环或者下一首
             * 为了能够在点击模式按钮时调用
             */
            if(!mMediaPlayer.isPlaying()){
                mMediaPlayer.start();
            }
            playTimer = new Timer();
            playTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if(mMediaPlayer.isPlaying()){
                        currentPosition = mMediaPlayer.getCurrentPosition();
                        //循环模式
                        if(MainActivity.instance.isCycle == true){
                            mMediaPlayer.setLooping(true);
                        }
                        //顺序模式
                        else {
                            mMediaPlayer.setLooping(false);
                            if(currentPosition <= mMediaPlayer.getDuration() && currentPosition >= mMediaPlayer.getDuration() - 300){
                                MainActivity.instance.left();
                                playTimer.cancel();
                            }
                        }
                    }
                }
            };
            playTimer.schedule(playTimerTask, 0, 10);
        }
        /**
         *  当点击图片暂停时，再点击时，播放音乐的逻辑
         *  与上面的区别是，点击暂停后再点击同样取消了30s高潮的监听，而下面的逻辑不会取消，会继续监听
         *  同样有模式判断
         *
         */
        public void playMusicTwo() {
            if(!mMediaPlayer.isPlaying()){
                mMediaPlayer.start();
            }
            playTimer = new Timer();
            playTimerTask = new TimerTask() {
                @Override
                public void run() {
                    if(mMediaPlayer.isPlaying()){
                        currentPosition = mMediaPlayer.getCurrentPosition();
                        //循环模式
                        if(MainActivity.instance.isCycle == true){
                            mMediaPlayer.setLooping(true);
                        }
                        //顺序模式
                        else {
                            mMediaPlayer.setLooping(false);
                            if(currentPosition == mMediaPlayer.getDuration()){
                                MainActivity.instance.left();
                                playTimer.cancel();
                            }
                        }
                    }
                }
            };
            playTimer.schedule(playTimerTask, 0, 1000);
        }
        /**
         * 暂停播放
         */
        public void pauseMusic() {
            if (mMediaPlayer.isPlaying()) {
                //如果还开始播放，就暂停
                mMediaPlayer.pause();
                MainActivity.instance.pauseBtn_image.setVisibility(View.VISIBLE);
            }
        }
        /**
         *  停止播放
         */
        public void stopMuisc(){
            mMediaPlayer.stop();
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
            // 播放逻辑
            //切换歌曲reset()很重要很重要很重要，没有会报IllegalStateException
            mMediaPlayer.reset();
            musicPath.remove(0);
            startTimeList.remove(0);
            iniMediaPlayerFile(i);  //准备

        }
        /**
         *  30s高潮播放逻辑
         */
        public void climaxMusic(){
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

        mMediaPlayer.seekTo(Integer.parseInt(startTimeList.get(0)));//首次播放的歌，指定位置为数组第一项的开始时间
        mMediaPlayer.start();
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mMediaPlayer.isPlaying() && mTimer!= null && mMediaPlayer!=null) {
                    Log.i("","进入到TimerTask");
                    currentPosition = mMediaPlayer.getCurrentPosition();//当视频播放的时间点和指定结束点相同时暂停视频
                    //下面是 循环or顺序 模式的情况
                    if(MainActivity.instance.isCycle == true){
                        if (currentPosition >= ( Integer.parseInt(startTimeList.get(0))) + 11000 ) {
                            mMediaPlayer.seekTo(Integer.parseInt(startTimeList.get(0)));//回到从高潮起始部分
                            mMediaPlayer.start();
                        }
                        //这里是为了当用户点击图片暂停，再点击播放的情况，mTimer会失效 所以加了播放位置的判断
                        else if (!mMediaPlayer.isPlaying() && currentPosition >= ( Integer.parseInt(startTimeList.get(0))) + 11000 ){
                            mTimer.cancel();
                            mTimer = null;
                        }
                    }
                    else {
                        if (currentPosition >= ( Integer.parseInt(startTimeList.get(0))) + 11000 ) {
                            Log.i("","高潮听完了，下一首");
                            MainActivity.instance.left();//高潮播完就下一首,并且切换图片
                        }
                        //这里是为了当用户点击图片暂停，再点击播放的情况，mTimer会失效 所以加了播放位置的判断
                        else if (!mMediaPlayer.isPlaying() && currentPosition >= ( Integer.parseInt(startTimeList.get(0))) + 11000 ){
                            mTimer.cancel();
                            mTimer = null;
                        }
                    }
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);//每10ms就run一次
    }
    /**
     * 添加file文件到MediaPlayer对象并且准备播放音频
     */
    public void iniMediaPlayerFile(int dex) {
        mMediaPlayer = new MediaPlayer();
            try {

                mMediaPlayer.setDataSource(musicPath.get(dex));
                mMediaPlayer.prepare();
                mainStartMusic();
            } catch (IOException e) {
                Log.d(TAG, "设置资源，准备阶段出错");
                e.printStackTrace();
            }

    }


}








