@echo off
echo Starting MongoDB directly...
echo This window will close automatically if MongoDB starts successfully.
echo If you see an error, please take a screenshot and send it to me.
echo.
"C:\Program Files\MongoDB\Server\8.3\bin\mongod.exe" --dbpath "C:\Users\Lenovo\mongodb-data" --logpath "C:\Users\Lenovo\mongodb-data\mongod.log" --port 27017
echo.
echo MongoDB exited with code %ERRORLEVEL%
pause
