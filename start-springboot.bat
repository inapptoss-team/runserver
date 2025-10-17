@echo off
echo ========================================
echo   연구실 탈출 게임 - 스프링부트 서버
echo ========================================
echo.

REM Java 설치 확인
java -version >nul 2>&1
if errorlevel 1 (
    echo ❌ Java가 설치되지 않았습니다.
    echo 📥 Java 17 이상을 설치하세요.
    pause
    exit /b 1
)

REM Maven 설치 확인
mvn --version >nul 2>&1
if errorlevel 1 (
    echo ❌ Maven이 설치되지 않았습니다.
    echo 📥 Maven을 설치하거나 Maven wrapper를 사용하세요.
    pause
    exit /b 1
)

REM 정적 리소스 디렉토리 생성
if not exist "src\main\resources\static" (
    echo 📁 정적 리소스 디렉토리를 생성합니다...
    mkdir "src\main\resources\static"
)

REM 기존 정적 파일들을 static 디렉토리로 복사
echo 📋 정적 파일들을 복사합니다...
xcopy /E /I /Y "assets" "src\main\resources\static\assets"
copy /Y "index.html" "src\main\resources\static\"

REM 서버 시작
echo.
echo 🚀 스프링부트 서버를 시작합니다...
echo 📱 안드로이드에서 접속: http://[서버IP]:8080
echo 💻 로컬에서 접속: http://localhost:8080
echo.
mvn spring-boot:run
