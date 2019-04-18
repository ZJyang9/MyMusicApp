package com.example.zijieyang.mymusicapp;

import android.content.Context;
import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

public class pick_rvAdapter extends RecyclerView.Adapter<pick_rvAdapter.MyViewHolder> {
    private Context context;
    private List<String> datas;
    private Boolean  selected[] = {false,false,false,false}; //未被选择
    private  List<Integer> keyId = new ArrayList<Integer>();
    private Boolean b_sub_square = false;

    //private OnItemClickListener mOnItemClickListener = null;


    public pick_rvAdapter(Context context, List<String> datas) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //holder.imageView.setImageResource(datas.get(position));
        holder.button.setText(datas.get(position));

        holder.button.setOnClickListener(new View.OnClickListener() {
            int mposition = position;
            @Override
            public void onClick(View v ) {
                /*if(selected == false){
                    selected = true;
                }
                else{
                    selected = false;
                }*/

                switch (mposition){
                    case 0 :
                        /*
                            这里写点击每个分类的逻辑
                         */
                        if(!selected[0]){
                            //表示选中
                            selected[0] = true;
                            holder.button.setActivated(selected[0]); //激活按钮
                            Toast.makeText(context,  "华语选择状态为"+selected[0],Toast.LENGTH_SHORT).show();

                        }
                        else{
                            //表示未被选中
                            selected[0] = false;
                            holder.button.setActivated(selected[0]); //激活按钮
                            Toast.makeText(context,  "华语选择状态为"+selected[0],Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1 :
                        if(!selected[1]){
                            //表示选中
                            selected[1] = true;
                            holder.button.setActivated(selected[1]); //激活按钮
                            Toast.makeText(context,  "欧美选择状态为"+selected[1],Toast.LENGTH_SHORT).show();

                        }
                        else{
                            //表示未被选中
                            selected[1] = false;
                            holder.button.setActivated(selected[1]); //激活按钮
                            Toast.makeText(context,  "欧美选择状态为"+selected[1],Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2 :
                        if(!selected[2]){
                            //表示选中
                            selected[2] = true;
                            holder.button.setActivated(selected[2]); //激活按钮
                            Toast.makeText(context,  "日语选择状态为"+selected[2],Toast.LENGTH_SHORT).show();

                        }
                        else{
                            //表示未被选中
                            selected[2] = false;
                            holder.button.setActivated(selected[2]); //激活按钮
                            Toast.makeText(context,  "日语选择状态为"+selected[2],Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 3 :
                        if(!selected[3]){
                            //表示选中
                            selected[3] = true;
                            holder.button.setActivated(selected[3]); //激活按钮
                            Toast.makeText(context,  "韩语选择状态为"+selected[3],Toast.LENGTH_SHORT).show();

                        }
                        else{
                            //表示未被选中
                            selected[3] = false;
                            holder.button.setActivated(selected[3]); //激活按钮
                            Toast.makeText(context,  "韩语选择状态为"+selected[3],Toast.LENGTH_SHORT).show();
                        }
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
        private ImageView imageView;
        public TextView textViewv;
        private Button button;

        public MyViewHolder(View itemView) {
            super(itemView);
            //imageView = (ImageView) itemView.findViewById(R.id.symbol_one_image);
            //textViewv = (TextView) itemView.findViewById(R.id.symbol_one_text);
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
