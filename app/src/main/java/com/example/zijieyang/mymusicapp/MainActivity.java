package com.example.zijieyang.mymusicapp;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar; //导入类！！
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private String[] lvs = {"List Item 01", "List Item 02", "List Item 03", "List Item 04"};
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter arrayAdapter;
    private ListView lvLeftMenu;
    private Button login_btn,setting_btn,next_btn,play_btn;
    private MediaPlayer mediaPlayer;
    private ImageButton disLike_btn,like_btn;
    public ImageView backImage,pauseBtn_image;
    private Handler mHandler = new Handler();
    private MediaService.MyBinder mMyBinder;
    private SeekBar mSeekBar;
    private TextView mTextView;

    //进度条下面的当前进度文字，将毫秒化为m:ss格式
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");
    //“绑定”服务的intent
    Intent MediaServiceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn = findViewById(R.id.login_btn);
        setting_btn = findViewById(R.id.setting_btn);
        //next_btn = findViewById(R.id.next_btn);
        play_btn = findViewById(R.id.play_btn);
        disLike_btn = findViewById(R.id.disLike_btn);
        like_btn = findViewById(R.id.like_btn);
        backImage = findViewById(R.id.backImage);
        pauseBtn_image = findViewById(R.id.pauseBtn_image);
        mSeekBar = findViewById(R.id.mSeekBar);
        mTextView = findViewById(R.id.mTextView);


        play_btn.setOnClickListener(music_click);
        backImage.setOnClickListener(music_click);
        like_btn.setOnClickListener(music_click);

        /*
            以下是实现toolbar以及侧边滑动的一些设置
         */
        toolbar  = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvLeftMenu = (ListView) findViewById(R.id.dl_left_menu); //侧边菜单
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MediaServiceIntent = new Intent(this, MediaService.class);


        /*toolbar.setTitle("主标题");
        toolbar.setSubtitle("副标题");
        还可以代码设置标题颜色
        toolbar.setSubtitleTextColor(Color.WHITE);
        设置logo。您要注意logo与导航位置图标的区别
        toolbar.setLogo(R.drawable.test);

        添加导航位置图标
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drawerLayout.openDrawer(Gravity.LEFT);
                //Toast.makeText(MainActivity.this, "aaaaa", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*
           侧滑菜单关闭/打开监听
         */
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.syncState();
        drawerLayout.addDrawerListener(mDrawerToggle);
        //设置菜单列表
        //arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        //lvLeftMenu.setAdapter(arrayAdapter);

        /*
            给菜单按钮设置监听
         */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this,pickActivity.class);
                Bundle bundle = new Bundle();

                intent.putExtras(bundle); //传数据
                startActivity(intent);
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.pick:
                        msg += "search";
                        break;
                }

                if(!msg.equals("")) {
                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,loginActivity.class);
                Bundle bundle = new Bundle();

                intent.putExtras(bundle); //传数据
                startActivity(intent);
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,settingActivity.class);
                Bundle bundle = new Bundle();

                intent.putExtras(bundle); //传数据
                startActivity(intent);
            }
        });


        //判断权限够不够，不够就给
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        } else {
            //够了就设置路径等，准备播放
            bindService(MediaServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        }
    }

        //获取到权限回调方法
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        bindService(MediaServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
                    } else {
                        Toast.makeText(this, "权限不够获取不到音乐，程序将退出", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                default:
                    break;
            }
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
//            mMediaService = ((MediaService.MyBinder) service).getInstance();
            mSeekBar.setMax(mMyBinder.getProgress());

            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
                    if(fromUser){
                        mMyBinder.seekToPositon(seekBar.getProgress());
//                    mMediaService.mMediaPlayer.seekTo(seekBar.getProgress());
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

            Log.d(TAG, "Service与Activity已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };






    /*
        显示toolbar上设置按钮的
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    private View.OnClickListener music_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.play_btn:
                    mMyBinder.playMusic();
                    break;
                case R.id.backImage:
                    if(pauseBtn_image.getVisibility() == View.VISIBLE){//返回值为0，visible；返回值为4，invisible；返回值为8，gone
                        mMyBinder.playMusic();
                        pauseBtn_image.setVisibility(View.INVISIBLE);
                    }
                    else if(pauseBtn_image.getVisibility() == View.INVISIBLE){
                        pauseBtn_image.setVisibility(View.VISIBLE);
                        mMyBinder.pauseMusic();
                    }
                    break;
                case R.id.like_btn:
                    pauseBtn_image.setVisibility(View.INVISIBLE);
                     mMyBinder.nextMusic();
                     break;
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //我们的handler发送是定时1000s发送的，如果不关闭，MediaPlayer release掉了还在获取getCurrentPosition就会爆IllegalStateException错误
        mHandler.removeCallbacks(mRunnable);

        mMyBinder.closeMedia();
        unbindService(mServiceConnection);
    }

    /**
     * 更新ui的runnable
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSeekBar.setProgress(mMyBinder.getPlayPosition());
            mTextView.setText(time.format(mMyBinder.getPlayPosition()) + "s");
            mHandler.postDelayed(mRunnable, 1000);
        }
    };


    /**
    * 播放音乐
     */
    /*protected void play() {
        String path = musicPath[0];
        File file = new File(path);
        //Toast.makeText(MainActivity.this, "点击播放", Toast.LENGTH_SHORT).show();
        if (file.exists() && file.length() > 0) {
            try {
                mediaPlayer = new MediaPlayer();
                // 设置指定的流媒体地址
                mediaPlayer.setDataSource(path);
                //mediaPlayer=MediaPlayer.create(this, R.raw.love);
                // 设置音频流的类型
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                // 通过异步的方式装载媒体资源
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                     public void onPrepared(MediaPlayer mp) {
                        // 装载完毕 开始播放流媒体
                        mediaPlayer.start();
                        Toast.makeText(MainActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
                        // 避免重复播放，把播放按钮设置为不可用
                        play_btn.setEnabled(false);
                    }
                 });
                // 设置循环播放
                //mediaPlayer.setLooping(true);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                     public void onCompletion(MediaPlayer mp) {
                        // 在播放完毕被回调
                        play_btn.setEnabled(true);
                    }
                 });

                mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                    @Override
                     public boolean onError(MediaPlayer mp, int what, int extra) {
                        // 如果发生错误，重新播放
                        replay();
                        return false;
                    }
                 });

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "播放失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
        }
    }*/

    /*protected void play(){
        mediaPlayer = new MediaPlayer();
        Toast.makeText(MainActivity.this, "点击播放", Toast.LENGTH_SHORT).show();
        mediaPlayer = MediaPlayer.create(this,R.raw.love);
        mediaPlayer.start();
    }
     *//**
       * 暂停
       *//*
     protected void pause() {
         if (pauseBtn_image.getVisibility() == View.VISIBLE) {
             pauseBtn_image.setVisibility(View.GONE);
             //btn_pause.setText("暂停");
             mediaPlayer.start();
             Toast.makeText(this, "继续播放", Toast.LENGTH_SHORT).show();
             return;
             }

         if (mediaPlayer != null && mediaPlayer.isPlaying()) {
             mediaPlayer.pause();
             pauseBtn_image.setVisibility(View.VISIBLE);
             //btn_pause.setText("继续");
             Toast.makeText(this, "暂停播放", Toast.LENGTH_SHORT).show();
         }
     }*/

}
