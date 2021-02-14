package com.zappproject.clubstorage.database.Item;

import androidx.room.Embedded;
import androidx.room.Ignore;

public class ItemWithNumber {
    @Embedded
    public Item item;
    public double number;

    public ItemWithNumber(Item item, double number) {
        this.item = item;
        this.number = number;
    }

    @Ignore
    public ItemWithNumber(Item item) {
        this.item = item;
        this.number = 0.0;
    }
}
