package com.suda.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "phonelocation";
	public static final String DATABASE_TABLE = "phonelocation";
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE
			+ " (_id integer primary key autoincrement, "
			+ "phone_number text, phone_location text, last_update long);";

	public DBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
