package com.example.journal.data

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journal.R
import com.example.journal.adapter.ItemAdapter
import com.example.journal.databinding.ActivityListNotesBinding
import com.example.journal.model.Story
import com.example.journal.service.AddNoteActivity
import kotlinx.android.synthetic.main.activity_list_notes.*
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class ListViewActivity : AppCompatActivity() {
    private val stories = mutableListOf<Story>()
    lateinit var addButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        val str = "LIST ACTIVITY"
        Log.i(str, "Am intrat in list")

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_notes)

        supportActionBar?.hide()

        val window: Window = this@ListViewActivity.window
        window.statusBarColor = ContextCompat.getColor(this@ListViewActivity, R.color.black)

        initStories()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ItemAdapter(this, stories)

        addButton = findViewById(R.id.addNoteButton)

        addButton.setOnClickListener{
            val intent = Intent(applicationContext, AddNoteActivity::class.java)
            startActivityForResult(intent, 3);
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val bundle = data.getBundleExtra("storyBundle")
                    val story = bundle?.getParcelable<Story>("story")
                    if (story != null) {
                        addStoryToList(story)
                    }
                }
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
                recyclerView.adapter?.notifyItemInserted(stories.size - 1)
            }
        } else if(requestCode == 5){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    val bundle = data.getBundleExtra("storyBundle")
                    val story = bundle?.getParcelable<Story>("story")
                    val id = data.getIntExtra("id", -1)
                    if(story != null && id != -1){
                        updateStory(story, id)
                    }
                }
            }
        }
    }

    private fun updateStory(story: Story, id: Number) {
        for(i in 0 until stories.size){
            if(stories[i].id == id){
                stories[i] = story
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
                recyclerView.adapter?.notifyItemChanged(i)
            }
        }
    }

    private fun addStoryToList(story: Story) {
        stories.add(story);
    }

    fun getId(): Number{
        return stories.size + 1;
    }


    private fun initStories() {

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val story1 = Story(
            "Lola",
            "aaaaa",
            LocalDate.parse("03-04-1999", formatter),
            "Happy",
            "Some review..."
        )
        val story2 = Story(
            "Lola2",
            "aaaaa",
            LocalDate.parse("04-04-1999", formatter),
            "Sad",
            "Some review..."
        )
        val story3 = Story(
            "Lola3",
            "aaaaa",
            LocalDate.parse("05-04-1999", formatter),
            "Angry",
            "Some review..."
        )
        val story4 = Story(
            "Lola4",
            "aaaaa",
            LocalDate.parse("06-04-1999", formatter),
            "Anxious",
            "Some review..."
        )
        val story5 = Story(
            "Lola5",
            "aaaaa",
            LocalDate.parse("07-04-1999", formatter),
            "Neutral",
            "Some review..."
        )
        val story6 = Story(
            "Lola6",
            "aaaaa",
            LocalDate.parse("08-04-1999", formatter),
            "Ambitious",
            "Some review..."
        )
        val story7 = Story(
            "Lola7",
            "aaaaa",
            LocalDate.parse("09-04-1999", formatter),
            "Nervous",
            "Some review..."
        )


        stories.add(story1)
        stories.add(story2)
        stories.add(story3)
        stories.add(story4)
        stories.add(story5)
        stories.add(story6)
        stories.add(story7)
    }

}