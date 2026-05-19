# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

**Kaplia** — Android-приложение: AI-питомец с 30-дневным жизненным циклом, permadeath с 8-го дня, генетическим наследованием и LLM-личностью. Pre-MVP / Phase 0 — большая часть `app/` ещё пустая, инфраструктура (CI, ADR, шаблоны) уже на месте.

## Stack

- **Android:** Kotlin, Jetpack Compose, Clean + MVI
- **Data:** Room (local), Supabase (sync/backup), Health Connect (telemetry)
- **LLM:** two-tier (Claude Haiku 4.5 / Sonnet 4.6) через Cloudflare Worker (`backend/cloudflare-worker/`)
- **Анимации:** Rive + Lottie fallback для слабых устройств
- **CI:** GitHub Actions → Google Play Internal Testing

## Common commands

Все команды запускаются из корня. Gradle wrapper появится после INFRA-issue «Android CI».

```bash
./gradlew ktlintCheck                  # lint Kotlin
./gradlew detekt                       # static analysis
./gradlew testDebugUnitTest            # unit tests
./gradlew testDebugUnitTest --tests "com.kaplia.SomeTest"   # single test class
./gradlew assembleDebug                # build debug APK
./gradlew bundleRelease                # signed AAB for Play
```

## Architecture: the big picture

```
app/src/main/java/com/kaplia/
├── data/        Room entities, Supabase repos, Health Connect adapters
├── domain/      Бизнес-логика: геном, метрики, lifecycle state machine
├── ui/          Compose screens (onboarding, main, memorial, inheritance)
├── llm/         LLM-прокси клиент, prompt assembly, fallback chain
├── animation/   Rive integration + Lottie fallback selector
└── analytics/   Firebase Analytics events + Crashlytics custom keys
```

**Ключевые потоки**, которые требуют чтения нескольких файлов:

1. **Жизненный цикл Капли** — state machine в `domain/lifecycle/`, влияет на UI (`ui/`), анимации (`animation/`), LLM-промпты (`llm/`). См. [ADR-0001](docs/decisions/0001-30-day-lifecycle.md), [ADR-0003](docs/decisions/0003-permadeath-from-day-8.md).
2. **LLM routing** — `llm/` решает Haiku vs Sonnet на основе типа события из `domain/`. Запросы идут через Cloudflare Worker, который sanitize'ит ввод и применяет safety guardrails. См. [ADR-0002](docs/decisions/0002-two-model-llm.md).
3. **Наследование** — после смерти `domain/inheritance/` собирает 80% генома + 3 воспоминания, передаёт новому экземпляру. UX-цепочка: death screen → memorial (hibernation 7 дней, см. [ADR-0005](docs/decisions/0005-hibernation-mechanic.md)) → family screen → naming.
4. **Бесполость** — местоимение хранится в Room и обязательно передаётся в каждый LLM-промпт. Все UI-строки параметризованы. См. [ADR-0004](docs/decisions/0004-genderless-character.md).

## Conventions

- **Branching:** `feature/KAP-XX-описание` от `develop`. См. [CONTRIBUTING.md](CONTRIBUTING.md).
- **Commits:** Conventional Commits (`feat(scope): …`).
- **Локализация:** строки только в `strings.xml` (`values-ru`, `values-en`) — никаких хардкодов. Lint-проверка обязательна.
- **PII:** никогда не попадает в analytics, crashlytics, prompts, или логи. Sanitize-слой в LLM-proxy.
- **Архитектурное решение:** новый файл в `docs/decisions/` обязателен ДО мерджа PR с изменением.

## Decision log

Все спорные решения зафиксированы в [`docs/decisions/`](docs/decisions/). Перед изменением чего-то, что выглядит «странно» — проверьте, нет ли там обоснования.

## Source of truth для документации

- **Концепт** — `docs/concept.md` (markdown, diffable)
- **ТЗ** — `docs/tz.md` (в миграции из DOCX)
- **DOCX** в корне (`Kaplia_*.docx`) — pretty-версия для не-разработчиков, **не source of truth**, в `.gitignore`.

## Что НЕ делать

- Не запускать `gh repo create`, `gh repo delete`, `gh pr merge` без явной просьбы — это разрушительные/публичные действия.
- Не править ADR со статусом `accepted` — создавайте новый ADR со статусом `superseded by`.
- Не добавлять зависимости, которые тянут tracking SDK (Facebook, AppsFlyer и т.п.) без обсуждения — у нас compliance-чувствительный домен (mental health adjacent + Health Connect).
