package ru.kiruxadance.notesapp.note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.kiruxadance.notesapp.note.data.convertors.NoteConvertor
import ru.kiruxadance.notesapp.note.data.convertors.PathWrapperConvertor
import ru.kiruxadance.notesapp.note.domain.model.Note

@Database(
    entities = [Note::class],
    version = 5
)
@TypeConverters(PathWrapperConvertor::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}