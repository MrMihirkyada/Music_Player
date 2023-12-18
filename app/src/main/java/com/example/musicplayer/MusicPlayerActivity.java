package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity{

    TextView song_title, current_time, total_time;
    SeekBar seekBar;
    ImageView imgPlayPause, imgnext, imgprevious, music_icon;
    ArrayList<AudioModel> SongsList;
    AudioModel currentSong;

    public int songNumber = 0;
    MediaPlayer mediaPlayers = new MediaPlayer();
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);


        initview();
    }

    private void initview()
    {
        song_title = findViewById(R.id.song_title);
        current_time = findViewById(R.id.current_time);
        total_time = findViewById(R.id.total_time);

        seekBar = findViewById(R.id.seekBar);

        imgPlayPause = findViewById(R.id.imgPlayPause);
        imgnext = findViewById(R.id.imgnext);
        imgprevious = findViewById(R.id.imgprevious);
        music_icon = findViewById(R.id.music_icon);

        song_title.setSelected(true);

        Intent i = getIntent();
        String music = i.getStringExtra("List");
        song_title.setText(music);

        SongsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");


        MusicPlayerActivity.this.runOnUiThread(new Runnable()
        {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    current_time.setText(convertToMMSS(mediaPlayer.getCurrentPosition() + ""));

                    if (mediaPlayer.isPlaying())
                    {
//                      mediaPlayer = MediaPlayer.create(getApplicationContext(),);
                        imgPlayPause.setImageResource(R.drawable.pause);
                        music_icon.setRotation(songNumber++);
                    }
                    else
                    {
                        imgPlayPause.setImageResource(R.drawable.play);
                        music_icon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        setResourcesWithMusic();
    }


    void setResourcesWithMusic()
    {
        currentSong = SongsList.get(MyMediaPlayer.currentIndex);

        song_title.setText(currentSong.getTitle());

        total_time.setText(convertToMMSS(currentSong.getDuration()));

        imgPlayPause.setOnClickListener(v -> playpause());

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    playNextSong();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        imgnext.setOnClickListener(v -> {
            try {
                playNextSong();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        imgprevious.setOnClickListener(v -> {
            try {
                playpreviousSong();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        playMusic();
    }

    private void playMusic()
    {
        mediaPlayer.reset();
        try
        {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void playNextSong() throws IOException {

        if (MyMediaPlayer.currentIndex == SongsList.size() - 1)
            return;
        MyMediaPlayer.currentIndex += 1;

        mediaPlayer.reset();

        setResourcesWithMusic();
    }

    private void playpreviousSong() throws IOException {
        if (MyMediaPlayer.currentIndex == 0)
            return;
        MyMediaPlayer.currentIndex -= 1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void playpause()
    {
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }

    public static String convertToMMSS(String duration)
    {

        long millis = Long.parseLong(duration);


        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }


}