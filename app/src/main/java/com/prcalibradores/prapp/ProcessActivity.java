package com.prcalibradores.prapp;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class ProcessActivity extends SingleFragmentActivity {

    private static final String EXTRA_MODEL_ID = "extra_model_id";

    public static Intent getIntent(String modelId, Context packageContext) {
        Intent intent = new Intent(packageContext, ProcessActivity.class);
        intent.putExtra(EXTRA_MODEL_ID, modelId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        return ProcessFragment.newInstance(
                getIntent().getStringExtra(EXTRA_MODEL_ID)
        );
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.process_container;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_process;
    }
}
