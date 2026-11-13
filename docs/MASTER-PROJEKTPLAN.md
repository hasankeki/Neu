# NurWallpaper – Masterprojektplan und Entscheidungsgrundlage

**Stand:** 12. November 2026  
**Zweck:** Zentrale, fortlaufend zu pflegende Projektquelle. Dieses Dokument führt die belegten Entscheidungen aus den bisherigen Übergaben, dem MVP-Pflichtenheft, den Projektgesprächen und dem aktuellen Repository-Stand zusammen. Es soll verhindern, dass bereits geklärte Punkte erneut abgefragt oder bei der Umsetzung vergessen werden.

> **Arbeitsregel:** Erst das Gesamtprodukt und seine Qualitätsziele berücksichtigen, dann umsetzen. Bereits entschiedene Punkte werden nicht erneut als offene Grundsatzfragen behandelt. Keine neue Planungsschleife, wenn die vorhandene Entscheidung ausreicht. Nur echte Widersprüche oder technische Hindernisse werden gemeldet. Nichts als fertig bezeichnen, was nicht tatsächlich gebaut und geprüft wurde.

## 1. Produktvision

NurWallpaper soll ein hochwertiges, eigenständiges islamisches Android-Live-Wallpaper-Erlebnis werden – nicht bloß eine Sammlung religiöser Sprüche auf einem dunklen Hintergrund.

Das Produkt verbindet:

- beeindruckende, hochwertige Wallpaper-Motive (Natur, Nacht, Himmel, islamische Architektur und ausgewählte minimalistische Designs),
- lebendige, aber ruhige Animationen und individuelle Lichtimpulse,
- religiöse Inhalte mit korrektem arabischem Original, Übersetzung und genauer Quelle,
- eine elegante, verständliche App-Oberfläche mit individuellen Kacheln und Lichtreaktionen,
- Gebetszeiten und ausgewählte islamische App-Funktionen,
- eine hochwertige kostenlose Basis ohne Werbung.

**Qualitätsziel:** Beim Einschalten des Handys oder Öffnen der App soll das Ergebnis sofort hochwertig und besonders wirken. „Edel“ bedeutet nicht einfach Schwarz plus Gold. Es bedeutet überzeugende Bildkomposition, Lichtführung, Materialwirkung, Tiefe, Typografie, Bewegung und saubere Details. Ein ruhiges, schlichtes Motiv ist willkommen, wenn es bewusst gestaltet und hochwertig ausgeführt ist.

**Leitprinzip für religiöse Inhalte:** Die Botschaft muss lesbar bleiben. Effekte unterstützen die Gestaltung, verdrängen aber niemals den Text.

## 2. Nutzer- und Produktentscheidungen

### Plattform und Betrieb

- Android ist die erste Plattform; iPhone/iOS ist eine spätere Phase.
- Es wird ein natives Android-Live-Wallpaper gebaut, nicht nur eine PWA oder HTML-Seite.
- Offline-first: bereits installierte religiöse Inhalte, Favoriten und gespeicherte Wallpaper sollen offline nutzbar sein.
- Keine Werbung und kein Benutzerkonto als Voraussetzung.
- Die App soll auf Samsung, Google Pixel, Xiaomi und weiteren Android-Geräten zuverlässig funktionieren. Herstellerunterschiede beim Sperrbildschirm/Laufzeitverhalten müssen real getestet werden.
- Der Nutzer arbeitet primär mit einem Android-Handy und hat keinen PC/kein Android Studio. GitHub und GitHub Actions übernehmen Repository- und Build-Arbeit.

### Kosten und Freigaben

- Zusätzliches Projektbudget: **0 €** – keine kostenpflichtigen Server, APIs, Abos, bezahlten Testphasen, Billing-Aktivierungen oder automatisch kostenpflichtigen Dienste einführen.
- Keine Bereitstellung/Deployment eines Backends ohne ausdrückliche Zustimmung.
- Keine Veröffentlichung, kein Merge des Draft-PR und keine externe Freigabe ohne ausdrückliche Zustimmung.
- PRO-Funktionen sind konzeptionell beschrieben, werden aber erst nach stabiler kostenloser Basis umgesetzt. Kein Preis ist final beschlossen.
- Eine frühere 50-MB-Grenze wurde ausdrücklich aufgehoben. Keine künstliche Dateigrößenobergrenze einführen. Qualität ist wichtiger; Speicher, Ladezeit und Akku trotzdem sinnvoll optimieren, ohne sichtbaren Qualitätsverlust.

