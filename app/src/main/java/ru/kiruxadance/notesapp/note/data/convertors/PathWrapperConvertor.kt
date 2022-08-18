package ru.kiruxadance.notesapp.note.data.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.utils.PathWrapper

class PathWrapperConvertor {
    @TypeConverter
    fun fromGroupTaskMemberList(value: List<PathWrapper>): String {
        val gson = Gson()
        val type = object : TypeToken<List<PathWrapper>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGroupTaskMemberList(value: String): List<PathWrapper> {
        val gson = Gson()
        val type = object : TypeToken<List<PathWrapper>>() {}.type
        return gson.fromJson(value, type)
    }
}