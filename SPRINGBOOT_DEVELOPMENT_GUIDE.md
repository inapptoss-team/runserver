# 스프링부트 기반 연구실 탈출 게임 개발 가이드

## 🎯 프로젝트 개요

이 프로젝트는 프론트엔드와 백엔드를 완전히 분리하여 구현된 탈출 게임입니다.

### 주요 특징

- **백엔드**: 스프링부트로 퍼즐 데이터, 정답 검증, 진행 상태 관리
- **프론트엔드**: 순수 JavaScript로 게임 UI 및 사용자 상호작용
- **API 통신**: RESTful API를 통한 데이터 교환
- **보안**: 정답은 백엔드에서만 관리, 프론트엔드로 노출되지 않음

## 🏗️ 아키텍처 구조

```
프로젝트 루트/
├── src/main/java/com/graduateescape/          # 스프링부트 백엔드
│   ├── GraduateEscapeApplication.java        # 메인 애플리케이션
│   ├── controller/                           # REST API 컨트롤러
│   │   ├── GameController.java              # 게임 진행 상태 관리
│   │   └── PuzzleController.java            # 퍼즐 관련 API
│   ├── service/                             # 비즈니스 로직
│   │   ├── GameService.java                 # 게임 진행 상태 서비스
│   │   └── PuzzleService.java               # 퍼즐 관리 서비스
│   ├── dto/                                 # 데이터 전송 객체
│   │   ├── GameProgress.java                # 게임 진행 상태
│   │   ├── Puzzle.java                      # 퍼즐 정보
│   │   └── PuzzleAnswer.java                # 퍼즐 정답
│   └── config/                              # 설정
│       └── WebConfig.java                   # 웹 설정 (CORS, 정적 리소스)
├── src/main/resources/                      # 리소스 파일
│   ├── application.yml                      # 애플리케이션 설정
│   └── static/                              # 정적 파일 (HTML, CSS, JS, 이미지)
└── assets/                                  # 프론트엔드 파일
    ├── js/
    │   ├── api/                             # API 연동 모듈
    │   │   ├── game-api.js                  # 게임 API 클라이언트
    │   │   └── puzzle-manager-api.js        # API 기반 퍼즐 매니저
    │   └── ...                              # 기존 게임 로직
    └── ...
```

## 🚀 개발 환경 설정

### 1. 필수 요구사항

- **Java 17+**
- **Maven 3.6+**
- **Node.js 16+** (개발 시 정적 파일 서빙용)

### 2. 프로젝트 설정

```bash
# 프로젝트 클론 후
cd inapptoss-front

# Maven 의존성 설치
mvn clean install

# 스프링부트 애플리케이션 실행
mvn spring-boot:run
```

### 3. 개발 서버 실행

```bash
# 백엔드 서버 (포트 8080)
mvn spring-boot:run

# 프론트엔드 개발 서버 (포트 3000, 선택사항)
npm run dev
```

## 📡 API 엔드포인트

### 게임 진행 상태 관리

```
GET    /api/game/progress/{playerId}          # 진행 상태 조회
POST   /api/game/progress                     # 진행 상태 저장
POST   /api/game/complete-puzzle              # 퍼즐 완료 처리
POST   /api/game/reset/{playerId}             # 진행 상태 리셋
POST   /api/game/unlock-all/{playerId}        # 모든 퍼즐 잠금 해제
```

### 퍼즐 관리

```
GET    /api/puzzle/all                        # 모든 퍼즐 조회 (정답 제외)
GET    /api/puzzle/player/{playerId}          # 플레이어별 퍼즐 상태
GET    /api/puzzle/{puzzleId}                 # 특정 퍼즐 조회
POST   /api/puzzle/submit                     # 퍼즐 정답 제출
GET    /api/puzzle/status/{playerId}/{puzzleId} # 퍼즐 상태 확인
GET    /api/puzzle/order                      # 퍼즐 순서 조회
```

## 🔧 개발 워크플로우

### 1. 새로운 퍼즐 추가

```java
// PuzzleService.java의 initializePuzzles() 메서드에 추가
Puzzle newPuzzle = new Puzzle(
    "new-puzzle-id",
    "새 퍼즐 제목",
    "퍼즐 설명",
    Puzzle.PuzzleType.TEXT_INPUT,
    "정답"
);
newPuzzle.setSuccessMessage("성공 메시지");
newPuzzle.setOrder(5); // 순서 설정
puzzles.put("new-puzzle-id", newPuzzle);
```

