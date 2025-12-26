# 확장자 차단 관리 과제 - 프론트엔드 개발 정리 

## 📋 프로젝트 개요

파일 업로드 시 차단할 확장자를 관리하는 웹 애플리케이션입니다. 고정 확장자와 커스텀 확장자를 체크박스 및 입력 폼을 통해 관리할 수 있습니다.

## 🛠 기술 스택

- **Framework**: Next.js 16.1.1 (App Router)
- **Language**: TypeScript 5
- **UI**: Tailwind CSS 4
- **State Management**: React Hooks (useState, useEffect)
- **HTTP Client**: Fetch API

## 📁 프로젝트 구조

```
frontend/
├── app/
│   ├── components/
│   │   ├── ExtensionManager.tsx      # 메인 관리 컴포넌트
│   │   ├── FixedExtensionList.tsx    # 고정 확장자 체크박스 리스트
│   │   └── CustomExtensionInput.tsx  # 커스텀 확장자 입력 및 표시
│   ├── lib/
│   │   └── api.ts                    # API 호출 유틸리티 함수
│   ├── types/
│   │   └── extension.ts              # TypeScript 타입 정의
│   ├── page.tsx                      # 메인 페이지
│   └── layout.tsx                    # 레이아웃
├── public/                           # 정적 파일
│   └── test.html                     # supabase 데이터베이스 연동 페이지 (모니터링)
└── package.json
```

## 구현된 기능

### 1️⃣ 고정 확장자 관리

**요구사항**:
- 고정 확장자는 차단을 자주하는 확장자들 리스트이며, default는 unCheck 되어져 있습니다.

**구현 내용**:
- 애플리케이션 시작 시 백엔드 API를 통해 고정 확장자 목록을 조회
- 체크박스 상태 변경 시 즉시 API 호출

**코드 위치**: `app/components/FixedExtensionList.tsx`

```typescript
<input
  type="checkbox"
  checked={extension.isCheck}
  onChange={(e) => onToggle(extension.fixExtensionName, e.target.checked)}
/>
```

-----

### 2️⃣ 커스텀 확장자 최대 입력 길이 20자로 제한

**요구사항**: 
- 확장자 최대 입력 길이는 20자리

**구현 내용**:
- HTML `maxLength` 속성으로 입력 제한
- 실시간으로 입력 길이 표시 (`{inputValue.length}/20자`)
- 20자 초과 입력 시 자동으로 차단

**코드 위치**: `app/components/CustomExtensionInput.tsx`

```typescript
const MAX_LENGTH = 20;

<input
  type="text"
  value={inputValue}
  onChange={(e) => {
    const value = e.target.value;
    if (value.length <= MAX_LENGTH) {
      setInputValue(value);
    }
  }}
  maxLength={MAX_LENGTH}
/>
```

-----

### 3️⃣ 커스텀 확장자 추가 및 화면에 표시 

**요구사항**: 추가 버튼 클릭 시, db에 저장되며 아래쪽 영역에 표현됩니다.

**구현 내용**:
- "추가" 버튼 클릭 즉시, `POST /api/extensions/custom` API 호출  
- 성공 시 상태 업데이트하여 화면에 즉시 반영

**코드 위치**: `app/components/CustomExtensionInput.tsx`

```typescript
const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  const trimmedValue = inputValue.trim().toLowerCase();
  const newExtension = await addCustomExtension(trimmedValue);
};
```

-----

### 4️⃣ 커스텀 확장자 최대 개수 200개로 제한 

**요구사항**: 커스텀 확장자는 최대 200개까지 추가가 가능

**구현 내용**:
- 프론트엔드에서 사전 검증 (200개 초과 시 경고 메시지)
- 현재 개수 실시간 표시 (`현재 {extensions.length}/200개`)

**코드 위치**: `app/components/CustomExtensionInput.tsx`

