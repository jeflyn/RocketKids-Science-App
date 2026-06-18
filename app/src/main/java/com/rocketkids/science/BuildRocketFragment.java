package com.rocketkids.science;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuildRocketFragment extends Fragment {

    private RecyclerView recyclerView;
    private RocketStageAdapter adapter;
    private List<RocketStage> stagesList;

    private MaterialButton btnCheckAnswer, btnReset;
    private LinearLayout victoryOverlay;
    private MediaPlayer correctPlayer;
    private MediaPlayer wrongPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_build_rocket, container, false);

        recyclerView = view.findViewById(R.id.recycler_stages);
        btnCheckAnswer = view.findViewById(R.id.btn_check_answer);
        btnReset = view.findViewById(R.id.btn_reset);
        victoryOverlay = view.findViewById(R.id.victory_success_card);
        correctPlayer = MediaPlayer.create(getContext(), R.raw.correct_sound);
        wrongPlayer = MediaPlayer.create(getContext(), R.raw.wrong_sound);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        setupDataStructures();
        setupRecyclerView();

        btnCheckAnswer.setOnClickListener(v -> executeOrderValidation());
        btnReset.setOnClickListener(v -> resetAssemblyLine());

        return view;
    }

    private void setupDataStructures() {
        stagesList = new ArrayList<>();
        // Payload -> Second Stage -> Booster -> Engine Cluster
        stagesList.add(new RocketStage("PAYLOAD", "Payload / Capsule", "Top of rocket — carried to orbit", Color.parseColor("#00E5FF"), "POS_0"));
        stagesList.add(new RocketStage("STAGE_2", "Second Stage", "Pushes to orbital speed", Color.parseColor("#A855F7"), "POS_1"));
        stagesList.add(new RocketStage("BOOSTER", "First Stage Booster", "Lifts the rocket off the ground", Color.parseColor("#FF6B00"), "POS_2"));
        stagesList.add(new RocketStage("ENGINE", "Engine Cluster", "Burns propellant for thrust", Color.parseColor("#FFC107"), "POS_3"));

        Collections.shuffle(stagesList);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RocketStageAdapter(stagesList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback touchCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder src, @NonNull RecyclerView.ViewHolder target) {
                adapter.onItemMove(src.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        };

        ItemTouchHelper helper = new ItemTouchHelper(touchCallback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void executeOrderValidation() {
        adapter.setValidationState(true);
        List<RocketStage> currentOrder = adapter.getDataset();
        boolean isAllPerfect = true;

        for (int i = 0; i < currentOrder.size(); i++) {
            if (!currentOrder.get(i).getCorrectIdPosition().equals("POS_" + i)) {
                isAllPerfect = false;
                break;
            }
        }

        if (isAllPerfect) {
            if (correctPlayer != null) correctPlayer.start();
            victoryOverlay.setVisibility(View.VISIBLE);
        } else {
            if (wrongPlayer != null) wrongPlayer.start();
        }
    }

    private void resetAssemblyLine() {
        victoryOverlay.setVisibility(View.GONE);
        adapter.setValidationState(false);

        Collections.shuffle(stagesList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (correctPlayer != null) { correctPlayer.release(); correctPlayer = null; }
        if (wrongPlayer != null) { wrongPlayer.release(); wrongPlayer = null; }
    }
}