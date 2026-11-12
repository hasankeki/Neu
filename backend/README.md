# NurWallpaper Audio API

A server-side adapter for Quran Foundation's authenticated Content Audio API.
The Android app calls this service; it must never contain the Quran Foundation
`client_secret` or request Content API tokens itself.

## Routes

- `GET /health` — health check.
- `GET /v1/reciters` — approved ayah-by-ayah reciters and their Quran Foundation IDs.
- `GET /v1/audio?verse_key=2:255&recitation_id=7` — URL for exactly one verse.

Android streams the returned HTTPS URL only after the user taps Play. The service does
not download, bundle, cache, or redistribute audio files.

## Configuration

- `QF_CLIENT_ID` — Quran Foundation backend app client ID.
- `QF_CLIENT_SECRET` — secret stored only in server environment variables.
- `QF_ENV` — `prelive` (default) or `production`.
- `PORT` — optional, defaults to 8080.
- `QF_API_BASE` and `QF_OAUTH_BASE` — optional HTTPS overrides for the matching environment.
- `QF_APPROVED_RECITATION_IDS` — comma-separated ayah-recitation IDs that have passed
  recording-rights and attribution review. Leave empty until that review is complete.

Create a Backend/server app in the Quran Foundation Developer Console and enable the
Client Credentials grant with `content.audio.read`. New backend apps use granular
Content scopes. Start with pre-live credentials and endpoints; move to production only
after production access is approved. Never mix credentials and endpoints from different
environments.

## Run locally

```sh
QF_CLIENT_ID="..." QF_CLIENT_SECRET="..." npm start
```

Run automated checks with `npm test`. Node.js 20 or later is required; no runtime
dependencies are used.

## Security and release requirements

- Serve this API over HTTPS in production.
- Keep credentials in platform environment variables; never commit them.
- The process applies a basic per-IP limit (60 requests/minute); configure an additional
  gateway/platform rate limit for production and multi-instance deployments.
- Health checks are intentionally unauthenticated and contain no sensitive data.
- Review Quran Foundation's current Developer Terms and reciter-specific permissions/
  attribution before publishing any reciter. The allowlist is enforced on both listing
  and playback endpoints.
- Do not cache QF content beyond the limits in its Developer Terms.
- Publish NurWallpaper's privacy policy and terms before enabling the feature.

The adapter returns the original audio URL and reciter ID. It does not assert that every
available recording is independently cleared for every commercial use.
