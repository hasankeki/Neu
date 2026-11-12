# NurWallpaper

Android-Live-Wallpaper-App mit einer offline nutzbaren Bibliothek religiöser Inhalte.

## Inhaltsbereiche
- Koranverse
- Hadithe
- Du'a und Dhikr
- Esmaül Hüsna

## Grundprinzipien
- Offline-first, ohne Benutzerkonto und ohne Werbung
- Favoriten kostenlos
- Inhaltswechsel nach einstellbarem Intervall
- Dynamischer Kontrastschutz
- Quellen- und Rechteprüfung vor Veröffentlichung
- Audio nur nach ausdrücklichem Tippen; keine automatische Wiedergabe

## Projektstatus (12.11.2026)
- Android-Debug-Build auf dem Entwicklungszweig erfolgreich; APK-Artefakt wird von GitHub Actions erzeugt.
- Backend-Tests erfolgreich.
- Die Audioplayer-Wiedergabe nach Track-Ende wurde korrigiert.
- Die Android-Build-Prüfung läuft auch auf dem Entwicklungszweig.
- Ein echter Test auf Pixel, Samsung und Xiaomi steht noch aus.
- Die lokale Inhaltsbibliothek ist derzeit leer. Inhalte werden erst nach vollständiger Quellen-, Übersetzungs- und Rechteprüfung freigegeben.
- Quran-Foundation-Audio ist noch nicht live verbunden: kein Backend deployed, keine Zugangsdaten konfiguriert, keine Rezitatoren freigeschaltet.
- Offline-Audio ist ebenfalls noch nicht implementiert. Die Rechte- und Paketfreigabe ist in [docs/AUDIO-REZITATOREN-RECHTEPRUEFUNG.md](docs/AUDIO-REZITATOREN-RECHTEPRUEFUNG.md) dokumentiert.

## Kosten- und Freigaberegel
Entwicklung und Betrieb müssen bei **0 € Zusatzkosten** bleiben. Keine Zahlungsdaten, keine Billing-Aktivierung, keine kostenpflichtigen Dienste oder automatisch kostenpflichtigen Testangebote. Wenn eine Funktion nicht dauerhaft kostenlos umsetzbar ist, bleibt sie deaktiviert.

Änderungen bleiben auf dem Entwicklungszweig. Kein Merge und keine Veröffentlichung ohne ausdrückliche Zustimmung.
