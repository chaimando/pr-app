package com.prcalibradores.prapp;

import android.content.Context;
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

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import library.minimize.com.chronometerpersist.ChronometerPersist;

import static com.prcalibradores.prapp.R.drawable.ic_pause;
import static com.prcalibradores.prapp.R.drawable.ic_play;

public class ProcessFragment extends Fragment {
    private static final String TAG = "ProcessFragmentLog";

    private static final String ARG_MODEL_ID = "arg_model_id";

    private static final String STATE_DEATHS = "state_deaths";
    private static final String STATE_CHRONOMETER = "state_chronometer";
    private static final String STATE_ELAPSED_TIME = "state_elapsed_time";

    private FloatingActionButton mPlayButton;
    private FloatingActionButton mStopButton;
    private FloatingActionButton mDeathButton;
    private ChronometerPersist mChronometer;

    private int mDeathCounter;
    private boolean mIsRunning;
    private long mElapsedTime;

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
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);

        mPlayButton = view.findViewById(R.id.process_button_play);
        mStopButton = view.findViewById(R.id.process_button_stop);
        mDeathButton = view.findViewById(R.id.process_button_death);

        mChronometer = ChronometerPersist.Companion.getInstance(
                (Chronometer) view.findViewById(R.id.process_chronometer),
                getActivity().getPreferences(Context.MODE_PRIVATE)
        );

        mStopButton.setEnabled(mIsRunning);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChronometer.isRunning()) {
                    setPlayButtonImage(ic_play);
                    mChronometer.pauseChronometer();
                    getElapsedTime();
                }
                else {
                    mChronometer.setMTimeBase(SystemClock.elapsedRealtime());
                    setPlayButtonImage(ic_pause);
                    mChronometer.startChronometer();
                }
                mStopButton.setEnabled(true);
            }
        });

        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayButton.setEnabled(false);
                mDeathButton.setEnabled(false);
                mStopButton.setEnabled(false);
                setPlayButtonImage(ic_play);

                mChronometer.pauseChronometer();
                getElapsedTime();
                mChronometer.stopChronometer();

                Log.i(TAG, "onClick: Final time: " + mElapsedTime);
                Toast.makeText(getActivity(), "Tiempo final: ", Toast.LENGTH_SHORT).show();
            }
        });

        mDeathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChronometer.pauseChronometer();
                getElapsedTime();
                mDeathCounter ++;
                Log.d(TAG, "onClick: Number of deaths" + mDeathCounter);
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
}
