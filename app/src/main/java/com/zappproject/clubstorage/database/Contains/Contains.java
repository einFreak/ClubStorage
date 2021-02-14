package com.zappproject.clubstorage.database.Contains;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import com.zappproject.clubstorage.database.Item.Item;
import com.zappproject.clubstorage.database.Item.ItemTable;
import com.zappproject.clubstorage.database.Shelf.Shelf;
import com.zappproject.clubstorage.database.Shelf.ShelfTable;

@Entity(tableName = ContainsTable.TABLE_NAME,
		primaryKeys = {"sId", "iId"},
		foreignKeys = {
				@ForeignKey(entity = Shelf.class,
						parentColumns = ShelfTable.S_ID,
						childColumns = ContainsTable.S_ID,
						onDelete = ForeignKey.CASCADE),

				@ForeignKey(entity = Item.class,
						parentColumns = ItemTable.I_ID,
						childColumns = ItemTable.I_ID,
						onDelete = ForeignKey.CASCADE)
		}
)
public class Contains {

	private int sId;
	private int iId;

	private double number;

	public int getSId() {
		return sId;
	}
	public void setSId(int sId) {
		this.sId = sId;
	}
	public int getIId() {
		return iId;
	}
	public void setIId(int iId) {
		this.iId = iId;
	}
	public double getNumber() {
		return number;
	}
	public void setNumber(double number) {
		this.number = number;
	}

	@Ignore
	public Contains(int number) {
		this.number = number;
	}

	public Contains(int sId, int iId, double number) {
		this.sId = sId;
		this.iId = iId;
		this.number = number;
	}
}
