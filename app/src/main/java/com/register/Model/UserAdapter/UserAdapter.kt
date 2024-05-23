package com.register.Model.UserAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.register.Model.User
import com.register.R
import org.w3c.dom.Text

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.greetingcard, null)
        return UserViewHolder(itemView)
    }

    private val differCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.firstName == newItem.firstName
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = differ.currentList[position]

        holder.textView.text =user.firstName
        holder.textView1.text = user.greeting

    }
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.greeting)
        val textView1: TextView = itemView.findViewById(R.id.userName)
    }
}