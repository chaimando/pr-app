package com.prcalibradores.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prcalibradores.app.networking.RestClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProcessFinishedDialog extends DialogFragment {
    private static final String ARG_PIECE_ID = "arg_piece_id";
    private static final String ARG_JSON_PROCESS = "arg_json_process";
    private static final String ARG_DEATHS = "arg_deaths";

    public static final String EXTRA_ANOTHER_PIECE = "extra_another_piece";

    private static final String TAG = "ProcessFinishedDialog";
    private static final String STATE_NEXT_PROCESS_TEXT_VIEW = "state_next_process";

    private LinearLayout mLayoutFinished;
    private LinearLayout mLayoutProgress;
    private TextView mNextProcess;

    private String mNextProcessText;
    private String mPieceId;
    private JSONObject mJSONObject;
    private int mDeaths;

    public static ProcessFinishedDialog newInstance(String mPieceId, JSONObject json, int deaths) {
        Bundle args = new Bundle();
        args.putString(ARG_PIECE_ID, mPieceId);
        args.putString(ARG_JSON_PROCESS, json.toString());
        args.putInt(ARG_DEATHS, deaths);
        ProcessFinishedDialog fragment = new ProcessFinishedDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNextProcessText = savedInstanceState.getString(STATE_NEXT_PROCESS_TEXT_VIEW);
        }
        if (getArguments() != null) {
            mPieceId = getArguments().getString(ARG_PIECE_ID);
            try {
                mJSONObject = new JSONObject(getArguments().getString(ARG_JSON_PROCESS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mDeaths = getArguments().getInt(ARG_DEATHS);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(
                getContext()).inflate(R.layout.dialog_finished_process, null);

        mLayoutFinished = view.findViewById(R.id.dialog_finished_process_layout);
        mLayoutProgress = view.findViewById(R.id.dialog_finished_process_progress_layout);
        mNextProcess = view.findViewById(R.id.dialog_next_process);
        mNextProcess.setText(mNextProcessText);

        if (savedInstanceState == null) {
            mLayoutFinished.setVisibility(View.INVISIBLE);
            mLayoutProgress.setVisibility(View.VISIBLE);
            new RestClient().setTime(mPieceId, mJSONObject, mDeaths,
                new RestClient.Callback() {
                @Override
                public void onSuccess(JSONArray result) throws JSONException {
                    mLayoutFinished.setVisibility(View.VISIBLE);
                    mLayoutProgress.setVisibility(View.INVISIBLE);
                    String nextProcess = getString(R.string.dialog_next_process_text,
                            result.getJSONObject(0).getString("process_name"));
                    mNextProcess.setText(nextProcess);
                    Log.d(TAG, "onSuccess: Registrado");
                }

                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    mLayoutFinished.setVisibility(View.VISIBLE);
                    mLayoutProgress.setVisibility(View.INVISIBLE);
                    String nextProcess = getString(R.string.dialog_next_process_text,
                            result.getString("process_name"));
                    mNextProcess.setText(nextProcess);
                    Log.d(TAG, "onSuccess: Registrado");
                }

                @Override
                public void onFailure(String response) { }
            });
        }

        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.process_dialog_finished_title)
                .setView(view)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(true);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(false);
                    }
                })
                .create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NEXT_PROCESS_TEXT_VIEW, mNextProcess.getText().toString());
    }

    private void sendResult(boolean anotherPiece) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ANOTHER_PIECE, anotherPiece);

        if (getTargetFragment() == null) {
            Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        } else {
            dismiss();
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }
    }
}
