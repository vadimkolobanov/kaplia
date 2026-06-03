# Event Taxonomy

Все события Firebase Analytics. Имена `snake_case`, параметры в `lower_snake`.

## Принципы

- **Никаких PII в параметрах** — ни имени Капли, ни текста диалогов
- Каждое событие имеет назначение: либо считаем funnel, либо мониторим качество
- Новое событие → PR с обновлением этого файла + обновлением dashboards

## Жизненный цикл

| Event                       | Параметры                           | Назначение                          |
|-----------------------------|-------------------------------------|-------------------------------------|
| `app_open`                  | `source` (cold/warm/push)           | DAU, частота открытий               |
| `onboarding_step_1_complete`| `age_gate_passed: bool`             | D1 funnel                           |
| `onboarding_step_2_complete`| `name_length`                       | Имянаречение конверсия              |
| `onboarding_step_3_complete`| `llm_response_ms`                   | Первый диалог конверсия             |
| `kaplia_born`               | `genome_hash`                       | Старт цикла                         |
| `kaplia_named`              | `name_length`                       | (имя сохраняется отдельно, в Room)  |
| `first_chat`                | `prompt_archetype`, `llm_tier`      | First-day engagement                |
| `daily_dialogue`            | `day_n`, `llm_tier`, `length_class` | Daily engagement                    |
| `kaplia_died`               | `cause`, `day_n`, `lifecycle_stage` | Permadeath analytics                |
| `inheritance_offered`       | `hibernation_days`                  | Inheritance funnel                  |
| `inheritance_accepted`      | `hibernation_days`                  | Return rate                         |
| `inheritance_declined`      | `hibernation_days`                  | Drop-off после смерти               |

## Метрики здоровья

| Event                       | Параметры                           | Назначение                          |
|-----------------------------|-------------------------------------|-------------------------------------|
| `health_sync_ok`            | `source` (health_connect/manual)    | Подключение HC                      |
| `health_sync_fail`          | `error_class`                       | Quality, debug                      |
| `kaplia_mood_changed`       | `mood`, `trigger`                   | Telemetry → behaviour link          |

## LLM

| Event                       | Параметры                                       | Назначение                |
|-----------------------------|-------------------------------------------------|---------------------------|
| `llm_request`               | `tier`, `tokens_in_class`, `latency_ms_class`   | Costs, latency            |
| `llm_fallback_triggered`    | `from_tier`, `to_tier`, `reason`                | Reliability               |
| `llm_safe_fallback_shown`   | `trigger_class`                                 | Safety guardrails         |

## Monetisation (Phase 2+)

| Event                       | Параметры                           |
|-----------------------------|-------------------------------------|
| `paywall_shown`             | `placement`, `day_n`                |
| `paywall_dismissed`         | `placement`                         |
| `subscription_started`      | `plan`, `placement`                 |
| `subscription_cancelled`    | `plan`, `day_n`                     |

## Crashlytics custom keys

Всегда устанавливать (без PII):

- `kaplia_age` — текущий день жизненного цикла (1–30)
- `lifecycle_stage` — childhood/adolescence/maturity/aging
- `llm_tier` — haiku/sonnet

## KPI и алерты

| KPI               | Источник                             | Алерт                |
|-------------------|--------------------------------------|----------------------|
| D1 retention      | `app_open` после `kaplia_born`       | < 40%                |
| D7 retention      | same                                 | < 20%                |
| D30 retention     | same                                 | < 10%                |
| Onboarding drop   | step_1 → step_3 funnel               | drop > 25%           |
| Crash-free rate   | Crashlytics                          | < 99%                |
| LLM p95 latency   | `llm_request.latency_ms_class`       | > 5000ms             |
| Inheritance rate  | offered → accepted                   | < 60%                |
