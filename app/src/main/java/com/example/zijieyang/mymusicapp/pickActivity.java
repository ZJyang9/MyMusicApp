package com.example.zijieyang.mymusicapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class pickActivity extends AppCompatActivity {

    private RecyclerView recyclerView_one,recyclerView_two,recyclerView_three;
    private String[] symbol_three = new String[]{"华语","欧美","日语","韩语","粤语","小语种"};//language
    private String[] symbol_four = new String[]{"流行","摇滚","R&B","电音","嘻哈","民谣","轻音乐","古风","复古","舞曲","古典","二次元","另类/独立"};//style
    private String[] symbol_two = new String[]{"今年","2018","2017","2016","10'","2000'","90'","更早"};//date
    private String[] symbol_one = new String[]{"翻唱","ACG","影视BGM","3D音乐","丧曲","小众音乐人","抖音"};//Trending Now
    private Button ok_btn,random_btn,button,test_btn;
    private Boolean random_isSelected = false;
    private Toolbar toolbar;
    private TextView pick_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        Intent intent = getIntent();

        /*
            几个标签就写几个适配器
         */
        recyclerView_one   = (RecyclerView) findViewById(R.id.recyclerView_one);
        recyclerView_two   = (RecyclerView) findViewById(R.id.recyclerView_two);
        recyclerView_three = (RecyclerView) findViewById(R.id.recyclerView_three);

        toolbar    = findViewById(R.id.pick_toolbar);
        ok_btn     = findViewById(R.id.ok_btn);
        random_btn = findViewById(R.id.random_btn);
        test_btn   = findViewById(R.id.test_btn);
        pick_title = findViewById(R.id.pick_title); //标题文字

        Typeface typeface = ResourcesCompat.getFont(this, R.font.pingfang_std);
        ok_btn.setTypeface(typeface);

        Typeface typeface1 = ResourcesCompat.getFont(this, R.font.pingfang_light);
        random_btn.setTypeface(typeface1);
        pick_title.setTypeface(typeface);

        initData();

        setSupportActionBar(toolbar);//这句代码让toobar显示
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.pick_back);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(pickActivity.this,  "ok",Toast.LENGTH_SHORT).show();
            }
        });
        random_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                random_isSelected = !random_isSelected;
                if(random_isSelected) {
                    test_btn.setEnabled(false);
                }
                else {
                    test_btn.setEnabled(true);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /*
       初始化数据
     */
    private void initData() {
        List<String> datas_one   = new ArrayList<String>();
        List<String> datas_two   = new ArrayList<String>();
        List<String> datas_three = new ArrayList<String>();
        List<String> datas_four  = new ArrayList<String>();

        /*/
            语言分类的循环添加数据
         */
        for (int i = 5; i >= 0; i--) {
            //Resources res = getResources();
            //datas.add(res.getIdentifier("test" + i, "drawable", getPackageName()));
            datas_one.add(0,symbol_three[i]);
        }

        /*/
            风格分类的循环添加数据
         */
        for (int i = 12; i >= 0; i--) {
            datas_two.add(0,symbol_four[i]);
        }
        /*/
            年份分类的循环添加数据
         */
        for (int i = 7; i >= 0; i--) {
            datas_three.add(0,symbol_two[i]);
        }
        /*/
            Trending Now分类的循环添加数据
         */
        for (int i = 6; i >= 0; i--) {
            datas_four.add(0,symbol_one[i]);
        }
       /*
         *用来确定每一个item如何进行排列摆放
         * LinearLayoutManager 相当于ListView的效果
         GridLayoutManager相当于GridView的效果
         StaggeredGridLayoutManager 瀑布流
        */
        recyclerView_one.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_two.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView_three.setLayoutManager(new GridLayoutManager(this, 4));

        /*
        *  调整每个item位置
        * */
        recyclerView_one.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.left  = 15;
                outRect.right = 15;
                outRect.top   = 15;
                outRect.top   = 60;

            }
        });

        recyclerView_two.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.left  = 15;
                outRect.right = 15;
                outRect.top   = 15;
                outRect.top   = 60;

            }
        });

        recyclerView_three.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

                outRect.left  = 15;
                outRect.right = 15;
                outRect.top   = 15;
                outRect.top   = 60;

            }
        });

        /*
           给每个recyclerView附上适配器
         */
        recyclerView_one.setAdapter(new pick_rvAdapter(this, datas_one));
        recyclerView_two.setAdapter(new pick_rvAdapter_two(this, datas_two));
        recyclerView_three.setAdapter(new pick_rvAdapter_three(this, datas_three));

        /*pick_rvAdapter_one.setOnItemClickListener(new pick_rvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(pickActivity.this, "我点击了" + position, Toast.LENGTH_SHORT).show();
            }
        });*/
    }





}