```typescript
const MAX_COUNT = 200;

if (extensions.length >= MAX_COUNT) {
  alert(`커스텀 확장자는 최대 ${MAX_COUNT}개까지 추가할 수 있습니다.`);
  return;
}
```

-----

### 5️⃣ X를 클릭하면 커스텀 확장자 삭제 

**요구사항**: 확장자 옆 x를 클릭 시 db에서 삭제됨

**구현 내용**:
- 각 확장자 박스 옆에 "×" 버튼 배치
- 클릭 시 `DELETE /api/extensions/custom/{customExtensionName}` API 호출
- URL 인코딩 처리 (`encodeURIComponent`)로 특수문자 확장자도 안전하게 삭제
- 삭제 성공 시 상태에서 제거하여 UI 즉시 반영

**코드 위치**: `app/lib/api.ts`, `app/components/CustomExtensionInput.tsx`

```typescript
// URL 인코딩 처리
await deleteCustomExtension(encodeURIComponent(customExtensionName));
```

-----
<br> 

## 🎯 요건 이외에 추가로 고려한 사항

<br> 

### 1. 실시간 입력 길이 및 개수 표시
<br> 

**구현 이유**: 사용자가 현재 입력 상태와 제한을 명확히 인지할 수 있도록 UX 개선

**구현 내용**:
- 입력 필드 아래에 `{현재 길이}/{최대 길이}자` 표시
- 커스텀 확장자 목록 아래에 `현재 {개수}/{최대 개수}개` 표시

---

### 2. 에러 처리 및 사용자 피드백
<br> 

**구현 내용**:
- API 호출 실패 시 `try-catch`로 에러 처리
- 사용자에게 `alert`를 통해 명확한 에러 메시지 제공
- 로딩 상태 및 에러 상태 UI 제공

**코드 위치**: `app/components/ExtensionManager.tsx`

```typescript
const [loading, setLoading] = useState(true);
const [error, setError] = useState<string | null>(null);

if (loading) return <div>로딩 중...</div>;
if (error) return <div>오류: {error}</div>;
```

---

### 3. URL 인코딩 처리
<br> 

**구현 이유**: 확장자 이름에 특수문자(예: `+`, `#`, `%` 등)가 포함될 경우 URL 파라미터로 전달 시 문제 발생 가능

**구현 내용**:
- `encodeURIComponent()`를 사용하여 안전하게 URL 인코딩

```typescript
await deleteCustomExtension(encodeURIComponent(customExtensionName));
```

-----

## 🚀 실행 방법

### 1️⃣ 로컬 개발
- 의존성 설치
```bash
npm install
```

- 개발 서버 실행
```bash
npm run dev
```
- 브라우저에서 [http://localhost:3000](http://localhost:3000) 접속

### 2️⃣ 배포 환경
- Vercel 배포: https://flow-extension-blocking.vercel.app/

-----

## 🔌 API 엔드포인트

프론트엔드는 다음 백엔드 API를 사용합니다:

### 고정 확장자
- `GET /api/extensions/fixed` → 고정 확장자 목록 조회
- `POST /api/extensions/fixed` → 고정 확장자 체크 상태 업데이트

### 커스텀 확장자
- `GET /api/extensions/custom` → 커스텀 확장자 목록 조회
- `POST /api/extensions/custom` → 커스텀 확장자 추가
- `DELETE /api/extensions/custom/{customExtensionName}` → 커스텀 확장자 삭제

-----

## 📝 주요 설계 내용

- `Next.js App Router 사용`: 최신 Next.js 기능 활용, 파일 기반 라우팅으로 직관적인 구조
- `TypeScript 사용`: 타입 안정성 확보, 인터페이스 정의로 데이터 구조 명확화, IDE 자동완성 및 에러 사전 방지
- `Fetch API 사용`: 별도 라이브러리 없이 네이티브 API 사용, Promise 기반 비동기 처리
- `Tailwind CSS 사용`: 유틸리티 퍼스트 CSS로 빠른 스타일링, 반응형 디자인 쉽게 구현, 일관된 디자인 시스템
