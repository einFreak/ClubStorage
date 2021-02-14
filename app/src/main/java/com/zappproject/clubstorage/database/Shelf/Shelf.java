package com.zappproject.clubstorage.database.Shelf;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = ShelfTable.TABLE_NAME)
public class Shelf {

    @PrimaryKey(autoGenerate = true)
    private int sId;
    private String title;
    private String note;


    @Ignore
    public Shelf(int sId, String title, String note) {
        this(title, note);
        this.sId = sId;
    }

    public Shelf(String title, String note) {
        this.title = title;
        this.note = note;
    }

    public int getSId() {
        return sId;
    }

    public void setSId(int sId) {
        this.sId = sId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
