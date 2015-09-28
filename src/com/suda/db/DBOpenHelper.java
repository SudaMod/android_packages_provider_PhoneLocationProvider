/*
 * Copyright (C) 2015 The SudaMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.suda.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "phonelocation";
    public static final String DATABASE_TABLE = "phonelocation";
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_CREATE = "create table " + DATABASE_TABLE
            + " (_id integer primary key autoincrement, "
            + "phone_number text, phone_location text, last_update long);";

    public static final String UPDATE_TO_2 = "alter table " + DATABASE_TABLE
            + " add mark_type int;";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
	db.execSQL(UPDATE_TO_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2) {
            db.execSQL(UPDATE_TO_2);
        }
    }

}
