# 안드로이드 스튜디오에서 게임 실행하기

## 방법 1: 간단한 HTTP 서버 사용 (추천)

### 1단계: Node.js 설치

- [Node.js 공식 사이트](https://nodejs.org/)에서 LTS 버전 다운로드
- 설치 후 터미널에서 `node --version` 확인

### 2단계: 서버 설치 및 실행

```bash
# 프로젝트 폴더에서 실행
npm install
npm run android
```

### 3단계: 안드로이드에서 접속

- 안드로이드 기기와 PC가 같은 Wi-Fi에 연결되어 있는지 확인
- 터미널에 표시된 IP 주소로 안드로이드 브라우저에서 접속
- 예: `http://192.168.1.100:8080`

## 방법 2: Python 서버 사용

### 1단계: Python 설치

- Python 3.x가 설치되어 있어야 함

### 2단계: 서버 실행

```bash
python server.py
```

### 3단계: 안드로이드에서 접속

- 터미널에 표시된 IP 주소로 접속

## 방법 3: 안드로이드 WebView 앱 만들기

### 1단계: 안드로이드 스튜디오에서 새 프로젝트 생성

- "Empty Activity" 템플릿 선택

### 2단계: WebView 설정

```xml
<!-- activity_main.xml -->
<WebView
    android:id="@+id/webview"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

```kotlin
// MainActivity.kt
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebSettings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.webview)

        // WebView 설정
        webView.webViewClient = WebViewClient()
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
        }

        // 로컬 파일 로드
        webView.loadUrl("file:///android_asset/index.html")
    }
}
```

### 3단계: 게임 파일을 assets 폴더에 복사

- 프로젝트의 모든 파일을 `app/src/main/assets/` 폴더에 복사

## 네트워크 설정

### 방화벽 설정 (Windows)

1. Windows 방화벽에서 "Windows Defender 방화벽" 열기
2. "고급 설정" 클릭
3. "인바운드 규칙" → "새 규칙"
4. "포트" 선택 → "TCP" → "특정 로컬 포트" → "8080"
5. "연결 허용" 선택

### 안드로이드 기기 설정

- Wi-Fi가 PC와 같은 네트워크에 연결되어 있는지 확인
- 개발자 옵션에서 "USB 디버깅" 활성화 (필요시)

## 문제 해결

### 접속이 안 될 때

1. PC와 안드로이드가 같은 Wi-Fi에 연결되어 있는지 확인
2. 방화벽 설정 확인
3. IP 주소가 올바른지 확인 (ifconfig 또는 ipconfig 명령어 사용)

### 게임이 제대로 작동하지 않을 때

1. 브라우저 콘솔에서 JavaScript 오류 확인
2. localStorage가 활성화되어 있는지 확인
3. 파일 경로가 올바른지 확인
