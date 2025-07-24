package com.example.journal.data

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.journal.R
import com.example.journal.adapter.ItemAdapter
import com.example.journal.client.StoryAPI
import com.example.journal.database.DatabaseRepository
import com.example.journal.database.Utils
import com.example.journal.model.Story
import com.example.journal.service.AddNoteActivity
import kotlinx.android.synthetic.main.activity_list_notes.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListViewActivity : AppCompatActivity() {

    private lateinit var readableDatabase: SQLiteDatabase
    private lateinit var writableDatabase: SQLiteDatabase
    private lateinit var storyRepository: DatabaseRepository;

    lateinit var addButton: ImageView
    private lateinit var dbHelper: Utils;

    private var context = this;

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

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
        storyRepository = DatabaseRepository.getInstance(readableDatabase, writableDatabase)!!

        lifecycleScope.launch {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = ItemAdapter(context)
        }

        if (!isNetworkConnected()) {
            showDialog()
        }

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                lifecycleScope.launch {
                    StoryAPI.retrofitService.retrieveAllStories()
                        .enqueue(object : Callback<List<Story>?> {
                            override fun onResponse(
                                call: Call<List<Story>?>,
                                response: Response<List<Story>?>
                            ) {

                                val storiesServer = response.body()!!
                                Log.d("Stories server", storiesServer.toString())
                                val storiesDatabase = storyRepository.getStories();

                                for (s1: Story in storiesDatabase) {
                                    var exists = false
                                    for (s2: Story in storiesServer) {
                                        if (s1.id!!.equals(s2.id)) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        StoryAPI.retrofitService.createStory(s1)
                                            .enqueue(object : Callback<Story?> {
                                                override fun onResponse(
                                                    call: Call<Story?>,
                                                    response: Response<Story?>
                                                ) {
                                                    Log.d(
                                                        "Added item from local db",
                                                        "Success: " + s1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Story?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to add item from local db!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Added item from local db",
                                                        "Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }

                                }


                                for (s2: Story in storiesServer) {
                                    var exists = false
                                    for (s1: Story in storiesDatabase) {
                                        if (s1.id!!.equals(s2.id)) {
                                            exists = true
                                        }
                                    }

                                    if (!exists) {
                                        StoryAPI.retrofitService.deleteStory(s2.id!!)
                                            .enqueue(object : Callback<Story?> {
                                                override fun onResponse(
                                                    call: Call<Story?>,
                                                    response: Response<Story?>
                                                ) {

//                                                stories.remove(s2)
                                                    Log.d(
                                                        "Deleted item from server",
                                                        "Success!"
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Story?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to remove item from server!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Deleted item from server",
                                                        "Failed! " + t.message
                                                    )
                                                }
                                            })
                                    }
                                }


                                for (s1: Story in storiesDatabase) {
                                    var different = false
                                    for (s2: Story in storiesServer) {
                                        if (s1.id!!.equals(s2.id) && (!s1.title.equals(s2.title) || !s1.text.equals(
                                                s2.text
                                            ) || !s1.motivationalMessage.equals(s2.motivationalMessage) || !s1.date.equals(
                                                s2.date
                                            ) || !s1.emotion.equals(s2.emotion))
                                        ) {
                                            different = true
                                            Log.d(
                                                "Updated item from the server",
                                                "Success: " + s2
                                            )
                                        }
                                    }

                                    if (different) {
                                        StoryAPI.retrofitService.updateStory(s1.id!!, s1)
                                            .enqueue(object : Callback<Story?> {
                                                override fun onResponse(
                                                    call: Call<Story?>,
                                                    response: Response<Story?>
                                                ) {
                                                    Log.d(
                                                        "Updated item from the server",
                                                        "Success: " + s1
                                                    )
                                                }

                                                override fun onFailure(
                                                    call: Call<Story?>,
                                                    t: Throwable
                                                ) {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to update item from server!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d(
                                                        "Updated item from the server",
                                                        "Failed: " + t.message
                                                    )
                                                }
                                            })
                                    }

                                }
                            }

                            override fun onFailure(call: Call<List<Story>?>, t: Throwable) {
                                lifecycleScope.launch {
                                    Toast.makeText(
                                        context,
                                        "Failed to check differences between local db and server!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d(
                                        "Check for differences between local db and server",
                                        "Failed: " + t.message
                                    )
                                }
                            }
                        })
                }
            }
        }
        )

        addButton = findViewById(R.id.addNoteButton)

        addButton.setOnClickListener {
            val intent = Intent(applicationContext, AddNoteActivity::class.java)
            startActivityForResult(intent, 3);
        }


    }

    private fun showDialog() {
        AlertDialog.Builder(this).setTitle("No Internet Connection")
            .setMessage("Fallback on local DB")
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val bundle = data.getBundleExtra("storyBundle")
                    val story = bundle?.getParcelable<Story>("story")
                    if (story != null) {
                        addStory(story)
                    }
                }
                Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val bundle = data.getBundleExtra("storyBundle")
                    val story = bundle?.getParcelable<Story>("story")
                    val id = data.getLongExtra("id", -1)
                    if (story != null && id != (-1).toLong()) {
                        updateStory(story, id)
                        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun deleteStory(id: Long) {
        if (isNetworkConnected()) {
            lifecycleScope.launch {
                StoryAPI.retrofitService.deleteStory(id).enqueue(object : Callback<Story?> {
                    override fun onResponse(call: Call<Story?>, response: Response<Story?>) {
                        storyRepository.delete(id)
                        recyclerView.adapter?.notifyDataSetChanged()
                        Log.d("Delete story action - server", "Success: " + response.body())
                    }

                    override fun onFailure(call: Call<Story?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to delete story" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Delete story action - server", "Failed: " + t.message)
                    }
                })
            }

        } else {
            showDialog()
            storyRepository.delete(id)
            Log.d("Delete story action - local database", "Success!")
        }
        recyclerView.adapter?.notifyDataSetChanged()

    }

    fun updateStory(story: Story, id: Long) {
        if (isNetworkConnected()) {
            lifecycleScope.launch {
                StoryAPI.retrofitService.updateStory(id, story).enqueue(object : Callback<Story?> {
                    override fun onResponse(call: Call<Story?>, response: Response<Story?>) {
                        storyRepository.update(story, id)
                        recyclerView.adapter?.notifyDataSetChanged()
                        Log.d("Update story action - server", "Success: " + response.body())
                    }

                    override fun onFailure(call: Call<Story?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to update story!" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Update story action - server", "Failed: " + t.message)
                    }
                })
            }
        } else {
            showDialog()
            storyRepository.update(story, id)
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("Update story action - local database", "Success!")
        }
    }

    fun addStory(story: Story) {

        var storyId = storyRepository.add(story)

        story.id = storyId

        if (isNetworkConnected()) {
            lifecycleScope.launch {
                StoryAPI.retrofitService.createStory(story).enqueue(object : Callback<Story?> {
                    override fun onResponse(call: Call<Story?>, response: Response<Story?>) {
                        recyclerView.adapter?.notifyDataSetChanged()
                        Log.d("Add story action - server", "Success: " + response.body().toString())
                    }

                    override fun onFailure(call: Call<Story?>, t: Throwable) {
                        Toast.makeText(
                            context,
                            "Failed to add story!" + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("Add story action - server", "Failed: " + t.message)
                    }
                })
            }
        } else {
            showDialog()
            recyclerView.adapter?.notifyDataSetChanged()
            Log.d("Add story action - local database", "Success!")
        }
    }
}