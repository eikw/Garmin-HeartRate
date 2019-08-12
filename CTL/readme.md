# Setup

1.  Aktuelle Eclipse Version installieren, falls nötig
2.  Garmin SDK einrichten. Siehe https://developer.garmin.com/connect-iq/programmers-guide/getting-started/
3.  Projekt klonen und testen

## Simulator

-  Simuliertes Gerät: Fenix s5 plus

Vor dem Start:
-  ADB Port muss weitergeleitet werden
   -  "adb forward tcp:7381 tcp:7381" in der Kommandozeile ausführen
-  FIT-Daten Simulation gestartet werden
   -  Im Simulator: Simulation -> FIT Data -> Simulate Data
-  Das Auslesen der Daten wird mit dem oberen rechten Knopf gestartet und auch wieder gestoppt
   -  Vorher ADB Verbindung aktivieren: adb Connection -> Start 
   -  Hinweis: Funktioniert nur während des hinzufügens einer neuen session in der App
-  In der Console werden die Rohdaten angezeigt, Auf der Uhr nur die Herzfrequenz