package com.app.users.base;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.app.users.BR;
import com.app.users.R;
import com.app.users.data.local.AppDatabase;
import com.app.users.utils.ViewUtil;

public abstract class BaseActivity<DATA_BINDING extends ViewDataBinding, VIEW_MODEL extends BaseViewModel> extends AppCompatActivity {
    public DATA_BINDING viewDataBinding;

    public VIEW_MODEL mViewModel;

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDatabase();
        performDataBinding();
        initObservers();
    }

    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewDataBinding.setLifecycleOwner(this);
        viewDataBinding.executePendingBindings();
        mViewModel = getViewModel();
        viewDataBinding.setVariable(getBindingVariable(), mViewModel);
        ViewUtil.setupUI(this, viewDataBinding.getRoot());
    }

    private void initObservers() {
        if (mViewModel != null) {
            mViewModel.showToastEvent.observe(this, this::showToastMessage);
        }
    }

    public void showToastMessage(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void setupToolbar(Toolbar toolbar, String title, boolean backButtonEnabled) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(backButtonEnabled);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(title);
    }

    public void setDatabase() {
        appDatabase = AppDatabase.getDatabase(this);
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    @LayoutRes
    public abstract Integer getLayoutId();

    public abstract VIEW_MODEL getViewModel();

    public Integer getBindingVariable() {
        return BR.viewModel;
    }
}
