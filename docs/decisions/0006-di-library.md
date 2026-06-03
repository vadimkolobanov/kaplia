# 0006. DI-библиотека — Hilt

- **Status:** accepted
- **Date:** 2026-06-03
- **Deciders:** @vadimkolobanov

## Контекст

Phase 1 пишет domain/data-слои, и всем нужен общий граф зависимостей: `PetViewModel` (и будущие ViewModel'и), репозитории Room ([#19](../../issues/19)), Health Connect ([#54](../../issues/54)), LLM-клиент ([#53](../../issues/53)). Без зафиксированного выбора первый разработчик соберёт DI как захочет, а потом всё переписывать. В `gradle/libs.versions.toml` не было ни Hilt, ни Koin.

Силы, которые давят:
- Проект **Android-only** (KMP в планах нет — см. [README](../../README.md) стек).
- Архитектура Clean + MVI: ViewModel'и + репозитории + use-case'ы → много точек инъекции.
- Compose-first UI: нужна нативная интеграция с `ViewModel` и навигацией.
- Соло-разработчик, учится «правильному» Android → ценность индустриального стандарта.

## Рассмотренные варианты

1. **Hilt** (Google) — compile-time safety, KSP, нативная интеграция с Compose (`hiltViewModel()`) и `ViewModel`. Минус: KSP-процессор, чуть больше boilerplate, медленнее сборка.
2. **Koin** — Kotlin-first DSL, без annotation processing (быстрее сборка), KMP-ready. Минус: runtime-резолюция (ошибки графа всплывают в рантайме, не при компиляции), менее «стандарт» для Android-найма.
3. **Ручной DI / Service Locator** — отказ: не масштабируется на Clean+MVI, теряется тестируемость и явность графа.

## Решение

**Hilt.** Compile-time safety (ошибки графа ловятся при сборке, а не у пользователя в рантайме) + нативная интеграция с Compose/ViewModel + индустриальный стандарт Android перевешивают чуть более медленную сборку. KSP вместо kapt — быстрее и совместимо с Kotlin 2.x.

## Последствия

### Позитивные
- Граф зависимостей проверяется компилятором — меньше класс рантайм-падений.
- `@HiltViewModel` + `hiltViewModel()` убирают ручное проксирование ViewModel.
- Стандарт Google → проще онбординг новых разработчиков и ценность для портфолио.

### Негативные
- KSP добавляет шаг в сборку (медленнее, чем чистый Koin).
- Больше boilerplate-аннотаций, чем в Koin DSL.
- Hilt-компоненты привязаны к Android-жизненному циклу — domain-слой должен оставаться чистым (Hilt только на границах).

### Что станет сложнее
- Тестирование: для инструментальных тестов нужен `@HiltAndroidTest` + test runner; юнит-тесты domain-слоя должны оставаться без Hilt (конструкторная инъекция).
- Версии: KSP привязан к линии Kotlin (для Kotlin 2.3.x — KSP `2.3.x`, новая standalone-схема). При бампе Kotlin обновлять KSP синхронно.
- **Hilt запинён на `2.58`** — последняя версия с поддержкой AGP 8.x. Hilt `2.59+` требует AGP 9 (и Gradle 9.1+). Бамп Hilt ≥ 2.59 связан с отдельной миграцией на AGP 9 — не делать в рамках DI.

## Реализация

- `gradle/libs.versions.toml`: версии `hilt`, `ksp`; плагины `hilt`, `ksp`; библиотеки `hilt-android`, `hilt-compiler`.
- Корневой `build.gradle.kts`: плагины с `apply false`.
- `app/build.gradle.kts`: плагины `hilt` + `ksp`, зависимости `implementation(hilt-android)` + `ksp(hilt-compiler)`.
- `KapliaApplication` аннотирован `@HiltAndroidApp`.
- `com.kaplia.di.AppModule` — пустая точка входа `@Module @InstallIn(SingletonComponent::class)`, наполняется по мере появления репозиториев.

## Ссылки

- Issue: [#39](../../issues/39)
- Blocks: [#17](../../issues/17), [#18](../../issues/18), [#19](../../issues/19) — и весь data/LLM/health-слой
- Связанные ADR: [0002](0002-two-model-llm.md) (LLM-клиент будет инъектиться)
