package com.example.journal.database

import android.provider.BaseColumns

class TableEntry
private constructor(){
    object Entry : BaseColumns {
        const val TABLE_NAME = "Story"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DATE = "date"
        const val COLUMN_NAME_EMOTION = "emotion"
        const val COLUMN_NAME_MOTIVATIONAL_MESSAGE = "motivationalMessage"
        const val COLUMN_NAME_TEXT = "text"
    }
}
