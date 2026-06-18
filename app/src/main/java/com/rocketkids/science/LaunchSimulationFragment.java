package com.rocketkids.science;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;

public class LaunchSimulationFragment extends Fragment {

    private TextView txtMetricThrust, txtMetricAltitude, txtMetricStatus, txtThrustComparison;
    private SeekBar sliderThrust;
    private ImageView imgSimRocket;
    private MaterialButton btnActionLaunch;
    private boolean isLaunched = false;
    private int currentThrust = 0;
    private ValueAnimator flightAnimator;
    private MediaPlayer launchSoundPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launch_simulation, container, false);

        txtMetricThrust = view.findViewById(R.id.txt_metric_thrust);
        txtMetricAltitude = view.findViewById(R.id.txt_metric_altitude);
        txtMetricStatus = view.findViewById(R.id.txt_metric_status);
        txtThrustComparison = view.findViewById(R.id.txt_thrust_comparison);
        sliderThrust = view.findViewById(R.id.slider_thrust);
        imgSimRocket = view.findViewById(R.id.img_sim_rocket);
        btnActionLaunch = view.findViewById(R.id.btn_action_launch);
        launchSoundPlayer = MediaPlayer.create(getContext(), R.raw.rocket_launch_sound);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });

        setupThrustSlider();

        btnActionLaunch.setOnClickListener(v -> {
            if (!isLaunched) {
                tryExecutionLaunch();
            } else {
                abortMissionFlight();
            }
        });

        return view;
    }

    private void setupThrustSlider() {
        sliderThrust.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentThrust = progress;
                txtMetricThrust.setText(progress + "%");

                if (progress >= 50) {
                    txtThrustComparison.setText("Greater than Gravity ✅");
                    txtThrustComparison.setTextColor(Color.parseColor("#10B981"));
                } else {
                    txtThrustComparison.setText("Less than Gravity ❌");
                    txtThrustComparison.setTextColor(Color.parseColor("#EF4444"));
                }
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void tryExecutionLaunch() {
        if (currentThrust < 50) {
            txtMetricStatus.setText("Failed");
            txtMetricStatus.setTextColor(Color.parseColor("#EF4444"));

            showGravityWarningDialog();
            return;
        }

        if (launchSoundPlayer != null) {
            launchSoundPlayer.start();
        }

        isLaunched = true;
        sliderThrust.setEnabled(false);

        txtMetricStatus.setText("Ascending");
        txtMetricStatus.setTextColor(Color.parseColor("#10B981"));

        btnActionLaunch.setText("🛑 ABORT MISSION");
        btnActionLaunch.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#851E1E")));

        float startY = imgSimRocket.getTranslationY();
        float endY = startY - dpToPx(380);

        flightAnimator = ValueAnimator.ofFloat(startY, endY);
        flightAnimator.setDuration(4000);
        flightAnimator.setInterpolator(new android.view.animation.AccelerateInterpolator(1.5f));

        flightAnimator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            imgSimRocket.setTranslationY(value);

            float fraction = animation.getAnimatedFraction();
            int currentAltitude = (int) (fraction * 120);
            txtMetricAltitude.setText(currentAltitude + " km");
        });

        flightAnimator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (isLaunched) {
                    txtMetricStatus.setText("Completed");
                    txtMetricStatus.setTextColor(Color.parseColor("#10B981"));
                    btnActionLaunch.setText("🔄 RESET SIMULATION");
                    btnActionLaunch.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF6B00")));

                    btnActionLaunch.setOnClickListener(v -> resetSimulationEnvironment());
                }
            }

            @Override public void onAnimationStart(android.animation.Animator animation) {}
            @Override public void onAnimationCancel(android.animation.Animator animation) {}
            @Override public void onAnimationRepeat(android.animation.Animator animation) {}
        });

        flightAnimator.start();
    }

    private void showGravityWarningDialog() {
        LinearLayout dialogLayout = new LinearLayout(requireContext());
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.setBackgroundColor(Color.parseColor("#161B22"));
        int padding = dpToPx(24);
        dialogLayout.setPadding(padding, padding, padding, padding);
        dialogLayout.setGravity(android.view.Gravity.CENTER);

        TextView titleTv = new TextView(requireContext());
        titleTv.setText("⚠️ LAUNCH FAILAURE");
        titleTv.setTextColor(Color.parseColor("#EF4444"));
        titleTv.setTextSize(20);
        titleTv.setPadding(0, 0, 0, dpToPx(12));
        titleTv.setGravity(android.view.Gravity.CENTER);

        TextView messageTv = new TextView(requireContext());
        messageTv.setText("Engine thrust is too low! You must drag the slider until thrust is greater than gravity to clear the launch pad.");
        messageTv.setTextColor(Color.parseColor("#94A3B8"));
        messageTv.setTextSize(15);
        messageTv.setGravity(android.view.Gravity.CENTER);
        messageTv.setPadding(0, 0, 0, dpToPx(20));

        com.google.android.material.button.MaterialButton dismissButton = new com.google.android.material.button.MaterialButton(requireContext());
        dismissButton.setText("Try Again");
        dismissButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF6B00")));
        dismissButton.setTextColor(Color.WHITE);
        dismissButton.setCornerRadius(dpToPx(12));

        dialogLayout.addView(titleTv);
        dialogLayout.addView(messageTv);
        dialogLayout.addView(dismissButton);

        androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setView(dialogLayout)
                .setCancelable(true)
                .create();

        dismissButton.setOnClickListener(v -> dialog.dismiss());

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
    }

    private void abortMissionFlight() {
        if (flightAnimator != null) {
            flightAnimator.cancel();
        }
        txtMetricStatus.setText("Aborted 🛑");
        txtMetricStatus.setTextColor(Color.parseColor("#A855F7"));

        imgSimRocket.animate()
                .translationY(0)
                .setDuration(800)
                .setListener(null)
                .start();

        resetSimulationEnvironment();
    }

    private void resetSimulationEnvironment() {
        isLaunched = false;
        sliderThrust.setEnabled(true);
        sliderThrust.setProgress(0);
        currentThrust = 0;

        txtMetricAltitude.setText("0 km");
        txtMetricStatus.setText("Ready");
        txtMetricStatus.setTextColor(Color.parseColor("#A855F7"));

        imgSimRocket.setTranslationY(0);

        btnActionLaunch.setText("🚀 LAUNCH!");
        btnActionLaunch.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#FF6B00")));
        btnActionLaunch.setOnClickListener(v -> tryExecutionLaunch());
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (launchSoundPlayer != null) {
            launchSoundPlayer.release();
            launchSoundPlayer = null;
        }

        if (flightAnimator != null) {
            flightAnimator.cancel();
        }
    }
}