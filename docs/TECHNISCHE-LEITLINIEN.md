# NurWallpaper – verbindliche technische Leitlinien

Stand: 11.11.2026

Diese Leitlinien ergänzen das MVP-Pflichtenheft. Sie sind bei Architektur, Implementierung und Abnahme zu berücksichtigen.

## 1. Lesbarkeit und automatischer Kontrastschutz – MUST HAVE

- Hinter jedem Textbereich wird die Lesbarkeit automatisch abgesichert.
- Wenn der lokale Hintergrund ausreichend kontrastreich ist, bleibt er unverändert.
- Wenn der Hintergrund zu hell, unruhig oder kontrastarm ist, wird automatisch ein dezenter halbtransparenter Schatten-/Verlaufsschutz direkt hinter dem Text eingeblendet. Der gewünschte Stil ist weich und unaufdringlich („Vintage“-Schatten), keine harte, auffällige Box.
- Ein einfacher Textschatten darf ergänzen, ersetzt aber kein Overlay, wenn der Kontrast sonst nicht reicht.
- Schutzfläche wächst mit dem tatsächlichen Textlayout und umfasst arabischen Originaltext, Übersetzung und Quellenzeile vollständig inklusive Vokalzeichen und ausreichendem Innenabstand.
- Schutz darf bei Szenenwechseln oder Animationen nicht flackern oder vorübergehend verschwinden.
- Der Schutz ist kostenlos und kann nicht so deaktiviert werden, dass die Mindestlesbarkeit unterschritten wird.
- Zielwert: mindestens 4,5:1 für normalen Text; 3:1 nur bei tatsächlich groß dargestelltem Text. Im Zweifel gilt 4,5:1.
- Prüfung anhand des lokalen Bereichs hinter den Textzeilen, nicht anhand eines globalen Bildschirm-Durchschnittswerts.
- Abnahme mit hellen und dunklen Hintergründen, langen mehrzeiligen Texten, arabischen Vokalzeichen, allen Übersetzungs-/Quellenzeilen, Displaygrößen und Animationszuständen. Reale Android-Gerätetests bleiben erforderlich.

## 2. Kleiner App-Kern und On-Demand-Downloads

Ziel: Die Erstinstallation bleibt möglichst klein. Optionale Medien werden nicht pauschal mit der App ausgeliefert.

- Der App-Kern enthält die notwendige Anwendung, Einstellungen, Text-/Quellenmetadaten, Renderer und eine kleine robuste Standarddarstellung.
- Zusätzliche Wallpaper-Pakete bzw. größere Hintergrunddateien werden erst geladen, wenn der Nutzer sie auswählt oder ausdrücklich herunterlädt.
- Audio-Dateien werden ebenfalls nur bei ausdrücklichem Bedarf geladen; keine automatische Audio-Wiedergabe.
- Heruntergeladene Pakete werden lokal zwischengespeichert und nach Möglichkeit bei erneutem Bedarf offline verwendet.
- Vor dem Download: Dateigröße anzeigen, WLAN-/mobile-Daten-Nutzung berücksichtigen und dem Nutzer eine bewusste Auswahl ermöglichen.
- Downloads müssen fortsetzbar bzw. bei Fehlern sicher wiederholbar sein. Unvollständige Dateien dürfen nicht als verfügbar gelten.
- Integrität (z. B. Prüfsumme), Versionsstand und Lizenz-/Attributionsinformationen jedes Medienpakets werden geprüft bzw. mitgeführt.
- Nutzer können optionale Downloads und deren Cache in der App verwalten und löschen.
- Ohne Internet muss die App mit den installierten Kernfunktionen und bereits gespeicherten Inhalten nutzbar bleiben. Fehlende optionale Medien dürfen nie die Textanzeige oder das Wallpaper-Grundsystem blockieren.
- Keine fremden Bilder oder Audiodateien ohne geklärte Nutzungsrechte. Quellen und Lizenzen werden vor Bereitstellung dokumentiert.

## 3. Architekturhinweis

On-Demand-Medien sind optionale Ressourcen, keine Voraussetzung für das Live-Wallpaper. Textdarstellung, Favoriten, Rotation und Kontrastschutz müssen unabhängig von Netzwerk und optionalen Medienpaketen funktionieren. Gezeichnete lokale Standardszenen bleiben als Offline-Fallback erhalten.

## 4. Zeitgesteuerte Inhaltsrotation

Die kostenlosen Standardintervalle sind fest definiert:

- **Oft:** 30 Minuten.
- **Selten:** 3 Stunden.
- **Nie:** keine automatische Rotation.
- **Nur Favoriten:** alle 30 Minuten, ausschließlich aus dem Favoriten-Pool.

Der nächste Wechselzeitpunkt wird dauerhaft gespeichert. Ein Entsperren allein löst keinen Wechsel aus und setzt den Timer nicht zurück. Ist das Wallpaper während eines Fälligkeitszeitpunkts nicht sichtbar, wird bei der nächsten Sichtbarkeit höchstens ein Wechsel ausgeführt; danach beginnt ein neues vollständiges Intervall. Der aktive Pool wird ohne Wiederholung durchlaufen, bevor Einträge erneut erscheinen. Erweiterte Intervallsteuerung bleibt eine PRO-Funktion.

## 5. Geräte- und Herstellerkompatibilität

Ein gemeinsames Android-APK soll auf mehreren Herstellern funktionieren. Die Abnahme muss deshalb mindestens folgende Plattformfamilien berücksichtigen:

- Google Pixel / Android.
- Samsung Galaxy / One UI.
- Xiaomi / Redmi / POCO / HyperOS.
- OnePlus / OxygenOS.
- Motorola / My UX.
- OPPO und vivo.

Zu prüfen sind Auswahl und Wiederherstellung des Live-Wallpapers, Sichtbarkeit/Lifecycle, Rotation und Timer nach Standby/Neustart, Displaygrößen, RTL/Arabisch, Energieverbrauch und herstellerspezifische Hintergrundbeschränkungen. Emulatoren ersetzen keinen realen Gerätetest; nicht verfügbare Geräte werden als „nicht getestet“ dokumentiert.

## 6. Umsetzungsstatus

Diese Datei dokumentiert verbindliche Anforderungen und Architekturentscheidungen. Sie bedeutet ausdrücklich nicht, dass Downloadverwaltung, Cache, Audio oder der vollständige dynamische Kontrastschutz bereits implementiert oder getestet sind. Jede Funktion erhält erst nach Codeprüfung und Tests den Status „fertig“.
