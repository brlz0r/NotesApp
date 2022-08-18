package ru.kiruxadance.notesapp.note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.kiruxadance.notesapp.note.data.convertors.PathWrapperConvertor
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.utils.PathWrapper

@Entity

data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    @TypeConverters(PathWrapperConvertor::class) val pathWrappers: List<PathWrapper>,
    @PrimaryKey val id: Int? = null
)
class InvalidNoteException(message: String): Exception(message)