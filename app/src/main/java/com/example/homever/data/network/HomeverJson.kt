package com.example.homever.data.network

import com.example.homever.domain.model.MatchingPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

object HomeverJson {
    val gson: Gson = GsonBuilder()
        .registerTypeAdapterFactory(MatchingPreferencesAdapterFactory)
        .create()
}

/** Keep an explicit null upper budget bound without changing other profile fields. */
private object MatchingPreferencesAdapterFactory : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        if (type.rawType != MatchingPreferences::class.java) return null
        val delegate = gson.getDelegateAdapter(this, type)
        return object : TypeAdapter<T>() {
            override fun write(writer: JsonWriter, value: T) {
                if (value == null) {
                    writer.nullValue()
                    return
                }
                val previous = writer.serializeNulls
                writer.serializeNulls = true
                try {
                    delegate.write(writer, value)
                } finally {
                    writer.serializeNulls = previous
                }
            }

            override fun read(reader: JsonReader): T = delegate.read(reader)
        }
    }
}
