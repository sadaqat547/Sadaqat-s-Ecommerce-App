package com.example.shopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        final ViewPager2 videosViewpager = findViewById(R.id.videosViewPager);

        List<VideoItem> videoItems = new ArrayList<>();

        VideoItem videoItemcelebration = new VideoItem();
        videoItemcelebration.videoUrl = "https://firebasestorage.googleapis.com/v0/b/myshopper-211a5.appspot.com/o/vivo_Y%20Series_EN.mp4?alt=media&token=9e6e80e7-135c-4485-83fc-5b1ede1ec22e";
        videoItemcelebration.videoTitle = "asssssssssssssssssssssss";
        videoItemcelebration.videoDescription = "jksajdohdlfjd";
        videoItems.add(videoItemcelebration);



        VideoItem videoItemcelebrationn = new VideoItem();
        videoItemcelebrationn.videoUrl = "https://firebasestorage.googleapis.com/v0/b/myshopper-211a5.appspot.com/o/vivo_Y%20Series_EN.mp4?alt=media&token=9e6e80e7-135c-4485-83fc-5b1ede1ec22e";
        videoItemcelebrationn.videoTitle = "asssssssssssssssssssssssssssssssssssssssssss";
        videoItemcelebrationn.videoDescription = "jksajdoadhdjslksdksfkslkfmsklfjsoldfjsdksfjdlshjksdnskdhskdsndkshfidikjfdlfnskjdfhsfoisjdflsjfdohfdkngdflgkdfpodjfkshdsihdskfhdofjdlfjd";
        videoItems.add(videoItemcelebrationn);
        videosViewpager.setAdapter(new VideoAdapter(videoItems));
    }
}