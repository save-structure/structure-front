package com.example.osproject;

import com.bumptech.glide.Glide;

public class Song {
    private int id;
    private String feeling_num;
    private String title;
    private String singer;
    private String song_url;
    private String img_url;

    public Song(String title, String singer, String img_url, String youtube_id){
        this.title = title;
        this.singer = singer;
        this.img_url = img_url;
        this.song_url = youtube_id;
    }

    public Song(int id, String feeling_num, String title, String singer, String song_url, String img_url){
            this.id = id;
            this.feeling_num = feeling_num;
            this.title = title;
            this.singer = singer;
            this.song_url = song_url;
            this.img_url = img_url;
    }

    // getter
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getSinger() { return singer; }
    public String getImg_url() { return img_url; }
    public String getSong_url() { return song_url; }
    //setter
    public void setId(int id) { this.id = id; }
}
