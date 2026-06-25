@echo off
setlocal enabledelayedexpansion

set "BASE_DIR=%~dp0Backend"

echo ============================================
echo Starting Trace Microservices
echo ============================================
echo.

echo [1/4] Starting Discovery Service...
pushd "%BASE_DIR%\discovery-service"
start "Trace - Discovery Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
timeout /t 10 /nobreak
echo.

echo [2/4] Starting Core Service (Auth + Users + Products + Etablissements)...
pushd "%BASE_DIR%\core-service"
start "Trace - Core Service" powershell -NoExit -Command "$env:SPRING_PROFILES_ACTIVE='dev'; mvn spring-boot:run 2>&1"
popd
timeout /t 7 /nobreak
echo.

echo [3/4] Starting Gateway Service...
pushd "%BASE_DIR%\gateway-service"
start "Trace - Gateway Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
timeout /t 7 /nobreak
echo.

echo [4/4] Starting Dashboard Service...
pushd "%BASE_DIR%\dashboard-service"
start "Trace - Dashboard Service" powershell -NoExit -Command "mvn spring-boot:run 2>&1"
popd
echo.

echo ============================================
echo All services are starting!
echo ============================================
echo.
echo Services:
echo - Discovery Service : http://localhost:8761
echo - Gateway           : http://localhost:8080
echo - Core Service (API): http://localhost:8080/api
echo - Dashboard         : http://localhost:8080/dashboard
echo - Users             : http://localhost:8080/users
echo - Profiles          : http://localhost:8080/profiles
echo - Permissions       : http://localhost:8080/permissions
echo - Products          : http://localhost:8080/products
echo - Gammes            : http://localhost:8080/gammes
echo - Etablissements    : http://localhost:8080/etablissements
echo - H2 Console        : http://localhost:8081/h2-console
echo - Swagger UI        : http://localhost:8080/swagger-ui.html
echo.
echo Default users: admin/admin123, manager/manager123, user/user123
PAUSE
