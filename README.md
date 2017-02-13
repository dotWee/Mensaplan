# <img src="app\src\main\res\mipmap-xxxhdpi\ic_launcher.png" width="50px" height="50pc" /> Mensaplan
Eine kleine native Android Applikation, welche den wöchentlichen Speiseplan von verschiedenen Mensas in Regensburg anzeigt.

## Unterstütze Standorte
+ OTH Seybothstraße (auch Abends)
+ OTH Prüfening
+ Universität

## Features
+ Dynamisch änderbarer Standort und Wochentag
+ Speicherung des Speiseplans beim ersten Aufruf im internen Cache
+ Buntes Farbschema für den Speiseplan (siehe Screenshots)
+ Wählbare Preisanzeige zwischen Student, Mitarbeiter, Gast und Generell
+ Beschreibung von Gerichten zeigt enthaltene (Allergene-) Inhaltsstoffe

## Screenshots
<img src="art/default.png" height="400px"/>
&nbsp;<img src="art/default_ingredients.png" height="400px"/>
&nbsp;<img src="art/default_settings.png" height="400px"/>

<img src="art/colored_1.png" height="400px"/>
&nbsp;<img src="art/colored_2.png" height="400px"/>
&nbsp;<img src="art/colored_3.png" height="400px"/>

## Todo
+ Wecken, wenn ein bestimmtes Gericht auf dem Speiseplan steht
+ Speiseplan von letzter Woche aus dem Cache löschen
+ ~~Letzten Standort als Standard festlegen~~
+ Backport zu Android 4.1 Jelly Bean (sdk16)
+ Implementierung von Instumental-Tests
+ Veröffentlichung im Google Play Store
+ Veröffentlichung im F-Droid Market
+ Vollständige Orientierung an Google's Material Design
+ Card-Design für den wöchentlichen Speiseplan

## Lizenz
Copyright (c) 2015 Lukas 'dotwee' Wolfsteiner

Binaries und Quellcode können nach den Regeln der [Apache License, Version 2.0](LICENSE) benutzt werden.