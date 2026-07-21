# Media Downloader

Небольшой сервис для скачивания видео через Telegram-бота.

Пользователь отправляет боту ссылку, приложение скачивает видео и отправляет его обратно. Файлы до 50 МБ уходят прямо в Telegram. Для больших файлов создаётся ссылка на скачивание, которая живёт 30 минут.

## Сервисы

- `telegram-app` — принимает сообщения от пользователя;
- `rest-app` — создаёт задачи и отдаёт файлы по ссылке;
- `download-service` — получает метаданные и скачивает видео через `yt-dlp`;
- `delivery-service` — отправляет результат и удаляет просроченные файлы;
- PostgreSQL — хранит задачи и информацию о файлах;
- Kafka — передаёт события между сервисами;
- Caddy — принимает внешние HTTP-запросы и проксирует их в `rest-app`.

У хранилища есть общий лимит. Перед скачиванием место резервируется, поэтому несколько параллельных задач не смогут случайно занять больше доступного объёма.

## Запуск в Docker

Нужны Docker и Docker Compose.

Создать файл с настройками:

```bash
cp .env.example .env
```

Заполнить `.env`:

```dotenv
DB_NAME=media_downloader
DB_USERNAME=media
DB_PASSWORD=your-password
TELEGRAM_BOT_TOKEN=your-token
PUBLIC_DOMAIN=http://your-server-ip
PUBLIC_DOWNLOAD_BASE_URL=http://your-server-ip
```

Запустить всю систему:

```bash
docker compose up --build -d
```

Проверить состояние и посмотреть логи:

```bash
docker compose ps
docker compose logs -f
```

Остановить:

```bash
docker compose down
```

## Локальная разработка

Если Spring-приложения запускаются из IDE, отдельно можно поднять только инфраструктуру:

```bash
docker compose -f docker-compose.dev.yml up -d
```

REST API по умолчанию доступен на `http://localhost:8080/api/v1/downloads`.
