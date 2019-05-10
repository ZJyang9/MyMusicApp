package com.example.zijieyang.mymusicapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class pick_rvAdapter_three extends RecyclerView.Adapter<pick_rvAdapter_three.MyViewHolder> {
    private Context context;
    private List<String> datas;

    public pick_rvAdapter_three(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView= LayoutInflater.from(context).inflate(R.layout.rv_list_first,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(contentView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.button.setText(datas.get(position));

        Typeface typeface = ResourcesCompat.getFont(context, R.font.pingfang_light);
        holder.button.setTypeface(typeface);

        holder.button.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v ) {
                int mposition = position;
                switch (mposition){
                    case 0 :
                           /*这里写点击每个分类的逻辑*/
                        Toast.makeText(context,  "今年." + 0,Toast.LENGTH_SHORT).show();
                        break;
                    case 1 :
                        Toast.makeText(context,  "2018." + 1,Toast.LENGTH_SHORT).show();
                        break;
                    case 2 :
                        Toast.makeText(context,  "2017." + 2,Toast.LENGTH_SHORT).show();
                        break;
                    case 3 :
                        Toast.makeText(context,  "2016." + 3,Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public MyViewHolder(View itemView) {
            super(itemView);
            //imageView = (ImageView) itemView.findViewById(R.id.symbol_one_image);
           // textViewv = (TextView) itemView.findViewById(R.id.symbol_one_text);
            button = (Button) itemView.findViewById(R.id.button);
        }
        /*@Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v,(int)v.getTag());
            }
        }*/
    }


}
