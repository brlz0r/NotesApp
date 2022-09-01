package ru.kiruxadance.notesapp.note.domain.model

data class Token(
    val accessToken: String,
    val refreshToken: String
)
