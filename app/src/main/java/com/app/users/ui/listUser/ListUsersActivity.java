package com.app.users.ui.listUser;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.users.R;
import com.app.users.base.BaseActivity;
import com.app.users.databinding.ActivityListUsersBinding;
import com.app.users.ui.addUser.AddUserActivity;
import com.app.users.utils.DateUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ListUsersActivity extends BaseActivity<ActivityListUsersBinding, UsersListViewModel> {

    private UsersListAdapter usersListAdapter;

    @Override
    public Integer getLayoutId() {
        return R.layout.activity_list_users;
    }

    @Override
    public UsersListViewModel getViewModel() {
        UsersListViewModel usersListViewModel = new ViewModelProvider(this).get(UsersListViewModel.class);
        usersListViewModel.setRepository(new UsersListRepositoryImpl(getAppDatabase()));
        return usersListViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        observableListener();
        mViewModel.fetchAllUsers(0, 0);
    }

    private void initViews() {
        setupToolbarView();
        setupRecyclerView();
        FloatingActionButton fab = findViewById(R.id.fabAddUser);
        fab.setOnClickListener(view -> openAddUserActivity());
    }

    private void setupToolbarView() {
        setupToolbar((Toolbar) viewDataBinding.toolbar, getString(R.string.app_name), false);
        ConstraintLayout clMenu = viewDataBinding.toolbar.findViewById(R.id.cl_menu);
        clMenu.setVisibility(View.VISIBLE);
        clMenu.setOnClickListener(view -> openFilterDialog());
    }

    private void setupRecyclerView() {
        usersListAdapter = new UsersListAdapter(this, new UsersListAdapter.UserDiff());
        viewDataBinding.rvUsers.setAdapter(usersListAdapter);
        viewDataBinding.rvUsers.setItemAnimator(null);
        viewDataBinding.rvUsers.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        viewDataBinding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void observableListener() {
        mViewModel.allUsersLiveDate.observe(this, userModels -> {
            usersListAdapter.submitList(userModels);
            viewDataBinding.setHasList(!userModels.isEmpty());
        });
    }

    long lastFromTimeStamp = DateUtils.getTodayDate();
    long lastToTimeStamp = DateUtils.getTomorrowDate();

    private void openFilterDialog() {
        FilterBtmSheetFragment filterBtmSheetFragment = FilterBtmSheetFragment.newInstance();
        filterBtmSheetFragment.setLastTimesTamp(lastFromTimeStamp, lastToTimeStamp);
        filterBtmSheetFragment.setFilterListener(new FilterListener() {
            @Override
            public void onFilterClicked(long fromDate, long toDate) {
                lastFromTimeStamp = fromDate;
                lastToTimeStamp = toDate;
                mViewModel.fetchAllUsers(fromDate, toDate);
            }

            @Override
            public void onResetClicked() {
                lastFromTimeStamp = System.currentTimeMillis();
                lastToTimeStamp = System.currentTimeMillis();
                mViewModel.fetchAllUsers(0, 0);
            }
        });
        filterBtmSheetFragment.show(getSupportFragmentManager(), "FilterBtmSheetFragment");
    }

    private void openAddUserActivity() {
        AddUserActivity.startActivity(this);
    }

}