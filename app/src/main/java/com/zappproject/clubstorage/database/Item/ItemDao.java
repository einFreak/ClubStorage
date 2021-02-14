package com.zappproject.clubstorage.database.Item;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.zappproject.clubstorage.database.Contains.ContainsTable;
import com.zappproject.clubstorage.database.Shelf.ShelfTable;

import java.util.List;

@Dao
public interface ItemDao {

    @Insert
    void insert(Item item);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Item item);

    @Delete
    void delete(Item item);

    @Query("SELECT * FROM " + ItemTable.TABLE_NAME + " ORDER BY " + ItemTable.TITLE)
    LiveData<List<Item>> getAllItems();

    @Query("Select i." + ItemTable.I_ID + ", i." + ItemTable.TITLE + ", i." + ItemTable.PRICE + ", " +
            "i." + ItemTable.UNIT + ", coalesce(i." + ItemTable.NOTE + ", \" \" ) as note, " +
            "coalesce(sum(c." + ContainsTable.NUMBER + "), 0.0) as number " +
            "from " + ItemTable.TABLE_NAME + " i " +
            "left outer join " + ContainsTable.TABLE_NAME + " c " +
            "on i. " + ItemTable.I_ID + " = c." + ContainsTable.I_ID +
            " group by i." + ItemTable.I_ID + ", i." + ItemTable.TITLE + ", i." + ItemTable.PRICE +
            ", i." + ItemTable.UNIT + ", i." + ItemTable.NOTE +
            " order by i." + ItemTable.TITLE)
    LiveData<List<ItemWithNumber>> getAllItemsWithNumber(); // get all Items with the number of the Item in all shelves

    @Query("SELECT i.*, c." + ContainsTable.NUMBER +
            " FROM " + ItemTable.TABLE_NAME + " i, " +
            ContainsTable.TABLE_NAME + " c, " +
            ShelfTable.TABLE_NAME + " l " +
            " WHERE c." + ContainsTable.I_ID + " = i." + ItemTable.I_ID +
            " and c." + ContainsTable.S_ID + " = l." + ShelfTable.S_ID +
            " and l." + ShelfTable.S_ID + " = :shelfId" +
            " ORDER BY i." + ItemTable.TITLE)
    List<ItemWithNumber> getAllItemsOfList(int shelfId);

    // Returns all items that exist with the number of items in the list
    @Query("SELECT i.*, coalesce(c." + ContainsTable.NUMBER + ", 0.0) as number " +
            "FROM " + ItemTable.TABLE_NAME + " i " +
            "LEFT OUTER JOIN (" +
            "SELECT * FROM " + ContainsTable.TABLE_NAME +
            " WHERE " + ContainsTable.S_ID + " = :shelfId" + ") c " +
            "on i." + ItemTable.I_ID + " = c." + ContainsTable.I_ID +
            " ORDER BY number DESC, i." + ItemTable.TITLE)
    List<ItemWithNumber> getAllItemsWithNumberOfList(int shelfId);
}
