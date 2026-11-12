# NurWallpaper – Inhaltsimport und Redaktionsstatus

Stand: 12.11.2026

## Verbindliche Fortschrittskorrektur – Nutzerfreigabe vom 10.11.2026

Der Nutzer hat am 12.11.2026 ausdrücklich klargestellt, dass die bereits gemeinsam ausgewählten und bestätigten Inhalte nicht immer wieder neu zur Prüfung vorgelegt werden sollen. Diese redaktionelle Auswahl gilt als entschieden und ist ohne erneute Grundsatzprüfung umzusetzen. Die aus früheren Gesprächen konkret belegten bestätigten Hadith-Auswahlen umfassen Bukhārī 6306, Muslim 2692, Muslim 713a, Bukhārī 744 / Muslim 598 sowie Bukhārī 6224 / Muslim 2991. Als weitere zuvor bestätigte Datensätze wurden Bukhārī 3481 und Muslim 2756a dokumentiert. Bestätigte Quran-Auswahlen umfassen 51:22, 3:173 und 28:24; der Dhikr Astaghfirullāh ist ebenfalls als Auswahl festgehalten.

Wichtig: Auswahl-/Inhaltsfreigabe und technische Veröffentlichungsreife sind getrennte Statusfelder. Die Auswahl darf nicht erneut infrage gestellt werden. Vor dem tatsächlichen App-Import müssen jedoch die bereits vereinbarten, konkreten Text- und Rechte-Metadaten im vorhandenen Katalog nachgeschlagen und in den Datensatz übertragen werden; es dürfen keine Übersetzungsrechte oder Wortlaute erfunden werden. Die ausdrücklich nur als Recherchequellen akzeptierten Links sind keine Übernahmelizenz. Spätere, ausdrücklich als Recherchekandidaten dokumentierte Erweiterungen (z. B. Hadith-Aufstockung Runde 3 oder neue Ayat-Vorschläge) werden dadurch nicht automatisch freigegeben. Die verbindliche Quellenbegrenzung auf Ṣaḥīḥ al-Bukhārī und Ṣaḥīḥ Muslim bleibt bestehen.


## Aktueller technischer Stand

`app/src/main/assets/content-library.json` ist derzeit absichtlich leer. Das ist kein Verlust der redaktionellen Arbeit: Die bisherigen Inhalte liegen in separaten Arbeitskatalogen. Sie dürfen erst nach der jeweiligen Quellen-, Übersetzungs-, Lizenz- und Fachprüfung in die App-Datenbank übernommen werden.

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

Bis dahin bleibt der Katalog leer in der App. Das ist der korrekte Schutz gegen ungeprüfte religiöse Inhalte – nicht ein Grund, die bisherige Recherche zu verwerfen.

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
