package com.kaplia.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaplia.R
import com.kaplia.domain.model.LifecycleStage
import com.kaplia.ui.component.KapliaBlob
import com.kaplia.ui.model.PetState
import com.kaplia.ui.theme.DeepSpace
import com.kaplia.ui.theme.KapliaBlue
import com.kaplia.ui.theme.KapliaGlowCyan
import com.kaplia.ui.theme.KapliaLavender
import com.kaplia.ui.theme.MetricEnergy
import com.kaplia.ui.theme.MetricHealth
import com.kaplia.ui.theme.MetricHunger
import com.kaplia.ui.theme.MetricMood
import com.kaplia.ui.theme.SpaceSurfaceHigh
import com.kaplia.ui.theme.SpaceSurfaceVariant
import com.kaplia.ui.theme.TextMuted
import com.kaplia.ui.theme.TextPrimary
import com.kaplia.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    pet: PetState,
    onFeed: () -> Unit,
    onPlay: () -> Unit,
    onRest: () -> Unit,
    onNextDay: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DeepSpace)
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DayCounter(day = pet.day, onNextDay = onNextDay)

            Spacer(Modifier.weight(1f))

            PetCharacter(
                name = pet.name,
                healthFraction = pet.health,
                stage = pet.stage,
            )

            Spacer(Modifier.weight(1f))

            MetricsPanel(pet = pet)

            Spacer(Modifier.height(16.dp))

            ActionButtonsRow(
                onFeed = onFeed,
                onPlay = onPlay,
                onRest = onRest,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(Modifier.height(16.dp))

            GradientButton(
                text = stringResource(R.string.home_action_talk),
                onClick = { },
                modifier = Modifier.padding(horizontal = 32.dp),
            )

            Spacer(Modifier.height(32.dp))
        }

        if (pet.isDead) {
            DeadOverlay(name = pet.name)
        }
    }
}

// ── Day counter ───────────────────────────────────────────────────────────────

@Composable
private fun DayCounter(
    day: Int,
    onNextDay: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(SpaceSurfaceVariant)
                .padding(horizontal = 20.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.home_day_counter, day),
                color = TextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(KapliaBlue.copy(alpha = 0.20f))
                .clickable { onNextDay() }
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.home_action_next_day),
                color = KapliaBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

// ── Pet character (blob + name + status) ─────────────────────────────────────

@Composable
private fun PetCharacter(
    name: String,
    healthFraction: Float,
    stage: LifecycleStage,
) {
    KapliaBlob(
        modifier = Modifier.size(220.dp),
        primaryColor = KapliaBlue,
        glowColor = KapliaGlowCyan,
        healthFraction = healthFraction,
    )

    Spacer(Modifier.height(20.dp))

    Text(
        text = name,
        color = TextPrimary,
        fontSize = 26.sp,
        fontWeight = FontWeight.Bold,
    )

    Spacer(Modifier.height(8.dp))

    Text(
        text = stageStatus(stage),
        color = TextSecondary,
        fontSize = 14.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 40.dp),
        lineHeight = 20.sp,
    )
}

// ── Metrics panel ─────────────────────────────────────────────────────────────

@Composable
private fun MetricsPanel(pet: PetState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(SpaceSurfaceHigh)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricBar(
                label = stringResource(R.string.home_metric_hunger),
                value = pet.hunger,
                color = MetricHunger,
                modifier = Modifier.weight(1f),
            )
            MetricBar(
                label = stringResource(R.string.home_metric_energy),
                value = pet.energy,
                color = MetricEnergy,
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricBar(
                label = stringResource(R.string.home_metric_mood),
                value = pet.mood,
                color = MetricMood,
                modifier = Modifier.weight(1f),
            )
            MetricBar(
                label = stringResource(R.string.home_metric_health),
                value = pet.health,
                color = MetricHealth,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun MetricBar(
    label: String,
    value: Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = label, color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(
                text = "${(value * 100).toInt()}%",
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(KapliaLavender.copy(alpha = 0.12f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(value.coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(color),
            )
        }
    }
}

// ── Action buttons ────────────────────────────────────────────────────────────

@Composable
private fun ActionButtonsRow(
    onFeed: () -> Unit,
    onPlay: () -> Unit,
    onRest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ActionButton(
            text = stringResource(R.string.home_action_feed),
            onClick = onFeed,
            modifier = Modifier.weight(1f),
        )
        ActionButton(
            text = stringResource(R.string.home_action_play),
            onClick = onPlay,
            modifier = Modifier.weight(1f),
        )
        ActionButton(
            text = stringResource(R.string.home_action_rest),
            onClick = onRest,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(50))
            .background(SpaceSurfaceHigh)
            .border(1.dp, KapliaBlue.copy(alpha = 0.40f), RoundedCornerShape(50))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = KapliaGlowCyan,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

// ── Death overlay ─────────────────────────────────────────────────────────────

@Composable
private fun DeadOverlay(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp),
        ) {
            KapliaBlob(
                modifier = Modifier.size(160.dp),
                healthFraction = 0f,
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = name,
                color = TextSecondary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.home_status_dead),
                color = TextMuted,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
            )
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

@Composable
private fun stageStatus(stage: LifecycleStage): String =
    stringResource(
        when (stage) {
            LifecycleStage.NEWBORN -> R.string.home_status_newborn
            LifecycleStage.GROWING -> R.string.home_status_growing
            LifecycleStage.MATURE -> R.string.home_status_mature
            LifecycleStage.AGING -> R.string.home_status_aging
            LifecycleStage.CRITICAL -> R.string.home_status_critical
            LifecycleStage.DEAD -> R.string.home_status_dead
        },
    )
