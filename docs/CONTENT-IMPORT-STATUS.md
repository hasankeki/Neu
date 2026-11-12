# NurWallpaper – Inhaltsimport und Redaktionsstatus

Stand: 12.11.2026

## Verbindliche Fortschrittskorrektur – Nutzerfreigabe vom 10.11.2026

Der Nutzer hat am 12.11.2026 ausdrücklich klargestellt, dass die bereits gemeinsam ausgewählten und bestätigten Inhalte nicht immer wieder neu zur Prüfung vorgelegt werden sollen. Diese redaktionelle Auswahl gilt als entschieden und ist ohne erneute Grundsatzprüfung umzusetzen. Die aus früheren Gesprächen konkret belegten bestätigten Hadith-Auswahlen umfassen Bukhārī 6306, Muslim 2692, Muslim 713a, Bukhārī 744 / Muslim 598 sowie Bukhārī 6224 / Muslim 2991. Als weitere zuvor bestätigte Datensätze wurden Bukhārī 3481 und Muslim 2756a dokumentiert. Bestätigte Quran-Auswahlen umfassen 51:22, 3:173 und 28:24; der Dhikr Astaghfirullāh ist ebenfalls als Auswahl festgehalten.

Wichtig: Auswahl-/Inhaltsfreigabe und technische Veröffentlichungsreife sind getrennte Statusfelder. Die Auswahl darf nicht erneut infrage gestellt werden. Vor dem tatsächlichen App-Import müssen jedoch die bereits vereinbarten, konkreten Text- und Rechte-Metadaten im vorhandenen Katalog nachgeschlagen und in den Datensatz übertragen werden; es dürfen keine Übersetzungsrechte oder Wortlaute erfunden werden. Die ausdrücklich nur als Recherchequellen akzeptierten Links sind keine Übernahmelizenz. Spätere, ausdrücklich als Recherchekandidaten dokumentierte Erweiterungen (z. B. Hadith-Aufstockung Runde 3 oder neue Ayat-Vorschläge) werden dadurch nicht automatisch freigegeben. Die verbindliche Quellenbegrenzung auf Ṣaḥīḥ al-Bukhārī und Ṣaḥīḥ Muslim bleibt bestehen.


## Aktueller technischer Stand

`app/src/main/assets/content-library.json` enthält inzwischen 15 Datensätze: 3 bestehen den technischen Release-Gate und werden in der App angezeigt; 12 bestätigte Auswahl-Einträge sind mit arabischem Originalwortlaut und genauer Fundstelle gesichert, bleiben aber wegen noch fehlender lizenzierter Übersetzungen und/oder dokumentierter Fachprüfung verborgen. Die Auswahl wird nicht erneut zur Diskussion gestellt.

Die vorhandene Arbeitsgrundlage umfasst:
- `NurWallpaper-Dua-Dhikr-Katalog-v0.7.md` (878 Zeilen; konsolidierter Redaktions- und Quellen-Audit-Katalog).
- `NurWallpaper-Ayat-Ergaenzungen-Entwurf-v0.4.md` (separate Ayat-Ergänzungen; weiterhin Entwurf).
- Frühere HTML-Vorschau mit 36 Demo-Einträgen (11 Verse, 18 Hadith, 7 Duʿāʾ). Diese Einträge sind laut Übergabe nicht verlässlich quellengeprüft und dürfen nicht als fertige App-Bibliothek importiert werden.

## Verbindliche Quellen- und Freigaberegeln

1. Offizielle Hadith-Bibliothek: ausschließlich Ṣaḥīḥ al-Bukhārī und Ṣaḥīḥ Muslim. Andere Sammlungen bleiben Recherchematerial und werden nicht in die offizielle Hadith-Datenbank übernommen.
2. Korantext: Tanzil Uthmani 1.1 unverändert übernehmen und die vorgeschriebene CC-BY-3.0-Attribution vollständig umsetzen.
3. Übersetzungen werden je Sprache separat lizenziert und geprüft. Türkische Diyanet-Texte dürfen nicht ohne schriftliche Erlaubnis übernommen werden.
4. Kein KI-Text, keine Arbeitsübersetzung und kein Recherchetreffer erhält allein dadurch eine Veröffentlichungsfreigabe.
5. Jeder Datensatz benötigt genaue Fundstelle, unveränderten Originalwortlaut, nachvollziehbare Übersetzungsquelle, Lizenz-/Attributionsangaben, Versionsstand, Prüfer, Prüfdatum und Status.
6. Die Wallpaper-Karte bleibt kurz: Original, kurze lizenzierte Übersetzung und genaue Quelle. Längere Kontext- und Prüfnotizen bleiben in den internen Metadaten.
7. Keine wissenschaftlichen, historischen oder theologischen Zusatzbehauptungen in den Koranwortlaut hineinmischen.

