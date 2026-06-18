package com.rocketkids.science;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MaterialCardView cardLearn = view.findViewById(R.id.card_learn);
        MaterialCardView cardPlay = view.findViewById(R.id.card_play);
        MaterialCardView cardQuiz = view.findViewById(R.id.card_quiz);
        MaterialCardView cardCredits = view.findViewById(R.id.card_credits);

        cardLearn.setOnClickListener(v -> navigateTo(new LearnFragment()));
        cardPlay.setOnClickListener(v -> navigateTo(new PlayFragment()));
        cardQuiz.setOnClickListener(v -> navigateTo(new QuizFragment()));
        cardCredits.setOnClickListener(v -> navigateTo(new AboutMeFragment()));

        return view;
    }

    private void navigateTo(Fragment fragment) {
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}