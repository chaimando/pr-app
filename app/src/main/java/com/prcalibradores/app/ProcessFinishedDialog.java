package com.prcalibradores.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private static final String STATE_NEXT_PROCESS_TEXT_VIEW = "state_next_process_text_view";
    private static final String STATE_FINISHED_PIECES_TEXT_VIEW = "state_finished_pieces_text_view";
    private static final String STATE_PIECES_TEXT_VIEW = "state_pieces_text_view";
    private static final String STATE_YES_BUTTON = "state_yes_button";

    private LinearLayout mLayoutFinished;
    private LinearLayout mLayoutProgress;
    private LinearLayout mLayoutAnotherPiece;
    private TextView mNextProcess;
    private TextView mPieces;
    private TextView mFinishedPieces;
    private Button mYesButton;
    private Button mNoButton;

    private String mNextProcessText;
    private String mPiecesText;
    private String mFinishedPiecesText;

    private String mPieceId;
    private JSONObject mJSONObject;
    private int mDeaths;
    private int mYesButtonVisibility;

    public static ProcessFinishedDialog newInstance(String mPieceId, JSONObject json, int deaths) {
        Bundle args = new Bundle();
        args.putString(ARG_PIECE_ID, mPieceId);
        args.putString(ARG_JSON_PROCESS, json.toString());
        args.putInt(ARG_DEATHS, deaths);
        ProcessFinishedDialog fragment = new ProcessFinishedDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_finished_process, null);
        mLayoutFinished     = view.findViewById(R.id.dialog_finished_process_layout);
        mLayoutProgress     = view.findViewById(R.id.dialog_finished_process_progress_layout);
        mLayoutAnotherPiece = view.findViewById(R.id.dialog_finished_layout_another_piece);

        mYesButton  = view.findViewById(R.id.dialog_finished_button_yes);
        mYesButton.setVisibility(mYesButtonVisibility);
        if (mYesButtonVisibility == View.GONE)
            mLayoutAnotherPiece.setVisibility(View.GONE);
        mNoButton   = view.findViewById(R.id.dialog_finished_button_no);

        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult(true);
            }
        });
        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendResult(false);
            }
        });

        mNextProcess    = view.findViewById(R.id.dialog_next_process);
        mNextProcess.setText(mNextProcessText);
        mPieces         = view.findViewById(R.id.dialog_pieces);
        mPieces.setText(mPiecesText);
        mFinishedPieces = view.findViewById(R.id.dialog_finished_pieces);
        mFinishedPieces.setText(mFinishedPiecesText);

        if (getArguments() != null) {
            mPieceId = getArguments().getString(ARG_PIECE_ID);
            try {
                mJSONObject = new JSONObject(getArguments().getString(ARG_JSON_PROCESS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mDeaths = getArguments().getInt(ARG_DEATHS);
        }

        if (savedInstanceState == null) {
            mLayoutFinished.setVisibility(View.INVISIBLE);
            mLayoutProgress.setVisibility(View.VISIBLE);
            new RestClient().setTime(mPieceId, mJSONObject, mDeaths, new RestClient.Callback() {
                @Override
                public void onSuccess(JSONArray result) {

                }

                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    mLayoutFinished.setVisibility(View.VISIBLE);
                    mLayoutProgress.setVisibility(View.INVISIBLE);
                    int pieces = Integer.parseInt(
                            result.getString("model_pieces")
                    );
                    int finishedPieces = Integer.parseInt(
                            result.getString("model_finished_pieces")
                    );
                    String piecesString = getString(R.string.dialog_finished_pieces, pieces);
                    String finishedPiecesString = getString(R.string.dialog_finished_finished_pieces,
                            finishedPieces);
                    mPieces.setText(piecesString);
                    mFinishedPieces.setText(finishedPiecesString);
                    Log.d(TAG, "onSuccess: " + finishedPieces);
                    Log.d(TAG, "onSuccess: " + pieces);
                    if (pieces >= finishedPieces) {
                        String nextProcess = getString(R.string.dialog_next_process_text,
                                result.getString("process_name"));
                        mNextProcess.setText(nextProcess);
                    } else {
                        mYesButton.setVisibility(View.GONE);
                        mLayoutAnotherPiece.setVisibility(View.GONE);
                    }
                    Log.d(TAG, "onSuccess: Registrado");
                }

                @Override
                public void onFailure(String response) { }
            });
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mNextProcessText = savedInstanceState.getString(STATE_NEXT_PROCESS_TEXT_VIEW);
            mPiecesText = savedInstanceState.getString(STATE_PIECES_TEXT_VIEW);
            mFinishedPiecesText = savedInstanceState.getString(STATE_FINISHED_PIECES_TEXT_VIEW);
            mYesButtonVisibility = savedInstanceState.getInt(STATE_YES_BUTTON);
        }
    }

    /*@NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(
                getContext()).inflate(R.layout.dialog_finished_process, null);

        mLayoutFinished = view.findViewById(R.id.dialog_finished_process_layout);
        mLayoutProgress = view.findViewById(R.id.dialog_finished_process_progress_layout);
        mLayoutAnotherPiece = view.findViewById(R.id.dialog_finished_layout_another_piece);
        mNextProcess = view.findViewById(R.id.dialog_next_process);
        mPieces = view.findViewById(R.id.dialog_pieces);
        mFinishedPieces = view.findViewById(R.id.dialog_finished_pieces);

        mNextProcess.setText(mNextProcessText);
        mPieces.setText(mPiecesText);
        mFinishedPieces.setText(mFinishedPiecesText);

        if (savedInstanceState == null) {
            mLayoutFinished.setVisibility(View.INVISIBLE);
            mLayoutProgress.setVisibility(View.VISIBLE);
            new RestClient().setTime(mPieceId, mJSONObject, mDeaths,
                new RestClient.Callback() {
                @Override
                public void onSuccess(JSONArray result) {

                }

                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    mLayoutFinished.setVisibility(View.VISIBLE);
                    mLayoutProgress.setVisibility(View.INVISIBLE);
                    int pieces = Integer.parseInt(
                            result.getString("model_pieces")
                    );
                    int finishedPieces = Integer.parseInt(
                            result.getString("model_finished_pieces")
                    );
                    String piecesString = getString(R.string.dialog_finished_pieces, pieces);
                    String finishedPiecesString = getString(R.string.dialog_finished_finished_pieces,
                            finishedPieces);
                    String nextProcess = getString(R.string.dialog_next_process_text,
                            result.getString("process_name"));
                    mNextProcess.setText(nextProcess);
                    mPieces.setText(piecesString);
                    mFinishedPieces.setText(finishedPiecesString);
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
                }).setNegativeButton(R.string.dialog_finished_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(false);
                    }
                })
                .create();
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_NEXT_PROCESS_TEXT_VIEW, mNextProcess.getText().toString());
        outState.putString(STATE_PIECES_TEXT_VIEW, mPieces.getText().toString());
        outState.putString(STATE_FINISHED_PIECES_TEXT_VIEW, mFinishedPieces.getText().toString());
        outState.putInt(STATE_YES_BUTTON, mYesButton.getVisibility());
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
