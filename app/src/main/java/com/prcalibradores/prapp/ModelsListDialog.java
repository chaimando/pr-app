package com.prcalibradores.prapp;

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
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.PersistentCookieStore;
import com.prcalibradores.prapp.model.Model;
import com.prcalibradores.prapp.model.User;
import com.prcalibradores.prapp.model.UsersLab;
import com.prcalibradores.prapp.networking.RestClient;
import com.prcalibradores.prapp.networking.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ModelsListDialog extends DialogFragment {

    private static final String ARG_PROJECT_ID = "arg_project_id";
    private static final String TAG = "ModelsListDialog";
    static final String EXTRA_MODEL_ID = "com.prcalibradores.prapp.EXTRA_MODEL_ID";

    private String mProjectId;
    private ConstraintLayout mProgressLayout;
    private RecyclerView mRecyclerView;
    private ModelsAdapter mModelsAdapter;
    private ConstraintLayout mEmptyListLayout;

    static ModelsListDialog newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, id);
        ModelsListDialog fragment = new ModelsListDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectId = getArguments().getString(ARG_PROJECT_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_list_models, null);

        mProgressLayout = view.findViewById(R.id.models_progess_layout);
        mEmptyListLayout = view.findViewById(R.id.models_empty_list_layout);

        mRecyclerView = view.findViewById(R.id.models_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.models_dialog_title)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
    }

    private void updateUI() {
        final ArrayList<Model> models = new ArrayList<>();

        RestClient client = new RestClient();
        mProgressLayout.setVisibility(View.VISIBLE);
        mEmptyListLayout.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        PersistentCookieStore cookieStore = new PersistentCookieStore(getActivity());
        client.getClient().setCookieStore(cookieStore);
        String userId = cookieStore.getCookies().get(0).getValue();
        User user = UsersLab.get(getActivity()).getUser(UUID.fromString(userId));
        client.getModels(mProjectId, user.getIDDB(), new RestClient.Callback() {
            @Override
            public void onSuccess(JSONArray result) throws JSONException {
                for (int i = 0; i < result.length(); i++) {
                    models.add(
                            Utils.getModelFromJson(result.getJSONObject(i))
                    );
                    Log.d(TAG, "onSuccess: " + models.get(i).toString());
                }
                if (mModelsAdapter == null) {
                    mModelsAdapter = new ModelsAdapter(models);
                    mRecyclerView.setAdapter(mModelsAdapter);
                } else {
                    mModelsAdapter.setModels(models);
                    mModelsAdapter.notifyDataSetChanged();
                }
                mProgressLayout.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(JSONObject result) {
                models.add(Utils.getModelFromJson(result));
                if (mModelsAdapter == null) {
                    mModelsAdapter = new ModelsAdapter(models);
                    mRecyclerView.setAdapter(mModelsAdapter);
                } else {
                    mModelsAdapter.setModels(models);
                    mModelsAdapter.notifyDataSetChanged();
                }
                mProgressLayout.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String response) {
                Log.e(TAG, "onFailure: " + response);
                mEmptyListLayout.setVisibility(View.VISIBLE);
                mProgressLayout.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private class ModelsAdapter extends RecyclerView.Adapter<ModelsHolder> {
        private List<Model> mModels;

        ModelsAdapter(List<Model> models) {
            mModels = models;
        }

        @Override
        public ModelsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ModelsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ModelsHolder holder, int position) {
            holder.bind(mModels.get(position));
        }

        @Override
        public int getItemCount() {
            return mModels.size();
        }

        void setModels(ArrayList<Model> models) {
            mModels = models;
        }
    }

    private class ModelsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Model mModel;

        private TextView mIdTextView;
        private TextView mNameTextView;
        private TextView mDescriptionTextView;

        ModelsHolder (LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_models, parent, false));

            mIdTextView = itemView.findViewById(R.id.model_list_item_id);
            mNameTextView = itemView.findViewById(R.id.model_list_item_name);
            mDescriptionTextView = itemView.findViewById(R.id.model_list_item_description);
            itemView.setOnClickListener(this);
        }

        void bind(Model model) {
            mModel = model;
            mIdTextView.setText(model.getId());
            mNameTextView.setText(model.getName());
            mDescriptionTextView.setText(model.getDescription());
        }

        @Override
        public void onClick(View view) {
            sendResult(mModel.getId());
        }

        void sendResult(String modelId) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_MODEL_ID, modelId);

            if (getTargetFragment() == null) {
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            } else {
                dismiss();
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            }
        }
    }

}
