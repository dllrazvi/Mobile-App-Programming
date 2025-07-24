package com.example.journal.model

import android.os.Parcelable
import android.util.Log
import com.google.android.gms.vision.clearcut.LogUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Story(
    var title: String, var text: String, var date: String,
    var emotion: String, var motivationalMessage: String, var id: Long?): Parcelable{

//    companion object {
//        var currentId = 0
//    }
//
//    constructor(title: String, text: String,
//    date: org.threeten.bp.LocalDate, emotion: String,
//    motivationalMessage: String) : this(title, text, date, emotion, motivationalMessage, currentId++){
//            Log.i("Model Story Class: ", "CurrentId is $currentId")
//    }

}