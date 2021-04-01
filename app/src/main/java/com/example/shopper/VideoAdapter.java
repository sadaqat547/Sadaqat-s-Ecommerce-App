package com.example.shopper;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cindyhuang.marqueetextview.MarqueeTextView;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.videoViewHolder> {

    private List<VideoItem> videoItems;

    public VideoAdapter(List<VideoItem> videoItems) {
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public videoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new videoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_video,parent,false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull videoViewHolder holder, int position) {
        holder.setVideoData(videoItems.get(position));
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    static  class videoViewHolder extends RecyclerView.ViewHolder{
        VideoView videoView;
        TextView textvideoTitle;
        TextView textvideoDescription;
        SpinKitView progressbar;


        public videoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.video_view);
            textvideoTitle = itemView.findViewById(R.id.story_videoTitle);
            textvideoDescription = itemView.findViewById(R.id.story_video_description);
            progressbar = itemView.findViewById(R.id.video_progressbar);

        }
        void  setVideoData(final VideoItem videoItem){
            textvideoTitle.setText(videoItem.videoTitle);
            textvideoTitle.setText(videoItem.videoDescription);
            videoView.setVideoPath(videoItem.videoUrl);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressbar.setVisibility(View.GONE);
                    mp.start();

                    float videoRatio = mp.getVideoWidth() / (float) mp.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                    float scale = videoRatio/screenRatio;

                    if (scale != 1f){
                        videoView.setScaleX(scale);
                    }else {
                        videoView.setScaleY(1f/scale);
                    }
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });

        }
    }
}
