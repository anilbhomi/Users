package com.app.users.ui.listUser;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.users.databinding.FragmentFilterBinding;
import com.app.users.utils.DateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class FilterBtmSheetFragment extends BottomSheetDialogFragment {

    private FilterListener filterListener;

    private FragmentFilterBinding mBinding;

    private long fromTimeStamp;

    private long toTimeStamp;

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public void setLastTimesTamp(long from, long to) {
        fromTimeStamp = from;
        toTimeStamp = to;
    }

    public void removeListener() {
        filterListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentFilterBinding.inflate(inflater, container, false);
        mBinding.executePendingBindings();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        mBinding.tvFromDate.setText(DateUtils.generateDateFromTimeStamp(fromTimeStamp));
        mBinding.tvToDate.setText(DateUtils.generateDateFromTimeStamp(toTimeStamp));

        mBinding.btnApply.setOnClickListener(view -> {
            filterListener.onFilterClicked(fromTimeStamp, toTimeStamp);
            dismiss();
        });

        mBinding.tvReset.setOnClickListener(view -> {
            filterListener.onResetClicked();
            dismiss();
        });

        mBinding.llTo.setOnClickListener(view -> openToDatePicker());

        mBinding.llFrom.setOnClickListener(view -> openFromDatePicker());
    }

    private void openFromDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fromTimeStamp);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    fromTimeStamp = calendar.getTimeInMillis();
                    mBinding.tvFromDate.setText(DateUtils.generateDateFromTimeStamp(fromTimeStamp));
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(toTimeStamp);
        datePickerDialog.show();
    }

    private void openToDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(toTimeStamp);
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    toTimeStamp = calendar.getTimeInMillis();
                    mBinding.tvToDate.setText(DateUtils.generateDateFromTimeStamp(toTimeStamp));
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDetach() {
        removeListener();
        super.onDetach();
    }

    public static FilterBtmSheetFragment newInstance() {
        return new FilterBtmSheetFragment();
    }

}

interface FilterListener {
    void onFilterClicked(long fromDate, long toDate);

    void onResetClicked();
}
