@echo off
chcp 65001
set "PRUEBA=%~dp0"
set "ORIGEN=%PRUEBA%..\target"

copy "%ORIGEN%\ServerApp-jar-with-dependencies.jar"  "%PRUEBA%server_principal\"
copy "%ORIGEN%\ServerApp-jar-with-dependencies.jar"  "%PRUEBA%server_respaldo\"
copy "%ORIGEN%\TotemApp-jar-with-dependencies.jar"   "%PRUEBA%totem_001\"
copy "%ORIGEN%\TotemApp-jar-with-dependencies.jar"   "%PRUEBA%totem_002\"
copy "%ORIGEN%\PuestoApp-jar-with-dependencies.jar"  "%PRUEBA%puesto_001\"
copy "%ORIGEN%\PuestoApp-jar-with-dependencies.jar"  "%PRUEBA%puesto_002\"
copy "%ORIGEN%\MonitorApp-jar-with-dependencies.jar" "%PRUEBA%monitor\"
echo Copiado exitoso.
pause