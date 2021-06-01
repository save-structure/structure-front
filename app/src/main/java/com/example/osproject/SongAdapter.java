package com.example.osproject;

import android.content.Context;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


// MyView와 ArrayList<Song>을 사용하기 위해 필요
public class SongAdapter extends RecyclerView.Adapter<MyView> {

    private ActionMode actionMode;

    public SongAdapter(List<Song> songs) {
        this.songs = songs;
    }

    // SongAdapter가 instanciate?될때 ArrayList 노래 목록을 이 그냥 List songs에 전달할꺼임
    List<Song> songs;

    // 추상클래스?? 얘 뭐하는애임 ?
   Context context;

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("onCreateViewHolder : ", "called");
        // item_layout 부분을 컨트롤하는 부분 (노래 한곡 부분)
        context = parent.getContext();
        // fragment의 view를 만들거나 Recyclerview에서 viewholder를 만들때 사용하는
        LayoutInflater inflater = LayoutInflater.from(context);
        View songView = inflater.inflate(R.layout.item_layout, parent, false);
        MyView viewHolder = new MyView(songView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Log.e("onBindViewHolder : ", "called");
        // 각 노래 보여지는 부분에 어떤 노래가 보여질지..?
        Song song = songs.get(position);

        TextView artist = holder.singer;
        artist.setText(song.getSinger());

        TextView title = holder.title;
        title.setText(song.getTitle());

        ImageView album_img = holder.album_img;

        String img_url = song.getImg_url();
        if (img_url.equals("null") || img_url.equals(""))
            album_img.setImageResource(R.drawable.ic_baseline_music_note_24);
        else Glide.with(context).load(img_url).into(album_img);

        Glide.with(context).load(song.getImg_url()).into(album_img);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

}
