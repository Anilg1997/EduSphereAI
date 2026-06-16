@echo off
echo ========================================
echo  Starting MongoDB Service Permanently
echo ========================================
echo.
echo This script must be run as Administrator!
echo.
echo Checking MongoDB service status...
sc query MongoDB | find "RUNNING" > nul
if %ERRORLEVEL%==0 (
    echo MongoDB is already RUNNING!
    goto :end
)
echo.
echo Starting MongoDB service...
net start MongoDB
if %ERRORLEVEL%==0 (
    echo.
    echo MongoDB service started successfully!
    echo The service is configured to AUTO-START on boot.
    echo You will never see this error again.
) else (
    echo.
    echo Failed! Please run this script as Administrator:
    echo   1. Right-click on this file
    echo   2. Select "Run as administrator"
)
:end
echo.
pause
