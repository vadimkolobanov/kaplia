# Contributing to Kaplia

Спасибо за интерес. Этот документ описывает как устроена работа в репо — придерживайтесь его, и PR пройдёт быстро.

## TL;DR

1. Issue → ветка `feature/KAP-XX-описание` от `develop`
2. Коммиты — [Conventional Commits](https://www.conventionalcommits.org/)
3. PR → `develop`, заполнить шаблон, дождаться зелёного CI
4. Squash-merge

---

## Branching strategy

| Ветка                       | Назначение                                                    |
|-----------------------------|---------------------------------------------------------------|
| `main`                      | Стабильная. Авто-деплой в Google Play Internal Testing.       |
| `develop`                   | Интеграционная — все feature/fix мёрджатся сюда.              |
| `feature/KAP-XX-описание`   | Новая функциональность                                        |
| `fix/KAP-XX-описание`       | Багфиксы                                                      |
| `release/v0.X`              | Стабилизация релиза перед merge в `main`                      |
| `hotfix/KAP-XX-описание`    | Срочный фикс прямо из `main`                                  |

`KAP-XX` = номер issue.

## Release process

1. Cut `release/v0.X` из `develop`
2. Только bugfix-коммиты в release-ветку
3. После QA: merge в `main` + тег `v0.X.Y`
4. CI собирает signed AAB → Internal Testing → Closed → Production
5. После релиза: `release/*` мёрджится обратно в `develop`

## Conventional Commits

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

**Типы:** `feat`, `fix`, `refactor`, `docs`, `test`, `chore`, `ci`, `perf`, `style`.

Примеры:
```
feat(onboarding): добавить age-gate с проверкой 18+
fix(llm): тайм-аут запроса увеличен до 5s
docs(adr): зафиксировать решение по permadeath с 8-го дня
```

## Перед открытием PR

- [ ] Локально проходит `./gradlew ktlintCheck detekt testDebugUnitTest`
- [ ] Покрытие тестами для domain-кода ≥ 70%
- [ ] Нет хардкод-строк (всё через `strings.xml`, RU + EN)
- [ ] Если меняется архитектура — обновили [ADR](docs/decisions/)
- [ ] Если UI — приложили скриншот/видео
- [ ] PR title в стиле Conventional Commit

## Code review

- Минимум 1 approve
- CI должен быть зелёный
- Conversations resolved
- Merge только через squash

## Issue conventions

Используйте шаблоны: 🎨 Design / ✨ Feature / 🐛 Bug / 🛠 Infrastructure.

Лейблы:
- `area:*` — где живёт (android, design, infra, llm, animation, docs)
- `priority:*` — critical / high / medium / low
- `type:*` — feature / bug / refactor / deps

## Куда писать

- Архитектурные решения → новый ADR в `docs/decisions/`
- Промпты → `docs/prompts/` + eval-сет в `tests/llm/`
- Метрики → `docs/analytics.md`

## Security

Уязвимости — НЕ в публичные issues. См. [`SECURITY.md`](SECURITY.md).
