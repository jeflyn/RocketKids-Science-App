package com.rocketkids.science;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.HashSet;

public class LabelRocketFragment extends Fragment implements View.OnClickListener {

    private TextView txtPartTitle;
    private TextView txtPartDescription;
    private TextView txtPartsDiscovered;
    private ProgressBar progressBarDiscovered;
    private View currentlySelectedDot = null;
    private final HashSet<Integer> discoveredPartsSet = new HashSet<>();
    private final int TOTAL_PARTS = 6;

    private static class RocketPart {
        String title;
        String description;

        RocketPart(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_label_rocket, container, false);

        txtPartTitle = view.findViewById(R.id.txt_part_title);
        txtPartDescription = view.findViewById(R.id.txt_part_description);
        txtPartsDiscovered = view.findViewById(R.id.txt_parts_discovered);
        progressBarDiscovered = view.findViewById(R.id.progress_bar_discovered);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        view.findViewById(R.id.dot_nose_cone).setOnClickListener(this);
        view.findViewById(R.id.dot_payload_bay).setOnClickListener(this);
        view.findViewById(R.id.dot_fuel_tank).setOnClickListener(this);
        view.findViewById(R.id.dot_oxidizer_tank).setOnClickListener(this);
        view.findViewById(R.id.dot_stabiliser_fins).setOnClickListener(this);
        view.findViewById(R.id.dot_engine_nozzle).setOnClickListener(this);

        progressBarDiscovered.setMax(TOTAL_PARTS);
        updateProgressUI();

        return view;
    }

    @Override
    public void onClick(View view) {
        RocketPart selectedPart = null;
        int viewId = view.getId();

        if (currentlySelectedDot != null) {
            currentlySelectedDot.setSelected(false);
        }

        currentlySelectedDot = view;
        currentlySelectedDot.setSelected(true);

        if (viewId == R.id.dot_nose_cone) {
            selectedPart = new RocketPart(
                    "Nose Cone",
                    "Aerodynamic tip that slices through the air and reduces drag."
            );
            discoveredPartsSet.add(1);
        } else if (viewId == R.id.dot_payload_bay) {
            selectedPart = new RocketPart(
                    "Payload Bay",
                    "Carries the satellite, spacecraft, or scientific instruments to orbit."
            );
            discoveredPartsSet.add(2);
        } else if (viewId == R.id.dot_fuel_tank) {
            selectedPart = new RocketPart(
                    "Liquid Fuel Tank",
                    "Stores liquid hydrogen — the fuel that powers the engines."
            );
            discoveredPartsSet.add(3);
        } else if (viewId == R.id.dot_oxidizer_tank) {
            selectedPart = new RocketPart(
                    "Oxidizer Tank",
                    "Stores liquid oxygen, needed to burn the fuel without air."
            );
            discoveredPartsSet.add(4);
        } else if (viewId == R.id.dot_stabiliser_fins) {
            selectedPart = new RocketPart(
                    "Stabiliser Fins",
                    "Keep the rocket pointing straight by providing aerodynamic stability."
            );
            discoveredPartsSet.add(5);
        } else if (viewId == R.id.dot_engine_nozzle) {
            selectedPart = new RocketPart(
                    "Engine Nozzle",
                    "Hot gases shoot out here at enormous speed, pushing rocket upward!"
            );
            discoveredPartsSet.add(6);
        }

        if (selectedPart != null) {
            txtPartTitle.setText(selectedPart.title);
            txtPartDescription.setText(selectedPart.description);
            updateProgressUI();
        }
    }

    private void updateProgressUI() {
        int currentScore = discoveredPartsSet.size();

        progressBarDiscovered.setProgress(currentScore);

        String scoreText = currentScore + " / " + TOTAL_PARTS;
        txtPartsDiscovered.setText(scoreText);
    }
}