package com.example.osproject;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyView extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView singer;
    public ImageView album_img;
    public Button button;

    public MyView(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.textTitle);
        singer = itemView.findViewById(R.id.textArtist);
        album_img = itemView.findViewById(R.id.imageAlbum);

//        itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(itemView.getContext(),YouTubePlayerFrag.class);
//                intent.putExtra("data","play");
//                intent.putExtra("youtube", ((MainActivity)getActivity()).youtube1);
//                startActivityForResult(intent,2);
//            }
//        });

    }
}
