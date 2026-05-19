# Kaplia — GitHub Project Setup v1.1

**Что изменилось в v1.1:**
- Добавлен раздел Cross-cutting infrastructure (CI/CD, аналитика, crash reporting)
- Шаблоны acceptance criteria для типов задач
- Onboarding разбит на 3 issue
- Inheritance flow выделен в отдельный UX-flow
- Lottie fallback → `priority:high`
- Issue/PR templates с содержимым
- Branching strategy и release process
- Decision log как отдельная практика
- Расширены Phase 2 и Phase 3

---

## 1. Структура репозитория

```
kaplia-android/
├── .github/
│   ├── ISSUE_TEMPLATE/
│   │   ├── design.md
│   │   ├── feature.md
│   │   ├── bug.md
│   │   └── infra.md
│   ├── PULL_REQUEST_TEMPLATE.md
│   └── workflows/
│       ├── android-ci.yml          # build, lint, unit tests
│       └── deploy-internal.yml     # автодеплой в Internal Testing
├── app/
│   └── src/main/java/com/kaplia/
│       ├── data/                   # Room, Supabase, Health Connect
│       ├── domain/                 # Бизнес-логика, геном, метрики
│       ├── ui/                     # Compose screens
│       ├── llm/                    # LLM-прокси, промпты, fallback
│       ├── animation/              # Rive integration
│       └── analytics/              # Firebase, события
├── docs/
│   ├── concept.md                  # Концепт в markdown (диффабельно)
│   ├── tz.md                       # ТЗ в markdown
│   ├── decisions/                  # Decision log (ADR-формат)
│   │   ├── 0001-30-day-lifecycle.md
│   │   ├── 0002-two-model-llm.md
│   │   └── README.md
│   └── prompts/                    # LLM system prompts
├── backend/
│   └── cloudflare-worker/
└── README.md
```

> **Важно:** концепт и ТЗ дублируются в markdown. DOCX оставляем как «pretty» версию для не-разработчиков, но source of truth — markdown в git.

---

## 2. Cross-cutting Infrastructure (новый раздел)

Это сквозные задачи, которые не вписываются в фазы — они нужны с первого дня, иначе вся последующая работа сломается.

### 🛠 CI/CD

- [ ] **[INFRA] GitHub Actions: Android CI**
  Сборка APK на каждый PR, запуск ktlint + detekt + unit tests. Минут 5 на pipeline.
  `area:infra` `priority:critical`
  **AC:**
  - [ ] PR не мёрджится при упавших проверках
  - [ ] Badge статуса в README
  - [ ] Secrets для подписи APK хранятся в GitHub Secrets

- [ ] **[INFRA] GitHub Actions: автодеплой в Internal Testing**
  Merge в `main` → подписанная сборка → загрузка в Google Play Internal Testing.
  `area:infra` `priority:high`
  **AC:**
  - [ ] Версия увеличивается автоматически
  - [ ] Changelog генерируется из commit messages
  - [ ] Тестировщики получают уведомление

### 📊 Аналитика (критично, переехала из Phase 2)

- [ ] **[INFRA] Firebase Analytics — инструментирование**
  События с первого дня: app_open, onboarding_step_X, kaplia_born, kaplia_named, first_chat, kaplia_died, inheritance_accepted.
  `area:infra` `priority:critical`
  **AC:**
  - [ ] Event taxonomy зафиксирована в `docs/analytics.md`
  - [ ] Покрыты все ключевые user flows
  - [ ] Тестовый событийный поток виден в DebugView

- [ ] **[INFRA] Метрики retention — дашборд**
  D1, D7, D30. Funnel: install → first dialogue → day 8 → Crystal stage.
  `area:infra` `priority:high`
  **AC:**
  - [ ] Дашборд в Firebase или внешнем инструменте
  - [ ] Алерт при просадке D7 ниже 20%

### 🚨 Crash reporting

