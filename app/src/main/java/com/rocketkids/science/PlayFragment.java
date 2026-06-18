package com.rocketkids.science;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class PlayFragment extends Fragment {

    public PlayFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play, container, false);

        MaterialCardView btnLabelRocket = view.findViewById(R.id.btn_game_label_rocket);
        MaterialCardView btnMatchTerms = view.findViewById(R.id.btn_game_match_terms);
        MaterialCardView btnLaunchSim = view.findViewById(R.id.btn_game_launch_simulation);
        MaterialCardView btnBuildRocket = view.findViewById(R.id.btn_game_build_rocket);

        btnLabelRocket.setOnClickListener(v -> switchGameFragment(new LabelRocketFragment()));

        btnMatchTerms.setOnClickListener(v -> switchGameFragment(new MatchTermsFragment()));

        btnLaunchSim.setOnClickListener(v -> switchGameFragment(new LaunchSimulationFragment()));

        btnBuildRocket.setOnClickListener(v -> switchGameFragment(new BuildRocketFragment()));

        return view;
    }

    private void switchGameFragment(Fragment nextGame) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, nextGame)
                    .addToBackStack(null)
                    .commit();
        }
    }
}