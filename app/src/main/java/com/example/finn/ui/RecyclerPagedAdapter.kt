package com.example.finn.ui

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finn.R
import com.example.finn.models.Data
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Picasso

class RecyclerPagedAdapter : PagedListAdapter<Data, RecyclerPagedAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data=getItem(position)
        if (data!=null){
            holder.email.text=data.email
            holder.name.text= "${data.first_name} ${data.last_name}"
            Picasso.get()
                .load(data.avatar)
                .into(holder.image)
            holder.shuimmerLay.visibility= GONE
            holder.mainLay.visibility= VISIBLE
        }
        else{
            holder.shuimmerLay.visibility=VISIBLE
            holder.mainLay.visibility= GONE
            holder.shuimmerLay.startShimmerAnimation()
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val name : TextView = itemView.findViewById(R.id.name)
        val image: ImageView = itemView.findViewById(R.id.image)
        val email : TextView = itemView.findViewById(R.id.email)
        val shuimmerLay : ShimmerFrameLayout = itemView.findViewById(R.id.shuimmerLay)
        val mainLay : ConstraintLayout = itemView.findViewById(R.id.mainLay)

    }

    companion object {

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.id.equals(newItem.id)
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }
        }
    }
}