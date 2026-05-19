# ТЗ — Kaplia

> **Источник:** `Kaplia_TZ_v2.docx` (исходный DOCX, не в git — добавляйте сюда правки в markdown).
> Этот файл — source of truth. DOCX поддерживается как «pretty» версия для не-разработчиков.

## Статус миграции

- [ ] Полный текст ТЗ перенесён из DOCX в markdown
- [ ] Разделы разбиты на подфайлы (`tz/onboarding.md`, `tz/llm.md`, `tz/lifecycle.md`, …) если станет неудобно
- [ ] Все ключевые решения вынесены в [ADR](decisions/)

## Текущее содержание

См. также:

- [Концепт](concept.md) — стратегия, рынок, позиционирование
- [Decision log](decisions/) — архитектурные решения
- [Event taxonomy](analytics.md) — события и метрики
- [LLM prompts](prompts/) — системные промпты

## Жизненный цикл (TL;DR)

См. [ADR-0001](decisions/0001-30-day-lifecycle.md) — 30-дневный цикл, permadeath с 8-го дня ([ADR-0003](decisions/0003-permadeath-from-day-8.md)), hibernation 7 дней между поколениями ([ADR-0005](decisions/0005-hibernation-mechanic.md)).

## LLM

См. [ADR-0002](decisions/0002-two-model-llm.md) — Haiku 4.5 для рутины, Sonnet 4.6 для ключевых сцен. Routing в `backend/cloudflare-worker/`.

## Персонаж

См. [ADR-0004](decisions/0004-genderless-character.md) — бесполость, пользователь выбирает местоимение.

---

> ⚠️ TODO: перенести полный текст ТЗ из `Kaplia_TZ_v2.docx`. Сейчас этот файл — каркас.
