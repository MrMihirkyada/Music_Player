package com.example.musicplayer;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder>
{

//    ArrayList<String> songsList;
    ArrayList<AudioModel> songsList;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MusicListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder,  int position)
    {
        AudioModel songData = songsList.get(position);
        holder.txtSongName.setText(songData.getTitle());

        if(MyMediaPlayer.currentIndex == position)
        {
            holder.txtSongName.setTextColor(Color.parseColor("#FF0000"));
        }
        else
        {
            holder.txtSongName.setTextColor(Color.parseColor("#FFFFFF"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                 MyMediaPlayer.getInstance().reset();
                 MyMediaPlayer.currentIndex = position;
                 Intent i = new Intent(context, MusicPlayerActivity.class);
                 i.putExtra("LIST",songsList);
                 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return songsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView crdicon;
        TextView txtSongName;
        public ViewHolder( View itemView)
        {
            super(itemView);

            crdicon = itemView.findViewById(R.id.crdicon);
            txtSongName = itemView.findViewById(R.id.txtSongName);
        }
    }
}
