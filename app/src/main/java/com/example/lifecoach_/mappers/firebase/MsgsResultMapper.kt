package com.example.lifecoach_.mappers.firebase

import android.util.Log
import com.example.lifecoach_.model.messages.MessageApp
import com.example.lifecoach_.model.messages.TextMessage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot

class MsgsResultMapper {
    fun resultToMessagesApp(query: QuerySnapshot, currentUid: String): MutableList<MessageApp> {
        val msgs = mutableListOf<MessageApp>()
        query.documents.forEach { doc ->
            val data = doc.data
            if (data != null) {
                Log.i("MSGS", "Intentando Mapear $data")
                msgs.add(
                    TextMessage(
                        data["from"] as String == currentUid,
                        (data["date"] as Timestamp).toDate(),
                        data["msg"] as String
                    )
                )
            }
        }
        return msgs
    }
}