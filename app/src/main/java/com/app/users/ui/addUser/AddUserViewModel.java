package com.app.users.ui.addUser;

import androidx.lifecycle.MutableLiveData;

import com.app.users.R;
import com.app.users.base.BaseViewModel;
import com.app.users.data.local.UserModel;

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
                showToastMessage("User added successfully.");
            } else {
                showToastMessage("Failed");
            }
        }
    }

    public String validateDate() {
        if (Objects.equals(etName.getValue(), "") || Objects.equals(etName.getValue(), null)) {
            return "Name is required.";
        } else if (Objects.equals(etPhone.getValue(), "") || Objects.equals(etPhone.getValue(), null)) {
            return "Phone number is required.";
        } else if (Objects.requireNonNull(etPhone.getValue()).length() != 10) {
            return "Phone number should be of 10 digits.";
        } else if (Objects.equals(etEmail.getValue(), "") || Objects.equals(etEmail.getValue(), null)) {
            return "Email address is required.";
        } else if (!isEmailValid(etEmail.getValue())) {
            return "Enter valid email address.";
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
                return "Male";
            } else if (selectedGender.getValue() == R.id.rbFemale) {
                return "Female";
            } else {
                return "Others";
            }
        } else {
            return "Male";
        }
    }
}