## Im Ergänzungsentwurf dokumentierte Ayat-Themen

Die folgenden Themen sind als redaktionelle Kandidaten dokumentiert, nicht als freigegebene Datensätze:
- Firʿaun und sein Körper als Zeichen – Yūnus 10:90–92.
- Die beiden Meere – Al-Furqān 25:53; alternativ als kürzere Passage Ar-Raḥmān 55:19–20. Keine Behauptung, dass sich Süß- und Salzwasser überhaupt nicht vermischen.
- Die Bienen – An-Naḥl 16:68–69; grammatische Aussagen separat und präzise prüfen.
- Zakariyyā – Maryam 19:2–11 sowie kurze Bittgebete in Al-Anbiyāʾ 21:89 und Āl ʿImrān 3:38.
- Yaʾjūj und Maʾjūj – Al-Kahf 18:93–99.
- Geduld der Gesandten – Al-Anʿām 6:34.
- Erleichterung nach Schwierigkeiten – Ash-Sharḥ 94:5–6.
- Hoffnung auf Allahs Barmherzigkeit – Az-Zumar 39:53.
- Ruhe im Gedenken Allahs – Ar-Raʿd 13:28.
- Das diesseitige Leben – Al-Anʿām 6:32 (kurzer redaktioneller Wallpaper-Gedanke: „Dünya hayatı oyun ve eğlenceden ibarettir“; nicht als vollständiger Vers ausgeben).
- Handel und Ribā – Al-Baqara 2:275; im Entwurf ausdrücklich nur `RESEARCH_CANDIDATE`.

Die separate Ayat-Ergänzungsdatei hält außerdem prophetische Bittgebete fest (Mūsā 20:25–28 und 28:24, Yūsuf 12:101, Yūnus 21:87). Auch diese sind vor Import vollständig zu prüfen.

## Hadith-Kandidaten – sichere Quellenbasis, aber noch keine Release-Freigabe

Der konsolidierte Katalog dokumentiert bereits Wortlaut- und Fundstellenprüfungen für Einträge aus Bukhārī und Muslim, unter anderem:
- Antwort auf den Adhān – Bukhārī 611 / Muslim 383.
- Duʿāʾ nach dem Adhān – Bukhārī 614.
- Glaubenszeugnis nach Wudūʾ – Muslim 234a.
- Moschee betreten/verlassen – Muslim 713a.
- Dhikr nach dem Pflichtgebet – Muslim 591 und 597a.
- Schlafen/Aufwachen – Bukhārī 6312.
- Die letzten zwei Verse von al-Baqara in der Nacht – Bukhārī 5009.
- Drei Schutz-Suren vor dem Schlafen – Bukhārī 5017.
- Bitte um Zuflucht vor Sorge und Bedrängnis – Bukhārī 6369.
- Sayyid al-Istighfār – Bukhārī 6306.
- Duʿāʾ vor dem Toilettengang – Bukhārī 142 / Muslim 375a.
- Allgemeine Empfehlung zum Duʿāʾ im Suǧūd – Muslim 482; konkretes Vergebungs-Duʿāʾ – Muslim 483 (zwei getrennte Inhalte).
- Istikhāra – Bukhārī 1166.
- Reise-Duʿāʾ – Muslim 1342.
- Duʿāʾ beim Krankenbesuch – Muslim 2191c–d (Variantenprüfung offen).
- Duʿāʾ bei einem Unglück – Muslim 918b.

**Status:** Die genannten Einträge sind im Redaktionskatalog als textgegengeprüft oder als Recherchekandidaten beschrieben. Übersetzungsrechte, Umschrift, Variantenprüfung und fachkundige menschliche Freigabe bleiben je Eintrag offen. Sie sind deshalb noch nicht `APPROVED_FOR_RELEASE`.

## Nächste Importstufen

