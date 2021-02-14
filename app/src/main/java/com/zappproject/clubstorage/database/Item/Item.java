package com.zappproject.clubstorage.database.Item;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = ItemTable.TABLE_NAME)
public class Item {

	@PrimaryKey(autoGenerate = true)
	private int iId;

	private String title;

	private double price;

	private String note;

	private int unit;

	@Ignore
	public static final String[] UNITS = {"Stk.", "Liter", "kg"};
	@Ignore
	public static final int PEACES = 0;
	@Ignore
	public static final int LITER = 1;
	@Ignore
	public static final int KG = 2;


	@Ignore
	public Item(int id, String title, double price, String note, int unit) {
		this(title, price, note, unit);
		this.iId = id;
	}

	public Item(String title, double price, String note, int unit) {
		this.title = title;
		this.price = price;
		this.note = note;
		this.unit = unit;
	}

	public int getIId() {
		return iId;
	}

	public void setIId(int iId) {
		this.iId = iId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public double getPrice() {
		return price;
	}

//	public void setPrice(double price) {
//		this.price = price;
//	}

	public String getNote() {
		return note;
	}

//	public void setNote(String note) {
//		this.note = note;
//	}

	public int getUnit() {
		return this.unit;
	}

//	public void setUnit(int unit) {
//		this.unit = unit;
//	}
}
