package com.example.lifecoach_.repositories

import com.example.lifecoach_.model.messages.MessageApp
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MsgRepository {
    private val db = Firebase.firestore
    private val msgRef = db.collection("msgs")

    fun sendMessage(from: String, to: String, msg: String, callback: () -> Unit) {
        msgRef.add(hashMapOf(
            "from" to from,
            "to" to to,
            "msg" to msg
        )).addOnSuccessListener {
            callback()
        }
    }

    fun registerMessageListener(
        from: String,
        to: String,
        callback: (MutableList<MessageApp>) -> Unit
    ) {
        msgRef.where(
            Filter.or(
                Filter.and(
                    Filter.equalTo("from", from),
                    Filter.equalTo("to", to)
                ),
                Filter.and(
                    Filter.equalTo("from", to),
                    Filter.equalTo("to", from)
                )
            )
        ).addSnapshotListener { query, error ->
            // TODO: Mapear todos los mensajes a objetos MessageApp

            // TODO: Retornar lista de mensajes
        }
    }
}