## 3. Wettbewerbsvergleich und Rezensionen

### Vergleichs-Apps, die in der Recherche genannt wurden

**App Store – dokumentierte Vergleichsliste:**
- Gallerie Wallpapers
- WallPics
- Wallcraft
- MyScreen
- Mico
- Themify
- ThemePack
- iScreen
- Silly Fun Smile Live Wallpaper
- Zedge

**Google Play – dokumentierte Vergleichsliste:**
- Zedge
- Silly Smile Wallpaper
- WallPics
- Wallcraft
- 3D Live Wallpapers
- 3D Live Wallpapers 4D
- Neon Love Theme
- Neon Clock Wallpaper
- Liquid Lock Screen: KaLock 3

Die Liste ist eine dokumentierte Auswahl aus der Recherche, keine behauptete offizielle Rangliste. Die vollständige, abgeschlossene Top-50-Auswertung ist in den verfügbaren Projektunterlagen **nicht nachgewiesen**. Deshalb dürfen keine vermeintlichen Top-50-Favoriten oder persönlichen Sieger erfunden werden.

### Aus Rezensionen festgehaltene Problemfelder

Die zusammengefassten Nutzerbeschwerden der damaligen Store-Recherche betrafen:

- zu viel bzw. störende Werbung, insbesondere Videoanzeigen,
- unklare Käufe, Abos oder Probleme nach dem Bezahlen,
- Abstürze, Verzögerungen und instabile Anwendung,
- Probleme beim Suchen, Verbinden und Herunterladen,
- Schwierigkeiten beim Anwenden von Live-Wallpapers und beim Speichern,
- Sprach-/Lokalisierungsprobleme,
- unterschiedliche Ergebnisse je nach Gerät/Hersteller.

### Konsequenzen für NurWallpaper

Diese Kritikpunkte müssen als konkrete Qualitätsanforderungen berücksichtigt werden:

1. Keine Werbung.
2. Keine überraschenden Abos, versteckten Kosten oder irreführenden Kaufabläufe.
3. Stabilität und flüssige Bedienung vor zusätzlichen Effekten.
4. Suche, Kategorien, Download und Anwenden müssen verständlich sein.
5. Klare Sprachunterstützung und korrekte Lokalisierung.
6. Kompatibilität auf mehreren Herstellern und Displaygrößen testen.
7. Offline-Verfügbarkeit für bereits geladene Inhalte und Wallpaper.
8. Konkurrenz ist Qualitätsmaßstab und Lernquelle – **nicht kopieren**. NurWallpaper braucht eine eigene visuelle Identität.

**Wichtige Dokumentationslücke:** Die verfügbaren Quellen enthalten nicht die vollständigen Original-Rezensionen, eine vollständige positive/negative Bewertungsmatrix oder die vollständige Top-50-Auswertung. Vorhandene Erkenntnisse oben sind belegt; fehlende Einzelheiten werden nicht erfunden. Die frühere Analyse soll nicht erneut als erledigt ausgegeben werden, solange die vollständigen Datensätze nicht vorliegen.

## 4. Visuelles Designsystem – verbindliche Richtung

### 4.1 Wallpaper-Motive

Das Startangebot darf nicht auf drei Motive begrenzt sein. Ziel ist eine kuratierte erste Auswahl von ungefähr **30 hochwertigen Motiven** als Startpunkt, die später wachsen kann.

Geplante Motivwelten:

- Natur: dramatische Berge, Seen, Wasserfälle, Wälder im Nebel, Sonnenuntergänge und besondere Landschaften.
- Himmel und Nacht: Sternenhimmel, Milchstraße, Polarlichter, Mond, kosmische Lichtstimmungen.
- Islamische Architektur: Moscheen, Kuppeln, Minarette, Innenhöfe, kunstvolle Details und stimmungsvolle Beleuchtung.
- Minimalistisch: hochwertige dunkle, helle, geometrische und schwarz-goldene Designs.
- Kalligrafie und islamische Muster, wenn Typografie und Quellenlage stimmen.

