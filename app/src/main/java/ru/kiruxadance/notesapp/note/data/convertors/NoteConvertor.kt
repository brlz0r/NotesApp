package ru.kiruxadance.notesapp.note.data.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.kiruxadance.notesapp.note.domain.model.Note

class NoteConvertor {
    @TypeConverter
    public fun fromJson(value: String): Note {
        return Gson().fromJson(value, Note::class.java)
    }

    @TypeConverter
    public fun toJson(note: Note): String {
        return Gson().toJson(note)
    }
}