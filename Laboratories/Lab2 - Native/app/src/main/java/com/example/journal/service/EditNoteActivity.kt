package com.example.journal.service

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.journal.R
import com.example.journal.model.Story
import kotlinx.android.synthetic.main.add_note.*
import kotlinx.android.synthetic.main.edit_note.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException

class EditNoteActivity : AppCompatActivity() {

    lateinit var id: Number;
    private lateinit var initialStory: Story;

    private lateinit var cancelButton: Button;
    private lateinit var saveButton: Button;

    override fun onCreate(savedInstanceState: Bundle?) {

        val str = "EDIT NOTE ACTIVITY"
        Log.i(str, "Am intrat in edit note")

        super.onCreate(savedInstanceState)

        setContentView(R.layout.edit_note)

        supportActionBar?.hide()


        val window: Window = this@EditNoteActivity.window
        window.statusBarColor = ContextCompat.getColor(this@EditNoteActivity, R.color.black)


        val bundle = intent.getBundleExtra("storyBundle")
        if(bundle != null) {
            val story = bundle.getParcelable<Story>("story")
            if (story != null) {
                initialStory = story
                id = story.id
            }
        }

        initializeInputs()

        saveButton = saveButtonUpdate
        cancelButton = cancelButtonUpdate

        saveButton.setOnClickListener(){
            editStory()
        }

        cancelButton.setOnClickListener(){
            goBack()
        }

    }

    private fun editStory() {
        if(checkInputs()){

            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            initialStory.title = titleInputUpdate.text.toString();
            initialStory.date = LocalDate.parse(dateInputUpdate.text.toString(), formatter)
            initialStory.emotion = emotionInputUpdate.text.toString()
            initialStory.motivationalMessage = motivationalInputUpdate.text.toString()
            initialStory.text = textInputUpdate.text.toString()

            val bundle = Bundle()
            bundle.putParcelable("story", initialStory)
            intent.putExtra("storyBundle", bundle)
            intent.putExtra("id", id)
            setResult(RESULT_OK, intent)
            finish()
        } else
        {
            Toast.makeText(this, "All fields must be completed and the date must have the format yyyy-MM-dd!", Toast.LENGTH_LONG).show();
        }
    }

    private fun goBack(){
        intent = Intent()
        finish()
    }

    private fun checkInputs(): Boolean {
        if(titleInputUpdate.text.isEmpty() or emotionInputUpdate.text.isEmpty() or motivationalInputUpdate.text.isEmpty() or dateInputUpdate.text.isEmpty() or textInputUpdate.text.isEmpty()){
            return false
        }

        val dateChecker = dateInputUpdate.text.toString();
        if(!isValidDate(dateChecker))
            return false

        return true
    }

    fun isValidDate(stringToTest: String): Boolean {

        try {
            LocalDate.parse(stringToTest)

        } catch (pe: DateTimeParseException) {

            return false

        }

        return true
    }

    private fun initializeInputs(){

        idInputUpdate.setText(id.toString());
        idInputUpdate.isEnabled = false
        titleInputUpdate.setText(initialStory.title)
        dateInputUpdate.setText(initialStory.date.toString())
        emotionInputUpdate.setText(initialStory.emotion)
        motivationalInputUpdate.setText(initialStory.motivationalMessage)
        textInputUpdate.setText(initialStory.text)

    }



}