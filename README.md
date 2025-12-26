# 확장자 차단 관리 과제

파일 업로드 시 차단할 확장자를 관리하는 웹 애플리케이션입니다.

<br>

## 📁 프로젝트 구조

```
flow/
├── frontend/   # 프론트엔드 (Next.js)
└── backend/    # 백엔드 (Spring Boot)
```

<br>

## 🛠 기술 스택

### Backend
- Spring Boot
- Java
- JPA / Hibernate
- H2 Database (개발 환경)
- Gradle
- Lombok

### Frontend
- Next.js 16.1.1 (App Router)
- TypeScript 5
- Tailwind CSS 4
- React Hooks

<br>

## ✨ 주요 기능

1. **고정 확장자 관리**
   - 7개 고정 확장자 (bat, cmd, com, cpl, exe, scr, js)
   - 체크박스로 체크/언체크 상태 관리
   - 체크 상태는 DB에 저장되어 새로고침 후에도 유지

2. **커스텀 확장자 관리**
   - 최대 20자까지 입력 가능
   - 최대 200개까지 추가 가능
   - 추가/삭제 기능

3. **요건 외 추가 고려사항**
   - 대소문자 구분 없이 중복 체크
   - 커스텀 확장자의 실시간 입력 길이와 개수 표시
   - 에러 처리 및 메시지
   - URL 인코딩 처리

<br>

## 🚀 실행 방법

### 1. Backend 실행

```bash
cd backend
./gradlew bootRun
```

서버 실행 후 DB 확인: `http://localhost:8080/h2-console`

### 2. Frontend 실행

```bash
cd frontend
npm install
npm run dev
```

브라우저에서: `http://localhost:3000`


<br>


## 📡 API 엔드포인트

- `GET /api/extensions/fixed` - 고정 확장자 목록 조회
- `POST /api/extensions/fixed` - 고정 확장자 체크 상태 업데이트
- `GET /api/extensions/custom` - 커스텀 확장자 목록 조회
- `POST /api/extensions/custom` - 커스텀 확장자 추가
- `DELETE /api/extensions/custom/{customExtensionName}` - 커스텀 확장자 삭제


<br>


## 📝 상세 문서

- [프론트엔드 README](./frontend/README.md)
- [백엔드 README](./backend/README.md)

<br>
