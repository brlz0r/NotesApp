package ru.kiruxadance.notesapp.note.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}