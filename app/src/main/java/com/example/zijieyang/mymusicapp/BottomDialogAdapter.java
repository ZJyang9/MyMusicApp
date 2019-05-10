package com.example.zijieyang.mymusicapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BottomDialogAdapter extends RecyclerView.Adapter<BottomDialogAdapter.MyViewHolder>{
    private Context context;
    private List<String> list;
    private List<Integer> iconList;

    public BottomDialogAdapter(Context context, List<String> list, List<Integer> iconList) {
        this.context   = context;
        this.list      = list;
        this.iconList  = iconList;
    }
    /*
        这里是创建一个视图拥有者(ViewHolder)，将rv_list_first渲染到每个recyclerView的item上
     */
    @Override
    public BottomDialogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView= LayoutInflater.from(context).inflate(R.layout.bottom_dialog_item,parent,false);
        BottomDialogAdapter.MyViewHolder viewHolder=new BottomDialogAdapter.MyViewHolder(contentView);
        return viewHolder;
    }

    /*
        这里绑定视图，可以操控每个item的具体样式或者监听
     */
    @Override
    public void onBindViewHolder(final BottomDialogAdapter.MyViewHolder holder, final int position) {

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
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView icon_dialog;
        private TextView  textView_dialog;
        public MyViewHolder(View itemView) {
            super(itemView);
            icon_dialog = itemView.findViewById(R.id.cover_photo);
            textView_dialog = itemView.findViewById(R.id.song_name);


        }
    }
}
