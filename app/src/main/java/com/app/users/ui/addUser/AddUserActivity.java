package com.app.users.ui.addUser;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.users.R;
import com.app.users.base.BaseActivity;
import com.app.users.databinding.ActivityAddUserBinding;
import com.bumptech.glide.Glide;

import java.util.Calendar;

public class AddUserActivity extends BaseActivity<ActivityAddUserBinding, AddUserViewModel> {

    private static final int REQUEST_GALLERY_IMAGE = 1000;

    @Override
    public Integer getLayoutId() {
        return R.layout.activity_add_user;
    }

    @Override
    public AddUserViewModel getViewModel() {
        AddUserViewModel addUserViewModel = new ViewModelProvider(this).get(AddUserViewModel.class);
        addUserViewModel.setRepository(new AddUserRepositoryImpl(getAppDatabase()));
        return addUserViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        observableListener();
    }

    private void initViews() {
        setupToolbar((Toolbar) viewDataBinding.toolbar, getString(R.string.add_users), true);

        RadioGroup rgGender = findViewById(R.id.rgGender);
        rgGender.setOnCheckedChangeListener((radioGroup, checkedId) -> mViewModel.selectedGender.setValue(checkedId));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.device_types, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewDataBinding.spinnerDeviceType.setAdapter(adapter);
        viewDataBinding.spinnerDeviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mViewModel.selectedDeviceType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ImageButton ibDateRefresh = findViewById(R.id.ibRefreshDate);
        ibDateRefresh.setOnClickListener(view -> openDatePicker());

        viewDataBinding.ivProfileImage.setOnClickListener(view -> openImagePicker());
    }

    private void observableListener() {
        mViewModel.resetField.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                clearSelection();
            }
        });
    }

    private void clearSelection() {
        Glide.with(AddUserActivity.this).load(R.drawable.ic_baseline_account_circle_24).into(viewDataBinding.ivProfileImage);
        viewDataBinding.spinnerDeviceType.setSelection(0);
        mViewModel.registeredAt.setValue(System.currentTimeMillis());
        mViewModel.selectedGender.setValue(R.id.rbMale);
        mViewModel.etName.setValue("");
        mViewModel.etEmail.setValue("");
        mViewModel.etPhone.setValue("");
        mViewModel.selectedDeviceType = "";
        mViewModel.profileImage = "";
        mViewModel.createdAt = System.currentTimeMillis();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GALLERY_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    mViewModel.profileImage = uri.toString();
                    Glide.with(this).load(uri).into(viewDataBinding.ivProfileImage);
                } else {
                    showToastMessage("Invalid Image");
                }
            }
        }
    }

    ActivityResultLauncher<String> mImageResult = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri != null) {
            mViewModel.profileImage = uri.toString();
            Glide.with(this).load(uri).into(viewDataBinding.ivProfileImage);
        } else {
            showToastMessage("Invalid Image");
        }
    });

    private void openDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    long timestamp = calendar.getTimeInMillis();
                    mViewModel.registeredAt.setValue(calendar.getTimeInMillis());
                    mViewModel.createdAt = timestamp;
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddUserActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageResult.unregister();
    }
}