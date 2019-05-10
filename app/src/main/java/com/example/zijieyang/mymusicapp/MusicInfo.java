package com.example.zijieyang.mymusicapp;

import java.util.ArrayList;
import java.util.List;

/*
    推荐歌曲的信息json类型
 */
public class MusicInfo {
    private List<Data>data; //数组用list来存储

    public MusicInfo(){

    }

    public List<Data> getData(){
        return data;

    }
    public void setData(List<Data> data){
        this.data = data;
    }


    /**
     * json对象里的data数组,每一项代表每一首歌的信息
     */
    public class Data{
        private String mixsongid,song_name,singer_name,play_url,lyric,
                cover_url,avatar;
        public ArrayList<String> photo_url;
        private Climax climax;

        public Data(){

        }
        public String getMixsongid(){
            return  mixsongid;
        }
        public void setMixsongid(String mixsongid){
            this.mixsongid = mixsongid;
        }
        public  Climax getClimax(){
            return climax;

        }
        public void setClimax(Climax climax){
            this.climax = climax;
        }
        public String getSong_name(){
            return song_name;
        }
        public String getSinger_name(){
            return singer_name;
        }
        public String getPlay_url(){
            return play_url;
        }
        public String getAvatar(){
            return avatar;
        }
        public String getCover_url(){
            return cover_url;
        }
        public String getLyric(){
            return lyric;
        }
        public ArrayList<String> getPhoto_url(){
            return photo_url;
        }
        /**
         *  data数组里的图片数组，歌曲背景图片
         */

        public class photo_url{

        }
        /**
         * data数组里的对象，30s高潮始末时间
         */
        public class Climax{
            private String start_time,end_time;

            public Climax(){

            }

            public String getStart_time(){
                return start_time;
            }
            public String getEnd_time(){
                return end_time;
            }
        }
    }
}
