package io.github.kushbhati7.camcode.ui.activities.main.datamodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf

class tracer(
    id: Int,
    xPos: Int,
    yPos: Int,
    xRad: Int,
    yRad: Int,
    decodedText: String
) {
    val id = mutableIntStateOf(id)
    val xPos = mutableIntStateOf(xPos)
    val yPos = mutableIntStateOf(yPos)
    val xRad = mutableIntStateOf(xRad)
    val yRad = mutableIntStateOf(yRad)
    val decodedText = mutableStateListOf(decodedText)
}