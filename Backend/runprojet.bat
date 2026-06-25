@echo off
@REM if not exist "logs" mkdir logs


echo Starting Discovery Service...
start "Discovery Service" cmd /c "java -jar discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar > logs/discovery-service.log 2>&1"

echo Waiting for Discovery Service...
timeout /t 3 /nobreak

echo Starting Gateway Service...
start "Gateway Service" cmd /c "java -jar gateway-service/target/gateway-service-0.0.1-SNAPSHOT.jar > logs/gateway-service.log 2>&1"

echo Starting Auth Service...
start "Auth Service" cmd /c "java -jar auth-service/target/auth-service-0.0.1-SNAPSHOT.jar > logs/auth-service.log 2>&1"

echo Starting Dashboard Service...
start "Dashboard Service" cmd /c "java -jar dashboard-service/target/dashboard-service-0.0.1-SNAPSHOT.jar > logs/dashboard-service.log 2>&1"

echo Starting User Service...
start "User Service" cmd /c "java -jar user-service/target/user-service-0.0.1-SNAPSHOT.jar > logs/user-service.log 2>&1"

echo Starting Product Service...
start "Product Service" cmd /c "java -jar product-service/target/product-service-0.0.1-SNAPSHOT.jar > logs/product-service.log 2>&1"

echo Starting Etablissement Service...
start "Etablissement Service" cmd /c "java -jar etablissement-service/target/etablissement-service-0.0.1-SNAPSHOT.jar > logs/etablissement-service.log 2>&1"

echo All services started. Logs are providing in /logs/ directory.


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
echo - Auth Service         : http://localhost:8080/auth
PAUSE