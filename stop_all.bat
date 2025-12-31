@echo off
echo Stopping all Trace microservices...



:: Ports: 8761 (Discovery), 8080 (Gateway), 8081 (Establishment), 8082 (Product), 8083 (Dashboard), 8084 (User)
set PORTS=8761 8080 8081 8082 8083 8084 8085

for %%p in (%PORTS%) do (
    echo Checking port %%p...
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%%p') do (
        echo Killing process on port %%p (PID: %%a)
        taskkill /F /PID %%a 2>nul
    )
)

echo.
echo All services stopped.
pause
