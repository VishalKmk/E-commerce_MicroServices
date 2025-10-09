@echo off
echo ==========================================
echo Starting E-commerce Microservices
echo ==========================================
echo.

REM Set color for better visibility
color 0A

REM Start Eureka Server (Service Discovery) first
echo [1/4] Starting Eureka Server (Service Discovery)...
echo ==========================================
cd /d "C:\OldD\Downloads\eda\server\server"
start "Eureka Server" cmd /k "mvnw.cmd spring-boot:run"
echo Waiting 20 seconds for Eureka Server to start...
timeout /t 20 /nobreak > nul

REM Start Demo Service (Product Service)
echo.
echo [2/4] Starting Demo Service (Product Service)...
echo ==========================================
cd /d "C:\OldD\Downloads\eda\demo"
start "Product Service" cmd /k "mvnw.cmd spring-boot:run"
echo Waiting 20 seconds for Product Service to start...
timeout /t 20 /nobreak > nul

REM Start Orders Service
echo.
echo [3/4] Starting Orders Service...
echo ==========================================
cd /d "C:\OldD\Downloads\eda\orders\orders"
start "Orders Service" cmd /k "mvnw.cmd spring-boot:run"
echo Waiting 20 seconds for Orders Service to start...
timeout /t 20 /nobreak > nul

REM Start API Gateway (last)
echo.
echo [4/4] Starting API Gateway...
echo ==========================================
cd /d "C:\OldD\Downloads\eda\gateway"
start "API Gateway" cmd /k "mvnw.cmd spring-boot:run"

echo.
echo ==========================================
echo All services are starting up!
echo ==========================================
echo.
echo Services will be available at:
echo - Eureka Server: http://localhost:8761
echo - Product Service: Check Eureka dashboard for port
echo - Orders Service: Check Eureka dashboard for port  
echo - API Gateway: Check application.yml for port
echo.
echo Each service is running in its own command window.
echo.

:menu
echo ==========================================
echo Choose an option:
echo ==========================================
echo 1. Exit script (keep all services running)
echo 2. Stop all services and exit
echo 3. Show service status
echo.
set /p choice=Enter your choice (1, 2, or 3): 

if "%choice%"=="1" goto exit_keep_services
if "%choice%"=="2" goto stop_all_services
if "%choice%"=="3" goto show_status
echo Invalid choice. Please try again.
echo.
goto menu

:show_status
echo.
echo ==========================================
echo Current Java processes (Spring Boot services):
echo ==========================================
tasklist /fi "imagename eq java.exe" /fo table
echo.
echo ==========================================
echo Open command windows:
echo ==========================================
tasklist /fi "imagename eq cmd.exe" /fo table | findstr /C:"Eureka" /C:"Product" /C:"Orders" /C:"Gateway" /C:"API"
echo.
echo Press any key to return to menu...
pause > nul
echo.
goto menu

:stop_all_services
echo.
echo ==========================================
echo Stopping all Spring Boot services...
echo ==========================================
echo.

REM Kill all java processes (Spring Boot services)
echo Terminating Java processes...
taskkill /f /im java.exe > nul 2>&1

REM Close service windows
echo Closing service windows...
for /f "tokens=2" %%i in ('tasklist /fi "windowtitle eq Eureka Server*" /fo csv ^| find /i "cmd.exe"') do taskkill /f /pid %%i > nul 2>&1
for /f "tokens=2" %%i in ('tasklist /fi "windowtitle eq Product Service*" /fo csv ^| find /i "cmd.exe"') do taskkill /f /pid %%i > nul 2>&1
for /f "tokens=2" %%i in ('tasklist /fi "windowtitle eq Orders Service*" /fo csv ^| find /i "cmd.exe"') do taskkill /f /pid %%i > nul 2>&1
for /f "tokens=2" %%i in ('tasklist /fi "windowtitle eq API Gateway*" /fo csv ^| find /i "cmd.exe"') do taskkill /f /pid %%i > nul 2>&1

echo.
echo All services have been terminated.
echo Press any key to exit...
pause > nul
goto exit_script

:exit_keep_services
echo.
echo ==========================================
echo Exiting script - All services remain running
echo ==========================================
echo.
echo Services are still running in their respective windows.
echo To stop individual services, close their command windows.
echo To stop all services, you can run this script again and choose option 2.
echo.
echo Press any key to exit...
pause > nul

:exit_script
REM Return to original directory
cd /d "C:\OldD\Downloads\eda"
