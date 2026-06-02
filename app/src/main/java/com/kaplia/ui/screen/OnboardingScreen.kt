package com.kaplia.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaplia.R
import com.kaplia.ui.component.KapliaBlob
import com.kaplia.ui.model.Pronoun
import com.kaplia.ui.theme.DeepSpace
import com.kaplia.ui.theme.KapliaBlue
import com.kaplia.ui.theme.KapliaGlowCyan
import com.kaplia.ui.theme.KapliaLavender
import com.kaplia.ui.theme.TextMuted
import com.kaplia.ui.theme.TextPrimary
import com.kaplia.ui.theme.TextSecondary
import kotlinx.coroutines.delay

import kotlin.math.PI
import kotlin.math.sin

private enum class OnboardingStep { WELCOME, AGE_GATE, CUSTOMIZE }

@Suppress("LongMethod")
@Composable
fun OnboardingScreen(onComplete: (name: String, pronoun: Pronoun) -> Unit) {
    var step by rememberSaveable { mutableStateOf(OnboardingStep.WELCOME) }
    var petName by rememberSaveable { mutableStateOf("") }
    var selectedPronoun by rememberSaveable { mutableStateOf(Pronoun.THEY) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace),
    ) {
        StarField(modifier = Modifier.fillMaxSize())

        when (step) {
            OnboardingStep.WELCOME -> WelcomeStep(
                onContinue = { step = OnboardingStep.AGE_GATE },
            )

            OnboardingStep.AGE_GATE -> AgeGateStep(
                onConfirm = { step = OnboardingStep.CUSTOMIZE },
            )

            OnboardingStep.CUSTOMIZE -> CustomizeStep(
                petName = petName,
                onNameChange = { petName = it },
                selectedPronoun = selectedPronoun,
                onPronounChange = { selectedPronoun = it },
                onHatch = {
                    val name = petName.trim().ifEmpty { "Kaplya" }
                    onComplete(name, selectedPronoun)
                },
            )
        }
    }
}

// ── Welcome ───────────────────────────────────────────────────────────────────

@Composable
private fun WelcomeStep(onContinue: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { it / 4 },
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                KapliaBlob(
                    modifier = Modifier.size(180.dp),
                    glowColor = KapliaGlowCyan,
                    primaryColor = KapliaBlue,
                )

                Spacer(Modifier.height(32.dp))

                Text(
                    text = stringResource(R.string.onboarding_welcome_title),
                    color = TextPrimary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.onboarding_welcome_subtitle),
                    color = TextSecondary,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp,
                )

                Spacer(Modifier.height(48.dp))

                GradientButton(
                    text = stringResource(R.string.onboarding_welcome_cta),
                    onClick = onContinue,
                )
            }
        }
    }
}

// ── Age gate ─────────────────────────────────────────────────────────────────

@Composable
private fun AgeGateStep(onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.onboarding_age_gate_title),
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(40.dp))

        GradientButton(
            text = stringResource(R.string.onboarding_age_gate_confirm),
            onClick = onConfirm,
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.onboarding_age_gate_deny),
            color = TextMuted,
            fontSize = 14.sp,
        )
    }
}

// ── Customize ─────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Suppress("LongMethod")
@Composable
private fun CustomizeStep(
    petName: String,
    onNameChange: (String) -> Unit,
    selectedPronoun: Pronoun,
    onPronounChange: (Pronoun) -> Unit,
    onHatch: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.onboarding_name_title),
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = petName,
            onValueChange = { if (it.length <= 20) onNameChange(it) },
            placeholder = {
                Text(
                    text = stringResource(R.string.onboarding_name_hint),
                    color = TextMuted,
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = KapliaBlue,
                unfocusedBorderColor = TextMuted,
                cursorColor = KapliaBlue,
            ),
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(36.dp))

        Text(
            text = stringResource(R.string.onboarding_pronoun_title),
            color = TextSecondary,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(16.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Pronoun.entries.forEach { pronoun ->
                PronounChip(
                    label = stringResource(pronoun.labelRes),
                    selected = pronoun == selectedPronoun,
                    onClick = { onPronounChange(pronoun) },
                )
            }
        }

        Spacer(Modifier.height(48.dp))

        GradientButton(
            text = stringResource(R.string.onboarding_hatch_cta),
            onClick = onHatch,
            enabled = petName.isNotBlank(),
        )
    }
}

// ── Shared components ─────────────────────────────────────────────────────────

@Composable
internal fun GradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val gradient = if (enabled) {
        Brush.linearGradient(listOf(KapliaBlue, KapliaLavender))
    } else {
        Brush.linearGradient(listOf(TextMuted, TextMuted))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(50))
            .background(gradient)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = if (enabled) Color.White else TextMuted,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun PronounChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) KapliaBlue else TextMuted.copy(alpha = 0.5f)
    val bgColor = if (selected) KapliaBlue.copy(alpha = 0.18f) else Color.Transparent

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = if (selected) KapliaBlue else TextSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

// ── Star field ────────────────────────────────────────────────────────────────

@Composable
private fun StarField(modifier: Modifier = Modifier) {
    val stars = remember {
        val rng = java.util.Random(42L)
        List(90) { Triple(rng.nextFloat(), rng.nextFloat(), rng.nextInt(3)) }
    }

    val transition = rememberInfiniteTransition(label = "stars")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2.0 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "starPhase",
    )

    Canvas(modifier = modifier) {
        stars.forEachIndexed { i, (x, y, tier) ->
            val alpha = (0.25f + 0.45f * (1f + sin(phase + i * 0.37f)) / 2f).coerceIn(0f, 1f)
            val radius = when (tier) {
                2 -> 2.2f
                1 -> 1.4f
                else -> 0.9f
            }
            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = radius,
                center = Offset(x * size.width, y * size.height),
            )
        }
    }
}
