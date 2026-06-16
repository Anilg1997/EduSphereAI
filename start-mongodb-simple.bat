@echo off
echo ============================================
echo   Starting MongoDB Directly
echo ============================================
echo.
echo Data directory: C:\Users\Lenovo\mongodb-data
echo Port: 27017
echo.
echo If this window closes immediately, there's an error.
echo Please take a screenshot and share it with me!
echo.
echo Starting MongoDB now...
echo.
"C:\Program Files\MongoDB\Server\8.3\bin\mongod.exe" --dbpath "C:\Users\Lenovo\mongodb-data" --port 27017
echo.
echo MongoDB exited with code: %ERRORLEVEL%
echo.
pause