Wald wurde in der ursprünglichen frühen Szenenplanung einmal abgelehnt; in der späteren Designrichtung wurden neblige Wälder ausdrücklich als mögliche Natur-Highlights genannt. Für den aktuellen visuellen Katalog gilt die spätere, ausdrücklich gewünschte Motivvielfalt. Keine Motive mit Kreuzen oder kreuzähnlichen Aufsätzen/Ornamenten an Moscheen. Architektur vor Verwendung kontrollieren.

### 4.2 Bildqualität

- Bilder sollen filmisch, detailreich und visuell eindrucksvoll sein – nicht einfache Farbverläufe oder generische Silhouetten als Ersatz für echte Motive.
- Unterschiedliche Bildstimmungen statt eines einzigen Schwarz-Gold-Schemas.
- KI-generierte Motive sind als mögliche eigene Assets vorgesehen; Nutzungsrechte, mögliche Rechte Dritter und rechtliche Grenzen sind je Asset zu beachten. Keine Exklusivität oder urheberrechtliche Schutzfähigkeit versprechen.
- Bildbibliothek, Lizenz-/Herkunftsdaten und Attributionen sauber verwalten.
- Keine starre 50-MB-Grenze. Automatische kleine Erstauswahl darf über WLAN aktualisiert werden; zusätzliche Wallpaper nur auf Nutzerwunsch herunterladen. Mobile Daten nicht ungefragt nutzen.

### 4.3 Live-Animationen

- Wallpaper ist eine hochwertige Bildgrundlage mit unabhängig animierten Ebenen; nicht einfach ein Video als Ersatz für ein echtes Live-Wallpaper.
- Tiefe/Parallax, Lichtstrahlen, Nebel/Wolken, dezente Partikel und atmosphärische Bewegung sollen zusammenwirken.
- Sternenstaub: winzige, kaum wahrnehmbare Lichtpartikel, die langsam wie im Wind driften. Keine großen klar erkennbaren Punkte, kein hektisches Fliegen.
- Animation muss ruhig, flüssig und hochwertig wirken. Bewegungen sollen nicht vom religiösen Text ablenken.
- Lichtimpulse und Effekte dürfen je Motiv/Farbwelt variieren; nicht überall dieselbe goldene Linie.
- Animationen müssen pausierbar bzw. in den Einstellungen abschaltbar sein; Lesbarkeitsschutz bleibt aktiv.

### 4.4 Kacheln und App-Oberfläche

- Kacheln sollen individuell gestaltet sein, nicht alle identisch aussehen.
- Lichtimpulse können von den Kanten einer Kachel ausgehen, sich über die Oberfläche ausbreiten und wieder abklingen.
- Farbwelten, die der Nutzer ausdrücklich als Beispiele genannt hat: Violett/Lila und Smaragdgrün; Gold ist eine weitere Gestaltungsrichtung, nicht die einzige.
- Der Effekt soll wie ein gezielter Impuls wirken, nicht wie dauerhaft leuchtender Rahmen oder blinkende Neon-Deko.
- Kacheln sollen hochwertig, klar, aufgeräumt und konsistent sein. Das genaue Verhalten muss sichtbar getestet werden, nicht nur textlich beschrieben.

### 4.5 Stilrichtungen

- „Golden Flow“, „Emerald Glow“ und „Silver Light“ wurden als mögliche Designrichtungen festgehalten.
- Sie sind keine starre Vorgabe, jedes Element gold/smaragd/silbern zu färben. Die Farbe muss zum jeweiligen Motiv und Element passen.
- Schlichte Muster sind erlaubt, wenn sie hochwertig wirken. „Schlicht“ ist nicht gleich „billig“ oder „unfertig“.
- Moschee-Motive müssen sauber islamisch wirken; keine Kreuze oder kreuzähnlichen Details.

## 5. Wallpaper-Auswahl und Rotation

