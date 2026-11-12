# NurWallpaper Audio API

A small server-side adapter for Quran Foundation's authenticated Content Audio API.
The Android app must call this service; it must never contain the Quran Foundation
`client_secret` or request Content API tokens itself.

## Routes

- `GET /health` — health check.
- `GET /v1/reciters` — available ayah-by-ayah reciters and their Quran Foundation IDs.
- `GET /v1/audio?verse_key=2:255&recitation_id=7` — audio URL for exactly one verse.

Audio is streamed by Android directly from the HTTPS URL returned by Quran Foundation.
The service returns URL metadata only; it does not download, bundle, cache, or redistribute
audio files. The Android client must request audio only after the user taps Play.

## Configuration

Set these environment variables on the server platform:

- `QF_CLIENT_ID` — Quran Foundation backend app client ID.
- `QF_CLIENT_SECRET` — secret stored only in server environment variables.
- `PORT` — optional, defaults to 8080.
- `QF_API_BASE` and `QF_OAUTH_BASE` — optional; production defaults are used.

Create a Backend/server app in the Quran Foundation Developer Console and request the
`content.audio.read` permission. Do not use mobile/frontend credentials for this server
flow. Test in pre-live first, then use production credentials only after access is approved.

## Run locally

```sh
QF_CLIENT_ID="..." QF_CLIENT_SECRET="..." npm start
```

The server requires Node.js 20 or later and uses only built-in modules.

## Security and release requirements

- Serve this API over HTTPS in production.
- Keep credentials in platform environment variables; never commit them.
- Restrict public access/rate usage before production deployment.
- Publish NurWallpaper's privacy policy and terms before enabling the feature.
- Review Quran Foundation's current Developer Terms and any reciter-specific recording
  permissions/attribution before publishing a reciter in the app.
- Do not cache QF content beyond the limits in its Developer Terms.
- Only expose reciters whose recording permissions and attribution have been reviewed.

This adapter intentionally returns the source URL and reciter ID; it does not assert that
every available recording is independently cleared for every commercial use.
