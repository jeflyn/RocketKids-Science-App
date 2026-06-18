package com.rocketkids.science;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutMeFragment extends Fragment {

    private static final String DEVELOPER_NAME   = "Jeflyn Khoo Jia Xuan";
    private static final String DEVELOPER_MATRIC = "Matric No: 23004938";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about_me, container, false);

        TextView txtName   = view.findViewById(R.id.txt_dev_name);
        TextView txtMatric = view.findViewById(R.id.txt_dev_matric);

        txtName.setText(DEVELOPER_NAME);
        txtMatric.setText(DEVELOPER_MATRIC);

        return view;
    }
}