- [ ] **[INFRA] Firebase Crashlytics**
  Все crashes и ANRs логируются. Custom keys: kaplia_age, lifecycle_stage, llm_tier.
  `area:infra` `priority:critical`
  **AC:**
  - [ ] Crash-free rate > 99% — KPI
  - [ ] Алерт в Slack/email при new crash signature
  - [ ] PII не попадает в crash logs

### 📝 Decision Log

- [ ] **[DOCS] Decision log — старт ADR-практики**
  Architectural Decision Records в `docs/decisions/`. Каждое спорное решение фиксируется.
  `area:docs` `priority:high`
  **AC:**
  - [ ] Создан шаблон ADR
  - [ ] Записаны первые 5 ADR из ТЗ (цикл 30 дней, две модели LLM, permadeath с 8-го дня, бесполость, hibernation)

### 🌍 Локализация infrastructure

- [ ] **[INFRA] Localization pipeline — RU/EN с первого дня**
  `strings.xml` структурирован под `values-ru`/`values-en` сразу. Никаких хардкодов.
  `area:android` `priority:high`
  **AC:**
  - [ ] Все строки в ресурсах
  - [ ] Lint-проверка на хардкод-строки
  - [ ] Hotline numbers в локалезависимых ресурсах

---

## 3. Шаблоны acceptance criteria

Чтобы команда не сдавала «вроде готово».

### Шаблон для DESIGN issue

```markdown
**Что:** [одно предложение]
**Зачем:** [связь с продуктом]

### Acceptance Criteria
- [ ] Figma-файл с финальным дизайном в `docs/design/`
- [ ] Экспорт в SVG для каждого варианта
- [ ] Указаны пропорции и направляющие
- [ ] Ревью с PM + ведущим разработчиком пройдено
- [ ] Все элементы названы по конвенции (kaplia_eye_lg, antenna_long и т.д.)

### Файлы / Артефакты
- Figma link:
- SVG export path:
```

### Шаблон для ANDROID issue

```markdown
**Что:** [одно предложение]
**Зачем:** [связь с продуктом]

### Acceptance Criteria
- [ ] Код покрыт unit-тестами (≥ 70% для domain-логики)
- [ ] Соответствует архитектуре (Clean / MVI / то что выбрали)
- [ ] Lint и detekt проходят без warnings
- [ ] Документация в KDoc для публичных API
- [ ] Manual QA на 2 устройствах (топ и API 29)

### Зависимости
- Blocks: #issue_id
- Blocked by: #issue_id
```

### Шаблон для LLM issue

```markdown
**Что:** [одно предложение]
**Зачем:** [связь с продуктом]

### Acceptance Criteria
- [ ] Промпт зафиксирован в `docs/prompts/`
- [ ] 10 примеров входов и желаемых выходов
- [ ] Edge cases покрыты (пустой ввод, длинный ввод, prompt injection)
- [ ] Costs замерены: токены на запрос среднее/p95
- [ ] Safe-fallback триггеры проверены

### Тесты
- Eval-сет в `tests/llm/`
- Прогон до и после изменений
```

---

## 4. .github/ISSUE_TEMPLATE содержимое

### design.md

```yaml
---
name: 🎨 Design task
about: Визуальный дизайн, Figma, персонаж
labels: area:design
---

**Что нужно нарисовать:**

**Связь с продуктом:**

### Acceptance Criteria
- [ ] Figma-ссылка
- [ ] SVG-экспорт
- [ ] Ревью пройдено
```

### feature.md

```yaml
---
name: ✨ Feature
about: Новая функциональность
labels: ''
---

**User story:**
Как [роль], я хочу [действие], чтобы [результат].

**Контекст из ТЗ:**

### Acceptance Criteria
- [ ]
- [ ]
- [ ]

### Зависимости
- Blocks:
- Blocked by:
```

### bug.md

