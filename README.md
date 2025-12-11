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
- [테스트](#테스트)

## 🛠 기술 스택

### Backend
- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security** (JWT, OAuth2)
- **Spring Data JPA**
- **QueryDSL 5.1.0**
- **MySQL**
- **H2 Database** (로컬 개발용)
- **AWS S3** (파일 저장)
- **LocalStack** (로컬 S3 테스트용)
- **Redis** (캐싱)

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
│   │       ├── common/      # 공통 유틸리티
│   │       ├── security/    # 보안 설정 (JWT, OAuth2)
│   │       ├── s3/          # S3 파일 업로드
│   │       └── tour/        # 여행 정보 (장소, 리뷰)
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
- JWT 기반 인증
- OAuth2 소셜 로그인
- 사용자 프로필 관리

### 게시판
- 게시글 작성, 수정, 삭제
- 댓글 기능
- 파일 첨부 (이미지 업로드)
- 태그 시스템
- 좋아요 기능

### 여행 정보
- 제주도 관광지 정보 조회
- 장소 상세 정보
- 여행 후기 작성 및 조회
- 카카오 맵 연동

### 관리자 기능
- JSON 데이터를 데이터베이스로 변환
- 여행 정보 배치 처리

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

**환경 변수 설정**

`src/main/resources/application.yml` 또는 `application-local.yml` 파일을 생성하고 다음 설정을 추가하세요:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jejutrip
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

  cloud:
    aws:
      credentials:
        access-key: your_aws_access_key
        secret-key: your_aws_secret_key
      region:
        static: ap-northeast-2
      s3:
        bucket: jejutrip-bucket
```

**애플리케이션 실행**

```bash
# Windows
.\gradlew bootRun --args='--spring.profiles.active=local'

# Linux/Mac
./gradlew bootRun --args='--spring.profiles.active=local'
```

또는 IDE에서 `JejutripApplication.java`를 실행하세요.

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

## ⚙️ 환경 설정

### 프로파일

- `local`: 로컬 개발 환경
  - H2 Database 사용 가능
  - 테스트 사용자 자동 생성
  - LocalStack S3 사용

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

### 주요 엔드포인트

#### 인증
- `POST /api/auth/login` - 로그인
- `POST /api/auth/logout` - 로그아웃
- `GET /api/auth/refresh` - 토큰 갱신

#### 게시판
- `GET /api/board/post` - 게시글 목록 조회
- `POST /api/board/post` - 게시글 작성
- `GET /api/board/post/{id}` - 게시글 상세 조회
- `PUT /api/board/post/{id}` - 게시글 수정
- `DELETE /api/board/post/{id}` - 게시글 삭제
- `POST /api/board/post/{id}/like` - 좋아요

#### 댓글
- `GET /api/board/comment?postId={id}` - 댓글 목록 조회
- `POST /api/board/comment` - 댓글 작성
- `PUT /api/board/comment/{id}` - 댓글 수정
- `DELETE /api/board/comment/{id}` - 댓글 삭제

#### 프로필
- `GET /api/profile` - 프로필 조회
- `PUT /api/profile` - 프로필 수정

#### 여행 정보
- `GET /api/tour/place` - 장소 목록 조회
- `GET /api/tour/place/{id}` - 장소 상세 조회
- `GET /api/tour/review?placeId={id}` - 리뷰 목록 조회

자세한 API 문서는 [backend/README.md](./backend/README.md)를 참고하세요.

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

## 📝 참고 사항

- 백엔드 기본 포트: `8080`
- 프론트엔드 기본 포트: `5173`
- LocalStack 기본 포트: `4566`
- 로컬 개발 시 CORS 설정이 포함되어 있습니다.

## 📄 라이선스

이 프로젝트는 개인 프로젝트입니다.

## 👥 기여자

- CodeCraft Team

---

**문의사항이나 버그 리포트는 이슈로 등록해주세요.**

