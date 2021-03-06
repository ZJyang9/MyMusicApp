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
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
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
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v7.widget.Toolbar; //导入类！！
import android.widget.Toast;


import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

/***
 *  大段注释
 */
/*
    大段中小段注释
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter arrayAdapter;
    private ListView lvLeftMenu;
    private Button login_btn,setting_btn,next_btn;
    private MediaPlayer mediaPlayer;
    private ImageButton disLike_btn,like_btn,play_btn,seek_next_btn;
    public  ImageView backImage,pauseBtn_image;
    private Handler mHandler = new Handler();
    private MediaService.MyBinder mMyBinder;
    private SeekBar mSeekBar;
    private TextView now_time,remaining_time,likeSongs_text,song_name,singer_name;
    private RelativeLayout seek_play;
    private List<back_imageBean> backImageList  = new ArrayList<>();
    private List<back_imageBean> myPhotoUrlList = new ArrayList<>();//背景图片数据列表
    private List<String> mySongNameList         = new ArrayList<>();//歌曲名字数据列表
    private List<String> mySingerNameList       = new ArrayList<>();//歌手名字数据列表
    private back_imageAdapter bkImgadapter; //背景图片适配器
    private SwipeFlingAdapterView flingContainer;
    //1、获取 OkHttpClient 对象
    private OkHttpClient mHttpClient            = new OkHttpClient();
    //进度条下面的当前进度文字，将毫秒化为m:ss格式
    private SimpleDateFormat time               = new SimpleDateFormat("m:ss");
    //“绑定”服务的intent
    Intent MediaServiceIntent;
    private final int PHOTO_URL_LIST   = 0;//handler处理
    private final int SONG_NAME_LIST   = 1;
    private final int SINGER_NAME_LIST = 2;
    private String[] image = new String[]{"http://imge.kugou.com/v2/mobile_portrait/51839374b4361c4a03e95237fd2aec88.jpg",
            "http://imge.kugou.com/v2/mobile_portrait/ea300406c1dcd774d76481e2f74c8724.jpg",
            "http://imge.kugou.com/v2/mobile_portrait/3cdefac43e690d7c04e6d41d82bc9955.jpg"};
    private Handler newHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            System.out.print("进入handler");
            switch (msg.what) {
                case PHOTO_URL_LIST:
                    myPhotoUrlList.addAll((List<back_imageBean>) msg.obj);
                    break;
                //myphotoUrlList = (List<back_imageBean>) msg.obj;  //这个方法无法将数据传给myphotoUrlList
                case SONG_NAME_LIST:
                    mySongNameList.addAll((List<String>) msg.obj);
                    break;
                case SINGER_NAME_LIST:
                    mySingerNameList.addAll((List<String>) msg.obj);
                    break;
            }
            System.out.print("歌曲名字列表" + SONG_NAME_LIST);
            System.out.print("歌手名字列表" + SINGER_NAME_LIST);
            bkImgadapter.notifyDataSetChanged();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("开始进入");
        initView();//初始化控件
        onPost("http://172.17.2.105:7000/requestmusic");//打开app请求数据

        swipeCard();

        play_btn.setOnClickListener(music_click);
        like_btn.setOnClickListener(music_click);
        disLike_btn.setOnClickListener(music_click);
        seek_next_btn.setOnClickListener(music_click);

        /**
         * 以下是实现toolbar以及侧边滑动的一些设置
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//这句代码让toobar显示
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvLeftMenu = (ListView) findViewById(R.id.dl_left_menu); //侧边菜单
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * 侧滑菜单打开/关闭监听
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
        drawerLayout.addDrawerListener(mDrawerToggle); //添加监听

        /**
         给菜单按钮设置监听，跳转到筛选页面
         */
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, pickActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle); //传数据
                startActivity(intent);
                return true;
            }
        });

        /**
         登录与设置按钮监听
         */
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                Bundle bundle = new Bundle();

                intent.putExtras(bundle); //传数据
                startActivity(intent);
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, settingActivity.class);
                Bundle bundle = new Bundle();

                intent.putExtras(bundle); //传数据
                startActivity(intent);
            }
        });
        /*likeSongs_text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,likeSongsActivity.class);
                Bundle bundle = new Bundle();

                intent.putExtras(bundle); //传数据
                startActivity(intent);
            }
        });*/
        System.out.println(" oncreate最后");

    }
    /**
     *  下面是写左右滑动的逻辑
     */

    private void swipeCard(){
        bkImgadapter = new back_imageAdapter(MainActivity.this, myPhotoUrlList,mySongNameList,mySingerNameList); //背景图适配器
        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);
        flingContainer.setAdapter(bkImgadapter);

        /*
            监听，分别是移除第一项、左滑、右滑、图片数组为空、左滑右滑上的图标透明度显示
        */
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                myPhotoUrlList.remove(0);
                mySongNameList.remove(0);
                mySingerNameList.remove(0);
                bkImgadapter.notifyDataSetChanged();
                mMyBinder.nextMusic();//切换图片就下一首歌
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
            }

            /*
                当数据为空的时候
             */
            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                onPost("http://172.17.2.105:7000/requestmusic");//打开app请求数据
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                try {
                    View view = flingContainer.getSelectedView();
                    view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                    view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /*
            给每项设置点击监听
         */
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "点击了背景图片", Toast.LENGTH_SHORT).show();
                if (pauseBtn_image.getVisibility() == View.VISIBLE) {//返回值为0，visible；返回值为4，invisible；返回值为8，gone
                    mMyBinder.playMusic();
                    pauseBtn_image.setVisibility(View.INVISIBLE);
                } else if (pauseBtn_image.getVisibility() == View.INVISIBLE) {
                    pauseBtn_image.setVisibility(View.VISIBLE);
                    mMyBinder.pauseMusic();
                }
                //Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });



    }


    /**
     *  开启服务
     */
    private void startService(ArrayList<String> musicPath){
        MediaServiceIntent = new Intent(MainActivity.this,MediaService.class);
        Bundle MediaServiceBundle = new Bundle();
        MediaServiceBundle.putStringArrayList("musicPath",musicPath);
        MediaServiceIntent.putExtras(MediaServiceBundle);
        bindService(MediaServiceIntent, serviceConnection, BIND_AUTO_CREATE);

    }
    /**
     这里是与服务连接，与音乐播放相关
     */
    private  ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
            mSeekBar.setMax(mMyBinder.getProgress()); //设置最大的进度条
            //监听进度条的变化
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

            Log.d(TAG, "Service与Activity已连接");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    /**
        这里是点击like or dislike 按钮左右滑动的逻辑
     */
        public void right() {
            flingContainer.getTopCardListener().selectRight();
        }

        public void left() {
            flingContainer.getTopCardListener().selectLeft();
        }

        //获取到权限回调方法
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[]permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case 1:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        bindService(MediaServiceIntent, serviceConnection, BIND_AUTO_CREATE);
                    } else {
                        Toast.makeText(this, "权限不够获取不到音乐，程序将退出", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                default:
                    break;
            }
    }




    /**
        显示toolbar上菜单按钮的
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    /**
        音乐相关点击监听
     */
    private View.OnClickListener music_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.play_btn:
                    //点击播放按钮 完整版播放样式显示 like & dislike隐藏
                    like_btn.setVisibility(View.INVISIBLE);
                    disLike_btn.setVisibility(View.INVISIBLE);
                    seek_play.setVisibility(View.VISIBLE);
                    play_btn.setVisibility(View.INVISIBLE);
                    mMyBinder.playMusic();
                    break;

                case R.id.like_btn:
                    //点击喜欢按钮，切换歌曲暂停按钮需要隐藏
                    pauseBtn_image.setVisibility(View.INVISIBLE);
                    mMyBinder.nextMusic();
                    right();
                    break;
                case R.id.disLike_btn:
                    //点击不喜欢按钮，切换歌曲暂停按钮需要隐藏
                    pauseBtn_image.setVisibility(View.INVISIBLE);
                    mMyBinder.nextMusic();
                    left();
                    break;
                case R.id.seek_next_btn:
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
        unbindService(serviceConnection);
    }

    /**
     * 更新ui的runnable 改变进度条以及时间
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mSeekBar.setProgress(mMyBinder.getPlayPosition()); //进度条跟着播放位置走
            now_time.setText(time.format(mMyBinder.getPlayPosition())); //目前播放时间
            remaining_time.setText(time.format(mMyBinder.getProgress()));//总时间
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

    /**
        okHttp post提交数据
     */
    public void onPost(String url) {
        Log.i("","进入到Post函数");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        final String jsonStr = "{\"user_id\":\"2\"}";//1、json数据写成字符串形式.
        RequestBody requestBody = RequestBody.create(JSON, jsonStr);//2、构造RequestBody
        FormEncodingBuilder builder = new FormEncodingBuilder();
        //3、构造Request
        final Request request = new Request
                                        .Builder()
                                        .post(requestBody)
                                        .url(url)
                                        .build();

        //4、将 Request 封装成 call
        final Call call = mHttpClient.newCall(request);
        //5、执行 call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                //Log.e(TAG, "onFailure");
//                Toast.makeText(MainActivity.this, "失败", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                Log.i("","进入到response");
                if (response.isSuccessful()) {
                    Log.i("","进入到response成功");
                    final String res = response.body().string();
                    final MusicInfo musicInfo = new Gson().fromJson(res,MusicInfo.class); //通过Gson解析服务器返回的json数据
                    System.out.print("musicInfo = " + musicInfo);
                    backImageList = new ArrayList<>();//背景图片
                    List<String> songNameList = new ArrayList<>();//歌曲名字
                    List<String> singerNameList = new ArrayList<>();//歌手名字
                    //循环获取json数据中不同的具体数据
                    for(int i = 0;i < musicInfo.getData().size();i++){
                        backImageList.add(new back_imageBean(musicInfo.getData().get(i).photo_url.get(0)));
                        songNameList.add(musicInfo.getData().get(i).getSong_name());
                        singerNameList.add(musicInfo.getData().get(i).getSinger_name());
                    }

                    /*Message message = new Message();
                    message.obj = backImageList;
                    message.what = type;
                    newHandler.sendMessage(message);*/
                    sendToHandler(backImageList,PHOTO_URL_LIST);
                    sendToHandler(songNameList,SONG_NAME_LIST);
                    sendToHandler(singerNameList,SINGER_NAME_LIST);

                    /*
                        获取Post返回的数据中的音乐播放路径，存储在列表中
                     */
                    ArrayList<String> mymusicPath = new ArrayList<>();
                    for(int i =0; i < musicInfo.getData().size();i++){
                        mymusicPath.add(musicInfo.getData().get(i).getPlay_url());
                        Log.i("歌曲路径 ", mymusicPath.get(i));

                    }
                    startService(mymusicPath);
                    //onResponse 方法不能直接操作 UI 线程，利用 runOnUiThread 操作 ui
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mTextView.setText(newMusic.url);
                            Log.i(" ", res);
                            Log.i(" ", musicInfo.getData().get(0).getPlay_url());
                            //textView2.setText(res);
                        }
                    });*/
                }
            }
        });

    }

    /**
        okHttp Get请求数据
     */
    public void onGet(){
        //okHttp的基本使用 --- get方法
        String url = "http://172.17.2.105:7000/test";
        //1,创建OKHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //2,创建一个Request
        Request request = new Request.Builder().url(url).build();
        //3,创建一个call对象
        Call call = mOkHttpClient.newCall(request);
        //4,将请求添加到调度中
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {


            }
            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String res = response.body().string();
                    // 利用runOnUiThread改变ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mTextView.setText(res);
                        }
                    });

                }
            }

        });
    }
    /**
     * 传递数据给handler,参数object表示传递的数据, type表示传给handler中哪个列表
     */
    public void sendToHandler(Object object ,Integer type){
        Message message = new Message();
        message.obj = object;
        message.what = type;
        newHandler.sendMessage(message);
    }
    /**
     * 初始界面控件
     */
    public void initView(){
        System.out.print("初始化控件");
        login_btn       = findViewById(R.id.login_btn);       //登录按钮
        setting_btn     = findViewById(R.id.setting_btn);     //设置按钮
        play_btn        = findViewById(R.id.play_btn);        //播放按钮
        disLike_btn     = findViewById(R.id.disLike_btn);     //左滑按钮
        like_btn        = findViewById(R.id.like_btn);        //右滑按钮
        flingContainer  = findViewById(R.id.frame);
        pauseBtn_image  = findViewById(R.id.pauseBtn_image);  //暂停icon
        mSeekBar        = findViewById(R.id.mSeekBar);        //进度条
        now_time        = findViewById(R.id.now_time);        //目前时间
        remaining_time  = findViewById(R.id.remaining_time);  //剩余时间修改 → 总时间
        seek_play       = findViewById(R.id.seek_play);       //完整版样式布局
        seek_next_btn   = findViewById(R.id.seek_next_btn);   //完整版下一首
        song_name       = findViewById(R.id.song_name);       //歌曲名字
        singer_name       = findViewById(R.id.singer_name);   //歌手名字
        //likeSongs_text  = findViewById(R.id.likeSongs_text);

    }
}
