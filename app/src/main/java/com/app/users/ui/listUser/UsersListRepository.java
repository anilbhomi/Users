package com.app.users.ui.listUser;

import androidx.lifecycle.LiveData;

import com.app.users.base.BaseRepository;
import com.app.users.data.local.AppDatabase;
import com.app.users.data.local.UserModel;

import java.util.List;

public interface UsersListRepository extends BaseRepository {
    LiveData<List<UserModel>> getAllUsers(long from, long to);
}

class UsersListRepositoryImpl implements UsersListRepository {
    private final AppDatabase appDatabase;

    public UsersListRepositoryImpl(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public LiveData<List<UserModel>> getAllUsers(long from, long to) {
        if (from == 0 && to == 0) {
            return appDatabase.userModelDao().getAllUsers();
        } else {
            return appDatabase.userModelDao().getFilteredUsers(from, to);
        }
    }

}

