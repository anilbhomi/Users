package com.app.users.ui.addUser;

import com.app.users.base.BaseRepository;
import com.app.users.data.local.AppDatabase;
import com.app.users.data.local.UserModel;

public interface AddUserRepository extends BaseRepository {
    Boolean insertUser(UserModel userModel);
}

class AddUserRepositoryImpl implements AddUserRepository {
    private final AppDatabase appDatabase;

    public AddUserRepositoryImpl(AppDatabase appDatabase) {
        this.appDatabase = appDatabase;
    }

    @Override
    public Boolean insertUser(UserModel userModel) {
        try {
            AppDatabase.databaseWriteExecutor.execute(() -> {
                appDatabase.userModelDao().insert(userModel);
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

