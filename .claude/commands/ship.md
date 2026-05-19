---
description: Закрыть работу — diff-ревью, коммит по Conventional Commits, push, PR в develop с Closes
---

Завершить работу на текущей feature-ветке: проверить изменения, закоммитить, запушить, открыть PR.

## Контекст для извлечения

Из имени текущей ветки (`git branch --show-current`) попытайся вытащить:
- **Тип** — префикс до `/`: `feature` / `fix` / `docs` / `refactor` / `chore`.
  Маппинг на Conventional Commits type: `feature → feat`, остальные — как есть.
- **Issue number** — паттерн `KAP-(\d+)` в имени ветки. Если не нашёл — спроси пользователя.

## Шаги

1. **Проверить что мы не на `main` или `develop`.** Если на них — категорически отказать («сначала `/feature <N>`»).

2. **Показать diff против `develop`.** `git diff develop...HEAD --stat` для обзора + `git diff develop...HEAD` для полного. Если diff пустой — нечего шипить, останавливаемся.

3. **Sanity-проверки на diff** — найди и флагни пользователю если в diff есть:
   - Файлы похожие на секреты: `*.jks`, `*.keystore`, `google-services.json`, `.env`, `id_rsa*`, `*.pem`, `keystore.properties`, любое с `_SECRET`/`_KEY`/`_TOKEN` в имени или содержимом
   - Бинарные файлы > 1 MB
   - `*.docx`, `*.xlsx` (у нас markdown — source of truth)
   - TODO / FIXME / `console.log` / `Log.d("DEBUG"` оставленные в коде
   - Хардкод-строки на русском в `.kt` файлах вне `strings.xml`-ресурсов (потенциальная локализация)

   Если хоть одна проверка сработала — покажи список, спроси у пользователя «продолжать или откатим?». Не блокируй автоматически, дай решить.

4. **Stage всё что в diff:** `git add -A` (но НЕ untracked файлы, которые не относятся к задаче — спроси если есть подозрительные untracked).

5. **Предложи commit message** в формате Conventional Commits:
   ```
   <type>(<scope>): <description>
   ```
   - `type` — из имени ветки (см. выше)
   - `scope` — из labels issue (`area:android` → `android`, `area:llm` → `llm`, и т.д.) или из верхней папки изменённого файла
   - `description` — короткое (≤ 60 символов), imperative mood, нижний регистр, без точки в конце
   Покажи пользователю, дай поправить, дождись подтверждения.

6. **Коммит:** `git commit -m "..."`.

7. **Push:** `git push -u origin HEAD` (без указания имени ветки — git возьмёт текущую).

8. **Создать PR:**
   ```
   gh pr create --base develop --head <branch> \
     --title "<тот же что и commit, если коммит один; иначе спросить>" \
     --body "<<EOF
   Closes #<N>

   ## Что
   <1–2 предложения из contextа>

   ## Как проверить
   - [ ] <шаг 1>
   EOF"
   ```
   Если issue number не определён — `--body` без `Closes`, но предупреди пользователя.

9. **Финальное сообщение:**
   - URL созданного PR
   - Статус CI: «жду пока зелёное, проверять через `gh pr checks`»
   - Когда зелёное — «можно мёрджить через `gh pr merge --squash --delete-branch`»

## Что НЕ делать

- НЕ мёрджить PR автоматически.
- НЕ делать `--force` push.
- НЕ амендить коммиты которые уже на GitHub (`git commit --amend` после push — табу).
- Если что-то сломалось — НЕ делать `git reset --hard`. Остановиться, объяснить пользователю.
- Не запускать `gh pr merge`, `git push --force`, `git reset --hard` без явного согласия.
