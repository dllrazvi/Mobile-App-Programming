package com.example.journal.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.journal.model.Story
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException


class DatabaseRepository private constructor(
    private var readableDatabase: SQLiteDatabase,
    private var writableDatabase: SQLiteDatabase
){
    private var storyList: MutableList<Story> = ArrayList();

    fun getStories(): MutableList<Story> {
        val data: MutableList<Story> = ArrayList()
        val cursor = readableDatabase.query(Utils.TABLE_NAME, null, null, null, null, null, null);

        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
            val date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("date")))
            val emotion = cursor.getString(cursor.getColumnIndexOrThrow("emotion"))
            val motivationalMessage = cursor.getString(cursor.getColumnIndexOrThrow("motivationalMessage"))
            val text = cursor.getString(cursor.getColumnIndexOrThrow("text"))

            val story = Story(title, text, date, emotion, motivationalMessage, id)

            data.add(story)
        }

        cursor.close()

        storyList = data

        return storyList.toMutableList()
    }

    fun add(story: Story): Boolean {
        val values = ContentValues()
        values.put(Utils.COL_TITLE, story.title)
        values.put(Utils.COL_DATE, story.date.toString())
        values.put(Utils.COL_EMOTION, story.emotion)
        values.put(Utils.COL_MOTIVATIONAL, story.motivationalMessage)
        values.put(Utils.COL_TEXT, story.text)

        val result = writableDatabase.insert(Utils.TABLE_NAME,null, values)

        if(result == (-1).toLong()){
            val str = "DATABASE -> ADD"
            Log.i(str, "An Error appeared when inserting the element to the database!")
            return false
        }

        return true
    }

    fun delete(id: Int): Boolean {
        val selection: String = Utils.COL_ID + " = ?"
        val selectionArgs = arrayOf(id.toString())

        val result = writableDatabase.delete(Utils.TABLE_NAME, selection, selectionArgs)

        if(result == -1){
            val str = "DATABASE -> Delete"
            Log.i(str, "An Error appeared when deleting the element from the database!")
            return false
        }

        return true
    }

    fun update(story: Story): Story? {
        val values = ContentValues()
        values.put(Utils.COL_TITLE, story.title)
        values.put(Utils.COL_DATE, story.date.toString())
        values.put(Utils.COL_EMOTION, story.emotion)
        values.put(Utils.COL_MOTIVATIONAL, story.motivationalMessage)
        values.put(Utils.COL_TEXT, story.text)

        val selection: String = Utils.COL_ID + " = ?"
        val selectionArgs = arrayOf(story.id.toString())

        val result = writableDatabase.update(Utils.TABLE_NAME, values, selection, selectionArgs)

        if(result == -1){
            val str = "DATABASE -> Update"
            Log.i(str, "An Error appeared when updating the element from the database!")
            return null
        }

        return story
    }

    fun getById(id: Int): Story? {
        for(i in storyList.indices){
            if(id == storyList[i].id){
                return storyList[i]
            }
        }
        return null
    }

    companion object{
        private var instance: DatabaseRepository? = null
        fun getInstance(readableDatabase: SQLiteDatabase, writableDatabase: SQLiteDatabase): DatabaseRepository? {
            if(instance == null) instance = DatabaseRepository(readableDatabase, writableDatabase)
            return instance
        }
    }

}