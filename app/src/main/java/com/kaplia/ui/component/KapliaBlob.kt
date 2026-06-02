package com.kaplia.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.lerp
import com.kaplia.ui.theme.DangerRed
import com.kaplia.ui.theme.KapliaBlue
import com.kaplia.ui.theme.KapliaGlowCyan
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val DeadGray = Color(0xFF3A3A4A)

@Suppress("LongMethod")
@Composable
fun KapliaBlob(
    modifier: Modifier = Modifier,
    primaryColor: Color = KapliaBlue,
    glowColor: Color = KapliaGlowCyan,
    healthFraction: Float = 1f,
) {
    val health = healthFraction.coerceIn(0f, 1f)

    val effectivePrimary = when {
        health <= 0f -> DeadGray
        health < 0.3f -> lerp(DangerRed, primaryColor, health / 0.3f)
        else -> primaryColor
    }
    val effectiveGlow = when {
        health <= 0f -> DeadGray
        health < 0.3f -> lerp(DangerRed, glowColor, health / 0.3f)
        else -> glowColor
    }
    val glowMult = if (health <= 0f) 0f else health.coerceAtLeast(0.25f)

    val transition = rememberInfiniteTransition(label = "kapliaBlob")

    val wobble1 by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3_400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "wobble1",
    )

    val wobble2 by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 5_100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "wobble2",
    )

    val pulse by transition.animateFloat(
        initialValue = 0.93f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2_200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val baseRadius = minOf(size.width, size.height) * 0.36f * pulse

        // Outer glow rings — layered for soft halo effect
        repeat(7) { i ->
            drawCircle(
                color = effectiveGlow.copy(alpha = 0.022f * (7 - i) * glowMult),
                radius = baseRadius * (1.08f + i * 0.14f),
                center = Offset(cx, cy),
            )
        }

        // Blob body with radial gradient
        drawPath(
            path = buildBlobPath(cx, cy, baseRadius, wobble1, wobble2),
            brush = Brush.radialGradient(
                colors = listOf(
                    effectiveGlow.copy(alpha = 0.9f),
                    effectivePrimary,
                    effectivePrimary.copy(alpha = 0.80f),
                ),
                center = Offset(cx - baseRadius * 0.15f, cy - baseRadius * 0.20f),
                radius = baseRadius * 1.35f,
            ),
        )

        if (health > 0f) {
            // Primary specular highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.20f * glowMult),
                radius = baseRadius * 0.28f,
                center = Offset(cx - baseRadius * 0.28f, cy - baseRadius * 0.32f),
            )
            // Secondary micro-highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.08f * glowMult),
                radius = baseRadius * 0.11f,
                center = Offset(cx + baseRadius * 0.18f, cy - baseRadius * 0.44f),
            )
        }
    }
}

private fun buildBlobPath(
    cx: Float,
    cy: Float,
    r: Float,
    w1: Float,
    w2: Float,
): Path {
    val numPts = 10
    val wobble = r * 0.22f

    fun pointAt(i: Int): Offset {
        val angle = (2.0 * PI * i / numPts).toFloat()
        val radius = r +
            wobble * sin(angle * 2f + w1) +
            wobble * 0.6f * cos(angle * 3f + w2) +
            wobble * 0.3f * sin(angle + w1 * 0.5f + w2 * 0.3f)
        return Offset(cx + radius * cos(angle), cy + radius * sin(angle))
    }

    fun mid(
        a: Offset,
        b: Offset,
    ) = Offset((a.x + b.x) / 2f, (a.y + b.y) / 2f)

    val path = Path()
    val startMid = mid(pointAt(numPts - 1), pointAt(0))
    path.moveTo(startMid.x, startMid.y)

    for (i in 0 until numPts) {
        val ctrl = pointAt(i)
        val endMid = mid(pointAt(i), pointAt((i + 1) % numPts))
        path.quadraticBezierTo(ctrl.x, ctrl.y, endMid.x, endMid.y)
    }

    path.close()
    return path
}
