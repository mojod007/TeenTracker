@echo off
echo Stopping all Trace microservices...

:: Ports: 8761 (Discovery), 8080 (Gateway), 8081 (Establishment), 8082 (Product), 8083 (Dashboard), 8084 (User)
set PORTS=8761 8080 8081 8082 8083 8084

for %%p in (%PORTS%) do (
    echo Checking port %%p...
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr :%%p') do (
        echo Killing process on port %%p (PID: %%a)
        taskkill /F /PID %%a 2>nul
    )
)

echo.
echo All services stopped.


@echo off
setlocal enabledelayedexpansion

:: Get the directory where this script is located
set "BASE_DIR=%~dp0Backend"

echo ============================================
echo Starting Trace Microservices
echo ============================================
echo.
if not exist "%~dp0logs" (
    echo Creating logs directory...
    mkdir "%~dp0logs"
)

echo [1/6] Starting Discovery Service...
pushd "%BASE_DIR%\discovery-service"
start "Trace - Discovery Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
timeout /t 10 /nobreak
echo.

echo [2/6] Starting Gateway Service...
pushd "%BASE_DIR%\gateway-service"
start "Trace - Gateway Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
timeout /t 7 /nobreak
echo.

echo [3/6] Starting Dashboard Service...
pushd "%BASE_DIR%\dashboard-service"
start "Trace - Dashboard Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
timeout /t 3 /nobreak
echo.

echo [4/6] Starting User Service...
pushd "%BASE_DIR%\user-service"
start "Trace - User Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
timeout /t 3 /nobreak
echo.

echo [5/6] Starting Product Service...
pushd "%BASE_DIR%\product-service"
start "Trace - Product Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
timeout /t 3 /nobreak
echo.

echo [6/6] Starting Etablissement Service...
timeout /t 3 /nobreak
pushd "%BASE_DIR%\etablissement-service"
start "Trace - Etablissement Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
echo.

echo ============================================
echo All services are starting!
echo ============================================
echo.
echo Services:
echo - Discovery Service    : http://localhost:8761
echo - Gateway              : http://localhost:8080
echo - Dashboard            : http://localhost:8080/
echo - User Service         : http://localhost:8080/users
echo - Product Service      : http://localhost:8080/products
echo - Etablissement Service: http://localhost:8080/etablissements
PAUSE
