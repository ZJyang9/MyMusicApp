package com.example.zijieyang.mymusicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = inflater.inflate(R.layout.backimage_item, parent,false); //记得加后面两个参数 坑！！！

            //对viewHolder的属性进行赋值
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.backImage);
            viewHolder.song_name = (TextView) convertView.findViewById(R.id.song_name);
            viewHolder.singer_name = (TextView) convertView.findViewById(R.id.singer_name);
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
        System.out.println("GetView:图片地址: " + Glide.with(context).load(backImageList.get(position).getBackImageRes()));
        System.out.println("GetView:歌曲歌名: " + songNameList.get(position));
        System.out.println("GetView:歌曲歌手: " + singerNameList.get(position));
        return convertView;
    }
    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class ViewHolder{
        public ImageView imageView;
        public TextView song_name,singer_name;
    }
}