### 2. 퍼즐 순서 변경

```java
// PuzzleService.java의 puzzleOrder 배열 수정
private final String[] puzzleOrder = {
    "chair-puzzle", "storage-clue", "cabinet-puzzle",
    "paper-clue", "mirror-puzzle", "new-puzzle-id"
};
```

### 3. 프론트엔드 퍼즐 로직 추가

```javascript
// puzzle-manager-api.js의 show() 메서드에 새 퍼즐 타입 처리 추가
else if (puzzle.type === 'new-puzzle-type') {
    this.puzzleInput.style.display = 'block';
    this.submitBtn.style.display = 'block';
    // 새 퍼즐 초기화 로직
    this.initNewPuzzle();
}
```

## 🛡️ 보안 고려사항

### 1. 정답 보호

- 퍼즐 정답은 백엔드에서만 관리
- API 응답에서 정답 필드 제외
- 클라이언트 사이드 검증 금지

### 2. 퍼즐 순서 제어

- 백엔드에서 퍼즐 잠금 상태 관리
- 이전 퍼즐 완료 여부에 따른 접근 제어
- 순서 우회 방지

### 3. CORS 설정

```java
// WebConfig.java에서 허용할 도메인 제한
registry.addMapping("/**")
    .allowedOriginPatterns("http://localhost:*", "https://yourdomain.com")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowCredentials(true);
```

## 🧪 테스트 방법

### 1. API 테스트

```bash
# 퍼즐 목록 조회
curl http://localhost:8080/api/puzzle/all

# 퍼즐 정답 제출
curl -X POST http://localhost:8080/api/puzzle/submit \
  -H "Content-Type: application/json" \
  -d '{"playerId":"test-player","puzzleId":"chair-puzzle","answer":"10001000"}'
```

### 2. 브라우저 테스트

```javascript
// 브라우저 콘솔에서 API 테스트
gameAPI.getAllPuzzles().then(console.log);
gameAPI.getPlayerPuzzles().then(console.log);
```

## 📱 안드로이드 연동

### 1. WebView 설정

```kotlin
// MainActivity.kt
webView.settings.apply {
    javaScriptEnabled = true
    domStorageEnabled = true
    allowFileAccess = true
    allowContentAccess = true
}

// API 서버 URL 설정
webView.loadUrl("http://YOUR_SERVER_IP:8080")
```

### 2. 네트워크 권한

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 🔄 배포 고려사항

### 1. 프로덕션 설정

```yaml
# application-prod.yml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  profiles:
    active: prod
  web:
    resources:
      static-locations: classpath:/static/
```

### 2. 데이터베이스 연동 (필요시)

```java
// JPA 엔티티로 GameProgress, Puzzle 관리
@Entity
public class GameProgress {
    @Id
    private String playerId;
    // ... 필드들
}
```

## 🐛 문제 해결

### 1. CORS 오류

- `WebConfig.java`에서 CORS 설정 확인
- 프론트엔드 도메인이 허용 목록에 있는지 확인

### 2. API 연결 실패

- 서버가 실행 중인지 확인 (`http://localhost:8080/api/game/info`)
- 네트워크 방화벽 설정 확인

### 3. 퍼즐 상태 동기화 문제

- 브라우저 캐시 삭제
- `gameAPI.resetPlayerId()`로 플레이어 ID 재설정

## 📚 추가 학습 자료

- [Spring Boot 공식 문서](https://spring.io/projects/spring-boot)
- [Spring Web MVC](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html)
- [CORS 설정 가이드](https://spring.io/guides/gs/rest-service-cors/)
- [RESTful API 설계 원칙](https://restfulapi.net/)

## 🤝 기여 방법

1. 새로운 퍼즐 추가 시 백엔드와 프론트엔드 모두 수정 필요
2. API 변경 시 문서 업데이트 필수
3. 테스트 코드 작성 권장
4. 코드 리뷰 후 머지

이 구조를 통해 확장 가능하고 유지보수가 쉬운 게임 서버를 구축할 수 있습니다!
