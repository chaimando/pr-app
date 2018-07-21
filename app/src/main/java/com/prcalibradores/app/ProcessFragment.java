package com.prcalibradores.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.PersistentCookieStore;
import com.prcalibradores.app.model.User;
import com.prcalibradores.app.model.UsersLab;
import com.prcalibradores.app.networking.RestClient;
import com.prcalibradores.app.networking.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import library.minimize.com.chronometerpersist.ChronometerPersist;

import static com.prcalibradores.app.R.drawable.ic_pause;
import static com.prcalibradores.app.R.drawable.ic_play;

public class ProcessFragment extends Fragment {
    private static final String TAG = "ProcessFragmentLog";

    private static final String ARG_MODEL_ID = "arg_model_id";

    private static final String STATE_DEATHS = "state_deaths";
    private static final String STATE_CHRONOMETER = "state_chronometer";
    private static final String STATE_ELAPSED_TIME = "state_elapsed_time";
    private static final String STATE_PIECE_ID = "state_piece_id";
    private static final String DIALOG_FINISHED_PROCESS = "dialog_finished_process";
    private static final int REQUEST_ANOTHER_PIECE = 120;

    private FloatingActionButton mPlayButton;
    private FloatingActionButton mStopButton;
    private FloatingActionButton mDeathButton;
    private ChronometerPersist mChronometer;

    private int mDeathCounter;
    private boolean mIsRunning;
    private long mElapsedTime;
    private String mPieceId;
    private String mModelId;

    static ProcessFragment newInstance(String modelId) {
        Bundle args = new Bundle();
        args.putString(ARG_MODEL_ID, modelId);
        ProcessFragment fragment = new ProcessFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mDeathCounter = savedInstanceState.getInt(STATE_DEATHS);
            mIsRunning = savedInstanceState.getBoolean(STATE_CHRONOMETER);
            mElapsedTime = savedInstanceState.getLong(STATE_ELAPSED_TIME);
            mPieceId = savedInstanceState.getString(STATE_PIECE_ID);
        }

        if (getArguments() != null) {
            mModelId = getArguments().getString(ARG_MODEL_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);

        final ConstraintLayout progressLayout = view.findViewById(R.id.process_progress_layout);
        final ConstraintLayout chronometerLayout = view.findViewById(R.id.process_chronometer_layout);

        if (savedInstanceState == null) {
            progressLayout.setVisibility(View.VISIBLE);
            chronometerLayout.setVisibility(View.INVISIBLE);

            PersistentCookieStore cookieStore = Utils.getCookieStore(getActivity());
            UUID id = UUID.fromString(cookieStore.getCookies().get(0).getValue());
            User user = UsersLab.get(getActivity()).getUser(id);

            new RestClient().setNewPiece(mModelId, user.getProcessId(),
                new RestClient.Callback() {
                @Override
                public void onSuccess(JSONArray result) throws JSONException {
                    progressLayout.setVisibility(View.INVISIBLE);
                    chronometerLayout.setVisibility(View.VISIBLE);
                    mPieceId = result.getJSONObject(0).getString("piece_id");
                }

                @Override
                public void onSuccess(JSONObject result) throws JSONException {
                    progressLayout.setVisibility(View.INVISIBLE);
                    chronometerLayout.setVisibility(View.VISIBLE);
                    mPieceId = result.getString("piece_id");
                }

                @Override
                public void onFailure(String response) {/*Desplegar un error*/}
            });
        } else {
            progressLayout.setVisibility(View.INVISIBLE);
            chronometerLayout.setVisibility(View.VISIBLE);
        }

        mPlayButton = view.findViewById(R.id.process_button_play);
        mStopButton = view.findViewById(R.id.process_button_stop);
        mDeathButton = view.findViewById(R.id.process_button_death);

        mChronometer = ChronometerPersist.Companion.getInstance(
                (Chronometer) view.findViewById(R.id.process_chronometer),
                Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE));

        mStopButton.setEnabled(mIsRunning);
        mDeathButton.setEnabled(mIsRunning);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChronometer.isRunning()) {
                    pauseChronometer();
                }
                else {
                    startChronometer();
                }
                mStopButton.setEnabled(true);
                mDeathButton.setEnabled(true);
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayButton.setEnabled(false);
                mDeathButton.setEnabled(false);
                mStopButton.setEnabled(false);

                if (mChronometer.isRunning()) {
                    pauseChronometer();
                }

                mChronometer.stopChronometer();

                Log.i(TAG, "onClick: Final time: " + mElapsedTime);
                Toast.makeText(getActivity(), "Tiempo final: " + mElapsedTime, Toast.LENGTH_SHORT).show();

                registTime();
            }
        });

        mDeathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChronometer.isRunning()) {
                    pauseChronometer();
                }
                mDeathCounter ++;
                Log.d(TAG, "onClick: Number of mDeaths" + mDeathCounter);
                Toast.makeText(getActivity(), "Número de muertes: " + mDeathCounter, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mChronometer.resumeState();
        if (mChronometer.isRunning()) {
            setPlayButtonImage(ic_pause);
        } else {
            setPlayButtonImage(ic_play);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_DEATHS, mDeathCounter);
        outState.putBoolean(STATE_CHRONOMETER, mChronometer.isRunning());
        outState.putLong(STATE_ELAPSED_TIME, mElapsedTime);
        outState.putString(STATE_PIECE_ID, mPieceId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ANOTHER_PIECE) {
            boolean anotherPiece = data.getBooleanExtra(ProcessFinishedDialog.EXTRA_ANOTHER_PIECE, false);
            if (!anotherPiece) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
            } else {
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager != null) {
                    Intent intent = ProcessActivity.getIntent(
                            mModelId,
                            getActivity());
                    Objects.requireNonNull(getActivity()).startActivity(intent);
                    getActivity().finish();
                }
            }
        }
    }

    private void setPlayButtonImage(int resDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPlayButton.setImageDrawable(Objects.requireNonNull(getActivity()).getDrawable(resDrawable));
        }
    }

    private void getElapsedTime() {
        mElapsedTime += SystemClock.elapsedRealtime() - mChronometer.getMTimeBase();
        Toast.makeText(getContext(), "Partial time: " + mElapsedTime, Toast.LENGTH_SHORT).show();
    }

    private void pauseChronometer() {
        setPlayButtonImage(ic_play);
        mChronometer.pauseChronometer();
        getElapsedTime();
    }

    private void startChronometer() {
        setPlayButtonImage(ic_pause);
        mChronometer.startChronometer();
    }

    private void registTime() {
        PersistentCookieStore cookieStore = Utils.getCookieStore(getActivity());
        UUID id = UUID.fromString(cookieStore.getCookies().get(0).getValue());
        User user = UsersLab.get(getActivity()).getUser(id);

        JSONObject process = Utils.getProcessJSON(user, mElapsedTime);

        FragmentManager manager = getFragmentManager();
        if (manager != null) {
            ProcessFinishedDialog dialog = ProcessFinishedDialog
                    .newInstance(mPieceId, process, mDeathCounter);
            dialog.setTargetFragment(ProcessFragment.this, REQUEST_ANOTHER_PIECE);
            dialog.setCancelable(false);
            dialog.show(manager, DIALOG_FINISHED_PROCESS);
        }
    }
}
