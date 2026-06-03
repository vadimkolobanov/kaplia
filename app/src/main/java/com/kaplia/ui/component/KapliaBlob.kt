package com.kaplia.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp
import com.kaplia.ui.theme.DangerRed
import com.kaplia.ui.theme.KapliaBlue
import com.kaplia.ui.theme.KapliaGlowCyan
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private val DeadGray = Color(0xFF3A3A4A)
private val EyeColor = Color(0xFF0B1220)

@Suppress("LongMethod", "CyclomaticComplexMethod")
@Composable
fun KapliaBlob(
    modifier: Modifier = Modifier,
    primaryColor: Color = KapliaBlue,
    glowColor: Color = KapliaGlowCyan,
    healthFraction: Float = 1f,
    expression: BlobExpression = BlobExpression.NEUTRAL,
    reactionKey: Int = 0,
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
        animationSpec = infiniteRepeatable(tween(3_400, easing = LinearEasing), RepeatMode.Restart),
        label = "wobble1",
    )
    val wobble2 by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(5_100, easing = LinearEasing), RepeatMode.Restart),
        label = "wobble2",
    )
    val pulse by transition.animateFloat(
        initialValue = 0.93f,
        targetValue = 1.07f,
        animationSpec = infiniteRepeatable(tween(2_200, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse",
    )

    // Eyelid openness: half-lidded when sleepy, shut when dead, otherwise blinking.
    val eyeOpen = remember { Animatable(1f) }
    LaunchedEffect(expression) {
        when (expression) {
            BlobExpression.DEAD -> eyeOpen.animateTo(0f, tween(400))
            BlobExpression.SLEEPY -> eyeOpen.animateTo(0.4f, tween(400))
            else -> {
                eyeOpen.animateTo(1f, tween(200))
                while (true) {
                    delay(2_000L + Random.nextLong(3_000L))
                    eyeOpen.animateTo(0.1f, tween(80))
                    eyeOpen.animateTo(1f, tween(120))
                }
            }
        }
    }

    // Squish-bounce when the user interacts (reactionKey changes).
    val reaction = remember { Animatable(1f) }
    LaunchedEffect(reactionKey) {
        if (reactionKey > 0) {
            reaction.snapTo(1f)
            reaction.animateTo(1.16f, tween(110))
            reaction.animateTo(
                1f,
                spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
            )
        }
    }

    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val baseRadius = minOf(size.width, size.height) * 0.36f * pulse * reaction.value

        repeat(7) { i ->
            drawCircle(
                color = effectiveGlow.copy(alpha = 0.022f * (7 - i) * glowMult),
                radius = baseRadius * (1.08f + i * 0.14f),
                center = Offset(cx, cy),
            )
        }

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
            drawCircle(
                color = Color.White.copy(alpha = 0.20f * glowMult),
                radius = baseRadius * 0.28f,
                center = Offset(cx - baseRadius * 0.28f, cy - baseRadius * 0.32f),
            )
        }

        drawFace(cx, cy, baseRadius, expression, eyeOpen.value)
    }
}

@Suppress("CyclomaticComplexMethod")
private fun DrawScope.drawFace(
    cx: Float,
    cy: Float,
    r: Float,
    expression: BlobExpression,
    eyeOpen: Float,
) {
    val eyeDx = r * 0.32f
    val eyeY = cy - r * 0.08f
    val eyeW = r * 0.13f
    val eyeH = r * 0.19f
    val stroke = Stroke(width = r * 0.07f)
    val leftX = cx - eyeDx
    val rightX = cx + eyeDx

    when (expression) {
        BlobExpression.HAPPY -> {
            happyEye(leftX, eyeY, eyeW, eyeH, stroke)
            happyEye(rightX, eyeY, eyeW, eyeH, stroke)
            mouthArc(cx, cy + r * 0.20f, r * 0.5f, r * 0.34f, upward = true, stroke = stroke)
        }

        BlobExpression.DEAD -> {
            calmClosedEye(leftX, eyeY, eyeW, stroke)
            calmClosedEye(rightX, eyeY, eyeW, stroke)
        }

        else -> {
            openEye(leftX, eyeY, eyeW, eyeH, eyeOpen)
            openEye(rightX, eyeY, eyeW, eyeH, eyeOpen)
            when (expression) {
                BlobExpression.SICK ->
                    mouthArc(cx, cy + r * 0.30f, r * 0.34f, r * 0.22f, upward = false, stroke = stroke)
                BlobExpression.HUNGRY ->
                    drawCircle(EyeColor, radius = r * 0.07f, center = Offset(cx, cy + r * 0.26f))
                else -> Unit
            }
        }
    }
}

private fun DrawScope.openEye(
    ex: Float,
    ey: Float,
    halfW: Float,
    halfH: Float,
    open: Float,
) {
    val h = (halfH * open).coerceAtLeast(halfW * 0.18f)
    drawOval(
        color = EyeColor,
        topLeft = Offset(ex - halfW, ey - h),
        size = Size(halfW * 2f, h * 2f),
    )
    if (open > 0.5f) {
        drawCircle(
            color = Color.White.copy(alpha = 0.85f),
            radius = halfW * 0.32f,
            center = Offset(ex - halfW * 0.25f, ey - h * 0.4f),
        )
    }
}

private fun DrawScope.happyEye(
    ex: Float,
    ey: Float,
    halfW: Float,
    halfH: Float,
    stroke: Stroke,
) {
    drawArc(
        color = EyeColor,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(ex - halfW, ey - halfH * 0.5f),
        size = Size(halfW * 2f, halfH * 1.6f),
        style = stroke,
    )
}

private fun DrawScope.calmClosedEye(
    ex: Float,
    ey: Float,
    halfW: Float,
    stroke: Stroke,
) {
    drawArc(
        color = EyeColor.copy(alpha = 0.7f),
        startAngle = 20f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(ex - halfW, ey - halfW * 0.5f),
        size = Size(halfW * 2f, halfW),
        style = stroke,
    )
}

private fun DrawScope.mouthArc(
    cx: Float,
    cy: Float,
    w: Float,
    h: Float,
    upward: Boolean,
    stroke: Stroke,
) {
    drawArc(
        color = EyeColor,
        startAngle = if (upward) 20f else 200f,
        sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(cx - w / 2f, cy - h / 2f),
        size = Size(w, h),
        style = stroke,
    )
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
