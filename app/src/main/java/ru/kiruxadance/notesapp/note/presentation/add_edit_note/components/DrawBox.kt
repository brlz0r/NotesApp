package ru.kiruxadance.notesapp.note.presentation.add_edit_note.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.controllers.DrawController
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.utils.createPath

@Composable
fun DrawBox (
    drawController: DrawController,
    modifier: Modifier = Modifier.fillMaxSize(),
    backgroundColor: Color = MaterialTheme.colors.background,
){
    Canvas(modifier = modifier
        .background(Color.Transparent)
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    drawController.insertNewPath(offset)
                }
            ) { change, _ ->
                val newPoint = change.position
                drawController.updateLatestPath(newPoint)
            }
        }) {

        drawController.pathList.forEach { pw ->
            drawPath(
                createPath(pw.points),
                color = pw.strokeColor,
                alpha = pw.alpha,
                style = Stroke(
                    width = pw.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}