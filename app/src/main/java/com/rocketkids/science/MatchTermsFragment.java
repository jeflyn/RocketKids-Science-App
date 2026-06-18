package com.rocketkids.science;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchTermsFragment extends Fragment {

    private LinearLayout containerTerms;
    private LinearLayout containerDefinitions;
    private TextView txtScorePercentage;
    private TextView txtStatusInstruction;
    private TextView txtSubtitle;
    private final List<DataPair> masterList = new ArrayList<>();
    private final List<MaterialCardView> termCards = new ArrayList<>();
    private final List<MaterialCardView> definitionCards = new ArrayList<>();
    private MaterialCardView selectedTermCard = null;
    private MaterialCardView selectedDefinitionCard = null;
    private int completedMatches = 0;
    private MediaPlayer correctPlayer;
    private MediaPlayer wrongPlayer;

    private static class DataPair {
        String term;
        String definition;

        DataPair(String term, String definition) {
            this.term = term;
            this.definition = definition;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_terms, container, false);

        containerTerms = view.findViewById(R.id.container_terms);
        containerDefinitions = view.findViewById(R.id.container_definitions);
        txtScorePercentage = view.findViewById(R.id.txt_score_percentage);
        txtStatusInstruction = view.findViewById(R.id.txt_status_instruction);
        txtSubtitle = view.findViewById(R.id.txt_subtitle);
        correctPlayer = MediaPlayer.create(getContext(), R.raw.correct_sound);
        wrongPlayer = MediaPlayer.create(getContext(), R.raw.wrong_sound);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        loadGameData();
        buildGameMatrix();

        return view;
    }

    private void loadGameData() {
        masterList.clear();
        masterList.add(new DataPair("Thrust", "The upward force produced by burning rocket propellant"));
        masterList.add(new DataPair("Gravity", "Invisible force pulling everything toward Earth's centre"));
        masterList.add(new DataPair("Velocity", "Speed measured in a specific direction of travel"));
        masterList.add(new DataPair("Propellant", "Rocket fuel and oxidizer that are burned together"));
    }

    private void buildGameMatrix() {
        txtSubtitle.setVisibility(View.VISIBLE);
        txtStatusInstruction.setVisibility(View.VISIBLE);
        containerTerms.setVisibility(View.VISIBLE);
        containerDefinitions.setVisibility(View.VISIBLE);

        containerTerms.removeAllViews();
        containerDefinitions.removeAllViews();
        termCards.clear();
        definitionCards.clear();

        List<DataPair> shuffledTerms = new ArrayList<>(masterList);
        List<DataPair> shuffledDefs = new ArrayList<>(masterList);
        Collections.shuffle(shuffledTerms);
        Collections.shuffle(shuffledDefs);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.weight = 1.0f;
        layoutParams.setMargins(6, 12, 6, 12);

        for (DataPair pair : shuffledTerms) {
            MaterialCardView card = createBaseCard(pair.term, false, pair);
            termCards.add(card);
            containerTerms.addView(card, layoutParams);
        }

        for (DataPair pair : shuffledDefs) {
            MaterialCardView card = createBaseCard(pair.definition, true, pair);
            definitionCards.add(card);
            containerDefinitions.addView(card, layoutParams);
        }
    }

    private MaterialCardView createBaseCard(String text, boolean isDefinition, DataPair associatedPair) {
        MaterialCardView card = new MaterialCardView(requireContext());
        card.setCardBackgroundColor(Color.parseColor("#161B22"));
        card.setStrokeColor(Color.parseColor("#21262D"));
        card.setStrokeWidth(3);
        card.setRadius(24f);
        card.setClickable(true);
        card.setFocusable(true);

        TextView textView = new TextView(requireContext());
        textView.setText(text);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(14);
        textView.setGravity(android.view.Gravity.CENTER);

        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(textParams);

        int paddingPx = dpToPx(12);
        textView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

        card.addView(textView);
        card.setTag(associatedPair);
        card.setOnClickListener(v -> handleCardSelection(card, isDefinition));

        return card;
    }

    private void handleCardSelection(MaterialCardView selectedCard, boolean isDefinition) {
        if (isDefinition) {
            if (selectedDefinitionCard != null) setCardState(selectedDefinitionCard, "DEFAULT");
            selectedDefinitionCard = selectedCard;
            setCardState(selectedDefinitionCard, "SELECTED");
        } else {
            if (selectedTermCard != null) setCardState(selectedTermCard, "DEFAULT");
            selectedTermCard = selectedCard;
            setCardState(selectedTermCard, "SELECTED");
        }

        if (selectedTermCard != null && selectedDefinitionCard != null) {
            DataPair termData = (DataPair) selectedTermCard.getTag();
            DataPair defData = (DataPair) selectedDefinitionCard.getTag();

            if (termData.term.equals(defData.term)) {
                if (correctPlayer != null) correctPlayer.start();
                setCardState(selectedTermCard, "CORRECT");
                setCardState(selectedDefinitionCard, "CORRECT");
                completedMatches++;

                int progress = (int) (((float) completedMatches / masterList.size()) * 100);
                txtScorePercentage.setText(progress + "%");

                selectedTermCard = null;
                selectedDefinitionCard = null;
                txtStatusInstruction.setText("✨ Correct match!");

                if (completedMatches == masterList.size()) {
                    showVictoryScreen();
                }
            } else {
                if (wrongPlayer != null) wrongPlayer.start();
                txtStatusInstruction.setText("❌ Try matching again!");

                final MaterialCardView temporaryTerm = selectedTermCard;
                final MaterialCardView temporaryDef = selectedDefinitionCard;

                selectedTermCard = null;
                selectedDefinitionCard = null;

                containerTerms.postDelayed(() -> {
                    if (temporaryTerm != null && temporaryDef != null) {
                        setCardState(temporaryTerm, "DEFAULT");
                        setCardState(temporaryDef, "DEFAULT");
                    }
                }, 1000);
            }
        }
    }

    private void setCardState(MaterialCardView card, String state) {
        TextView tv = (TextView) card.getChildAt(0);
        switch (state) {
            case "SELECTED":
                card.setCardBackgroundColor(Color.parseColor("#211533"));
                card.setStrokeColor(Color.parseColor("#A855F7"));
                tv.setTextColor(Color.parseColor("#A855F7"));
                break;
            case "CORRECT":
                card.setCardBackgroundColor(Color.parseColor("#0C211A"));
                card.setStrokeColor(Color.parseColor("#10B981"));
                tv.setTextColor(Color.parseColor("#10B981"));
                if (!tv.getText().toString().startsWith("✓")) {
                    tv.setText("✓ " + tv.getText());
                }
                card.setClickable(false);
                break;
            case "DEFAULT":
            default:
                card.setCardBackgroundColor(Color.parseColor("#161B22"));
                card.setStrokeColor(Color.parseColor("#21262D"));
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    private void showVictoryScreen() {
        txtSubtitle.setVisibility(View.GONE);
        txtStatusInstruction.setVisibility(View.GONE);
        containerTerms.setVisibility(View.GONE);
        containerDefinitions.setVisibility(View.GONE);

        ViewGroup rootView = (ViewGroup) getView();
        if (rootView == null) return;

        LayoutInflater.from(requireContext()).inflate(R.layout.layout_match_victory, rootView, true);

        View btnPlayAgain = rootView.findViewById(R.id.btn_play_again);
        btnPlayAgain.setOnClickListener(v -> {
            View victoryLayout = rootView.findViewById(R.id.victory_overlay_container);
            if (victoryLayout != null) {
                rootView.removeView(victoryLayout);
            }
            completedMatches = 0;
            txtScorePercentage.setText("0%");
            buildGameMatrix();
        });
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (correctPlayer != null) { correctPlayer.release(); correctPlayer = null; }
        if (wrongPlayer != null) { wrongPlayer.release(); wrongPlayer = null; }
    }
}