@echo off
title CodeAlpha - Task 1 - Student Grade Tracker
cd /d "%~dp0"
echo ============================================
echo  Task 1 - Student Grade Tracker
echo ============================================
echo.

where javac >nul 2>nul
if errorlevel 1 (
  echo [ERROR] Java JDK not found on this computer.
  echo Install it from https://adoptium.net  ^(choose JDK 17, tick "Add to PATH"^)
  echo then close this window and double-click run.bat again.
  echo.
  pause
  exit /b 1
)

if not exist out mkdir out
dir /b /s src\*.java > sources.txt
echo Compiling...
javac -encoding UTF-8 -d out @sources.txt
del sources.txt >nul 2>nul
if errorlevel 1 (
  echo.
  echo [ERROR] Compilation failed - see the messages above.
  pause
  exit /b 1
)
echo Compilation OK. Starting the program...
echo.

java -cp out StudentGradeTracker 
echo.
echo --------------------------------------------
echo Program finished. Press any key to close.
pause >nul
