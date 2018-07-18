package com.prcalibradores.prapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProcessFragment extends Fragment {
    private static final String ARG_MODEL_ID = "arg_model_id";

    public static ProcessFragment newInstance(String modelId) {

        Bundle args = new Bundle();
        args.putString(ARG_MODEL_ID, modelId);
        ProcessFragment fragment = new ProcessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);
        TextView textView = view.findViewById(R.id.process_text_view);
        textView.setText(getArguments().getString(ARG_MODEL_ID));
        return view;
    }
}
