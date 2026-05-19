# Security Policy

## Поддерживаемые версии

Pre-MVP — поддерживается только последний билд из `main`.

| Версия      | Поддержка |
|-------------|-----------|
| latest main | ✅        |
| прочее      | ❌        |

## Как сообщить об уязвимости

**Не открывайте публичный issue.**

Напишите на: **vadimkolobanov@users.noreply.github.com**
Или используйте [GitHub Private Security Advisories](https://github.com/vadimkolobanov/kaplia/security/advisories/new).

В письме опишите:
1. Тип уязвимости (XSS / injection / leak PII / прочее)
2. Шаги воспроизведения
3. Влияние (что злоумышленник может сделать)
4. Версия билда / коммит

## SLA на ответ

- **Подтверждение получения:** 48 часов
- **Первичная оценка:** 7 дней
- **Фикс или mitigation:** зависит от severity

После выпуска фикса — координированное раскрытие.

## Особо чувствительные области

- **LLM-промпты и proxy** (`backend/cloudflare-worker/`) — prompt injection, exfiltration
- **Health Connect интеграция** — утечка health-данных
- **Crashlytics / Analytics** — попадание PII в логи
- **Supabase storage** — RLS-политики
