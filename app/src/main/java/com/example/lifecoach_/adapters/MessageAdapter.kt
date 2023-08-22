package com.example.lifecoach_.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.lifecoach_.R
import com.example.lifecoach_.model.messages.MediaMessage
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.model.messages.TextMessage

class MessageAdapter(context: Context, messages: List<MessageApp>)
    : ArrayAdapter<MessageApp>(context, 0, messages) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        val item = getItem(position)

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.message_layout, parent, false)
        }

        val msg : TextView
        val img : ImageView
        val frameForwarded = itemView!!.findViewById<FrameLayout>(R.id.frameForwarded)
        val frameReceived = itemView.findViewById<FrameLayout>(R.id.frameReceived)


        if(item!!.forwarded){
            msg = itemView.findViewById(R.id.messageForwarded)
            img = itemView.findViewById(R.id.messageImgForwarded)

            val paramsframe = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            paramsframe.weight = 3.0f
            frameForwarded.layoutParams = paramsframe
            frameReceived.removeAllViews()
        }
        else {
            img = itemView.findViewById(R.id.messageImgReceived)
            msg = itemView.findViewById(R.id.messageReceived)
            val paramsframe = LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            paramsframe.weight = 3.0f
            frameReceived.layoutParams = paramsframe
            frameForwarded.removeAllViews()
        }

        when(item){
            is TextMessage-> {
                msg.text = item.text
            }
            is MediaMessage->{
                val message = item
                /*val imageStream = context.contentResolver.openInputStream(message.fileAttached!!)
                val bitmap = BitmapFactory.decodeStream(imageStream)
                img.setImageBitmap(bitmap)*/
                img.setImageResource(R.drawable.logolong)
                msg.text = ""
            }
        }
        return itemView
    }
}