```yaml
---
name: 🐛 Bug
about: Ошибка в работе
labels: 'bug'
---

**Что произошло:**
**Что ожидалось:**

**Шаги воспроизведения:**
1.
2.
3.

**Устройство / API:**
**Версия билда:**

### Severity
- [ ] Critical (краш / потеря данных)
- [ ] High (сломан core flow)
- [ ] Medium (мешает, но обходится)
- [ ] Low (косметика)
```

### infra.md

```yaml
---
name: 🛠 Infrastructure
about: CI/CD, аналитика, мониторинг
labels: area:infra
---

**Задача:**
**Почему сейчас:**

### Acceptance Criteria
- [ ]
- [ ]
```

---

## 5. PULL_REQUEST_TEMPLATE.md

```markdown
## Что делает этот PR

## Связанные issues
Closes #

## Чеклист
- [ ] Тесты добавлены/обновлены
- [ ] Lint и detekt проходят
- [ ] Документация обновлена (если нужно)
- [ ] Скриншоты/видео приложены (если UI)
- [ ] Decision log обновлён (если архитектурное решение)

## Скриншоты
```

---

## 6. Branching strategy и Release process

### Branching

- `main` — стабильная ветка, всегда деплоится в Internal Testing автоматически
- `develop` — интеграционная ветка для активной разработки
- `feature/KAP-XX-description` — фичи
- `fix/KAP-XX-description` — баги
- `release/v0.X` — релизные ветки для подготовки билда

### Release process

1. Создаём `release/v0.X` из `develop`
2. Только bugfix-коммиты в release-ветку
3. После QA → merge в `main` + тег `v0.X.Y`
4. CI автоматически: signed APK → Internal Testing → потом Closed → Production
5. После релиза: `release/*` мёрджится обратно в `develop`

---

## 7. Phase 0: Pre-MVP — изменения

### Onboarding разбит на 3 issue (вместо одного)

Onboarding определяет D1 retention — каждый шаг измеряется отдельно.

- [ ] **[UI] Onboarding шаг 1 — Welcome + age gate**
  Манифест в 2 строки + год рождения для проверки 18+.
  `area:android` `priority:critical`
  **AC:**
  - [ ] Drop-off на этом шаге < 10%
  - [ ] Age gate жёсткий, < 18 → блокировка с дружелюбным сообщением
  - [ ] Событие `onboarding_step_1_complete` в аналитике

- [ ] **[UI] Onboarding шаг 2 — Согревание, рождение, имянаречение**
  Пузырь на экране, тап «согревает» (haptic), вылупление, выбор имени, выбор местоимения.
  `area:android` `priority:critical`
  **AC:**
  - [ ] Haptic feedback при тапе
  - [ ] Анимация вылупления играет полностью
  - [ ] Имя сохраняется в Room
  - [ ] Местоимение влияет на LLM-промпт
  - [ ] Drop-off < 15%

- [ ] **[UI] Onboarding шаг 3 — Первый диалог**
  Первая фраза Капли через Claude Haiku 4.5 на основе генома. Пользователь отвечает.
  `area:android` `priority:critical`
  **AC:**
  - [ ] Первый ответ LLM приходит за < 3 сек
  - [ ] Если LLM не отвечает — graceful fallback
  - [ ] Событие `first_dialogue_complete` фиксируется
  - [ ] Конверсия из step 2 в step 3 > 85%

---

## 8. Phase 1: MVP Dev — изменения

### Inheritance flow — отдельный UX

Это самый эмоциональный момент продукта. Заслуживает выделения.

- [ ] **[UX] Inheritance flow — полный сценарий после смерти**
  Тип death-экрана → эмоциональная пауза → переход на Family screen → предложение наследника → имянаречение → первый диалог наследника со знанием 3 воспоминаний.
  `area:android` `priority:critical`
  **AC:**
  - [ ] Wireframes на все 6 экранов перехода
  - [ ] Пауза перед предложением наследника настраивается (нужно для UX-тестов)
  - [ ] Наследник наследует 80% генома + 3 воспоминания
  - [ ] Конверсия death → принят наследник > 60%
  - [ ] Событие `inheritance_accepted` / `inheritance_declined`

