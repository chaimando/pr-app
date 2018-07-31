package com.prcalibradores.app.view;

import com.prcalibradores.app.R;

import androidx.fragment.app.Fragment;

public class ProjectsListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ProjectsListFragment();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.projects_container;
    }

    @Override
    protected int getActivityLayoutId() {
        return R.layout.activity_projects_list;
    }

}
