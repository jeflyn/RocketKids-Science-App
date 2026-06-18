package com.rocketkids.science;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;
import android.widget.MediaController;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LearnFragment extends Fragment {

    private VideoView videoView;
    private FloatingActionButton btnPlay;
    private LinearLayout containerIntro, containerForces, containerRockets, containerFacts;
    private Button btnIntro, btnForces, btnRockets, btnFacts;

    public LearnFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn, container, false);

        videoView = view.findViewById(R.id.video_view_launcher);
        btnPlay = view.findViewById(R.id.fab_play_video);

        containerIntro = view.findViewById(R.id.view_container_intro);
        containerForces = view.findViewById(R.id.view_container_forces);
        containerRockets = view.findViewById(R.id.view_container_rockets);
        containerFacts = view.findViewById(R.id.view_container_facts);

        btnIntro = view.findViewById(R.id.btn_tab_intro);
        btnForces = view.findViewById(R.id.btn_tab_forces);
        btnRockets = view.findViewById(R.id.btn_tab_rockets);
        btnFacts = view.findViewById(R.id.btn_tab_facts);

        String videoPath = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.rocket_launch_video;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.GONE);
                videoView.start();
            }
        });

        btnIntro.setOnClickListener(v -> highlightAndSwitchTab(0));
        btnForces.setOnClickListener(v -> highlightAndSwitchTab(1));
        btnRockets.setOnClickListener(v -> highlightAndSwitchTab(2));
        btnFacts.setOnClickListener(v -> highlightAndSwitchTab(3));

        return view;
    }

    private void highlightAndSwitchTab(int position) {
        containerIntro.setVisibility(View.GONE);
        containerForces.setVisibility(View.GONE);
        containerRockets.setVisibility(View.GONE);
        containerFacts.setVisibility(View.GONE);

        btnIntro.setSelected(false);
        btnForces.setSelected(false);
        btnRockets.setSelected(false);
        btnFacts.setSelected(false);

        switch (position) {
            case 0:
                containerIntro.setVisibility(View.VISIBLE);
                btnIntro.setSelected(true);
                break;
            case 1:
                containerForces.setVisibility(View.VISIBLE);
                btnForces.setSelected(true);
                safelyPauseVideo();
                break;
            case 2:
                containerRockets.setVisibility(View.VISIBLE);
                btnRockets.setSelected(true);
                safelyPauseVideo();
                break;
            case 3:
                containerFacts.setVisibility(View.VISIBLE);
                btnFacts.setSelected(true);
                safelyPauseVideo();
                break;
        }
    }

    private void safelyPauseVideo() {
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }
}