- Hintergrundrotation ist von der Rotation der Verse/Hadithe/Duas unabhängig.
- Nutzer kann einen Vers behalten, während sich das Hintergrundbild ändert.
- Hintergrundauswahl und Inhaltsauswahl haben getrennte Einstellungen und Zeitpläne.
- Wechselintervalle für Hintergründe: **Oft = alle 30 Minuten**, **Selten = alle 3 Stunden**, **Nie = kein automatischer Wechsel**.
- Favoriten sollen regelmäßig in der Rotation berücksichtigt werden.
- Kein Hintergrund wird erneut gezeigt, bevor der aktive Hintergrund-Pool einmal durchlaufen wurde.
- Rotation ist zeitbasiert, nicht durch jedes Entsperren ausgelöst.
- Nächsten Wechselzeitpunkt speichern; nach Standby höchstens einen fälligen Wechsel anwenden, nicht mehrere Wechsel nachholen.
- Smart-Auswahl nach Tageszeit/Favoriten war als Verbesserung vorgesehen.
- Zusätzliche Wallpaper nur bei Nutzerwunsch laden; kleine Grundauswahl darf über WLAN aktualisiert werden.

## 6. Religiöse Inhalte und Sprachen

### Hauptbereiche

1. Verse (Koran)
2. Hadithe
3. Duʿāʾ & Dhikr
4. Esmaül Hüsna

### Sprachen

- Deutsch, Türkisch, Englisch und Arabisch zum Start.
- Bei Auswahl Arabisch wird nur der arabische Text angezeigt.
- Weitere Sprachen, darunter Französisch, sind spätere Erweiterungen.
- Arabische RTL-Darstellung, Vokalzeichen, Schrift, Zeilenumbrüche und Quellenzeilen müssen sorgfältig geprüft werden.

### Quellenstandard

- Offizielle Hadith-Quellenbasis: ausschließlich Ṣaḥīḥ al-Bukhārī und Ṣaḥīḥ Muslim. Tirmidhi und Ibn Majah werden nicht in die offizielle Bibliothek aufgenommen.
- Jeder Vers, Hadith, jedes Duʿāʾ und jeder Dhikr braucht überprüfbare Originalquelle, genaue Fundstelle, geprüften Wortlaut und verlässliche Authentizitätsangabe.
- Übersetzungen und Umschriften brauchen eigene Rechte-/Lizenz- und Attributionsangaben.
- Keine unbelegten Wiederholungszahlen oder besonderen Wirkungsversprechen.
- Nicht verifizierte Inhalte werden nicht veröffentlicht.
- Qurantext: festgelegte Tanzil-Uthmani-Ausgabe mit korrekter Attribution; Übersetzungen werden pro Sprache/Ausgabe/Lizenz geprüft.
- In der App gibt es einen Bereich „Quellen und Lizenzen“.

### Bibliotheksstand (12.11.2026)

- 144 Datensätze importiert und redaktionell ausgewählt: 27 Hadithe, 14 Verse, 4 Dhikr, 99 Esmaül Hüsna.
- „CONFIRMED“ bedeutet redaktionell ausgewählt/katalogisiert, **nicht** automatisch zur Veröffentlichung freigegeben.
- Nur Datensätze mit `APPROVED_FOR_RELEASE` und vollständigen Sprach-/Quellen-/Lizenz-/Attributions-/Prüfangaben dürfen in die Produktionsausgabe.
- Im letzten dokumentierten Stand waren 3 Hadithe technisch freigegeben; 24 Hadithe, 14 Verse, 4 Dhikr und 99 Asma waren noch gesperrt, weil erforderliche Übersetzungsrechte, Quellen-/Attributionsdaten oder menschliche Prüfung fehlten.
- Freigegebene Hadithe laut letztem Status: Bukhari 6306 (Sayyid al-Istighfar), Muslim 713a (Moschee betreten/verlassen), Muslim 598a–b (Eröffnungs-Duʿāʾ; parallele Formulierung Bukhari 744).
- Keine fehlenden Rechte-/Lizenzfelder durch Platzhalter oder Vermutungen füllen.

## 7. Gebetszeiten, Qibla, Benachrichtigungen und Audio

### Gebetszeiten

- Gebetszeiten gehören zum kostenlosen Funktionsumfang.
- Diyanet Deutschland ist Standard-Berechnungsmethode; andere Methoden optional auswählbar.
- Gebetserinnerungen sind standardmäßig ausgeschaltet und einzeln aktivierbar.
- Keine zusätzliche Uhr als unnötiges Element.

### Qibla

- Qibla-Kompass nur innerhalb der App.
- Kein Home-Screen-Widget.

### Benachrichtigungen

- Gebetszeiten und täglicher Vers können eingestellt werden.
- Bei Aktivierung täglicher Vers: eine tägliche Vers-Benachrichtigung.
- Erinnerungen bleiben standardmäßig aus, bis der Nutzer sie selbst aktiviert.

