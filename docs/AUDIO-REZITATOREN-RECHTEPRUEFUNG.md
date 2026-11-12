# NurWallpaper – Audio-Katalog und Rechteprüfung

Stand: 12.11.2026

Dieses Dokument ist eine Arbeitsliste, keine Freigabe zur Veröffentlichung und kein Download-Katalog. Es enthält bewusst keine erfundenen Quran-Foundation-IDs, Audio-URLs, Prüfsummen, Dateigrößen oder Lizenzbehauptungen.

## Verbindliche Regeln

- Zuerst wird Quran-Foundation-Streaming geprüft. Das Backend bleibt bis zu einer nachweislich dauerhaft kostenlosen Hosting-Möglichkeit undeployed.
- Es werden keine kostenpflichtigen Dienste, Zahlungsdaten, Billing-Aktivierungen oder Testangebote mit späterer Kostenpflicht verwendet.
- Falls Streaming technisch oder dauerhaft kostenlos nicht möglich ist, wird ein optionales Offline-Paket geprüft.
- Offline-Dateien dürfen nur aufgenommen werden, wenn die konkrete Aufnahme und die Weiterverbreitung/Offline-Speicherung ausdrücklich erlaubt sind. Eine Erlaubnis zum Anhören oder Streamen reicht dafür nicht automatisch.
- Keine Audiodatei wird in das APK eingebaut. Ein späteres Offline-Paket bleibt optional und wird nur auf ausdrücklichen Nutzerwunsch heruntergeladen.
- Keine Aufnahme wird veröffentlicht, solange Quelle, Rechte, Attribution, technische Integrität und menschliche Prüfung nicht dokumentiert und freigegeben sind.

## Vorgemerkte Rezitatoren

Alle Einträge stehen auf `PENDING`. Die Namen sind eine Merkliste und keine Aussage über Rechte oder Verfügbarkeit.

| Rezitator | Quran-Foundation-ID | Audioquelle | Offline-Weiterverbreitung | Status |
|---|---|---|---|---|
| Maher Al-Muaiqly | nicht verifiziert | nicht verifiziert | nicht geprüft | PENDING |
| Ali Al-Hudhaify | nicht verifiziert | nicht verifiziert | nicht geprüft | PENDING |
| Muhammad Ayyub | nicht verifiziert | nicht verifiziert | nicht geprüft | PENDING |
| Khalid Al-Muhanna | nicht verifiziert | nicht verifiziert | nicht geprüft | PENDING |
| Ibrahim Al-Akhdar | nicht verifiziert | nicht verifiziert | nicht geprüft | PENDING |
| Abdullah Al-Juhany | nicht verifiziert | nicht verifiziert | nicht geprüft | PENDING |

## Wunschliste – nicht Teil des Startpakets

- Yasser Al-Dosari
- Badr Al-Turki

Diese Namen werden nicht in die freigegebene Liste übernommen, bevor die konkrete Aufnahme und ihre Rechte geprüft wurden.

## Pflichtfelder je Aufnahme / Paket

Vor einer Freigabe muss ein Datensatz mindestens Folgendes enthalten:

- stabiler interner Rezitator- und Paketbezeichner
- vollständiger angezeigter Name
- Originalquelle und konkrete URL zur Aufnahme
- Quran-Foundation-Recitation-ID, falls die Quelle Quran Foundation ist
- genaue Lizenz-/Nutzungsbedingungen mit Link, Version oder archiviertem Nachweis
- getrennte Prüfung für Streaming, Download, Offline-Speicherung und Weiterverbreitung
- erforderliche Attribution im exakten Wortlaut
- erlaubte Gebiete, Nutzungsdauer und etwaige Einschränkungen
- Dateiformat, Codec, Dauer, Dateigröße und SHA-256-Prüfsumme
- Datum, Prüfer und Ergebnis der Rechteprüfung
- Freigabestatus: `PENDING`, `APPROVED` oder `REJECTED`

## Freigaberegel

Ein Offline-Paket darf nur erstellt werden, wenn **alle** Pflichtfelder belegt sind und die Rechteprüfung ausdrücklich `APPROVED` ergibt. Unbekannte oder widersprüchliche Angaben bedeuten `PENDING`, nicht Zustimmung. Eine Streaming-Freigabe darf niemals automatisch als Offline-Freigabe interpretiert werden.

## Geplanter technischer Ablauf nach Rechtefreigabe

1. Audio-Datei aus der erlaubten Originalquelle beziehen.
2. SHA-256 und Dateigröße lokal prüfen und in den Katalog eintragen.
3. Paketmanifest und Attribution mit der Datei ausliefern.
4. Downloadgröße vorab anzeigen; WLAN/mobile Daten bewusst wählen lassen.
5. Download atomar speichern; unvollständige Dateien niemals als verfügbar markieren.
6. Integrität vor Nutzung erneut prüfen.
7. Offline-Wiedergabe nur nach explizitem Tippen; keine Autowiedergabe.
8. Downloads in der App anzeigen und löschbar machen.

Bis dahin bleibt das Offline-Audio deaktiviert. Textbibliothek und Live-Wallpaper müssen unabhängig davon offline funktionieren.
