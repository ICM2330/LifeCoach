package com.example.lifecoach_.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.lifecoach_.R
import com.example.lifecoach_.databinding.FriendChatLayoutBinding
import com.example.lifecoach_.model.Friend

class FriendChatAdapter(context: Context, friends: MutableList<Friend>) :
    ArrayAdapter<Friend>(context, 0, friends) {

    private lateinit var binding : FriendChatLayoutBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val item = getItem(position)

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.friend_chat_layout, parent, false)
        }

        val friendPic = itemView!!.findViewById<ImageView>(R.id.fcFriendPic)
        val friendName = itemView.findViewById<TextView>(R.id.fcFriendName)
        val lastMessage = itemView.findViewById<TextView>(R.id.fcLastMessage)

        //friendPicLoad
        friendName.text = item!!.user.username
        if(item.chat.isEmpty())
            lastMessage.text = "Toca aqui para chatear"
        else lastMessage.text = item.chat.last().toString()
        return itemView
    }

}
