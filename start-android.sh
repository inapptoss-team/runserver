#!/bin/bash

echo "========================================"
echo "  연구실 탈출 게임 - 안드로이드 서버"
echo "========================================"
echo

# Node.js 설치 확인
if ! command -v node &> /dev/null; then
    echo "❌ Node.js가 설치되지 않았습니다."
    echo "📥 https://nodejs.org/ 에서 Node.js를 다운로드하여 설치하세요."
    exit 1
fi

# npm 패키지 설치
echo "📦 npm 패키지를 설치하고 있습니다..."
npm install

# 서버 시작
echo
echo "🚀 서버를 시작합니다..."
echo "📱 안드로이드에서 접속할 IP 주소를 확인하세요."
echo
npm run android
