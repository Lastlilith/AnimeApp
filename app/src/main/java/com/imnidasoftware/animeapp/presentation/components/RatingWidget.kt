package com.imnidasoftware.animeapp.presentation.components

import android.media.Rating
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imnidasoftware.animeapp.R
import com.imnidasoftware.animeapp.ui.theme.StarColor

@Composable
fun RatingWidget(
    modifier: Modifier,
    raiting: Double
) {

    val starPathString = stringResource(id = R.string.star_path)
    val starPath = remember {
        PathParser().parsePathString(pathData = starPathString).toPath()
    }
    val starOathBounds = remember {
        starPath.getBounds()
    }

    FilledStar(starPath = starPath, starPathBounds = starOathBounds)
}

@Composable
fun FilledStar(
    starPath: Path,
    starPathBounds: Rect,
    scaleFactor: Float = 1f
) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val canvasSize = size
        scale(scale = scaleFactor) {
            val pathWidth = starPathBounds.width
            val pathHeight = starPathBounds.height
            val left = (canvasSize.width / 2f) - (pathWidth / 1.7f)
            val top = (canvasSize.height / 2f) - (pathHeight / 1.7f)
            translate(
                left = left,
                top = top
            ) {
                drawPath(
                    path = starPath,
                    color = StarColor
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun FilledStarPreview() {
    RatingWidget(modifier = Modifier, raiting = 1.0)
}