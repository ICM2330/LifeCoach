package com.example.lifecoach_.repositories

import android.util.Log
import com.example.lifecoach_.mappers.firebase.MsgsResultMapper
import com.example.lifecoach_.model.messages.MessageApp
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date

class MsgRepository {
    private val db = Firebase.firestore
    private val msgRef = db.collection("msgs")

    private val msgsResultMapper = MsgsResultMapper()

    fun sendMessage(from: String, to: String, msg: String, callback: () -> Unit) {
        msgRef.add(hashMapOf(
            "from" to from,
            "to" to to,
            "msg" to msg,
            "date" to Date()
        )).addOnSuccessListener {
            callback()
        }
    }

    fun registerMessageListener(
        from: String,
        to: String,
        callback: (MutableList<MessageApp>) -> Unit
    ) {
        msgRef/*.where(
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
        ).orderBy("date", Query.Direction.ASCENDING)*/
            .addSnapshotListener { query, error ->
                // Mapear todos los mensajes a objetos MessageApp
                if (query != null) {
                    Log.i("MSGS", "Se recibieron cambios")
                    val msgs = msgsResultMapper.resultToMessagesApp(query, to)

                    // Retornar lista de mensajes
                    callback(msgs)
                } else {
                    Log.i("MSGS", "La consulta devolvió vacía")
                }

                if (error != null) {
                    Log.i("MSGS", "Ocurrió un error:")
                    error.printStackTrace()
                }
        }
    }
}