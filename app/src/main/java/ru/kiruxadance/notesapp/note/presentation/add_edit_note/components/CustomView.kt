package ru.kiruxadance.notesapp.note.presentation.add_edit_note.components

import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kiruxadance.notesapp.note.presentation.add_edit_note.controllers.DrawController

@Composable
fun ControlsBar(
    drawController: DrawController,
    onDownloadClick: () -> Unit,
    onColorClick: () -> Unit,
    onBgColorClick: () -> Unit,
    onSizeClick: () -> Unit,
    undoVisibility: MutableState<Boolean>,
    redoVisibility: MutableState<Boolean>,
    colorValue: MutableState<Color>,
    bgColorValue: MutableState<Color>,
    sizeValue: MutableState<Int>
) {
    Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceAround) {
        MenuItems(
            Icons.Filled.Download,
            "download",
            if (undoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            if (undoVisibility.value) onDownloadClick()
        }
        MenuItems(
            Icons.Filled.Undo,
            "undo",
            if (undoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            //if (undoVisibility.value) drawController.unDo()
            drawController.unDo()
        }
        MenuItems(
            Icons.Filled.Redo,
            "redo",
            if (redoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            //if (redoVisibility.value) drawController.reDo()
            drawController.reDo()
        }
        MenuItems(
            Icons.Filled.Refresh,
            "reset",
            if (redoVisibility.value || undoVisibility.value) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
        ) {
            drawController.reset()
        }
        MenuItems(Icons.Filled.ColorLens, "background color", bgColorValue.value, bgColorValue.value == MaterialTheme.colors.background) {
            onBgColorClick()
        }
        MenuItems(Icons.Filled.ColorLens, "stroke color", colorValue.value) {
            onColorClick()
        }
        MenuItems(Icons.Filled.ColorLens, "stroke size", MaterialTheme.colors.primary) {
            onSizeClick()
        }
    }
}

@Composable
fun RowScope.MenuItems(
    icon: ImageVector,
    desc: String,
    colorTint: Color,
    border: Boolean = false,
    onClick: () -> Unit
) {
    val modifier = Modifier.size(24.dp)
    IconButton(
        onClick = onClick, modifier = Modifier.weight(1f, true)
    ) {
        Icon(imageVector = icon, contentDescription = "Save note")
    }
}