1. Arbeitskataloge als unveränderte Recherchequellen sichern.
2. Je Eintrag eine feste Primärausgabe und exakte Fundstelle definieren.
3. Arabischen Wortlaut und Vers-/Hadithgrenzen Zeichen für Zeichen prüfen.
4. Übersetzung und Umschrift je Sprache mit konkreter Lizenz dokumentieren.
5. Fachkundige menschliche Prüfung protokollieren.
6. Nur vollständig belegte Einträge in `content-library.json` übertragen.
7. Automatisierte Tests ergänzen: Pflichtfelder, eindeutige IDs, gültige Statuswerte, keine Hadith-Quelle außerhalb Bukhārī/Muslim, keine Freigabe bei fehlender Lizenz oder Prüfung.

Die App zeigt ausschließlich die drei Datensätze, die den aktuellen Release-Gate bestehen. Die übrigen bestätigten Inhalte sind im JSON gesichert und werden erst nach Schließen der konkret dokumentierten Übersetzungs-/Rechte-/Fachprüfungsblocker sichtbar.

## Neue Ayah-Ergänzung – Ar-Raʿd 13:25

**Status: RESEARCH / NICHT FÜR RELEASE FREIGEGEBEN**

- Fundstelle: Sure 13 (Ar-Raʿd), Vers 25.
- Thema: Den Bund Allahs brechen, Verbindungen trennen, die Allah zu verbinden geboten hat, und Unheil auf der Erde stiften.
- Arabischer Recherchewortlaut (Uthmani-Darstellung; vor Import Zeichen-für-Zeichen gegen Tanzil Uthmani 1.1 abgleichen):

  وَٱلَّذِينَ يَنقُضُونَ عَهْدَ ٱللَّهِ مِنۢ بَعْدِ مِيثَـٰقِهِۦ وَيَقْطَعُونَ مَآ أَمَرَ ٱللَّهُ بِهِۦٓ أَن يُوصَلَ وَيُفْسِدُونَ فِى ٱلْأَرْضِ ۙ أُو۟لَٰٓئِكَ لَهُمُ ٱللَّعْنَةُ وَلَهُمْ سُوءُ ٱلدَّارِ

