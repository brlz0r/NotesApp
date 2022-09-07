package ru.kiruxadance.notesapp.note.domain.use_case.note

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import ru.kiruxadance.notesapp.common.Resource
import ru.kiruxadance.notesapp.note.data.repository.FakeNoteRepository
import ru.kiruxadance.notesapp.note.domain.model.Note
import ru.kiruxadance.notesapp.note.domain.util.NoteOrder
import ru.kiruxadance.notesapp.note.domain.util.OrderType
import ru.kiruxadance.notesapp.note.presentation.notes.NotesListState
import ru.kiruxadance.notesapp.note.presentation.notes.NotesViewModel

class GetNotesTest {
    private lateinit var getNotes: GetNotes
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setUp() {
        fakeNoteRepository = FakeNoteRepository()
        getNotes = GetNotes(fakeNoteRepository)

        val notesToInsert = mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            notesToInsert.add(
                Note(
                    id = index.toString(),
                    title = c.toString(),
                    content = c.toString(),
                    timestamp = index.toLong(),
                    pathWrappers = emptyList()
                )
            )
        }
        notesToInsert.shuffle()
        runBlocking {
            notesToInsert.forEach { fakeNoteRepository.insertNote(it) }
        }
    }

    @Test
    fun `Order notes by title ascending, correct order`() = runBlocking {
        var notes = emptyList<Note>()
        getNotes(NoteOrder.Title(OrderType.Ascending)).collectLatest {
            when(it) {
                is Resource.Success -> {
                    notes = it.data ?: emptyList()
                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }
        }

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isLessThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by title descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Title(OrderType.Descending)).first().data ?: emptyList()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].title).isGreaterThan(notes[i+1].title)
        }
    }

    @Test
    fun `Order notes by date ascending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Ascending)).first().data ?: emptyList()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isLessThan(notes[i+1].timestamp)
        }
    }

    @Test
    fun `Order notes by date descending, correct order`() = runBlocking {
        val notes = getNotes(NoteOrder.Date(OrderType.Descending)).first().data ?: emptyList()

        for(i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp).isGreaterThan(notes[i+1].timestamp)
        }
    }
}