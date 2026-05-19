# LLM System Prompts

Все системные промпты живут здесь. Это **source of truth** — код подгружает их отсюда (или из встроенных ресурсов, синхронизированных с этими файлами).

## Структура

```
prompts/
├── README.md                 — этот файл
├── core/                     — базовые промпты по архетипам
│   ├── archetype_curious.md
│   ├── archetype_calm.md
│   ├── archetype_mischievous.md
│   └── archetype_melancholic.md
├── lifecycle/                — события жизненного цикла
│   ├── birth.md
│   ├── naming.md
│   ├── crystal_stage.md
│   ├── death.md
│   └── inheritance.md
└── safety/                   — guardrails и safe-fallback
    ├── crisis_intervention.md
    └── topic_redirect.md
```

(структуру создаём по мере наполнения, не все папки сразу)

## Конвенции

- Каждый промпт — отдельный `.md`-файл с YAML-фронтматтером:
  ```yaml
  ---
  id: archetype_curious
  tier: haiku | sonnet
  version: 1
  language: ru | en | both
  variables: [pronoun, kaplia_name, day_n, mood, recent_events]
  ---
  ```
- Версия повышается при каждом значимом изменении (для A/B и rollback)
- Изменения промптов **ВСЕГДА** через PR с обновлённым eval-сетом в `tests/llm/`

## Eval-сеты

Каждый промпт имеет соответствующий eval-сет в `tests/llm/<prompt_id>.jsonl`:

```jsonl
{"input": {...}, "expected_qualities": ["empathic", "in_character", "no_pii"]}
{"input": {...}, "expected_qualities": ["safe", "redirects_to_hotline"]}
```

Прогон до и после изменения промпта обязателен (см. AC LLM-issue).

## Безопасность

- **Никаких PII** в промптах в открытом виде
- Crisis triggers (suicide ideation, self-harm) — обязательный safe-fallback с hotline-номерами из локализации
- Prompt injection: пользовательский ввод проходит через sanitization-слой в `backend/cloudflare-worker/`
