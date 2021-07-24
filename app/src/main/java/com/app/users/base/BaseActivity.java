package com.app.users.base;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.room.Room;

import com.app.users.BR;
import com.app.users.R;
import com.app.users.data.local.AppDatabase;

import org.w3c.dom.Text;

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
        setupUI(viewDataBinding.getRoot());
    }

    private void initObservers() {
        if (mViewModel != null) {
            mViewModel.showToastEvent.observe(this, this::showToastMessage);
        }
    }

    public void showToastMessage(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void setupToolbar(
            Toolbar toolbar,
            String title,
            boolean backButtonEnabled) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(backButtonEnabled);
        TextView tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        tvToolbarTitle.setText(title);
    }

    void setupUI(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    void hideSoftKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
