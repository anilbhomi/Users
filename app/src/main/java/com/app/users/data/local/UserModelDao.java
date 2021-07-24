package com.app.users.data.local;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserModelDao {
    @Query("SELECT * FROM users ORDER BY created_at ASC")
    LiveData<List<UserModel>> getAllUsers();

    @Query("SELECT * FROM users WHERE created_at  >= :from AND  created_at <= :to ORDER BY created_at ASC")
    LiveData<List<UserModel>> getFilteredUsers(long from, long to);

    @Insert
    void insert(UserModel userModel);

}