package com.zappproject.clubstorage.database.Shelf;

import androidx.room.Embedded;
import androidx.room.Ignore;

public class ShelfWithNumber {
    @Embedded public  Shelf shelf;
    public int number;  // The number of Items in this shelf

    @Ignore
    public ShelfWithNumber(Shelf shelf) {
        this.shelf = shelf;
        this.number = 0;
    }

    public ShelfWithNumber(Shelf shelf, int number) {
        this.shelf = shelf;
        this.number = number;
    }
}
