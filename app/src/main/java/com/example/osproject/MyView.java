package com.example.osproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.osproject.R;

public class MyView extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView singer;
    public ImageView album_img;

    public MyView(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.textTitle);
        singer = itemView.findViewById(R.id.textArtist);
        album_img = itemView.findViewById(R.id.imageAlbum);
    }
}
