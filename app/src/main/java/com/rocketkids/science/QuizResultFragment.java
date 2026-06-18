package com.rocketkids.science;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

public class QuizResultFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz_result, container, false);

        TextView txtScoreFraction = view.findViewById(R.id.txt_score_fraction);
        TextView txtScorePercent  = view.findViewById(R.id.txt_score_percent);
        TextView txtResultMessage = view.findViewById(R.id.txt_result_message);
        TextView txtCorrectCount  = view.findViewById(R.id.txt_correct_count);
        TextView txtIncorrectCount = view.findViewById(R.id.txt_incorrect_count);
        TextView txtScorePctPerf  = view.findViewById(R.id.txt_score_pct_perf);
        TextView star1            = view.findViewById(R.id.star_1);
        TextView star2            = view.findViewById(R.id.star_2);
        TextView star3            = view.findViewById(R.id.star_3);
        MaterialButton btnTryAgain = view.findViewById(R.id.btn_try_again);

        int score = 0, total = 10;
        if (getArguments() != null) {
            score = getArguments().getInt("final_score", 0);
            total = getArguments().getInt("total_questions", 10);
        }

        int incorrect = total - score;
        int pct = total > 0 ? (score * 100 / total) : 0;

        txtScoreFraction.setText(score + " / " + total);
        txtScorePercent.setText(pct + "%");
        txtCorrectCount.setText(String.valueOf(score));
        txtIncorrectCount.setText(String.valueOf(incorrect));
        txtScorePctPerf.setText(pct + "%");

        int stars = starsForScore(pct);
        star1.setText(stars >= 1 ? "⭐" : "☆");
        star2.setText(stars >= 2 ? "⭐" : "☆");
        star3.setText(stars >= 3 ? "⭐" : "☆");
        if (stars < 2) { star2.setTextColor(0xFF64748B); }
        if (stars < 3) { star3.setTextColor(0xFF64748B); }

        if (pct >= 80) {
            txtResultMessage.setText("Outstanding! You are ready for lift-off! 🚀✨");
        } else if (pct >= 50) {
            txtResultMessage.setText("Good effort! Review the lessons to boost your score. 📚");
        } else {
            txtResultMessage.setText("Keep studying and try again! 📚");
        }

        btnTryAgain.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new QuizFragment())
                    .commit();
        });

        return view;
    }

    private int starsForScore(int pct) {
        if (pct >= 80) return 3;
        if (pct >= 50) return 2;
        return 1;
    }
}