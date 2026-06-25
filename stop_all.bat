@echo off
echo Stopping all Trace microservices...
echo.

:: Ports: 8761 (Discovery), 8080 (Gateway), 8081 (Core), 8083 (Dashboard)
set PORTS=8761 8081 8083 8080

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
