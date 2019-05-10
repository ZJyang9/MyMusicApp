package com.example.zijieyang.mymusicapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class back_imageAdapter extends BaseAdapter {
    private List<back_imageBean> backImageList;//数据源
    private List<String> songNameList,singerNameList;
    private Context context;

    // 通过构造方法将数据源与数据适配器关联起来
    // context:要使用当前的Adapter的界面对象 , List<String> songNameList,List<String> singerNameList
    public back_imageAdapter(Context context, List<back_imageBean> list , List<String> songNameList,List<String> singerNameList) {
        this.backImageList = list;
        this.songNameList = songNameList;
        this.singerNameList = singerNameList;
        this.context = context;
        System.out.println("进入了适配器初始化");

    }

    @Override
    //ListView需要显示的数据数量
    public int getCount() {
        return backImageList.size();
    }

    @Override
    //指定的索引对应的数据项
    public Object getItem(int position) {
        return backImageList.get(position);
    }

    @Override
    //指定的索引对应的数据项ID
    public long getItemId(int position) {
        return position;
    }

    @Override
    //返回每一项的显示内容
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("进入了getView");
        final ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = inflater.inflate(R.layout.backimage_item, parent,false); //记得加后面两个参数 坑！！！

            viewHolder.imageView        = (ImageView) convertView.findViewById(R.id.backImage);
            viewHolder.pauseBtn_image   = (ImageView) convertView.findViewById(R.id.pauseBtn_image);
            //viewHolder.sequential_image = (ImageView) convertView.findViewById(R.id.sequential_image);
            //viewHolder.cycle_image      = (ImageView) convertView.findViewById(R.id.cycle_image);
            viewHolder.song_name        = (TextView)  convertView.findViewById(R.id.song_name);
            viewHolder.singer_name      = (TextView)  convertView.findViewById(R.id.singer_name);
            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //利用Glide框架 将列表中的图片资源地址传给R.id.backImage
        Glide.with(context)
                .load(backImageList.get(position).getBackImageRes())
                .error(R.mipmap.dislike)
                .into(viewHolder.imageView);
        viewHolder.song_name.setText(songNameList.get(position));
        viewHolder.singer_name.setText(singerNameList.get(position));
        /*viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.pauseBtn_image.getVisibility() == View.VISIBLE) {//返回值为0，visible；返回值为4，invisible；返回值为8，gone

                    MainActivity.instance.mMyBinder.playMusicTwo();
                    viewHolder.pauseBtn_image.setVisibility(View.INVISIBLE);

                } else if (viewHolder.pauseBtn_image.getVisibility() == View.INVISIBLE) {
                    viewHolder.pauseBtn_image.setVisibility(View.VISIBLE);
                    MainActivity.instance.mMyBinder.pauseMusic();

                }
                Log.i("","点击了图片，此时暂停图片的属性为" + viewHolder.pauseBtn_image.getVisibility() );
            }
        });*/
        /**
         * 播放模式的监听
         */
        /*viewHolder.cycle_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.cycle_image.setVisibility(View.INVISIBLE);
                viewHolder.sequential_image.setVisibility(View.VISIBLE);
                MainActivity.instance.isCycle = false;
            }
        });
        viewHolder.sequential_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.sequential_image.setVisibility(View.INVISIBLE);
                viewHolder.cycle_image.setVisibility(View.VISIBLE);
                MainActivity.instance.isCycle = true;
            }
        });*/
        return convertView;
    }

    class ViewHolder{
        public ImageView imageView,pauseBtn_image;
        public TextView song_name,singer_name;
    }
}
