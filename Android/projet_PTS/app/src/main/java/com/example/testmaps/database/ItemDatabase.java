package com.example.testmaps.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.testmaps.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDatabase extends SQLiteOpenHelper {

    // Define the database schema
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "item_db";
    private static final String TABLE_NAME = "item_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE_NAME = "image_name";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LNG = "lng";
    private static final String COLUMN_OBJECT_NAME= "object_name";

    // Constructor
    public ItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create the database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_IMAGE_NAME + " TEXT, "
                + COLUMN_OBJECT_NAME + " TEXT, "
                + COLUMN_LAT + " REAL, "
                + COLUMN_LNG + " REAL"
                + ")";
        db.execSQL(createTableQuery);
    }

    // Upgrade the database schema if needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert a new item into the database
    public long insertItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_NAME, item.image_name);
        values.put(COLUMN_LAT, item.lat);
        values.put(COLUMN_LNG, item.lng);
        values.put(COLUMN_OBJECT_NAME, item.object_name);
        long id = db.insert(TABLE_NAME, null, values);
       // db.close();
        return id;
    }

    // Get a list of all items in the database
    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        String selectAllQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                item.image_name = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_NAME));
                item.lat = cursor.getDouble(cursor.getColumnIndex(COLUMN_LAT));
                item.lng = cursor.getDouble(cursor.getColumnIndex(COLUMN_LNG));
                item.object_name = cursor.getString(cursor.getColumnIndex(COLUMN_OBJECT_NAME));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }
}
