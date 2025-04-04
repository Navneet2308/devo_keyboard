package com.example.keyboard_app.android.theming.shapes
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class DropShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height
        val controlPointOffsetX = width * 1.5f  // More exaggerated width
        val controlPointOffsetY = height * 0.8f // Pull the curves down more

        val path = Path().apply {
            moveTo(width / 2f, 0f) // Top center

            // Right side of drop
            cubicTo(
                width / 2f + controlPointOffsetX / 2f, 0f,
                width, controlPointOffsetY,
                width / 2f, height
            )

            // Left side of drop
            cubicTo(
                0f, controlPointOffsetY,
                width / 2f - controlPointOffsetX / 2f, 0f,
                width / 2f, 0f
            )

            close()
        }

        return Outline.Generic(path)
    }
}
