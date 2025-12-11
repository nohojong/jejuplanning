# JejuTrip (제주 여행 플랜)

제주도 여행 정보를 공유하고 관리하는 웹 애플리케이션입니다. 사용자들이 제주도의 관광지, 맛집, 여행 후기를 공유하고 탐색할 수 있는 플랫폼입니다.

## 📋 목차

- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [주요 기능](#주요-기능)
- [시작하기](#시작하기)
  - [필수 요구사항](#필수-요구사항)
  - [설치 및 실행](#설치-및-실행)
- [환경 설정](#환경-설정)
- [API 문서](#api-문서)
- [개발 가이드](#개발-가이드)

## 🛠 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security** (JWT, OAuth2)
- **Spring Data JPA**
- **QueryDSL 5.1.0**
- **MySQL** / **H2 Database** (로컬 개발용)
- **AWS S3** (파일 저장)
- **LocalStack** (로컬 S3 테스트용)

### Frontend
- **React 19**
- **Vite 7**
- **React Router 7**
- **Axios**
- **Kakao Maps SDK**
- **React Icons**

## 📁 프로젝트 구조

```
jejuplan/
├── backend/                 # Spring Boot 백엔드
│   ├── src/main/java/
│   │   └── net/codecraft/jejutrip/
│   │       ├── account/     # 사용자 계정 관리
│   │       ├── admin/       # 관리자 기능
│   │       ├── board/       # 게시판 (게시글, 댓글, 첨부파일)
│   │       ├── common/      # 공통 유틸리티 및 예외 처리
│   │       ├── security/    # 보안 설정 (JWT, OAuth2)
│   │       ├── s3/          # S3 파일 업로드
│   │       └── tour/        # 여행 정보 (장소, 리뷰)
│   ├── src/main/resources/
│   │   ├── application.yml          # 기본 설정
│   │   ├── application-local.yml    # 로컬 개발 설정
│   │   └── application-example.yml  # 설정 예제
│   ├── build.gradle
│   └── docker-compose.yml   # LocalStack 설정
│
└── frontend/                # React 프론트엔드
    ├── src/
    │   ├── api/            # API 클라이언트
    │   ├── components/     # 재사용 가능한 컴포넌트
    │   └── pages/          # 페이지 컴포넌트
    └── package.json
```

## ✨ 주요 기능

### 인증 및 인가
- JWT 기반 인증 (Access Token + Refresh Token)
- OAuth2 소셜 로그인 (Google, Kakao 등)
- 사용자 프로필 관리
- 권한 기반 접근 제어 (USER, MANAGER)

### 게시판
- 게시글 작성, 수정, 삭제
- 댓글 기능
- 파일 첨부 (이미지 업로드)
- 태그 시스템
- 좋아요 기능
- 검색 기능

### 여행 정보
- 제주도 관광지 정보 조회
- 장소 상세 정보
- 여행 후기 작성 및 조회
- 리뷰 수정 및 삭제
- 카카오 맵 연동

### 관리자 기능
- JSON 데이터를 데이터베이스로 변환
- Visit Jeju API 데이터 동기화
- 장소 정보 배치 처리

## 🚀 시작하기

### 필수 요구사항

- **Java 17** 이상
- **Node.js 18** 이상
- **MySQL 8.0** 이상 (또는 H2 Database)
- **Docker** (로컬 S3 테스트용, 선택사항)
- **Gradle 7** 이상

### 설치 및 실행

#### 1. 저장소 클론

```bash
git clone <repository-url>
cd jejuplan
```

#### 2. Backend 설정

```bash
cd backend
```

**설정 파일 확인**

프로젝트에는 이미 설정 파일이 포함되어 있습니다:
- `application.yml` - 기본 설정
- `application-local.yml` - 로컬 개발용 설정 (H2, LocalStack)
- `application-example.yml` - 설정 예제 및 가이드

**MySQL 사용 시 설정 변경**

`application-local.yml` 파일을 열어 데이터베이스 설정을 변경하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jejutrip?useSSL=false&serverTimezone=Asia/Seoul
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

**애플리케이션 실행**

```bash
# Windows
.\gradlew bootRun --args='--spring.profiles.active=local'

# Linux/Mac
./gradlew bootRun --args='--spring.profiles.active=local'
```

또는 IDE에서 `JejutripApplication.java`를 실행하세요.

**H2 Console 접속**

로컬 프로파일 실행 시 H2 Console에 접속할 수 있습니다:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:jejutrip`
- Username: `sa`
- Password: (비워두기)

#### 3. Frontend 설정

```bash
cd frontend
npm install
npm run dev
```

프론트엔드는 기본적으로 `http://localhost:5173`에서 실행됩니다.

#### 4. LocalStack 설정 (선택사항)

로컬에서 S3 기능을 테스트하려면 LocalStack을 실행하세요:

```bash
cd backend
docker-compose up -d
```

LocalStack은 `http://localhost:4566`에서 실행됩니다.

## ⚙️ 환경 설정

### 프로파일

- `local`: 로컬 개발 환경
  - H2 Database 사용 (인메모리)
  - LocalStack S3 사용
  - 디버그 로깅 활성화
  - H2 Console 활성화

### 필수 설정 값

`application-local.yml`에 포함된 필수 설정:

```yaml
server:
  url: localhost  # 쿠키 도메인

jwt:
  secret: your-secret-key  # 최소 256비트 권장
  access-token-expiration-minutes: 30
  refresh-token-expiration-days: 14

client:
  url: http://localhost:5173  # 프론트엔드 URL
```

### 테스트 사용자 계정

`local` 프로파일 실행 시 자동으로 생성되는 테스트 계정:

| 사용자 유형 | 이메일 | 비밀번호 | 권한 |
|-----------|--------|---------|------|
| 관리자 | `codecraft@example.com` | `code1234` | `MANAGER` |
| 일반 사용자 | `hello@google.com` | `jejutrip#2025` | `USER` |

### S3 설정 비활성화

파일 업로드 기능이 필요 없는 경우:

`backend/src/main/java/net/codecraft/jejutrip/s3/config/S3BucketInitializer.java` 파일에서 `@Component` 어노테이션을 주석 처리하세요.

## 📚 API 문서

### 인증

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 | ❌ |
| POST | `/api/auth/logout` | 로그아웃 | ✅ |
| GET | `/api/users/me` | 현재 사용자 정보 | ✅ |
| DELETE | `/api/users/me` | 회원 탈퇴 | ✅ |
| PATCH | `/api/users/me/password` | 비밀번호 변경 | ✅ |

### 게시판

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| GET | `/api/board/posts` | 게시글 목록 조회 | ❌ |
| GET | `/api/board/posts/{id}` | 게시글 상세 조회 | ❌ |
| POST | `/api/board/post` | 게시글 작성 | ✅ |
| PUT | `/api/board/posts/{id}` | 게시글 수정 | ✅ |
| DELETE | `/api/board/posts/{id}` | 게시글 삭제 | ✅ |
| GET | `/api/board/posts/search/{content}` | 게시글 검색 | ❌ |
| GET | `/api/board/posts/search/tags/{tag}` | 태그로 검색 | ❌ |

### 댓글

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| GET | `/api/board/comment/{postId}` | 댓글 목록 조회 | ❌ |
| POST | `/api/board/comment` | 댓글 작성 | ✅ |
| DELETE | `/api/board/comment/{commentId}` | 댓글 삭제 | ✅ |
| GET | `/api/board/comment/notifications` | 댓글 알림 | ✅ |

### 프로필

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| GET | `/api/profiles` | 프로필 조회 | ✅ |
| PUT | `/api/profiles` | 프로필 수정 | ✅ |
| GET | `/api/profiles/statistics` | 통계 조회 | ✅ |

### 여행 정보

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| GET | `/api/places` | 장소 목록 조회 (페이징) | ❌ |
| GET | `/api/places/{id}` | 장소 상세 조회 | ❌ |
| GET | `/api/places/search?title={title}` | 장소 검색 | ❌ |
| POST | `/api/places/{placeId}/reviews` | 리뷰 작성 | ✅ |
| PUT | `/api/places/{placeId}/reviews/{reviewId}` | 리뷰 수정 | ✅ |
| DELETE | `/api/places/{placeId}/reviews/{reviewId}` | 리뷰 삭제 | ✅ |

### 관리자

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|----------|
| POST | `/api/admin/places/sync` | 장소 데이터 동기화 | ✅ (MANAGER) |

## 🧪 테스트

### Backend 테스트

```bash
cd backend
./gradlew test
```

### Frontend 테스트

```bash
cd frontend
npm run lint
```

## 🔧 개발 가이드

### 빌드

```bash
cd backend
./gradlew clean build
```

### QueryDSL Q 클래스 생성

Q 클래스는 빌드 시 자동으로 생성됩니다:

```bash
./gradlew compileJava
```

생성 위치: `build/generated/sources/annotationProcessor/java/main`

### 코드 스타일

- Java 코드는 Google Java Style Guide를 따릅니다
- React 코드는 ESLint 규칙을 따릅니다

### 주요 개선 사항

- ✅ 의존성 주입 개선
- ✅ 보안 설정 강화
- ✅ 커스텀 예외 처리
- ✅ 로깅 개선
- ✅ 코드 정리 및 최적화

## 📝 참고 사항

- **백엔드 기본 포트**: `8080`
- **프론트엔드 기본 포트**: `5173`
- **LocalStack 기본 포트**: `4566`
- **H2 Console**: `http://localhost:8080/h2-console`
- 로컬 개발 시 CORS 설정이 포함되어 있습니다
- JWT 토큰은 쿠키에 저장됩니다

## 🐛 문제 해결

### 빌드 오류

Q 클래스를 찾을 수 없는 경우:
```bash
cd backend
./gradlew clean compileJava
```

### 데이터베이스 연결 오류

H2를 사용하는 경우 `application-local.yml`의 설정을 확인하세요.
MySQL을 사용하는 경우 데이터베이스가 실행 중인지 확인하세요.

### S3 오류

LocalStack이 실행 중인지 확인:
```bash
docker-compose ps
```

S3 기능이 필요 없는 경우 `S3BucketInitializer`의 `@Component`를 주석 처리하세요.

## 📄 라이선스

이 프로젝트는 개인 프로젝트입니다.

## 👥 기여자

- CodeCraft Team

---

**문의사항이나 버그 리포트는 이슈로 등록해주세요.**
