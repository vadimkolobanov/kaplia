package com.kaplia.domain.model

/**
 * Canonical lifecycle stages — see ADR-0001.
 * childhood 1–7 · adolescence 8–14 · maturity 15–21 · aging/Crystal 22–30 · death.
 * Permadeath unlocks from adolescence (day 8) — see ADR-0003.
 */
enum class LifecycleStage { CHILDHOOD, ADOLESCENCE, MATURITY, AGING, DEAD }