### Audio

- Quran-Rezitation nur online streamen; keine Audio-Dateien müssen für Offline-Nutzung enthalten sein.
- Qurantexte, Übersetzungen, gespeicherte Wallpaper und Favoriten bleiben offline nutzbar.
- Wiedergabe nur nach ausdrücklichem Antippen und nur für den aktuell angezeigten Vers/gewählten Ayah; keine automatische Wiedergabe.
- Kein 20-Sekunden-Vorschauclip.
- Sechs vorgemerkte Rezitatoren: Maher Al-Muaiqly, Ali Al-Hudhaify, Muhammad Ayyub, Khalid Al-Muhanna, Ibrahim Al-Akhdar, Abdullah Al-Juhany.
- Yasser Al-Dosari und Badr Al-Turki sind Wunschliste, nicht automatisch freigegeben.
- Rechte für Streaming und Offline-Weiterverbreitung getrennt prüfen. Der dokumentierte Rechte-Status der sechs Rezitatoren war noch PENDING; Audio bleibt bis zur Freigabe deaktiviert.
- Keine bezahlte Streaming-Infrastruktur oder Backend-Bereitstellung ohne Zustimmung; kostenlose, tragfähige Lösung erforderlich.

## 8. Free-/PRO-Modell, Speichern und Teilen

### Kostenlos (Free)

- Hochwertige Grundgestaltung, Lichtspiele, Sternenbewegung und Neon-Schweif.
- Gebetszeiten.
- Alle religiösen Inhalte und alle vier Hauptbereiche.
- Favoriten und dynamischer Kontrastschutz.
- Offline-Nutzung bereits installierter Inhalte und gespeicherter Wallpaper.
- Keine Werbung.

### PRO – erst nach stabiler Free-Basis

- Erweiterte Steuerung von Lichtfarbe und Lichtintensität.
- Erweiterte Steuerung von Zeitpunkt und Häufigkeit des Inhaltswechsels.
- Zusätzliche Schriften und erweiterte Animationssteuerung.
- Eigene Fotos als Hintergrund mit Abdunklungsregler und Textpositionierung.
- Preis und Abrechnung sind nicht final beschlossen; keine Billing-Integration oder Kosten auslösen, bevor ausdrücklich freigegeben.

### Teilen und Speichern

- Teilen ist für alle kostenlos und soll NurWallpaper erkennbar machen.
- Kostenloses Speichern in komprimierter Form mit NurWallpaper-Logo.
- PRO kann maximale Qualität ohne Logo speichern.
- Export/Teilen war in einer späteren MVP-Kurzfassung als nicht zwingend für den ersten funktionsfähigen Build eingeordnet; die vom Nutzer bestätigte Produktentscheidung zum kostenlosen Teilen/Speichern bleibt für die Gesamtplanung erhalten. Umsetzung kann nach stabilem Grundbuild erfolgen.

## 9. Technische Architektur und Arbeitsweise

- Native Android-Implementierung mit Kotlin und `WallpaperService`, `Canvas`, Textlayout und lokalen Daten.
- GitHub-Repository: `hasankeki/Neu`.
- Arbeitsbranch: `feature/free-foundation-golden-flow`.
- Draft PR #1: `Build NurWallpaper free foundation with Golden Flow`.
- GitHub Actions baut Android Debug APK und führt Backend-/Projektchecks aus.
- Nutzer kann die APK über GitHub Actions laden und auf Android testen; der Nutzer soll nicht mit langen manuellen Code-Anleitungen belastet werden.
- HTML-Vorschauen dienen ausschließlich zur visuellen Rückmeldung und ersetzen weder den nativen Android-Build noch den Gerätetest.
- Kein Backend-Deployment ohne explizite Zustimmung.
- Kein Merge und keine Veröffentlichung ohne explizite Zustimmung.

## 10. Tatsächlicher technischer Stand (letzter dokumentierter Stand)

