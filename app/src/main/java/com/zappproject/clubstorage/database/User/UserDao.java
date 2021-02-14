package com.zappproject.clubstorage.database.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM " + UserTable.TABLE_NAME + " ORDER BY " + UserTable.LASTNAME + " DESC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.EMAIL + "= :email LIMIT 1")
    User getUser(String email);

//    @Query("SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.U_ID + " = :id")
//    LiveData<User> getUser(int id);

//    @Query("SELECT * FROM " + UserTable.TABLE_NAME + " WHERE " + UserTable.PERMISSION + " = :permission")
//    LiveData<List<User>> getUsersOfPermission(int permission);
}
