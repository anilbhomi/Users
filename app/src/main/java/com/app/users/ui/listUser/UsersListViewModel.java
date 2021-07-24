package com.app.users.ui.listUser;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.app.users.base.BaseViewModel;
import com.app.users.data.local.UserModel;

import java.util.List;

public class UsersListViewModel extends BaseViewModel {

    public MutableLiveData<Pair<Long, Long>> fetchAllUsers = new MutableLiveData<>();

    public LiveData<List<UserModel>> allUsersLiveDate = Transformations.switchMap(fetchAllUsers, data -> ((UsersListRepository) getRepository()).getAllUsers(data.first, data.second));


    public void fetchAllUsers(long from, long to) {
        fetchAllUsers.setValue(new Pair<>(from, to));
    }
}