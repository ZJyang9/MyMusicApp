package com.example.zijieyang.mymusicapp;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.support.constraint.Constraints.TAG;

public class collectService extends Service {
    public static Timer playTimer;
    public static TimerTask playTimerTask;
    private int currentPosition; //现在播放位置的时间
    private collectService.MyBinder mBinder = new collectService.MyBinder();

    public static MediaPlayer mMediaPlayer  = new MediaPlayer();
    public static List<String> musicPath    = new ArrayList<>();
    public collectService instance = null;
    private int i = 0;



    public collectService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.print("进入到bind");
        musicPath = intent.getExtras().getStringArrayList("musicPath");

        return mBinder;
    }

    public class MyBinder extends Binder {
        /**
         * 正常播放音乐,有模式判断
         */
        public void playMusic() {
            Log.d(TAG, "时间监控取消2");
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
         * 暂停播放
         */
        public void pauseMusic() {
            if (mMediaPlayer.isPlaying()) {
                //如果还开始播放，就暂停
                mMediaPlayer.pause();
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
            if(i <= musicPath.size() - 1 ){
                i++;
            }
            else{
                i = 0;
            }
            iniMediaPlayerFile(i);  //准备

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
        /**
         * 添加file文件到MediaPlayer对象并且准备播放音频
         */
        public void iniMediaPlayerFile(int dex) {
            try {

                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(musicPath.get(dex));
                mMediaPlayer.prepare();
                mBinder.playMusic();
            } catch (IOException e) {
                Log.d(TAG, "设置资源，准备阶段出错");
                e.printStackTrace();
            }

        }
    }

}
