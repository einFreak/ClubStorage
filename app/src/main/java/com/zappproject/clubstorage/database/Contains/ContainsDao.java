package com.zappproject.clubstorage.database.Contains;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface ContainsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Contains contains);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Contains contains);

    @Delete
    void delete(Contains contains);
}
