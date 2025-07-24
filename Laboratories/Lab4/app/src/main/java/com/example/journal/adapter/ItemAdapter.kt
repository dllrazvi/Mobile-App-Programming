package com.example.journal.adapter

import android.app.Dialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.whenResumed
import androidx.recyclerview.widget.RecyclerView
import com.example.journal.MainActivity
import com.example.journal.R
import com.example.journal.data.ListViewActivity
import com.example.journal.database.DatabaseRepository
import com.example.journal.database.Utils
import com.example.journal.model.Story
import com.example.journal.service.EditNoteActivity
import kotlinx.android.synthetic.main.list_item.view.*


class ItemAdapter(private val context: ListViewActivity, private val stories: MutableList<Story>): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    var dbHelper = Utils(this.context)
    var storyRepository = DatabaseRepository.getInstance(dbHelper.readableDatabase, dbHelper.writableDatabase)!!

//    init {
//        liveDataToObserve.observe(lifecycleOwner) {
//            notifyDataSetChanged()
//        }
//    }

    inner class ItemViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val item = stories[position]
        holder.itemView.apply {
            titleID.text = item.title;
            dateID.text = item.date.toString();
            emotionID.text = item.emotion;
        }


        holder.itemView.deleteButton.setOnClickListener{

            val dialog = Dialog(context)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.delete_popup)

            val titleLabel = dialog.findViewById(R.id.titleLabel) as TextView

            var storyTitle = stories[position].title
            storyTitle += " ?"

            titleLabel.text = storyTitle


            val yesView = dialog.findViewById(R.id.yesButton) as View

            val noView = dialog.findViewById(R.id.noButton) as View

            yesView.setOnClickListener {
                storyRepository.delete(item.id!!)
//                stories.removeAt(position)
                notifyDataSetChanged()
                dialog.dismiss()
            }

            noView.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        holder.itemView.editButton.setOnClickListener(){
            val bundle = Bundle();
            val intent = Intent(context, EditNoteActivity::class.java)

            bundle.putParcelable("story", stories[position]);
            intent.putExtra("storyBundle", bundle);

            context.startActivityForResult(intent, 5)
        }

    }

    override fun getItemCount(): Int {
        return stories.size
    }


}