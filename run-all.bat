@echo off
echo ==============================
echo GraphQL Angular - AI RAG Agent
echo ==============================
echo.
echo Make sure MongoDB and Ollama are running first!
echo.
echo Starting Backend...
start "Backend" cmd /c "mvnw.cmd spring-boot:run"
echo Waiting 15 seconds for backend to start...
ping -n 15 127.0.0.1 >nul
echo Starting Frontend...
cd frontend
start "Frontend" cmd /c "npx ng serve --open"
echo.
echo Backend: http://localhost:8080/graphiql
echo Frontend: http://localhost:4200
