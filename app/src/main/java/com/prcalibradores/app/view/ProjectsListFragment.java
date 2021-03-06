package com.prcalibradores.app.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prcalibradores.app.R;
import com.prcalibradores.app.model.Project;
import com.prcalibradores.app.networking.RestClient;
import com.prcalibradores.app.networking.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectsListFragment extends Fragment {

    private static final int REQUEST_MODEL = 100;
    private static final String TAG = "ProjectsListFragment";
    private RecyclerView mProjectsRecyclerView;
    private ProjectsAdapter mProjectsAdapter;
    private ConstraintLayout mProgressLayout;
    private ConstraintLayout mEmptyListLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_projects, container, false);

        mProjectsRecyclerView = view.findViewById(R.id.projects_recycler_view);
        mProjectsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressLayout = view.findViewById(R.id.project_list_progress_layout);
        mEmptyListLayout = view.findViewById(R.id.projects_empty_list_layout);

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        final ArrayList<Project> projects = new ArrayList<>();

        RestClient client = new RestClient();
        mProgressLayout.setVisibility(View.VISIBLE);
        mProjectsRecyclerView.setVisibility(View.INVISIBLE);
        mEmptyListLayout.setVisibility(View.INVISIBLE);
        client.getProjects(new RestClient.Callback() {
            @Override
            public void onSuccess(JSONArray result) throws JSONException {
                for (int i = 0; i < result.length(); i++) {
                    projects.add(
                            Utils.getProjectFromJson(result.getJSONObject(i))
                    );
                }
                if (mProjectsAdapter == null) {
                    mProjectsAdapter = new ProjectsAdapter(projects);
                    mProjectsRecyclerView.setAdapter(mProjectsAdapter);
                } else {
                    mProjectsAdapter.setProjects(projects);
                    mProjectsAdapter.notifyDataSetChanged();
                }
                mProgressLayout.setVisibility(View.INVISIBLE);
                mProjectsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(JSONObject result) {
                projects.add(
                        Utils.getProjectFromJson(result)
                );
                if (mProjectsAdapter == null) {
                    mProjectsAdapter = new ProjectsAdapter(projects);
                    mProjectsRecyclerView.setAdapter(mProjectsAdapter);
                } else {
                    mProjectsAdapter.setProjects(projects);
                    mProjectsAdapter.notifyDataSetChanged();
                }
                mProgressLayout.setVisibility(View.INVISIBLE);
                mProjectsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String response) {
                Log.e("ProjectsListFragments", response);
                mProgressLayout.setVisibility(View.INVISIBLE);
                mProjectsRecyclerView.setVisibility(View.INVISIBLE);
                mEmptyListLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private class ProjectsAdapter extends RecyclerView.Adapter<ProjectsHolder> {

        private List<Project> mProjects;

        ProjectsAdapter(List<Project> projects) {
            mProjects = projects;
        }

        @NonNull
        @Override
        public ProjectsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new ProjectsHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ProjectsHolder holder, int position) {
            Project project = mProjects.get(position);
            holder.bind(project);
        }

        @Override
        public int getItemCount() {
            return mProjects.size();
        }

        void setProjects(List<Project> projects) {
            mProjects = projects;
        }
    }

    private class ProjectsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String DIALOG_MODEL = "dialog_model";
        private String DATE_PATTERN = "E MMM d, y";

        private TextView mIdTextView;
        //private TextView mNameTextView;
        private TextView mStartDateTextView;
        private TextView mDeadLineTextView;
        private TextView mStatusTextView;

        private Project mProject;

        ProjectsHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_project, parent, false));

            mIdTextView = itemView.findViewById(R.id.project_list_item_id);
            //mNameTextView = itemView.findViewById(R.id.project_list_item_name);
            mStartDateTextView = itemView.findViewById(R.id.project_list_item_start_date);
            mDeadLineTextView = itemView.findViewById(R.id.project_list_item_dead_line);
            mStatusTextView = itemView.findViewById(R.id.project_list_item_status);

            itemView.setOnClickListener(this);
        }

        void bind(Project project) {
            mProject = project;
            mIdTextView.setText(project.getId());
            //mNameTextView.setText(project.getName());
            mStartDateTextView.setText(DateFormat.format(DATE_PATTERN, project.getStartdate()));
            mDeadLineTextView.setText(DateFormat.format(DATE_PATTERN, project.getDeadline()));
            mStatusTextView.setText(project.getStatus());
        }

        @Override
        public void onClick(View view) {
            FragmentManager manager = getFragmentManager();
            if (manager != null) {
                ModelsListDialog dialog = ModelsListDialog.newInstance(mProject.getId());
                dialog.setTargetFragment(ProjectsListFragment.this, REQUEST_MODEL);
                dialog.show(manager, DIALOG_MODEL);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_MODEL) {
            String modelId = data.getStringExtra(ModelsListDialog.EXTRA_MODEL_ID);
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                Intent intent = ProcessActivity.getIntent(modelId, getActivity());
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
            }
        }
    }
}