- Native Android-Grundlage und GitHub-Actions-Workflows existieren.
- `NurWallpaperService.kt` zeichnet aktuell eine dunkle Grundgestaltung, goldene Lichtkurven/Golden Flow, Halo und ungefähr 52 Partikel; Animationsschleife läuft nur bei sichtbarem Wallpaper und aktivierten Lichteffekten.
- Anzeige von arabischem Original, ausgewählter Übersetzung und Quellenzeile ist vorhanden.
- Inhaltsrotation und Favoritenlogik sind teilweise implementiert; Wechsel-/Zeitlogik wurde später präzisiert und muss mit der verbindlichen Intervallregel abgeglichen werden.
- Dynamischer Kontrastschutz wurde als MUST HAVE festgelegt; seine native Implementierung und reale Abnahme sind nicht als abgeschlossen nachgewiesen.
- Echte hochwertige Wallpaper-Bildbibliothek, unabhängige Hintergrundrotation, Parallax-/Tiefenebenen und die vielfältigen Natur-/Nacht-/Moschee-Motive sind noch nicht vollständig implementiert.
- Die individuellen Impulsanimationen der App-Kacheln sind noch nicht umgesetzt.
- Quran Foundation Streaming-Anbindung ist nicht deployed und nicht live getestet; Audio-Rechte sind offen.
- Zuletzt dokumentierter Inhaltsstand: 144 Datensätze, 3 technisch freigegeben.
- Frühere CI-Läufe waren erfolgreich; nach dem letzten Content-Dokumentationscommit muss der neueste Workflow-Status erneut geprüft werden.
- HTML-Vorschauen 01/02 waren unzureichend und wurden vom Nutzer kritisiert: zu wenige Motive, zu schnelle Sterne, fehlerhaftes Wischverhalten, fehlende Eleganz/Qualität. Sie gelten nicht als Designabnahme und nicht als Grundlage, die Qualitätsziele herabzusetzen.

## 11. Test- und Abnahmekriterien

Vor einem vollständigen Testbuild müssen die vereinbarten Funktionen zusammen testbar sein. Keine künstlichen Teil-APKs als fertiges Ergebnis ausgeben.

### Visuell

- Mehr als drei überzeugende Motive; Startauswahl zielt auf ca. 30 hochwertige Motive.
- Jede Motivwelt wirkt bewusst gestaltet und unterscheidet sich sichtbar.
- Sternenstaub sehr langsam und subtil; keine hektischen Partikel.
- Lichtimpulse an Kachelkanten, individuell und farblich passend.
- Keine Kreuze/kreuzähnlichen Moschee-Details.
- Wischen/Wechseln darf Oberfläche oder Animation nicht beschädigen.
- Bewegungen bleiben ruhig und flüssig.

### Inhalt/Lesbarkeit

- Arabisch, DE/TR/EN, RTL, Vokalzeichen, Zeilenumbrüche und Quellen vollständig testen.
- Dynamischer Kontrastschutz in hellen, dunklen und unruhigen Bildbereichen prüfen.
- Keine nicht freigegebenen religiösen Datensätze anzeigen.

### Funktion/Technik

- Separate Inhalts- und Hintergrundrotation.
- Intervalle, Favoriten und keine Wiederholung vor vollständigem Pooldurchlauf.
- WLAN-Update der kleinen Grundauswahl; weitere Downloads nur auf Wunsch.
- Offline-Verhalten, Speicher, Ladezeit und Akku prüfen.
- Auf Samsung, Pixel und Xiaomi sowie verschiedenen Bildschirmgrößen testen.
- Android-Live-Wallpaper auf Sperrbildschirm und Homescreen prüfen; Herstellerverhalten dokumentieren.
- Kein Absturz, kein kaputtes Wischen, keine fehlerhafte Rotation.

### Freigabe

- Erfolgreicher Build ist nicht gleich erfolgreicher Gerätetest.
- Erst nach tatsächlicher Installation und Prüfung als getestet bezeichnen.
- Keine Veröffentlichung oder Merge ohne Nutzerfreigabe.

## 12. Aktuelle Prioritäten – ohne erneute Grundsatzdiskussion

1. Diese Mastergrundlage als zentrale Referenz sichern und in Library sowie GitHub pflegen.
2. Bestehende Projektentscheidungen und Code gegen dieses Dokument abgleichen; Abweichungen sichtbar machen.
3. Visuelles Gesamtsystem umsetzen: hochwertige Motive, eigene Licht-/Impulslogik, Kachelanimationen und ruhige atmosphärische Bewegung.
4. Wallpaper-Bibliothek und unabhängige Hintergrundrotation integrieren.
5. Free-Grundfunktionen vollständig zusammenführen und stabilisieren.
6. Dynamischen Kontrastschutz und religiöse Quellen-/Lizenz-Gates vollständig umsetzen.
7. CI prüfen, vollständigen Debug-Build erzeugen und auf realen Android-Geräten testen.
8. PRO, Audio und weitere Erweiterungen erst gemäß den bereits festgelegten Bedingungen und ohne zusätzliche Kosten angehen.

