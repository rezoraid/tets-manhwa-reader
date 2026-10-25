# Tets Reader

A native Android app for reading manhwa, manhua, and manga built with Kotlin and Jetpack Compose.

## Verified backend

Base URL:
- https://tets-server-production.up.railway.app

Verified endpoints from the backend repository and server behavior:
- GET /health
- GET /api
- GET /api/sources
- GET /api/home?source=all&refresh=false
- GET /api/list?source=all&page=1
- GET /api/search?q=judul&source=all&page=1
- GET /api/genres
- GET /api/genre/:genre?source=all&page=1
- GET /api/type/:type?source=all&page=1
- GET /api/comic/:source/:id
- GET /api/comic/:source/:id/chapters
- GET /api/comic/:source/:id/chapter/:chapter
- GET /api/image-proxy?url=...&source=...

The server returns a success/error envelope with `success`, `data`, and `error` blocks. It is a scraper backend, not a user-authenticated app API. Bookmarking and reading history are handled locally in the Android app.

## Features

- Home screen with sectioned content
- Search and explore flows
- Detail page and reader screen
- Local bookmarking and history in Room
- Material 3 dark theme designed around a cinematic comic aesthetic

## Build

Open in Android Studio and sync Gradle, or run:

```bash
gradle assembleDebug
```

## Notes

This project is intentionally built to follow the actual API contract and avoid fabricating unsupported endpoints or fields.
