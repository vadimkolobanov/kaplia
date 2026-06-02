package com.kaplia.ui.screen

import androidx.compose.foundation.background
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
import com.kaplia.ui.component.KapliaBlob
import com.kaplia.ui.model.Pronoun
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

data class PetState(
    val name: String,
    val pronoun: Pronoun,
    val day: Int = 1,
    val hunger: Float = 0.75f,
    val energy: Float = 0.82f,
    val mood: Float = 0.88f,
    val health: Float = 0.90f,
)

@Composable
fun HomeScreen(pet: PetState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── Top bar ──────────────────────────────────────────────────────────
        DayCounter(day = pet.day)

        Spacer(Modifier.weight(1f))

        // ── Blob ─────────────────────────────────────────────────────────────
        KapliaBlob(
            modifier = Modifier.size(220.dp),
            primaryColor = KapliaBlue,
            glowColor = KapliaGlowCyan,
        )

        Spacer(Modifier.height(20.dp))

        // ── Pet name ─────────────────────────────────────────────────────────
        Text(
            text = pet.name,
            color = TextPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(8.dp))

        // ── Status message ───────────────────────────────────────────────────
        Text(
            text = stringResource(R.string.home_status_newborn),
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 40.dp),
            lineHeight = 20.sp,
        )

        Spacer(Modifier.weight(1f))

        // ── Metrics ───────────────────────────────────────────────────────────
        MetricsPanel(pet = pet)

        Spacer(Modifier.height(24.dp))

        // ── Talk button ───────────────────────────────────────────────────────
        GradientButton(
            text = stringResource(R.string.home_action_talk),
            onClick = { /* TODO: open chat */ },
            modifier = Modifier.padding(horizontal = 32.dp),
        )

        Spacer(Modifier.height(32.dp))
    }
}

// ── Day counter ───────────────────────────────────────────────────────────────

@Composable
private fun DayCounter(day: Int) {
    Box(
        modifier = Modifier
            .padding(top = 16.dp)
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
        // Track
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(50))
                .background(KapliaLavender.copy(alpha = 0.12f)),
        ) {
            // Fill
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
