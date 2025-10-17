#!/usr/bin/env python3
"""
안드로이드 스튜디오에서 실행 가능한 간단한 웹 서버
Python 3.x 필요
"""

import http.server
import socketserver
import webbrowser
import os
import sys
from pathlib import Path

class CORSHTTPRequestHandler(http.server.SimpleHTTPRequestHandler):
    def end_headers(self):
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        super().end_headers()

    def do_OPTIONS(self):
        self.send_response(200)
        self.end_headers()

def start_server(port=8080, host='0.0.0.0'):
    """서버 시작"""
    os.chdir(Path(__file__).parent)
    
    with socketserver.TCPServer((host, port), CORSHTTPRequestHandler) as httpd:
        print(f"🚀 서버가 시작되었습니다!")
        print(f"📱 안드로이드에서 접속: http://{get_local_ip()}:{port}")
        print(f"💻 로컬에서 접속: http://localhost:{port}")
        print(f"⏹️  종료하려면 Ctrl+C를 누르세요")
        
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\n👋 서버를 종료합니다...")
            httpd.shutdown()

def get_local_ip():
    """로컬 IP 주소 가져오기"""
    import socket
    try:
        # 임시 소켓으로 로컬 IP 확인
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
        s.close()
        return ip
    except:
        return "localhost"

if __name__ == "__main__":
    port = 8080
    host = '0.0.0.0'  # 모든 인터페이스에서 접속 허용
    
    if len(sys.argv) > 1:
        port = int(sys.argv[1])
    
    start_server(port, host)
