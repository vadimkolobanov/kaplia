# Claude Code slash commands

Команды для автоматизации git-workflow проекта. Запускаются как `/<имя>` в Claude Code.

## Доступные команды

| Команда | Что делает |
|---|---|
| `/feature <issue-number>` | Создать новую feature-ветку от свежего `develop` по conventions проекта |
| `/ship` | Diff-ревью → commit (Conventional Commits) → push → PR в `develop` с `Closes #N` |
| `/control` | **Глубокий read-only аудит на Opus 4.7.** Ищет галлюцинации, расхождения дока↔код, утёкшие секреты, мёртвые ссылки, hardcode-строки. Только отчёт, без правок |

## Типичный flow

```text
/feature 9             # создаст feature/KAP-9-android-ci
# … правишь файлы …
/ship                  # покажет diff, спросит commit message, пушнёт, откроет PR
gh pr checks           # ждёшь зелёный CI
gh pr merge --squash --delete-branch
```

## Зачем

Раньше каждый раз приходилось помнить:
- Откуда отбранчеваться (`develop`, не `main`)
- Как назвать ветку (`<type>/KAP-N-slug`)
- Conventional Commits format
- `Closes #N` в PR body
- Squash merge, не merge commit
- Sanity-проверки: не закоммитить секреты, .docx, не оставить хардкод-строки

Команды держат эти правила в одном месте, проверяют что ничего не упущено, и при этом останавливаются на ключевых решениях — diff и commit message всегда подтверждаешь сам.

## Как менять

Команды — обычные markdown-файлы во frontmatter-формате. Правь, коммить, открывай PR — никаких сборок.

`$ARGUMENTS` в теле — это аргументы, переданные после `/команда`.

См. также: [CONTRIBUTING.md](../../CONTRIBUTING.md) — branching strategy и release process.
