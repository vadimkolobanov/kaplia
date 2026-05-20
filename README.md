# Kaplia

> AI-питомец с настоящей смертностью, характером на LLM и генетическим наследованием.
> Android, Kotlin, Jetpack Compose.

[![Android CI](https://github.com/vadimkolobanov/kaplia/actions/workflows/android-ci.yml/badge.svg)](https://github.com/vadimkolobanov/kaplia/actions/workflows/android-ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## Что это

**Kaplia** — не «тамагочи на телефоне». Это AI-питомец-компаньон, который живёт в реальном времени, имеет характер на LLM, реагирует на твою жизнь и однажды умирает, оставив следующему поколению свой геном, привычки и три воспоминания.

Ключевая ставка: **геймплейный минимализм + LLM-личность + permadeath + наследование** = эмоциональная плотность, которой нет ни у Finch, ни у Replika.

Подробности в [`docs/concept.md`](docs/concept.md) и [`docs/tz.md`](docs/tz.md).

## Статус

🚧 **Pre-MVP / Phase 0** — настраиваем инфраструктуру, домен, лендинг, концепт-арт.
Roadmap и фазы — в issues, сгруппированы по milestones.

## Стек

| Слой         | Технология                                           |
|--------------|------------------------------------------------------|
| Платформа    | Android (minSdk 26, targetSdk 36)                    |
| Язык         | Kotlin                                                |
| UI           | Jetpack Compose                                       |
| Архитектура  | Clean + MVI                                           |
| Данные       | Room, Supabase, Health Connect                        |
| LLM          | Two-tier (Haiku 4.5 / Sonnet 4.6) через Cloudflare Worker |
| Анимации     | Rive (+ Lottie fallback)                              |
| Аналитика    | Firebase Analytics, Crashlytics                       |
| CI/CD        | GitHub Actions → Google Play Internal Testing         |

## Структура репозитория

```
app/                 Android-приложение (Kotlin, Compose)
backend/             Cloudflare Worker — LLM-прокси
docs/                Концепт, ТЗ, ADR, промпты, аналитика
  decisions/         Architectural Decision Records
  prompts/           LLM system prompts
tests/llm/           Eval-сеты для промптов
.github/             Issue/PR-шаблоны, workflows, dependabot
```

## Разработка

См. [`CONTRIBUTING.md`](CONTRIBUTING.md) — там branching strategy, conventions, release process.

Коротко:
- `main` — стабильная, всегда деплоится в Internal Testing
- `develop` — интеграционная
- `feature/KAP-XX-…`, `fix/KAP-XX-…`, `release/v0.X` — рабочие ветки
- PR → `develop`. В `main` только через `release/*`.

## Документация и решения

- [Концепт продукта](docs/concept.md)
- [ТЗ](docs/tz.md)
- [Decision log (ADR)](docs/decisions/)
- [Event taxonomy](docs/analytics.md)

## Лицензия

[MIT](LICENSE)
