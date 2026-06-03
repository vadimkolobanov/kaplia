# Architectural Decision Records (ADR)

Каждое спорное архитектурное или продуктовое решение фиксируется здесь как короткий документ.

## Зачем

- Через 6 месяцев никто не помнит **почему** так решили
- Новые люди в команде понимают контекст за 10 минут
- Когда захочется переделать — видно, что взвешивалось

## Формат

См. [`template.md`](template.md). Каждый ADR:

- Номер (`0001-...`), порядковый
- Статус: `proposed` / `accepted` / `superseded by NNNN` / `deprecated`
- Контекст, решение, последствия

## Когда писать ADR

- Выбор между двумя+ архитектурными подходами
- Продуктовое решение с долгосрочными последствиями (permadeath, цикл жизни)
- Отказ от чего-то очевидного
- Любое решение, которое через полгода придётся объяснять

## Index

| #    | Title                                    | Status   |
|------|------------------------------------------|----------|
| 0001 | [30-day lifecycle](0001-30-day-lifecycle.md) | accepted |
| 0002 | [Two-model LLM (Haiku + Sonnet)](0002-two-model-llm.md) | accepted |
| 0003 | [Permadeath с 8-го дня](0003-permadeath-from-day-8.md) | accepted |
| 0004 | [Бесполость персонажа](0004-genderless-character.md) | accepted |
| 0005 | [Hibernation вместо instant respawn](0005-hibernation-mechanic.md) | accepted |
| 0006 | [DI-библиотека — Hilt](0006-di-library.md) | accepted |

**Practice started:** 2026-05-19
**Owner:** @vadimkolobanov
