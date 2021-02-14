package com.zappproject.clubstorage.database.Shelf;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zappproject.clubstorage.database.Contains.ContainsTable;

import java.util.List;

@Dao
public interface ShelfDao {

    @Insert
    void insert(Shelf shelf);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Shelf shelf);

    @Delete
    void delete(Shelf shelf);

    @Query("SELECT * FROM " + ShelfTable.TABLE_NAME + " ORDER BY " + ShelfTable.TITLE)
    LiveData<List<Shelf>> getAllLists();

    @Query("Select s.*, coalesce(count(c." + ContainsTable.NUMBER + "), 0) as number " +
            "from " + ShelfTable.TABLE_NAME + " s " +
            "left outer join " + ContainsTable.TABLE_NAME + " c " +
            "on s." + ShelfTable.S_ID + " = c." + ContainsTable.S_ID +
            " group by s." + ShelfTable.S_ID + ", s." + ShelfTable.TITLE + ", s." + ShelfTable.NOTE)
    LiveData<List<ShelfWithNumber>> getAllShelvesWithNumber();
}
