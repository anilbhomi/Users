package com.app.users.ui.addUser;

import androidx.lifecycle.MutableLiveData;

import com.app.users.R;
import com.app.users.base.BaseViewModel;
import com.app.users.data.local.UserModel;
import com.app.users.utils.AppConstants;

import java.util.Objects;

public class AddUserViewModel extends BaseViewModel {

    public MutableLiveData<String> etName = new MutableLiveData<>();
    public MutableLiveData<String> etPhone = new MutableLiveData<>();
    public MutableLiveData<String> etEmail = new MutableLiveData<>();
    public MutableLiveData<Long> registeredAt = new MutableLiveData<>(System.currentTimeMillis());
    public MutableLiveData<Integer> selectedGender = new MutableLiveData<>(R.id.rbMale);
    public String selectedDeviceType = "";
    public String profileImage = "";
    public long createdAt = System.currentTimeMillis();
    public MutableLiveData<Boolean> resetField = new MutableLiveData<>();


    public void onSubmit() {
        String message = validateDate();
        if (message != null) {
            showToastMessage(message);
        } else {
            UserModel userModel = new UserModel();
            userModel.name = etName.getValue();
            userModel.phone = etPhone.getValue();
            userModel.emailAddress = etEmail.getValue();
            userModel.gender = getGenderType();
            userModel.setDeviceType(selectedDeviceType);
            userModel.setCreatedAt(createdAt);
            userModel.setProfileImage(profileImage);
            if (((AddUserRepository) getRepository()).insertUser(userModel)) {
                resetField.setValue(true);
                showToastMessage(AppConstants.SUCCESS_MESSAGE);
            } else {
                showToastMessage(AppConstants.FAILED_MESSAGE);
            }
        }
    }

    public String validateDate() {
        if (Objects.equals(etName.getValue(), "") || Objects.equals(etName.getValue(), null)) {
            return AppConstants.ERR_EMPTY_NAME;
        } else if (Objects.equals(etPhone.getValue(), "") || Objects.equals(etPhone.getValue(), null)) {
            return AppConstants.ERR_EMPTY_PHONE;
        } else if (Objects.requireNonNull(etPhone.getValue()).length() != 10) {
            return AppConstants.ERR_PHONE_LENGTH;
        } else if (Objects.equals(etEmail.getValue(), "") || Objects.equals(etEmail.getValue(), null)) {
            return AppConstants.ERR_EMPTY_EMAIL;
        } else if (!isEmailValid(etEmail.getValue())) {
            return AppConstants.ERR_INVALID_EMAIL;
        } else if (profileImage.isEmpty()) {
            return AppConstants.ERR_EMPTY_PHOTO;
        } else {
            return null;
        }
    }

    boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    String getGenderType() {
        if (selectedGender.getValue() != null) {
            if (selectedGender.getValue() == R.id.rbMale) {
                return AppConstants.MALE;
            } else if (selectedGender.getValue() == R.id.rbFemale) {
                return AppConstants.FEMALE;
            } else {
                return AppConstants.OTHER;
            }
        } else {
            return AppConstants.MALE;
        }
    }
}