- [ ] **[UI] Memorial screen — пауза между смертью и наследником**
  Тихий экран: имя ушедшей Капли, время жизни, одно воспоминание. Кнопка «Готов(а) встретить нового» — не автоматический переход.
  `area:android` `priority:high`
  **AC:**
  - [ ] Текст не давит ни на церемонию, ни на «давай уже дальше»
  - [ ] Звук/haptic мягкие
  - [ ] Кнопка появляется через 3 сек

### Lottie fallback — переоценка приоритета

- [ ] **[ANIMATION] Lottie fallback**
  Для устройств API 29 / 3 GB RAM где Rive даёт проблемы с производительностью.
  `area:animation` `priority:high` *(было low)*
  **AC:**
  - [ ] Auto-detect устройства с проблемами Rive
  - [ ] Lottie-версии всех 14 состояний
  - [ ] Performance-тесты на Pixel 3a / эквиваленте

---

## 9. Phase 2: Closed Beta — расширено

- [ ] **[BETA] Closed Testing build — Google Play**
- [ ] **[BETA] Feedback сбор — встроенный NPS + опросник**
- [ ] **[BETA] Pacific Mode** (если > 15% жалоб на травму)
- [ ] **[BETA] Аналитика дашборд — retention funnel**
- [ ] **[BETA] Beta tester recruitment**
  Рекрутинг 50–200 тестеров. Каналы: вейтлист, Reddit r/tamagotchi, Discord-сообщества питомцев.
  **AC:**
  - [ ] Анкета входа: возраст, опыт с подобными приложениями, готовность давать фидбек
  - [ ] 30+ активных тестеров после первой недели
- [ ] **[BETA] A/B тест: длительность hibernation (5 vs 7 vs 10 дней)**
- [ ] **[BETA] A/B тест: текст memorial screen — церемония vs минимализм**
- [ ] **[BETA] User interview — 10 глубинных интервью**
  С теми, кто дошёл до Кристалла. И с теми, кто отвалился на дне 3–7.

---

## 10. Phase 3: Soft Launch — расширено

- [ ] **[LAUNCH] Premium инфраструктура — подписка**
- [ ] **[LAUNCH] Релиз в US + RU**
- [ ] **[LAUNCH] Paid traffic — первые кампании**
- [ ] **[LAUNCH] App Store assets — видео + 8 скриншотов**
  Промо-видео 30 сек, скриншоты с подписями, feature graphic.
  **AC:**
  - [ ] Видео шортовое (вертикальное)
  - [ ] Скриншоты локализованы (RU/EN)
- [ ] **[LAUNCH] Press kit**
  Описание продукта, hi-res скриншоты, лого в разных форматах, контакты, FAQ для журналистов.
- [ ] **[LAUNCH] Influencer outreach — 10 микро-инфлюенсеров**
  Категории: digital wellness, indie games, mental health, аниме/kawaii.
- [ ] **[LAUNCH] Community management — Discord-сервер**
  Канал для бета-тестеров → расширение на всех. Модерация, события, sharing «древа предков».
- [ ] **[LAUNCH] Crisis monitoring — на случай мощного отклика**
  Что делать, если приложение завирусится. Бюджет LLM на масштабе. Поддержка пользователей.

---

## 11. Первые 5 issues для старта (без изменений)

1. `[GROWTH] Домен kaplia.app — регистрация`
2. `[DESIGN] Финальный спрайт Капли — базовая форма`
3. `[GROWTH] Лендинг с вейтлистом — запуск`
4. `[LEGAL] Privacy Policy — написание и публикация`
5. `[LLM] Библиотека системных промптов — 4 архетипа`

Параллельно с этим, в первую же неделю:
- `[INFRA] GitHub Actions: Android CI` (даже до первого кода — пайплайн ждёт)
- `[DOCS] Decision log — старт ADR-практики` (фиксируем решения с нуля)

---

*Kaplia GitHub Setup v1.1 • Май 2025*
