@echo off
chcp 65001
set "BASE=%~dp0"

start "Server Principal" cmd /k "cd /d "%BASE%server_principal" && java -jar ServerApp-jar-with-dependencies.jar"
timeout /t 2
start "Server Respaldo"  cmd /k "cd /d "%BASE%server_respaldo"  && java -jar ServerApp-jar-with-dependencies.jar"
timeout /t 2
start "Totem 1"          cmd /k "cd /d "%BASE%totem_001"        && java -jar TotemApp-jar-with-dependencies.jar"
start "Totem 2"          cmd /k "cd /d "%BASE%totem_002"        && java -jar TotemApp-jar-with-dependencies.jar"
start "Puesto 1"         cmd /k "cd /d "%BASE%puesto_001"       && java -jar PuestoApp-jar-with-dependencies.jar"
start "Puesto 2"         cmd /k "cd /d "%BASE%puesto_002"       && java -jar PuestoApp-jar-with-dependencies.jar"
start "Monitor"          cmd /k "cd /d "%BASE%monitor"          && java -jar MonitorApp-jar-with-dependencies.jar"