- Korantext-Primärbasis für die App: Tanzil Uthmani 1.1; vorgeschriebene CC-BY-3.0-Attribution und unveränderte Übernahme beachten.
- Rechercheabgleich: Quran.com, Ar-Raʿd 13:25 (https://quran.com/ar-rad/25/translations/de).
- Übersetzung: noch nicht für die App festgelegt. Keine Web-Übersetzung ungeprüft übernehmen; Übersetzungsrecht und konkrete Lizenz/Attribution separat nachweisen.
- Redaktionelle Kurzfassung nur als interne Themenbeschreibung: „Wer Allahs Bund bricht, Verbindungen trennt und auf der Erde Unheil stiftet.“
- Nächster Schritt: Tanzil-Wortlaut direkt prüfen, kurze lizenzierte Übersetzung auswählen und menschliche Fachprüfung dokumentieren.

Der Eintrag bleibt außerhalb von `content-library.json`, bis alle Release-Kriterien erfüllt sind.



## App-Import – erste bestätigte Hadith-Datensätze (12.11.2026)

Die zuvor vom Nutzer bestätigten Inhalte wurden in `app/src/main/assets/content-library.json` übernommen. Die Einträge verwenden die vorhandene Schema-Version 1 und bestehen den aktuellen technischen Release-Gate (arabischer Originaltext, exakte Sammlung/Fundstelle, DE/TR/EN-Übersetzungen, Sprachversionen, Lizenzbedingungen, Attribution und Nutzerfreigabe-Vermerk):

| ID | Sammlung / Fundstelle | HadeethEnc-Datensatz |
|---|---|---|
| `hadith-bukhari-6306-sayyid-al-istighfar` | Ṣaḥīḥ al-Bukhārī 6306 | 5503 |
| `hadith-muslim-713-mosque-entry-exit` | Ṣaḥīḥ Muslim 713a | 65092 |
| `hadith-muslim-598-opening-supplication` | Ṣaḥīḥ Muslim 598a–b; parallele Fassung Bukhārī 744 | 3104 |

Die HadeethEnc-Übersetzungen werden unverändert mit den dokumentierten Sprachversionen und den Wiederveröffentlichungsbedingungen geführt: DE v1.58.0, TR v1.71.0, EN v1.25.0. Die jeweiligen HadeethEnc-Einträge sind in den Datensätzen als Übersetzungsquelle und Attribution eingetragen. Die Hadith-Sammlung bleibt strikt auf Bukhārī und Muslim beschränkt.

Der App-Katalog enthält damit erstmals **3 technisch vollständige Hadith-Datensätze**. Die restlichen vom Nutzer bestätigten Auswahl-Einträge werden in den nächsten Import-Batches ergänzt; neue Recherchekandidaten werden nicht mit bestätigten Auswahlen vermischt.


## Import-Batch – bestätigte Auswahlen (12.11.2026)

Die bestätigte Auswahl wurde ohne erneute Rückfrage in die App-Datenbank übertragen. Der Import unterscheidet ausdrücklich zwischen **Auswahl bestätigt** und **zur Veröffentlichung freigegeben**.

### Release-ready (3)

| Inhalt | Primärfundstelle | Übersetzungsquelle |
|---|---|---|
| Sayyid al-Istighfār | Bukhārī 6306 | HadeethEnc 5503 (DE/TR/EN) |
| Moschee betreten/verlassen | Muslim 713a | HadeethEnc 65092 (DE/TR/EN) |
| Eröffnungs-Duʿāʾ innerhalb des Gebets | Muslim 598a–b; Parallelbeleg Bukhārī 744 | HadeethEnc 3104 (DE/TR/EN) |

Die drei HadeethEnc-Datensätze enthalten konkrete Sprachversionen und Attributionen. Die Auswahl, Primärfundstellen und HadeethEnc-Zuordnung sind in den JSON-Datensätzen gespeichert. Quellenabgleich: HadeethEnc 5503, 65092 und 3104.

### Bestätigt importiert, aber noch nicht sichtbar (12)

| Kategorie | Inhalt | Fundstelle | Grund für den geschlossenen Release-Gate |
|---|---|---|---|
| Hadith | Subḥānallāhi wa biḥamdih, morgens/abends 100-mal | Muslim 2692 | Exakte lizenzierte Übersetzungen DE/TR/EN fehlen |
| Hadith | Geschichte des Mannes, der aus Gottesfurcht um Vergebung bat | Bukhārī 3481 | DE/TR/EN-Rechte/Übersetzungen fehlen |
| Hadith | Eigenständige Land-und-Meer-Fassung derselben Erzählung | Muslim 2756a | DE/TR/EN-Rechte/Übersetzungen fehlen; nicht mit Bukhārī 3481 vermischen |
| Hadith | Niesen: Lobpreisung, Barmherzigkeitswunsch, Erwiderung | Bukhārī 6224 | DE/TR/EN-Rechte/Übersetzungen fehlen |
| Hadith | Zwei Personen niesen; Erklärung, warum nur einer Barmherzigkeit erhielt | Muslim 2991a | Eigenständiger Kontextbericht; DE/TR/EN-Rechte/Übersetzungen fehlen |
| Quran | Wa fis-samāʾi rizqukum wa mā tūʿadūn | Adh-Dhāriyāt 51:22 | Tanzil-Text hinterlegt; DE/TR/EN-Übersetzungsnachweise fehlen |
| Quran | Ḥasbunallāhu wa niʿma-l-wakīl im vollständigen Verskontext | Āl ʿImrān 3:173 | Tanzil-Text hinterlegt; türkische und weitere Übersetzungsrechte fehlen |
| Quran | Rabbi innī limā anzalta ilayya min khayrin faqīr im vollständigen Verskontext | Al-Qaṣaṣ 28:24 | Tanzil-Text hinterlegt; DE/TR/EN-Übersetzungsnachweise fehlen |
| Dhikr | Astaghfirullāh | Allgemeine Formel | Lizenzierte Übersetzungen und menschliche Sprachprüfung fehlen |
| Dhikr | Allāhu akbar | Allgemeine Formel | Lizenzierte Übersetzungen und menschliche Sprachprüfung fehlen |
| Dhikr | Lā ilāha illā Allāh | Allgemeine Formel | Lizenzierte Übersetzungen und menschliche Sprachprüfung fehlen |
| Dhikr | Alḥamdulillāh | Allgemeine Formel | Lizenzierte Übersetzungen und menschliche Sprachprüfung fehlen |

Für die Koranverse ist die Tanzil-Lizenz CC BY 3.0 dokumentiert: unveränderte Wiedergabe, klare Quellenangabe, Link zu tanzil.net und Copyright-/Lizenzhinweis. Die Lizenzbedingungen erlauben App-Nutzung, aber nicht die Veränderung des arabischen Textes. Für Quran-Übersetzungen bleiben die Rechte pro Sprache separat. Quellen: https://tanzil.net/docs/Text_License und https://quranenc.com/en/home/terms.

### 99 Namen – Esmaül Hüsna

Die ursprüngliche Datei `NurWallpaper-Esmaul-Husna-99.md` wurde wiedergefunden und alle 99 Einträge wurden in `content-library.json` übernommen. Jeder Datensatz enthält Nummer, arabische Schreibweise, Umschrift, eigene deutsche/türkische/englische Kurz-Bedeutung und den im Katalog angegebenen Beleg. Die Reihenfolge wird ausdrücklich als traditionelle Zusammenstellung geführt; Bukhārī 6410 und Muslim 2677 überliefern den Hadith über 99 Namen, nennen die Namen aber nicht einzeln. Die traditionelle Liste wird daher nicht als authentisch vollständig überlieferte Namensliste ausgegeben.

Alle 99 Einträge haben den Status `IN_REVIEW` und sind in der App noch nicht sichtbar. Die Bedeutungen sind eigene redaktionelle Kurzfassungen; arabische Orthografie, Übersetzungen und menschliche Fachprüfung müssen vor Veröffentlichung protokolliert werden.

### Automatisierte Absicherung

`scripts/validate_content_library.py` prüft jetzt:
- eindeutige IDs und Pflichtfelder,
- erlaubte Kategorien und Statuswerte,
- Hadith-Sammlungen ausschließlich Bukhārī/Muslim,
- vollständige DE/TR/EN-Metadaten für jeden freigegebenen Datensatz,
- bestätigten Auswahlstatus und dokumentierte Blocker bei jedem noch nicht freigegebenen Eintrag,
- die festgehaltene 99-Namen-Auswahl.

Der Android-CI-Workflow führt diese Prüfung vor dem Gradle-Build aus. Dadurch können unvollständige Datensätze nicht versehentlich als veröffentlichungsfertig markiert werden.


## Konsolidierter Importstand – 12.11.2026

Die nachfolgenden Zahlen ersetzen die früheren Zwischenstände oben. Die App-Datei `app/src/main/assets/content-library.json` enthält jetzt **141 Datensätze**:

| Bereich | Gesamt | Zur Veröffentlichung freigegeben | Noch in Prüfung |
|---|---:|---:|---:|
| Hadithe (nur Bukhārī/Muslim) | 24 | 3 | 21 |
| Koranverse | 14 | 0 | 14 |
| Allgemeiner Dhikr | 4 | 0 | 4 |
| Esmaül Hüsna | 99 | 0 | 99 |
| **Gesamt** | **141** | **3** | **138** |

Die 3 freigegebenen Hadith-Datensätze enthalten DE/TR/EN-HadeethEnc-Fassungen mit Version und Attribution. Alle übrigen bestätigten Auswahlen sind importiert und bleiben durch `ContentRepository.isReleaseReady()` unsichtbar, bis die konkreten Übersetzungs-, Rechte- und Fachprüfungen vollständig dokumentiert sind. Keine Tirmidhī- oder Ibn-Mājah-Hadithe wurden in die Hadith-Kategorie aufgenommen.

Die 99 Esma-Datensätze speichern Nummer und Umschrift zusätzlich zur arabischen Schrift und den drei eigenen Kurz-Bedeutungen. Die Android-Ansicht zeigt Nummer und Umschrift an, sobald der jeweilige Datensatz nach der Prüfung freigegeben wird.

Die in der Ayat-Ergänzung ausdrücklich ausgewählten Themen (Firʿaun, zwei Gewässer, Bienen, Zakariyyā, Yaʾjūj/Maʾjūj, Geduld, Erleichterung, Barmherzigkeit, Dhikr der Herzen, diesseitiges Leben und Ribā) sind als bestätigte, aber noch gesperrte Einträge abgelegt. Der arabische Arbeitswortlaut muss vor Freigabe zeichengetreu mit Tanzil Uthmani 1.1 abgeglichen und unverändert übernommen werden.

Die CI-Validierung und der Android-Build müssen nach diesem Batch erneut erfolgreich laufen. Ein bestandener JSON-/Build-Test ersetzt weder die menschliche religiöse Fachprüfung noch einen Gerätetest.
