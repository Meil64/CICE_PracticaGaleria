package com.ccarrera.cameragallery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ccarrera.cameragallery.R

class PhotoAdapter(dataSet: List<String>, private val context: Context): RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    private lateinit var mListener : OnItemClickListener
    private var data = dataSet

    interface OnItemClickListener{
        fun onDeleteClick(position: Int)
    }

    fun deleteListItem(dataSet: List<String>, position: Int) {
        data = dataSet
        notifyItemRemoved(position)
    }

    fun addListItem(dataSet: List<String>, position: Int) {
        data = dataSet
        notifyItemInserted(position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    class ViewHolder(view: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(view){

        var photoImage: ImageView
        var deleteIcon: CardView

        init{
            photoImage = view.findViewById(R.id.item_imageView)
            deleteIcon = view.findViewById(R.id.item_deleteImageView)
            deleteIcon.setOnClickListener{
                listener.onDeleteClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        //Cargo la imagen
        Glide.with(context)
            .load(data[position])
            .into(holder.photoImage)
    }

    override fun getItemCount() = data.size
}