## 13. Dokumentationsstatus und Lücken

Die ursprüngliche lange Planungsrunde mit mehr als 50 Fragen ist als Tatsache aus dem Projektverlauf bekannt. Die vollständigen einzelnen Fragen und Antworten konnten in den derzeit verfügbaren Übergabe- und Pflichtenheftdateien jedoch nicht vollständig wiederhergestellt werden. Dieses Dokument enthält alle daraus wiederauffindbaren Entscheidungen, ohne Antworten zu erfinden.

Ebenso ist eine Top-10-Auswahl und eine spätere Top-50-Idee dokumentiert; die vollständige Top-50-Auswertung und eine vollständige Original-Rezensionsmatrix sind nicht nachgewiesen. Die belegten App-Listen und Kritikpunkte sind in Abschnitt 3 festgehalten. Fehlende Details sind als Dokumentationslücke markiert und dürfen nicht als bereits erledigte Analyse ausgegeben werden.

Der Nutzer hat ausdrücklich klargemacht: Er möchte nach der langen Planung sichtbare, testbare Ergebnisse und nicht immer wieder neue Erklärungen, Entschuldigungen oder erneute Grundsatzfragen. Die Projektverantwortung bedeutet, den Gesamtplan, die Qualität, die Rezensionserkenntnisse, die Technik und die offenen Abhängigkeiten gleichzeitig im Blick zu behalten.

---

## Quellen innerhalb des Projektarchivs

- `NurWallpaper-Uebergabe.md` (03.11.2026)
- `NurWallpaper-Uebergabe-v1.1.md` (09.11.2026)
- `NurWallpaper-MVP-Pflichtenheft-v0.3.md` (10.11.2026)
- `docs/CONTENT-IMPORT-STATUS.md` im Repository (12.11.2026)
- Projektgespräche vom 08.–12.11.2026 und die daraus festgehaltenen Nutzerentscheidungen.

**Pflege-Regel:** Neue Entscheidungen werden mit Datum und Status ergänzt. Frühere Entscheidungen werden nicht stillschweigend überschrieben; Änderungen bekommen einen kurzen Änderungsvermerk mit Datum und Grund.

## 11. Arbeitsstand – Designlabor 01 (12.11.2026)

- Erste interaktive Designprobe erstellt: `docs/design/Designlabor-01.html`.
- Enthält fünf auswählbare Bildszenen aus den geplanten Motivwelten (Polarlicht, Moschee-Innenhof, Milchstraße, Alpenpanorama, beleuchtetes Minarett), eine sechste Galerie-Kachel als ruhige Kosmos-Variante sowie vier unterschiedliche Kachel-Impulse (Emerald Pulse, Violet Aura, Golden Flow, Silver Mist).
- Lichtimpulse werden durch Antippen ausgelöst und laufen von der Kantenlinie aus; Farbpalette ist umschaltbar.
- Partikel sind bewusst sehr klein, spärlich und langsam; Animation kann pausiert oder weiter beruhigt werden. Wischen ist ausschließlich innerhalb des Wallpaper-Vorschaubereichs aktiv; Pfeile bieten eine alternative Bedienung.
- Die Bildbeispiele stammen aus Unsplash und sind als Designreferenzen gekennzeichnet. Vor einer Produktionsaufnahme sind Bildqualität, konkrete Lizenz-/Attributionsdaten und die Eignung jedes Motivs separat zu prüfen.
- HTML-/JavaScript-Syntax wurde lokal geprüft. Die Datei ist in der ChatGPT-Dateibibliothek unter `/NurWallpaper/Designlabor-01.html` gesichert.
- Status: **Designprobe, keine finale Gestaltung und keine Android-Integration**. Nächster Schritt ist Feedback anhand dieser sichtbaren Probe und gezielte Überarbeitung – keine neue Grundsatz-Planungsrunde.
