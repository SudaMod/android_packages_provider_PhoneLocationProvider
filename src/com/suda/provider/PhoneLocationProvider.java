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

package com.suda.provider;

import com.suda.db.DBOpenHelper;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class PhoneLocationProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.suda.provider.PhoneLocation";
    static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME
            + "/phonelocation");

    static final String _ID = "_id";
    static final String PHONENUMBER = "phone_number";
    static final String PHONELOCATION = "phone_location";
    static final String LAST_UPDATE = "last_update";

    static final int PHONELOCATIONS = 1;
    static final int PHONELOCATION_ID = 2;

    private static final UriMatcher uriMatcher;

    SQLiteDatabase phonelocationDB;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "phonelocation", PHONELOCATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "phonelocation/#", PHONELOCATION_ID);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DBOpenHelper dbHelper = new DBOpenHelper(context);
        phonelocationDB = dbHelper.getWritableDatabase();
        return (phonelocationDB == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        sqlBuilder.setTables(DBOpenHelper.DATABASE_TABLE);

        if (uriMatcher.match(uri) == PHONELOCATION_ID)
            sqlBuilder.appendWhere(_ID + " = " + uri.getPathSegments().get(1));

        if (sortOrder == null || sortOrder == "")
            sortOrder = PHONENUMBER;

        Cursor c = sqlBuilder.query(phonelocationDB, projection, selection,
                selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
        case PHONELOCATIONS:
            return "all_phonelocation ";

        case PHONELOCATION_ID:
            return "one_phonelocation ";

        default:
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = phonelocationDB.insert(DBOpenHelper.DATABASE_TABLE, "",
                values);


        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
        case PHONELOCATIONS:
            count = phonelocationDB.update(DBOpenHelper.DATABASE_TABLE, values,
                    selection, selectionArgs);
            break;
        case PHONELOCATION_ID:
            count = phonelocationDB.update(
                    DBOpenHelper.DATABASE_TABLE,
                    values,
                    _ID
                            + " = "
                            + uri.getPathSegments().get(1)
                            + (!TextUtils.isEmpty(selection) ? " AND ("
                                    + selection + ')' : ""), selectionArgs);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
