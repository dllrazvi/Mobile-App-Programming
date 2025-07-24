package com.example.journal.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journal.MainActivity
import com.example.journal.R
import com.example.journal.adapter.ItemAdapter
import com.example.journal.database.DatabaseRepository
import com.example.journal.database.Utils
import com.example.journal.databinding.ActivityListNotesBinding
import com.example.journal.model.Story
import com.example.journal.service.AddNoteActivity
import kotlinx.android.synthetic.main.activity_list_notes.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class   ListViewActivity : AppCompatActivity() {
    private lateinit var readableDatabase: SQLiteDatabase
    private lateinit var writableDatabase: SQLiteDatabase

    private lateinit var storyRepository: DatabaseRepository
    lateinit var addButton: ImageView
    private lateinit var dbHelper: Utils


    var storiesToObserve = MutableLiveData<MutableList<Story>>()
    private lateinit var stories: MutableList<Story>

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {

        val str = "LIST ACTIVITY"
        Log.i(str, "Am intrat in list")

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_notes)

        supportActionBar?.hide()

        val window: Window = this@ListViewActivity.window
        window.statusBarColor = ContextCompat.getColor(this@ListViewActivity, R.color.black)



        dbHelper = Utils(this)
        readableDatabase = dbHelper.readableDatabase;
        writableDatabase = dbHelper.writableDatabase;

        storyRepository = DatabaseRepository.getInstance(readableDatabase, writableDatabase)!!;


        recyclerView.layoutManager = LinearLayoutManager(context)
        val model = ViewModelProviders.of(this as FragmentActivity).get(MainActivity::class.java)

        recyclerView.adapter = ItemAdapter(this)


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
                        storyRepository.add(story)
                    }
                }
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
                recyclerView.adapter?.notifyItemInserted(storyRepository.getStories().size - 1)
            }
        } else if(requestCode == 5){
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    val bundle = data.getBundleExtra("storyBundle")
                    val story = bundle?.getParcelable<Story>("story")
                    val id = data.getIntExtra("id", -1)
                    if(story != null && id != -1){
                        storyRepository.update(story)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }


}