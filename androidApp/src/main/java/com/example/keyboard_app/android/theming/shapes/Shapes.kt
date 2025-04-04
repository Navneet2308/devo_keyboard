package com.example.keyboard_app.android.theming.shapes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import com.example.keyboard_app.android.theming.LargeSpacing
import com.example.keyboard_app.android.theming.MediumSpacing
import com.example.keyboard_app.android.theming.SmallSpacing


val Shapes = Shapes(
    small = RoundedCornerShape(SmallSpacing),
    medium = RoundedCornerShape(MediumSpacing),
    large = RoundedCornerShape(LargeSpacing)
)

