package com.example.journal.service

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.journal.R
import com.example.journal.model.Story
import kotlinx.android.synthetic.main.add_note.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import java.text.ParseException

class AddNoteActivity : AppCompatActivity() {

    lateinit var cancelButton: Button;
    lateinit var saveButton: Button;
    lateinit var id: String;


    override fun onCreate(savedInstanceState: Bundle?) {

        val str = "ADD NOTE ACTIVITY"
        Log.i(str, "Am intrat in add note")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note)

        supportActionBar?.hide()

        val window: Window = this@AddNoteActivity.window
        window.statusBarColor = ContextCompat.getColor(this@AddNoteActivity, R.color.black)


        idInputCreate.setText(Story.currentId.toString())

        idInputCreate.isEnabled = false


        saveButton = findViewById(R.id.saveButtonCreate);
        cancelButton = findViewById(R.id.cancelButtonCreate);

        saveButton.setOnClickListener() {
            addStory()
        }

        cancelButton.setOnClickListener() {
            goBack();
        }

    }

    private fun addStory() {
        if (checkInputs()) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val story = Story(
                titleInputCreate.text.toString(),
                textInputCreate.text.toString(),
                LocalDate.parse(dateInputCreate.text.toString(), formatter),
                emotionInputCreate.text.toString(),
                motivationalInputCreate.text.toString()
            )

            val bundle = Bundle()
            bundle.putParcelable("story", story);
            intent.putExtra("storyBundle", bundle)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            Toast.makeText(
                this,
                "All fields must be completed and the date must have the format yyyy-MM-dd!",
                Toast.LENGTH_LONG
            ).show();
        }
    }

    private fun checkInputs(): Boolean {
        if (titleInputCreate.text.isEmpty() or emotionInputCreate.text.isEmpty() or motivationalInputCreate.text.isEmpty() or dateInputCreate.text.isEmpty() or textInputCreate.text.isEmpty()) {
            return false
        }

        val dateChecker = dateInputCreate.text.toString();
        if (!isValidDate(dateChecker))
            return false

        return true
    }

//    fun isValidDate(stringToTest: String): Boolean {
//        Log.i(stringToTest, stringToTest)
//        val regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}".toRegex()
//
//        if(stringToTest.matches(regex))
//            return true
//
//        return false
//    }

    fun isValidDate(stringToTest: String): Boolean {

        try {
            val dateTime: LocalDate = LocalDate.parse(stringToTest)

        } catch (pe: DateTimeParseException) {

            return false

        }

        return true
    }

    private fun goBack() {
        intent = Intent()
        finish()
    }


}