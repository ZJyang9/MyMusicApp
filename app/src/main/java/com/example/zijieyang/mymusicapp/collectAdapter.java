package com.example.zijieyang.mymusicapp;

/**
 * 喜欢的歌的适配器
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class collectAdapter extends RecyclerView.Adapter<collectAdapter.MyViewHolder> {
    private Context context;
    private List<String> songNameList,singerNameList;
    private ArrayList<String> musicPathList;

    public collectAdapter(Context context, List<String> songNameList,List<String> singerNameList,ArrayList<String> musicPathList) {
        this.context       = context;
        this.songNameList  = songNameList;
        this.singerNameList= singerNameList;
        this.musicPathList = musicPathList;
    }
    /*
        这里是创建一个视图拥有者(ViewHolder)，将rv_list_first渲染到每个recyclerView的item上
     */
    @Override
    public collectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView= LayoutInflater.from(context).inflate(R.layout.collect_item,parent,false);
        collectAdapter.MyViewHolder viewHolder=new collectAdapter.MyViewHolder(contentView);
        return viewHolder;
    }

    /*
        这里绑定视图，可以操控每个item的具体样式或者监听
     */
    @Override
    public void onBindViewHolder(final collectAdapter.MyViewHolder holder, final int position) {

        holder.singer_name.setText(singerNameList.get(position));
        holder.song_name.setText(songNameList.get(position));
        holder.cover_photo.setImageResource(R.mipmap.ic_launcher_round);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击了每一项歌，
                //likeSongsActivity.instance.mMyBinder.pauseMusic();
                likeSongsActivity.instance.mMyBinder.iniMediaPlayerFile(position);
                Toast.makeText(context,  "点击了item" + position,Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
        返回item的数量
     */
    @Override
    public int getItemCount() {
        return songNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView cover_photo;
        private TextView  song_name,singer_name;
        public MyViewHolder(View itemView) {
            super(itemView);
            cover_photo = itemView.findViewById(R.id.cover_photo);
            song_name = itemView.findViewById(R.id.song_name);
            singer_name = itemView.findViewById(R.id.singer_name);

        }
    }
}
