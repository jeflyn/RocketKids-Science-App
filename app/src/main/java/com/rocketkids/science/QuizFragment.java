package com.rocketkids.science;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private TextView txtQuizCounter, txtScoreLive, txtQuestionEmoji, txtQuestionText;
    private LinearLayout optionA, optionB, optionC, optionD;
    private TextView txtOptionA, txtOptionB, txtOptionC, txtOptionD;
    private TextView labelA, labelB, labelC, labelD;
    private LinearLayout layoutFeedback;
    private TextView txtFeedbackIcon, txtFeedbackMessage;
    private MaterialButton btnSubmit;

    private final List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int selectedOptionIndex = -1;
    private boolean answerRevealed = false;
    private MediaPlayer correctPlayer;
    private MediaPlayer wrongPlayer;

    public static class Question {
        String question;
        String[] options;
        int correctIndex;
        String emoji;

        public Question(String question, String[] options, int correctIndex, String emoji) {
            this.question = question;
            this.options = options;
            this.correctIndex = correctIndex;
            this.emoji = emoji;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        txtQuizCounter   = view.findViewById(R.id.txt_quiz_counter);
        txtScoreLive     = view.findViewById(R.id.txt_score_live);
        txtQuestionEmoji = view.findViewById(R.id.txt_question_emoji);
        txtQuestionText  = view.findViewById(R.id.txt_question_text);

        optionA = view.findViewById(R.id.option_a);
        optionB = view.findViewById(R.id.option_b);
        optionC = view.findViewById(R.id.option_c);
        optionD = view.findViewById(R.id.option_d);

        txtOptionA = view.findViewById(R.id.txt_option_a);
        txtOptionB = view.findViewById(R.id.txt_option_b);
        txtOptionC = view.findViewById(R.id.txt_option_c);
        txtOptionD = view.findViewById(R.id.txt_option_d);

        labelA = view.findViewById(R.id.label_a);
        labelB = view.findViewById(R.id.label_b);
        labelC = view.findViewById(R.id.label_c);
        labelD = view.findViewById(R.id.label_d);

        layoutFeedback   = view.findViewById(R.id.layout_feedback);
        txtFeedbackIcon  = view.findViewById(R.id.txt_feedback_icon);
        txtFeedbackMessage = view.findViewById(R.id.txt_feedback_message);
        btnSubmit        = view.findViewById(R.id.btn_submit_answer);

        correctPlayer = MediaPlayer.create(getContext(), R.raw.correct_sound);
        wrongPlayer = MediaPlayer.create(getContext(), R.raw.wrong_sound);

        optionA.setOnClickListener(v -> selectAndReveal(0));
        optionB.setOnClickListener(v -> selectAndReveal(1));
        optionC.setOnClickListener(v -> selectAndReveal(2));
        optionD.setOnClickListener(v -> selectAndReveal(3));

        btnSubmit.setOnClickListener(v -> advanceQuestion());

        loadQuestions();
        displayQuestion();

        return view;
    }

    private void loadQuestions() {
        questionList.clear();
        questionList.add(new Question(
                "What invisible force pulls everything toward Earth's centre?",
                new String[]{"Thrust", "Gravity", "Wind", "Magnetism"}, 1, "🌍"));
        questionList.add(new Question(
                "What force does a rocket engine produce to push the rocket upward?",
                new String[]{"Steam", "Electricity", "Thrust", "Sound"}, 2, "🚀"));
        questionList.add(new Question(
                "Which of Newton's Laws explains why rockets can fly?\n(Every action has an equal and opposite reaction)",
                new String[]{"1st Law", "2nd Law", "3rd Law", "4th Law"}, 2, "⚖️"));
        questionList.add(new Question(
                "What speed must a rocket reach to escape Earth's gravity completely?",
                new String[]{"11.2 km/s", "5 km/s", "100 km/s", "0.5 km/s"}, 0, "💨"));
        questionList.add(new Question(
                "Why are rockets built in multiple stages?",
                new String[]{"To look cool", "To reduce weight as fuel is used",
                        "To carry more fuel", "To slow down in space"}, 1, "🔧"));
        questionList.add(new Question(
                "What do rocket engines burn to create thrust?",
                new String[]{"Gasoline", "Coal", "Propellant (fuel + oxygen)", "Just air"}, 2, "🔥"));
        questionList.add(new Question(
                "What is an orbit?",
                new String[]{"A type of planet", "Moving in a curved path around Earth",
                        "The Moon's colour", "A rocket part"}, 1, "🪐"));
        questionList.add(new Question(
                "How long did Apollo 11 take to travel from Earth to the Moon?",
                new String[]{"1 hour", "1 day", "3 days", "1 week"}, 2, "🌕"));
        questionList.add(new Question(
                "Approximately how fast does the International Space Station travel?",
                new String[]{"100 km/h", "2,000 km/h", "27,600 km/h", "500,000 km/h"}, 2, "🛸"));
        questionList.add(new Question(
                "What exits from a rocket's engine nozzle to create thrust?",
                new String[]{"Cold air", "Water only", "Hot gases at high speed", "Smoke only"}, 2, "💥"));
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            navigateToResults();
            return;
        }

        answerRevealed = false;
        selectedOptionIndex = -1;
        layoutFeedback.setVisibility(View.GONE);

        resetOptionStyles();

        Question q = questionList.get(currentQuestionIndex);
        txtQuizCounter.setText((currentQuestionIndex + 1) + " / " + questionList.size());
        txtScoreLive.setText("Score: " + score);
        txtQuestionEmoji.setText(q.emoji);
        txtQuestionText.setText(q.question);

        txtOptionA.setText(q.options[0]);
        txtOptionB.setText(q.options[1]);
        txtOptionC.setText(q.options[2]);
        txtOptionD.setText(q.options[3]);

        btnSubmit.setVisibility(View.GONE);
    }

    private void selectAndReveal(int index) {
        if (answerRevealed) return;

        answerRevealed = true;
        selectedOptionIndex = index;

        int correct = questionList.get(currentQuestionIndex).correctIndex;

        if (index == correct) {
            score++;
            if (correctPlayer != null) correctPlayer.start();
            applyCorrectStyle(getOptionLayout(index), getOptionLabel(index));
        } else {
            if (wrongPlayer != null) wrongPlayer.start();
            applyIncorrectStyle(getOptionLayout(index), getOptionLabel(index));
            applyCorrectStyle(getOptionLayout(correct), getOptionLabel(correct));
        }

        txtScoreLive.setText("Score: " + score);

        btnSubmit.setText(currentQuestionIndex == questionList.size() - 1
                ? "See Results" : "Next Question →");
        btnSubmit.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (correctPlayer != null) { correctPlayer.release(); correctPlayer = null; }
        if (wrongPlayer != null) { wrongPlayer.release(); wrongPlayer = null; }
    }

    private void advanceQuestion() {
        currentQuestionIndex++;
        displayQuestion();
    }

    private void resetOptionStyles() {
        LinearLayout[] layouts = {optionA, optionB, optionC, optionD};
        TextView[] labels = {labelA, labelB, labelC, labelD};
        for (int i = 0; i < 4; i++) {
            layouts[i].setBackgroundResource(R.drawable.bg_quiz_option_default);
            labels[i].setTextColor(0xFF94A3B8);
        }
    }

    private void applyCorrectStyle(LinearLayout layout, TextView label) {
        layout.setBackgroundResource(R.drawable.bg_quiz_option_correct);
        label.setTextColor(0xFF10B981);
    }

    private void applyIncorrectStyle(LinearLayout layout, TextView label) {
        layout.setBackgroundResource(R.drawable.bg_quiz_option_incorrect);
        label.setTextColor(0xFFEF4444);
    }

    private LinearLayout getOptionLayout(int index) {
        switch (index) {
            case 0: return optionA;
            case 1: return optionB;
            case 2: return optionC;
            case 3: return optionD;
            default: return optionA;
        }
    }

    private TextView getOptionLabel(int index) {
        switch (index) {
            case 0: return labelA;
            case 1: return labelB;
            case 2: return labelC;
            case 3: return labelD;
            default: return labelA;
        }
    }

    private void navigateToResults() {
        Bundle bundle = new Bundle();
        bundle.putInt("final_score", score);
        bundle.putInt("total_questions", questionList.size());

        QuizResultFragment resultFragment = new QuizResultFragment();
        resultFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .commit();
    }


}