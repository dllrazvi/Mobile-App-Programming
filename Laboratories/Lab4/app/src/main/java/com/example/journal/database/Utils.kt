package com.example.journal.database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class Utils(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Journal.db"
        const val TABLE_NAME = "Story"
        const val COL_ID = "id"
        const val COL_TITLE = "title"
        const val COL_DATE = "date"
        const val COL_EMOTION = "emotion"
        const val COL_MOTIVATIONAL = "motivationalMessage"
        const val COL_TEXT = "text"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    COL_TITLE + " TEXT, " +
                    COL_DATE + " TEXT, " +
                    COL_EMOTION + " TEXT, " +
                    COL_MOTIVATIONAL + " TEXT, " +
                    COL_TEXT + " TEXT)"

        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}